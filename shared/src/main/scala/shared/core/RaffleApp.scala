package shared.core

import scala.collection.mutable
import scala.util.Random


class RaffleApp {

  private val rnd = new Random()
  private val participants = mutable.Map[String, Participant]()
  private val knownNames = mutable.Map[String, String]()
  private var optAdmin: Option[Admin] = None

  def registerParticipant(uniqueId: String, p: Participant): Unit = {
    if (!alreadyRegistered(uniqueId)) {
      participants.put(uniqueId, p)
      p.onRegistered()
      for (admin <- optAdmin) {
        admin.onParticipantRegistered(uniqueId)
      }
      for (knownName <- knownNames.get(uniqueId)) {
        p.onSetName(knownName)
      }
    }
  }

  def registerAdmin(admin: Admin) = {
    if(isAdminConnected) {
      throw new IllegalStateException("Admin already connected")
    } else {
      val participantNameMap: Map[String, Option[String]] = {
        participants.keys.map { uniqueId =>
          uniqueId -> knownNames.get(uniqueId)
        }.toMap
      }
      optAdmin = Some(admin)
      admin.onRegistered(participantNameMap)
    }
  }

  def adminDisconnected():Unit = {
    optAdmin = None
  }

  def isAdminConnected = optAdmin.isDefined

  private def alreadyRegistered(uniqueId: String): Boolean = {
    participants.contains(uniqueId)
  }

  def participantDisconnected(uniqueId: String): Unit = {
    participants.remove(uniqueId)
    for (admin <- optAdmin) {
      admin.onParticipantDisconnected(uniqueId)
    }
  }

  def setName(uniqueId: String, name: String): Unit = {
    knownNames.put(uniqueId, name)
    for (admin <- optAdmin) {
      admin.onParticipantNameChanged(uniqueId, name)
    }
  }

  def runRaffle(): Unit = {

    if (participants.nonEmpty) {
      val rndIdx = rnd.nextInt(participants.size)
      val rndUniqueId = participants.keys.toSeq(rndIdx)
      val rndParticipant = participants(rndUniqueId)
      rndParticipant.onWinner()
      for (admin <- optAdmin) {
        admin.onWinner(rndUniqueId)
      }
    }

  }
}
