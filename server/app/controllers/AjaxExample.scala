package controllers

import play.api.mvc.Action
import play.api.mvc.Controller

object AjaxExample extends Controller {
  
  def get = Action {
    Ok("AJAX data from the server (GET)")
  }
  
  def post = Action {
    Ok("AJAX data from the server (POST)")
  }
  
}