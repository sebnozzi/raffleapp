import org.scalatest.{FreeSpec, Matchers}
import shared.core.{Admin, Participant, RaffleApp}

// Where we model the relationship between the Admin(Client) and
// the RaffleApp
class AdminSpec extends FreeSpec with Matchers {

  trait Fixture {
    val app = new RaffleApp()

    val p1 = new Participant()
    val p2 = new Participant()
    val p3 = new Participant()

    app.registerParticipant("1", p1)
    app.registerParticipant("2", p2)
    app.registerParticipant("3", p3)

    app.setName("1", "participantOne")
    app.setName("3", "participantThree")
  }

  "When the admin connects, it gets a list of connected participants, with uniqueId and names" in new Fixture {
    var receivedParticipants = Map[String, Option[String]]()
    val admin = new Admin() {
      override def onRegistered(participantNameMap: Map[String, Option[String]]): Unit = {
        receivedParticipants = participantNameMap
      }
    }

    app.registerAdmin(admin)

    receivedParticipants should have size 3
    receivedParticipants.values.toSet should be(Set(
      Some("participantOne"),
      None,
      Some("participantThree")))
    receivedParticipants.keys.toSet should be(Set("1", "2", "3"))
  }

  "When a raffle is done, the admin also gets informed about the winner" in new Fixture {
    var winnerId = ""
    val admin = new Admin() {
      override def onWinner(uniqueId: String): Unit = {
        winnerId = uniqueId
      }
    }
    app.registerAdmin(admin)

    (1 to 200).foreach { _ =>
      app.runRaffle()
      Set("1", "2", "3") should contain(winnerId)
    }
  }

  "When a participant registers, the app sends the update to the admin" in new Fixture {
    var registeredId = ""
    val admin = new Admin() {
      override def onParticipantRegistered(uniqueId: String): Unit = {
        registeredId = uniqueId
      }
    }
    app.registerAdmin(admin)

    app.registerParticipant("4", new Participant())

    registeredId should be("4")
  }

  "When a participant disconnects, the app sends the update to the admin" in new Fixture {
    var disconnectedId = ""
    val admin = new Admin() {
      override def onParticipantDisconnected(uniqueId: String): Unit = {
        disconnectedId = uniqueId
      }
    }
    app.registerAdmin(admin)

    app.participantDisconnected("2")

    disconnectedId should be("2")
  }

  "When a participant changes its name, the app sends the update to the admin" in new Fixture {
    var newName = ""
    var participantId = ""
    val admin = new Admin() {
      override def onParticipantNameChanged(uniqueId: String, name: String): Unit = {
        newName = name
        participantId = uniqueId
      }
    }
    app.registerAdmin(admin)

    app.setName("3", "newNameOfThree")

    participantId should be("3")
    newName should be("newNameOfThree")
  }

}
