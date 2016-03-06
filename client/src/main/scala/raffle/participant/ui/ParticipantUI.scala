package raffle.participant.ui

import org.scalajs.jquery._
import raffle.shared.ui.ParticipantWidget

abstract class ParticipantUI {

  private var participantWidget: ParticipantWidget = _
  private var nameForm: NameForm = _

  def init(): Unit = {
    participantWidget = new ParticipantWidget(id = 0,
      container = jQuery("#participant_widget_container"))
  }

  nameForm = new NameForm(){
    def onNameChange(newName:String) = {
      participantWidget.setName(newName)
      onNameChanged(newName)
    }
  }

  def onNameChanged(newName:String)

  def updateIdAndName(id: Int, optName: Option[String]) = {
    participantWidget.setId(id)
    for (name <- optName) {
      nameForm.setName(name)
      participantWidget.setName(name)
    }
  }

  def showWon(id: Int) = {
    val ourId = participantWidget.getId
    println(s"Winner? ourId == id ?")
    if (ourId == id)
      participantWidget.showAsWinner()
    else
      participantWidget.showInactive()
  }

  def updateConnectionStatus(connected: Boolean): Unit = {
    val statusStr =
      if(connected)
      "CONNECTED"
    else
      "DISCONNECTED"

    jQuery("#connectionStatus").html(statusStr)
  }

}

