package raffle.shared.communication.routes.impl

import scala.scalajs.js

/**
  * Created by sebnozzi on 06/03/16.
  */
trait HasJsURL extends js.Object {
  val url:String = js.native
  def webSocketURL():String = js.native
}
