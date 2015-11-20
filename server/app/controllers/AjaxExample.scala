package controllers

import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.Logger

object AjaxExample extends Controller {
  
  def get = Action {
    Ok("AJAX data from the server (GET)")
  }
  
  def post = Action { request =>
    for( data <- request.body.asText ) {
      Logger.debug(s"Client sent us via POST: $data")
    }
    Ok("AJAX data from the server (POST)")
  }
  
}