package raffle

import org.scalajs.dom.location
import shared.SharedSerializationClasses._

import scala.scalajs.js.annotation.JSExport


@JSExport
object ParticipantApp {

  private var server: Server = _
  private var ui: ParticipantUI = _

  @JSExport
  def main(): Unit = {

    ui = new ParticipantUI() {
      def onNameChanged(newName: String): Unit =
        server ! ParticipantNameChangedEvent(newName)
    }
    ui.init()

    server = new Server("ws://localhost:9000/raffle/participant/socket" + location.search) {

      override def onConnect(): Unit = {
        ui.updateConnectionStatus(connected = true)
      }

      override def onDisconnect(): Unit =
        ui.updateConnectionStatus(connected = false)

      def receive = {
        case AssignDataCmd(id, optName) =>
          ui.updateIdAndName(id, optName)
        case ParticipantWonEvent(id) =>
          ui.showWon(id)
      }
    }

  }


}
