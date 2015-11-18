package example

import org.scalajs.dom
import org.scalajs.dom.html
import scala.scalajs.js.annotation.JSExport

object AjaxExample {

  def doExample() = {
    doAjaxGet()
    doAjaxPost()
  }

  def doAjaxGet() = {
    val xhr = new dom.XMLHttpRequest()
    xhr.open("GET", "http://localhost:9000/ajaxExample/get")
    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200)
        println(xhr.responseText)
    }
    xhr.send()
  }

  def doAjaxPost() = {
    val xhr = new dom.XMLHttpRequest()
    xhr.open("POST", "http://localhost:9000/ajaxExample/post")
    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200)
        println(xhr.responseText)
    }
    xhr.send()
  }

}