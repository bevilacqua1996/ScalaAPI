package controllers

import models.ToDoListItem
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.collection.mutable

@Singleton
class ToDoListController @Inject() (val controllerComponents: ControllerComponents) extends BaseController {

  private val todoList = new mutable.ListBuffer[ToDoListItem]()
  todoList += ToDoListItem(1, "NIFF", true)
  todoList += ToDoListItem(2, "NISS", true)

  implicit val todoListJson = Json.format[ToDoListItem]

  def getAll(): Action[AnyContent] = Action {
    if(todoList.isEmpty) NoContent
    else Ok(Json.toJson(todoList))
  }

}
