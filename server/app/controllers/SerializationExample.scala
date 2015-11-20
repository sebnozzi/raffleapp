package controllers

import play.api.mvc._
import play.api.Logger
import prickle._
import shared.SharedSerializationClasses._
import scala.util.Success
import scala.util.Failure

object SerializationExample extends Controller {

  implicit val fruitPickler: PicklerPair[Fruit] = CompositePickler[Fruit].
    concreteType[Apple].concreteType[Lemon].concreteType[FruitSalad].concreteType[TheDurian.type]

  def post = Action { request =>

    for (data <- request.body.asText) {
      Unpickle[Fruit].fromString(data) match {
        case Success(fruit) => Logger.debug(s"Client sent us a serialized: $fruit")
        case Failure(e) => {
          Logger.warn(s"Could not de-pickle: $data")
          Logger.error("Unpicling error", e)
        }
      }
    }

    val fruitSalad: Fruit = {
      val sourLemon = Lemon(sourness = 100.0)
      FruitSalad(Seq(Apple(true), sourLemon, sourLemon, TheDurian))
    }

    Logger.debug(s"Sending back to the client a serialized: $fruitSalad")
    val response = Pickle.intoString(fruitSalad)
    Ok(response).as(JSON)
  }

}