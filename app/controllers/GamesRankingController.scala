package controllers

import models.Game
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.collection.mutable

@Singleton
class GamesRankingController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val gamesRanking = new mutable.ListBuffer[Game]()
  gamesRanking += Game(1, "Lies of P", 8.0f)
  gamesRanking += Game(2, "Sea Of Stars", 8.0f)

  implicit val todoListJson: OFormat[Game] = Json.format[Game]

  def getAll: Action[AnyContent] = Action {
    if(gamesRanking.isEmpty) NoContent
    else Ok(Json.toJson(gamesRanking))
  }

}
