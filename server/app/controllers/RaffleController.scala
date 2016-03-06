package controllers

import actors._
import controllers.PlayRoutesExample._
import play.api.Logger
import play.api.mvc._
import play.api.Play.current
import play.api.routing.JavaScriptReverseRouter


/**
 * Created by sebnozzi on 21/11/15.
 */
object RaffleController extends Controller {

  var debugMode = false

  def participant = Action {
    Ok(views.html.participant())
  }

  def adminPage = Action { request =>
    debugMode = request.getQueryString("debugMode").isDefined
    Ok(views.html.admin())
  }

  def adminSocket = WebSocket.acceptWithActor[String, String] { request =>
    out =>
      Logger.debug("Creating web-socket")
      AdminSocketActor.props(out)
  }

  def participantSocket = WebSocket.acceptWithActor[String, String] {  request =>
    out =>
      Logger.debug("Creating web-socket")
      ParticipantSocketActor.props(out, remoteAddress(request), initialName(request))
  }

  def socketRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("socketRoutes")(
        routes.javascript.RaffleController.adminSocket,
        routes.javascript.RaffleController.participantSocket)).as("text/javascript")
  }

  private def remoteAddress(request: RequestHeader): String = {
    val remoteAddress = {
      if (debugMode)
        request.getQueryString("remoteAddress").getOrElse(request.remoteAddress)
      else
        request.remoteAddress
    }
    remoteAddress
  }

  private def initialName(request: RequestHeader): Option[String] = {
    request.getQueryString("name")
  }
}

