package serialization

import prickle.{CompositePickler, PicklerPair}
import shared.SharedSerializationClasses._

/**
 * Created by sebnozzi on 21/11/15.
 */
object Picklers {

  implicit val adminProtocolPickler: PicklerPair[AdminProtocol] =
    CompositePickler[AdminProtocol].
      concreteType[StartRaffleClickedEvent].
      concreteType[AddParticipantCmd].
      concreteType[RemoveParticipantCmd].
      concreteType[ShowTemporarilyActive].
      concreteType[ShowAsWinner].
      concreteType[ChangeNameCmd]

  implicit val participantToAdminProtocolPickler: PicklerPair[ParticipantToAdminProtocol] =
    CompositePickler[ParticipantToAdminProtocol].
      concreteType[ParticipantNameChangedEvent]

  implicit val adminToParticipantProtocol: PicklerPair[AdminToParticipantProtocol] =
    CompositePickler[AdminToParticipantProtocol].
      concreteType[AssignDataCmd].
      concreteType[ParticipantWonEvent]

}
