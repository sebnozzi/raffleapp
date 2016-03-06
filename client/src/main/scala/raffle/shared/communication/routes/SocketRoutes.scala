package raffle.shared.communication.routes

import WsURLBuilder._
import raffle.shared.communication.routes.impl.NativeSocketRoutes

/**
  * Created by sebnozzi on 06/03/16.
  */
object SocketRoutes {

  val participantSocketURL = {
    val relativeURL = NativeSocketRoutes.controllers.RaffleController.participantSocket().url
    wsLocation(relativeURL)
  }

  val adminSocketURL = {
    val relativeURL = NativeSocketRoutes.controllers.RaffleController.adminSocket().url
    wsLocation(relativeURL)
  }

}
