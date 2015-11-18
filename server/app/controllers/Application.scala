package controllers

import play.api.mvc.Action
import play.api.mvc.Controller
import shared.SharedMessages

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(SharedMessages.itWorks))
  }
  
}
