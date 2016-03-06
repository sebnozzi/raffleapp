package raffle.participant.ui

import org.scalajs.jquery._

abstract class NameForm {

  private val inputField:JQuery = jQuery("#participant_name_input")
  private val inputForm:JQuery = jQuery("#participant_name_form")

  initEventHandlers()

  def onNameChange(newName:String):Unit

  def getName: String = {
    inputField.value().toString
  }

  def setName(name: String) = {
    inputField.value(name)
  }

  private def initEventHandlers(): Unit = {
    inputForm.submit((e: JQueryEventObject) => {
      e.preventDefault()
      inputField.blur()
    })

    inputField.blur((e: JQueryEventObject) => {
      val newName = getName
      onNameChange(newName)
    })

    /*
    inputField.focus((e: JQueryEventObject) => {
      jQuery(e.target).value("")
    })
    */

  }


}