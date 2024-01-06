package controllers

import models.{Game, GameDTO}
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.collection.mutable

@Singleton
class GamesRankingController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val gamesRanking = new mutable.ListBuffer[Game]()
  gamesRanking += Game(1, "Sea Of Stars", 9.0f)
  gamesRanking += Game(2, "Lies of P", 8.0f)
  gamesRanking += Game(3, "Sea Of Stars", 8.0f)

  implicit val gameRankingJson: OFormat[Game] = Json.format[Game]
  implicit val newGame: OFormat[GameDTO] = Json.format[GameDTO]

  def ranking: Action[AnyContent] = Action {
    if(gamesRanking.isEmpty) NoContent
    else Ok(Json.toJson(gamesRanking))
  }

  def rankingPosition(positionId: Long): Action[AnyContent] = Action {
    val gameFound = gamesRanking.find(_.positionId == positionId)
    gameFound match {
      case Some(game) => Ok(Json.toJson(game))
      case None => NotFound
    }
  }

  def updateRating(positionId: Long, newRating: Float): Action[AnyContent] = Action {
    val index = gamesRanking.indexWhere(_.positionId == positionId)

    if(index != -1) {
      gamesRanking(index) = gamesRanking(index).copy(rating = newRating)
    }

    Ok(Json.toJson(gamesRanking(index)))
  }

  def deleteGame(positionId: Long): Action[AnyContent] = Action {
    val index = gamesRanking.indexWhere(_.positionId == positionId)

    if(index != -1) {
      gamesRanking.remove(index)
    }

    Ok(Json.toJson(gamesRanking))
  }

  def addNewGame(): Action[AnyContent] = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val newGameItem: Option[GameDTO] =
      jsonObject.flatMap(Json.fromJson[GameDTO](_).asOpt)

    newGameItem match {
      case Some(gameItem) =>
        val gameToBeAdded = Game(gameItem.positionId, gameItem.gameName, gameItem.rating)
        gamesRanking += gameToBeAdded
        Created(Json.toJson(gameToBeAdded))
      case None => BadRequest
    }
  }

}
