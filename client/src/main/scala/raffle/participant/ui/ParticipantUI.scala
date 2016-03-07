package raffle.participant.ui

import org.scalajs.jquery._

abstract class ParticipantUI {

  private var participantWidget: ParticipantWidget = _
  private var nameForm: NameForm = _

  def init(): Unit = {
    participantWidget = new ParticipantWidget()
  }

  nameForm = new NameForm() {
    def onNameChange(newName: String) = {
      participantWidget.setName(newName)
      onNameChanged(newName)
    }
  }

  def onNameChanged(newName: String)

  def updateIdAndName(id: Int, optName: Option[String]) = {
    participantWidget.setId(id)
    for (name <- optName) {
      nameForm.setName(name)
      participantWidget.setName(name)
    }
  }

  def showWon(ourId:Int, winnerId: Int) = {
    println(s"Winner? ourId == id ?")
    if (ourId == winnerId)
      participantWidget.showAsWinner()
    else
      participantWidget.showInactive()
  }

  def updateConnectionStatus(connected: Boolean): Unit = {
    val (statusStr, alertClass) =
      if (connected)
        ("CONNECTED", "connected")
      else
        ("DISCONNECTED", "disconnected")

    val statusTextNode = jQuery("#connection-status-text")
    val statusNode = jQuery("#connection-status-container")

    statusTextNode.html(statusStr)

    statusNode.removeClass("connected")
    statusNode.removeClass("disconnected")
    statusNode.addClass(alertClass)
  }

}
