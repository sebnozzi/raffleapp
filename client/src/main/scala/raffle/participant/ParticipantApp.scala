package raffle.participant

import raffle.participant.ui.ParticipantUI
import raffle.shared.communication.Server
import raffle.shared.communication.routes.SocketRoutes
import shared.SharedSerializationClasses._

import scala.scalajs.js.annotation.JSExport


@JSExport
object ParticipantApp {

  private var ourId = 0
  private var server: Server = _
  private var ui: ParticipantUI = _

  @JSExport
  def main(): Unit = {
    val socketURL = SocketRoutes.participantSocketURL

    ui = new ParticipantUI() {
      def onNameChanged(newName: String): Unit =
        server ! ParticipantNameChangedEvent(newName)
    }
    ui.init()

    server = new Server(socketURL) {

      override def onConnect(): Unit = {
        ui.updateConnectionStatus(connected = true)
      }

      override def onDisconnect(): Unit =
        ui.updateConnectionStatus(connected = false)

      def receive = {
        case AssignDataCmd(id, optName) =>
          ourId = id
          ui.updateIdAndName(id, optName)
        case ParticipantWonEvent(winnerId) =>
          ui.showWon(ourId, winnerId)
      }
    }

  }


}
