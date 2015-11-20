package example

import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom.WebSocket
import shared.SharedMessages
import org.scalajs.dom.raw.Event
import org.scalajs.dom.raw.MessageEvent
import org.scalajs.dom.raw.CloseEvent
import org.scalajs.dom.raw.ErrorEvent

object ScalaJSExample extends js.JSApp {
  
  def main(): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
    
    WebSocketExample.doExample()
    AjaxExample.doExample()
    SerializationExample.doExample()
    
  }
  
}
