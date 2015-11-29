package raffle

import org.scalajs.jquery._

abstract class NameForm {

  initEventHandlers()

  def onNameChange(newName:String):Unit

  def getName: String = {
    jQuery("#participant_name_input").value().toString
  }

  def setName(name: String) = {
    jQuery("#participant_name_input").value(name)
  }

  private def initEventHandlers(): Unit = {
    jQuery("#particinapt_name_form").submit((e: JQueryEventObject) => {
      e.preventDefault()
      jQuery("#participant_name_input").blur()
    })

    jQuery("#participant_name_input").blur((e: JQueryEventObject) => {
      val newName = getName
      onNameChange(newName)
    })

    jQuery("#participant_name_input").focus((e: JQueryEventObject) => {
      jQuery(e.target).select()
    })
  }

}