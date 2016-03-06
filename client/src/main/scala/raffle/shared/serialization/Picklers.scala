package raffle.shared.serialization

import prickle.{CompositePickler, PicklerPair}
import shared.SharedSerializationClasses._

/**
 * Created by sebnozzi on 29/11/15.
 */
object Picklers {

  implicit val participantToAdminProtocolPickler: PicklerPair[ParticipantToAdminProtocol] =
    CompositePickler[ParticipantToAdminProtocol].
      concreteType[ParticipantNameChangedEvent]

  implicit val adminToParticipantPickler: PicklerPair[AdminToParticipantProtocol] =
    CompositePickler[AdminToParticipantProtocol].
      concreteType[AssignDataCmd].
      concreteType[ParticipantWonEvent]

}
