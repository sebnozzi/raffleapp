import org.scalatest.{FreeSpec, Matchers}
import shared.core.{Participant, RaffleApp}

// In which we show that participants can set their names
// and get them back when re-connecting
class NameSpec extends FreeSpec with Matchers {

  trait AppWithKnownName {
    var previousName = ""
    val app = new RaffleApp()
    val p = new Participant() {
      override def onSetName(name:String):Unit = {
        previousName=name
      }
    }
    app.registerParticipant("1", p)
    app.setName("1", "nameOfParticipant1")
    app.participantDisconnected("1")
  }

  "Participant sends name-change to the App" in {
    val app = new RaffleApp()
    val p = new Participant()
    app.registerParticipant("1", p)
    app.setName("1", "nameOfParticipant1")
  }

  "When (re-)connecting, if a name was previously known, it is send to the Participant" in new AppWithKnownName {
    app.registerParticipant("1", p)
    previousName should be("nameOfParticipant1")
  }

}
