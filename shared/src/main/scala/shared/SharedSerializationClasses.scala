package shared

object SharedSerializationClasses {

  sealed trait AdminProtocol

  case class StartRaffleClickedEvent() extends AdminProtocol

  case class AddParticipantCmd(id: Int, optName: Option[String]) extends AdminProtocol

  case class RemoveParticipantCmd(id: Int) extends AdminProtocol

  case class ShowTemporarilyActive(participantId: Int) extends AdminProtocol

  case class ShowAsWinner(participantId: Int) extends AdminProtocol

  case class ChangeNameCmd(id: Int, name: String) extends AdminProtocol

  // ---

  sealed trait ParticipantToAdminProtocol

  case class ParticipantNameChangedEvent(newName: String) extends ParticipantToAdminProtocol

  // ---

  sealed trait AdminToParticipantProtocol

  case class AssignDataCmd(participantId: Int, optName: Option[String]=None) extends AdminToParticipantProtocol

  case class ParticipantWonEvent(participantId: Int) extends AdminToParticipantProtocol


  // ---

  sealed trait Fruit

  case class Apple(isJuicy: Boolean) extends Fruit

  case class Lemon(sourness: Double) extends Fruit

  case class FruitSalad(components: Seq[Fruit]) extends Fruit

  case object TheDurian extends Fruit

}