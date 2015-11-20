package controllers

import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter
import play.api.Logger

object PlayRoutesExample extends Controller {

  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
        routes.javascript.AjaxExample.get,
        routes.javascript.AjaxExample.post,
        routes.javascript.WebSocketExample.connect,
        routes.javascript.PlayRoutesExample.getWithParams)).as("text/javascript")
  }

  def getWithParams(blogPostId: Int, titleUrl: String) = Action {
    Ok("Data from PlayRoutesExample on the server")
  }

}