package example

import scala.scalajs.js.Any.fromFunction1
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom

@JSExport
object AjaxExample {

  @JSExport
  def doExample() = {
    doAjaxGet()
    doAjaxPost()
  }

  def doAjaxGet() = {
    val xhr = new dom.XMLHttpRequest()
    xhr.open("GET", "http://localhost:9000/ajaxExample/get")
    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200)
        println(s"Response from AJAX-GET: ${xhr.responseText}")
    }
    xhr.send()
  }

  def doAjaxPost() = {
    val xhr = new dom.XMLHttpRequest()
    xhr.open("POST", "http://localhost:9000/ajaxExample/post")
    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200)
        println(s"Response from AJAX-POST: ${xhr.responseText}")
    }
    xhr.send("hello from Scala.js")
  }

}