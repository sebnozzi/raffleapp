package actors

import java.util.concurrent.TimeUnit

import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.Logger
import akka.actor.{PoisonPill, Props, Actor, ActorRef}
import shared.SharedSerializationClasses._
import scala.collection.mutable
import scala.concurrent.duration.FiniteDuration._
import scala.util.Random

/**
 * Created by sebnozzi on 21/11/15.
 */
object RaffleServer {

  lazy val instance: ActorRef = Akka.system.actorOf(Props[RaffleServer], name = "chat_server_actor")

}

object RaffleServerMessages {

  case class AddAdmin()

  case class RemoveAdmin()

  case class AddParticipant(remoteAdress: String)

  case class RemoveParticipant(remoteAdress: String)

  case class UpdateParticipantName(remoteAdress: String, newName: String)

}


class RaffleServer extends Actor {

  import RaffleServerMessages._
  import shared.SharedSerializationClasses._

  case class Participant(id: Int, var actorRef: ActorRef, var optName: Option[String] = None, var active: Boolean = true)

  private var optAdminUser: Option[ActorRef] = None
  private val participantMap = mutable.Map[String, Participant]()

  def forSenderHost(block: String => Any): Unit = {
    val optHost = sender().path.address.host
    for (host <- optHost) {
      block(host)
    }
  }

  def findExistingOrMakeNew(remoteAddress: String): Participant = {
    participantMap.get(remoteAddress) map { existing =>
      existing.active = true
      existing.actorRef = sender()
      existing
    } getOrElse {
      ParticipantMaker.makeNew()
    }
  }


  override def receive = {

    case AddAdmin() =>
      if (optAdminUser.isEmpty) {
        Logger.info("Admin connected")
        val adminUser = sender()
        optAdminUser = Some(adminUser)
        tellAdminToAddAllActiveParticipants(adminUser)
      } else {
        Logger.warn("Admin already connected - Only one allowed")
        sender ! PoisonPill
      }

    case RemoveAdmin() =>
      Logger.info("Admin disconnected")
      optAdminUser = None

    case AddParticipant(remoteAddress) =>
      val newParticipant = findExistingOrMakeNew(remoteAddress)
      participantMap.put(remoteAddress, newParticipant)
      Logger.debug("Added participant, sending ID")
      newParticipant.actorRef ! AssignDataCmd(newParticipant.id, newParticipant.optName)
      for (adminUser <- optAdminUser) {
        Logger.debug("Telling admin-user about new participant")
        adminUser ! AddParticipantCmd(newParticipant.id, newParticipant.optName)
      }

    case RemoveParticipant(remoteAddress) =>
      val participant = participantMap(remoteAddress)
      participant.active = false
      Logger.debug("Participant becomes inactive")
      for (adminUser <- optAdminUser) {
        adminUser ! RemoveParticipantCmd(participant.id)
      }

    case UpdateParticipantName(remoteAddress, newName) =>
      Logger.debug(s"Got name change $newName")
      val participant = participantMap(remoteAddress)
      participant.optName = Some(newName)
      optAdminUser foreach { adminUser =>
        adminUser ! ChangeNameCmd(participant.id, newName)
      }

    case StartRaffleClickedEvent() =>
      doRaffle()
  }

  def tellAdminToAddAllActiveParticipants(adminUser: ActorRef): Unit = {
    Logger.debug(s"Telling admin-client about registered participants: ${activeParticipants.size}")
    for (p <- activeParticipants) {
      adminUser ! AddParticipantCmd(p.id, p.optName)
    }
  }

  def activeParticipants: Seq[Participant] = participantMap.values.filter(_.active).toSeq

  def chooseRandomParticipant(): Participant = {
    val rnd = new Random()
    val participants: Seq[Participant] = activeParticipants
    val count = participants.size
    Logger.debug(s"Choosing among $count participants")
    val randomIdx = rnd.nextInt(count)
    Logger.debug(s"Random Index: $randomIdx")
    participants(randomIdx)
  }

  def doRaffle() = {
    Logger.debug("Doing raffle in server")
    for {
      adminUser <- optAdminUser
      randomParticipant = chooseRandomParticipant()
    } {
      Logger.debug(s"Chose random winner: $randomParticipant")
      adminUser ! ShowAsWinner(randomParticipant.id)
      for(p <- activeParticipants) {
        p.actorRef ! ParticipantWonEvent(randomParticipant.id)
      }
    }
  }

  object ParticipantMaker {
    private var participantId = 1

    def makeNew(): Participant = {
      val result = Participant(participantId, sender())
      participantId += 1
      result
    }
  }

}
