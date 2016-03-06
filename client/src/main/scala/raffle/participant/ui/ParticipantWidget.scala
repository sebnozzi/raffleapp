package raffle.participant.ui

import java.util.concurrent.TimeUnit

import org.scalajs.jquery._

import scala.concurrent.duration.FiniteDuration
import scala.scalajs.js.timers._

class ParticipantWidget {

  private var _id = 0

  private val participantDiv = jQuery("#participant-portrait")
  private val idDiv = jQuery("#participant-id-value")
  private val nameDiv = jQuery("#participant-name")

  setId(id=0)

  def showTemporarilyActive(milliseconds: Int) = {
    showActive()
    setTimeout(FiniteDuration(milliseconds, TimeUnit.MILLISECONDS)) {
      showInactive()
    }
  }

  def setName(name: String): Unit = {
    nameDiv.text(name)
  }

  def setId(id: Int): Unit = {
    idDiv.text(s"$id")
    _id = id
  }

  def showAsWinner(): Unit = {
    removeClasses()
    jQuery(participantDiv).addClass("winner")
  }

  def showActive(): Unit = {
    removeClasses()
    jQuery(participantDiv).addClass("active")
  }

  def showInactive(): Unit = {
    removeClasses()
  }

  private def removeClasses(): Unit = {
    jQuery(participantDiv).removeClass("winner")
    jQuery(participantDiv).removeClass("active")
  }

}