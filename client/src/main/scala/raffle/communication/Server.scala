package raffle.communication

import org.scalajs.dom.raw._
import prickle._
import raffle.serialization.Picklers._
import shared.SharedSerializationClasses._

import scala.util.{Failure, Success}

abstract class Server(socketUrl: String) {

  val socket = new WebSocket(socketUrl)

  initSocketHandlers()

  def !(msg: ParticipantToAdminProtocol): Unit = {
    val dataForServer = Pickle.intoString[ParticipantToAdminProtocol](msg)
    socket.send(dataForServer)
  }

  def receive: PartialFunction[AdminToParticipantProtocol, Any]

  def onDisconnect(): Unit = {}

  def onConnect(): Unit = {}

  private def initSocketHandlers() = {

    socket.onmessage = (e: MessageEvent) => {
      val response = e.data.toString
      Unpickle[AdminToParticipantProtocol].fromString(response) match {
        case Success(protocol: AdminToParticipantProtocol) =>
          receive.applyOrElse(protocol, (unknownMsg: AdminToParticipantProtocol) => {
            println(s"Did not understand: $unknownMsg")
          })
        case Failure(_) => System.err.println(s"Could not de-pickle: $response")
      }
    }

    socket.onopen = (e: Event) => onConnect()
    socket.onclose = (e: CloseEvent) => onDisconnect()
    socket.onerror = (e: ErrorEvent) => println("Error in Admin socket")

  }


}
