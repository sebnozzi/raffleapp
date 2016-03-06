package raffle.admin

import org.scalajs.dom.raw._
import org.scalajs.jquery._
import prickle.{CompositePickler, Pickle, PicklerPair, Unpickle}
import raffle.communication.routes.SocketRoutes
import raffle.participant.ui.ParticipantWidget
import shared.SharedSerializationClasses._

import scala.collection.mutable
import scala.scalajs.js.annotation.JSExport
import scala.util.{Failure, Success}

/**
 * Created by sebnozzi on 20/11/15.
 */
@JSExport
object AdminApp {


  implicit val adminProtocolPickler: PicklerPair[AdminProtocol] =
    CompositePickler[AdminProtocol].
      concreteType[StartRaffleClickedEvent].
      concreteType[AddParticipantCmd].
      concreteType[RemoveParticipantCmd].
      concreteType[ShowTemporarilyActive].
      concreteType[ShowAsWinner].
      concreteType[ChangeNameCmd]

  val participantMap = mutable.Map[Int, ParticipantWidget]()

  @JSExport
  def main(): Unit = {
    val adminSocketURL = SocketRoutes.adminSocketURL
    val server = buildAndStartServer(adminSocketURL)
    onStartRaffleButtonPressed {
      startRaffle(server)
    }
  }

  def onStartRaffleButtonPressed(callback: => Any): Unit = {
    jQuery("#raffle-button").click((e: JQueryEventObject) => {
      callback
    })
  }

  def startRaffle(server: Server): Unit = {
    server.sendStartRafflePressedEvent()
  }

  def buildAndStartServer(adminSocketURL:String): Server = {
    new Server(adminSocketURL)
  }

  def addParticipant(id: Int, optName: Option[String]): Unit = {
    if (!participantMap.contains(id)) {
      val newItem = jQuery("<li></li>")
      jQuery("#participants").append(newItem)
      val widget = new ParticipantWidget(id, newItem, optName)
      participantMap.put(id, widget)
    }
  }

  def removeParticipant(id: Int) = {
    participantMap.get(id).foreach { widget =>
      widget.remove()
      participantMap.remove(id)
    }
  }

  def showTemporarilyAsActive(id: Int): Unit = {
    participantMap.get(id).foreach { widget =>
      widget.showTemporarilyActive(200)
    }
  }

  def showAsWinner(winnerId: Int): Unit = {
    for ((id, participantWidget) <- participantMap) {
      if (id == winnerId) {
        participantWidget.showAsWinner()
      } else {
        participantWidget.showInactive()
      }
    }
  }

  def changeParticipantName(id: Int, name: String) = {
    participantMap.get(id).foreach { widget =>
      widget.setName(name)
    }
  }


  class Server(socketUrl: String) {

    val socket = new WebSocket(socketUrl)

    socket.onopen = (e: Event) => {
      println("Connected to Admin")
    }

    def sendStartRafflePressedEvent(): Unit = {
      val evt = Pickle.intoString[AdminProtocol](StartRaffleClickedEvent())
      socket.send(evt)
    }

    socket.onmessage = (e: MessageEvent) => {
      val response = e.data.toString
      Unpickle[AdminProtocol].fromString(response) match {
        case Success(adminProtocol) =>
          adminProtocol match {
            case AddParticipantCmd(id, name) =>
              addParticipant(id, name)
            case ChangeNameCmd(id, name) =>
              changeParticipantName(id, name)
            case RemoveParticipantCmd(id) =>
              removeParticipant(id)
            case ShowTemporarilyActive(id) =>
              showTemporarilyAsActive(id)
            case ShowAsWinner(participantId) =>
              showAsWinner(participantId)
          }
        case Failure(_) => System.err.println(s"Could not de-pickle: $response")
      }
    }

    socket.onclose = (e: CloseEvent) => {
      println("Socket to Admin closed")
    }
    socket.onerror = (e: ErrorEvent) => {
      println("Error in Admin socket")
    }

  }


}

