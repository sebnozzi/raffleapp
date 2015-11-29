package controllers

import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter
import play.api.Logger

object PlayRoutesExample extends Controller {

  def javascriptRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
        routes.javascript.RaffleController.participant)).as("text/javascript")
  }

  def getWithParams(blogPostId: Int, titleUrl: String) = Action {
    Ok("Data from PlayRoutesExample on the server")
  }

}