package example

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global
import scala.scalajs.js.Any.fromFunction1
import org.scalajs.dom
import org.scalajs.dom.raw.CloseEvent
import org.scalajs.dom.raw.MessageEvent
import org.scalajs.dom.raw.ErrorEvent
import org.scalajs.dom.raw.WebSocket
import org.scalajs.dom.raw.Event

object PlayRoutesExample {

  trait PlayRoute {
    def url: String
    def method: String
    def webSocketURL: String
    def absoluteURL: String
  }

  def playRoute(dyn: => js.Dynamic): PlayRoute = {
    new PlayRoute {
      def method = dyn.`type`.asInstanceOf[String]
      def url = dyn.url.asInstanceOf[String]
      def webSocketURL = dyn.webSocketURL().asInstanceOf[String]
      def absoluteURL = dyn.absoluteURL().asInstanceOf[String]
    }
  }

  def doExample(): Unit = {
    getFromRelativeUrl();
    getFromUrlWithParams();
    postToAbsoluteUrl();
    connectToWebSocket();
  }

  def getFromRelativeUrl(): Unit = {
    val route = playRoute(global.jsRoutes.controllers.AjaxExample.get())
    val xhr = new dom.XMLHttpRequest()
    xhr.open(route.method, route.url)
    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200)
        println(s"Response from AJAX-GET using relative PlayRoute: ${xhr.responseText}")
    }
    println(s"Getting from relative URL using PlayRoutes: ${route.url}")
    xhr.send()
  }
  
  def getFromUrlWithParams(): Unit = {
    val route = playRoute(global.jsRoutes.controllers.PlayRoutesExample.getWithParams(34, "some-cool-stuff"))
    val xhr = new dom.XMLHttpRequest()
    xhr.open(route.method, route.url)
    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200)
        println(s"Response from AJAX-GET using PlayRoute with params: ${xhr.responseText}")
    }
    println(s"Getting from *parametrized* URL using PlayRoutes: ${route.url}")
    xhr.send()
  }

  def postToAbsoluteUrl(): Unit = {
    val route = playRoute(global.jsRoutes.controllers.AjaxExample.post())
    val xhr = new dom.XMLHttpRequest()
    xhr.open(route.method, route.absoluteURL)
    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200)
        println(s"Response from AJAX-POST using absolute PlayRoute: ${xhr.responseText}")
    }
    println(s"Sending to absolute URL via POST using PlayRoutes: ${route.absoluteURL}")
    xhr.send()
  }

  def connectToWebSocket(): Unit = {
    val route = playRoute(global.jsRoutes.controllers.WebSocketExample.connect())
    val socketUrl = route.webSocketURL
    val socket = new WebSocket(socketUrl);
    socket.onopen = (e: Event) => {
      println(s"Connected to WebSocket using PlayRoutes: $socketUrl")
      val messageToTheServer = "Client sends to WebSocket using PlayRoutes"
      socket.send(messageToTheServer)
    }
    socket.onmessage = (e: MessageEvent) => {
      val dataString = e.data.toString()
      println(s"Using PlayRoutes, server responds: ${dataString}")
      socket.close(code = 1000, reason = "Normal example termination")
    }
    socket.onclose = (e: CloseEvent) => {
      println("Socket for PlayRoutes closed")
    }
    socket.onerror = (e: ErrorEvent) => {
      println("Socket error using PlayRoutes")
    }
  }

}