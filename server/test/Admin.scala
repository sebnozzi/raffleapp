/**
 * Created by sebnozzi on 06/12/15.
 */
class Admin {

  def onRegistered(participantNameMap: Map[String, Option[String]]): Unit = {}

  def onWinner(uniqueId: String): Unit = {}

  def onParticipantRegistered(uniqueId: String): Unit = {}

  def onParticipantDisconnected(uniqueId: String): Unit = {}

  def onParticipantNameChanged(uniqueId: String, name:String):Unit = {}

}