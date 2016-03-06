package raffle.communication.routes

import raffle.communication.routes.impl.NativeSocketRoutes
import WsURLBuilder._

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
