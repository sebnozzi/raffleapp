import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.{FreeSpec, Matchers}
import scala.collection.mutable
import scala.util.Random


class GuidingSpec extends FreeSpec with Matchers with MockitoSugar {

  // Guiding test...
  "given a raffle app" - {

    val app = new RaffleApp()

    "and given some participants" - {
      var winnerCount = 0
      var winnerSet = mutable.Set[Int]()


      (1 to 3) foreach { nr =>
        val participant = new Participant()  {
          override def onWinner():Unit = {
            winnerSet += nr
            winnerCount += 1
          }
        }
        app.registerParticipant(s"some_id_$nr", participant)
      }

      "only one winner should be chosen" in {
        app.runRaffle()
        winnerCount should be (1)
      }
      "not only the same winner should be chosen" in {
        (1 to 200).foreach(_ => app.runRaffle())
        winnerSet should be(Set(1,2,3))
      }

    }

  }


  // when a participant disconnects, its status should be
  // changed to disconnected, but not deleted
  // the admin-client should get an updated list of ACTIVE participants

  // when a REGISTERED participant re-connects, its status
  // should be changed back to connected
  // it should get back its ID and name
  // the admin-client should get an updated list of ACTIVE participants

  // when a participant sends a name-change message
  // we should change its name in our registry and
  // the admin-client should get an updated list of ACTIVE participants

  // when the admin-client connects, it
  // should get an updated list of ACTIVE participants

  // when the admin-client starts a new raffle
  // we should select a winner from the ACTIVE participants
  // and send back to each participant, and to the admin-client
  // a message telling that a participant won the raffle

  // -------

  // when the admin-client starts, it should connect to the server

  // when the admin-client gets a list of participants
  // it should update its UI

  // when the user clicks on the "raffle" button
  // it should send the event to the server

  // -------

  // when the participant-client starts, it should connect to the server

  // when the server sends ID / Name, we should update our UI

  // when the user enters a new name, we should
  // update our UI and
  // send this event to the server

  // when the server sends a X-won message
  // if we are the winner, we should show this in the UI
  // if we are not the winner, we should reset our UI state

}
