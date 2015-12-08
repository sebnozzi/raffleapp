package core

import org.scalatest.mock.MockitoSugar
import org.scalatest.{FreeSpec, Matchers}
import shared.core.{Participant, RaffleApp}

import scala.collection.mutable

// Guiding test...
class RaffleSpec extends FreeSpec with Matchers with MockitoSugar {

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


}
