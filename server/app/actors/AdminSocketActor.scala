package actors

import akka.actor.{Props, Actor, ActorRef}
import play.api.Logger
import prickle.{Pickle, Unpickle}
import shared.SharedSerializationClasses._

import scala.util.{Failure, Success}
import serialization.Picklers._

object AdminSocketActor {
  def raffleServer = RaffleServer.instance
  def props(out: ActorRef) = Props(new AdminSocketActor(out, raffleServer))
}

class AdminSocketActor(out: ActorRef, raffleServer: ActorRef) extends Actor {

  import actors.RaffleServerMessages._

  override def preStart(): Unit = {
    raffleServer ! AddAdmin()
    Logger.debug("Admin connected via WebSocket")
    // TODO: send all current participants to admin-client
  }

  override def postStop(): Unit = {
    raffleServer ! RemoveAdmin()
    Logger.debug("Admin disconnected from WebSocket")
  }

  def receive = {
    case cmd : AdminProtocol => // going back to admin-client
      sendToAdminClient(cmd)
    case data: String => // coming FROM admin-client
      Logger.debug("Admin-client sent us: " + data)
      handleStringData(data)
    case unknownMsg =>
      Logger.warn(s"AdminSocketActor did not understand: $unknownMsg")
    // out ! ("Hello from the server (via WebSockets)")
  }

  def sendToAdminClient(cmd: AdminProtocol): Unit = {
    val dataToAdminClient = Pickle.intoString(cmd)
    out ! dataToAdminClient
  }

  def handleStringData(data: String): Unit = {
    Unpickle[AdminProtocol].fromString(data) match {
      case Success(adminProtocol) =>
        handleAdminMsg(adminProtocol)
      case Failure(e) =>
        println(s"Could not de-pickle: $data")
    }
  }

  def handleAdminMsg(adminProtocol: AdminProtocol): Unit = {
    Logger.debug("Handling admin-protocol msg")
    adminProtocol match {
      case evt : StartRaffleClickedEvent =>
        Logger.debug("start-raffle evt")
        raffleServer ! evt
      case cmd : ChangeNameCmd => // We got this from one participant, and are sending it to the admin-client
        raffleServer ! cmd
    }
  }

}
