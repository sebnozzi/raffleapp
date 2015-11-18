package controllers

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.actorRef2Scala
import play.api.Logger
import play.api.Play.current
import play.api.mvc.WebSocket
import play.api.mvc.Controller

object WebSocketExample extends Controller {

  def connect = WebSocket.acceptWithActor[String, String] { request =>
    out =>
      Logger.debug("Creating web-socket")
      MyWebSocketActor.props(out)
  }
}

object MyWebSocketActor {
  def props(out: ActorRef) = Props(new MyWebSocketActor(out))
}

class MyWebSocketActor(out: ActorRef) extends Actor {
  def receive = {
    case msg: String =>
      Logger.debug("The client sent: " + msg)
      out ! ("Hello from the server (via WebSockets)")
  }
}
