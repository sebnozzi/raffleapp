package example

import org.scalajs.dom.raw.CloseEvent
import org.scalajs.dom.raw.MessageEvent
import org.scalajs.dom.raw.ErrorEvent
import org.scalajs.dom.raw.WebSocket
import org.scalajs.dom.raw.Event
import scala.scalajs.js.annotation.JSExport

@JSExport
object WebSocketExample {

  @JSExport
  def doExample(): Unit = {

    val socketUrl = s"ws://localhost:9000/webSocketExample/connect"
    val socket = new WebSocket(socketUrl);

    socket.onopen = (e: Event) => {
      val messageToTheServer = "Hello from the client (via WebSockets)"
      socket.send(messageToTheServer)
    }

    socket.onmessage = (e: MessageEvent) => {
      val dataString = e.data.toString()
      println(s"Server says: ${dataString}")
      socket.close(code = 1000, reason = "Normal close after successful example")
    }

    socket.onclose = (e: CloseEvent) => {
      println("Socket for WebSocketExample closed")
    }

    socket.onerror = (e: ErrorEvent) => {
      println("Socket error in WebSocketExample")
    }

  }

}