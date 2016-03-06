package raffle.shared.communication.routes.impl

import scala.scalajs.js

/**
  * Created by sebnozzi on 06/03/16.
  */
trait HasSocketURLs extends js.Object {
  def adminSocket():HasJsURL = js.native
  def participantSocket():HasJsURL = js.native
}
