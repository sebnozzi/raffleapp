package core

import org.scalatest.{BeforeAndAfter, FreeSpec, Matchers}
import shared.core.{Participant, RaffleApp}

import scala.collection.mutable


// In which we model that a participant connects, disconnects and re-connects
// The app will recognize a participant again using a unique-identifier
// (in practice its hostname / ip-address)
class ConnectionSpec extends FreeSpec with Matchers with BeforeAndAfter {

  trait RaffleAppFixture {
    var app = new RaffleApp()
  }

  trait HasWinner {
    var winnerId = ""
  }

  trait HasRegistrered {
    val registered = mutable.MutableList[Participant]()
  }

  trait OneParticipantFixture extends RaffleAppFixture with HasWinner with HasRegistrered {
    val p1 = new Participant() {
      override def onWinner(): Unit = {
        winnerId = "1"
      }
      override def onRegistered(): Unit = {
        registered += this
      }
    }
    app.registerParticipant("1", p1)
  }

  trait TwoParticipantsFixture extends OneParticipantFixture {
    val p2 = new Participant() {
      override def onWinner(): Unit = {
        winnerId = "2"
      }
    }
    app.registerParticipant("2", p2)
  }

  "when connecting, a participant gets called back saying it is registered" in new OneParticipantFixture  {
    registered should contain(p1)
  }

  "when connecting a second time (same uniqueId) it does not get the 'registered' callback" in new OneParticipantFixture {
    app.registerParticipant("1", p1)
    registered should have size 1
  }

  "a second instance of a participant is not added if one with same uniqueId is connected" in new TwoParticipantsFixture {
    val p2_bis = new Participant() {
      override def onWinner(): Unit = {
        fail("Should never be winner, because it should not be added")
      }
    }

    app.registerParticipant("2", p2_bis)

    // Should run without EVER choosing p2_bis
    (1 to 100).foreach { _ =>
      app.runRaffle()
    }
  }

  "when a participant disconnects, it does not participate in the raffle" in new TwoParticipantsFixture {
    app.participantDisconnected(uniqueId = "1")

    (1 to 100).foreach { _ =>
      app.runRaffle()
      winnerId should be("2")
    }
  }

}
