package example

import prickle._
import org.scalajs.dom
import scala.scalajs.js.annotation.JSExport

import shared.SharedSerializationClasses._
import scala.util.Success
import scala.util.Failure

@JSExport
object SerializationExample {

  implicit val fruitPickler: PicklerPair[Fruit] = CompositePickler[Fruit].
    concreteType[Apple].concreteType[Lemon].concreteType[FruitSalad].concreteType[TheDurian.type]

  @JSExport
  def doExample(): Unit = {

    val apple = Apple(isJuicy = true)
    val data = Pickle.intoString(apple: Fruit)

    println(s"Sending to the server a serialized: $apple")
    sendAndProcessSerialized(data, onResponse = (response: String) => {
      Unpickle[Fruit].fromString(response) match {
        case Success(fruit) => println(s"Server sent us back a serialized: $fruit")
        case Failure(_) => System.err.println(s"Could not de-pickle: $response")
      }
    })

  }

  def sendAndProcessSerialized(data: String, onResponse: (String) => Any) = {
    val xhr = new dom.XMLHttpRequest()
    xhr.open("POST", "http://localhost:9000/serializationExample/post")
    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200)
        onResponse(xhr.responseText)
    }
    xhr.send(data)
  }

}