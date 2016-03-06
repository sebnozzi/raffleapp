package raffle.communication.routes

import org.scalajs.dom.location

/**
  * We can not use Play's webSocketURL() method
  * (see https://www.playframework.com/documentation/2.4.x/ScalaJavascriptRouting)
  * because if the app is served from localhost behind an Apache-Proxy
  * it will answer thus in the URL. It will contain localhost:9000 instead
  * of the host address from which it's being served.
  *
  * That's why we need to ask the browser's host-page URL and construct
  * using that information.
  * */
object WsURLBuilder {

  val wsProtocol = {
    val isInsecure = location.protocol != "https:"
    if(isInsecure)
      "ws"
    else
      "wss"
  }

  val hostAndPort = location.host

  def wsLocation(tailURL:String):String = {
    s"$wsProtocol://$hostAndPort$tailURL"
  }

}
