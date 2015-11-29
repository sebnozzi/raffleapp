package actors

import akka.actor.{Actor, Props, ActorRef}
import play.api.Logger
import prickle.{Pickle, Unpickle}
import shared.SharedSerializationClasses._
import scala.util.{Failure, Success}
import serialization.Picklers._

object ParticipantSocketActor {
  def raffleServer = RaffleServer.instance
  def props(out: ActorRef, remoteAddress:String) = Props(new ParticipantSocketActor(out, remoteAddress, raffleServer))
}

class ParticipantSocketActor(out: ActorRef, remoteAddress:String, raffleServer: ActorRef) extends Actor {

  import actors.RaffleServerMessages._

  override def preStart(): Unit = {
    raffleServer ! AddParticipant(remoteAddress)
    Logger.debug("Participant connected via WebSocket")
  }

  override def postStop(): Unit = {
    raffleServer ! RemoveParticipant(remoteAddress)
    Logger.debug("Participant disconnected from WebSocket")
  }


  def receive = {
    case data: String =>
      Logger.debug("The client sent: " + data)
      handleStringData(data)
    case cmd : AdminToParticipantProtocol =>
      sendFromAdminToClient(cmd)
  }

  def handleStringData(data: String): Unit = {
    Unpickle[ParticipantToAdminProtocol].fromString(data) match {
      case Success(protocol) =>
        handleParticipantMsg(protocol)
      case Failure(e) =>
        println(s"Could not de-pickle: $data")
    }
  }

  def handleParticipantMsg(msg: ParticipantToAdminProtocol): Unit = {
    msg match {
      case ParticipantNameChangedEvent(newName) =>
        raffleServer ! UpdateParticipantName(remoteAddress, newName)
    }
  }

  def sendFromAdminToClient(cmd: AdminToParticipantProtocol): Unit = {
    val dataForClient = Pickle.intoString(cmd)
    out ! dataForClient
  }

}