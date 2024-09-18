package controllers

import play.api.libs.Files
import play.api.libs.json.Json
import play.api.mvc.{
  Action,
  BaseController,
  ControllerComponents,
  MultipartFormData
}
import service.RateService

import javax.inject.Inject
import scala.concurrent.Future

class RateController @Inject() (
    val controllerComponents: ControllerComponents,
    rateService: RateService
) extends BaseController {

  def getRatePerProvider(): Action[MultipartFormData[Files.TemporaryFile]] =
    Action.async(controllerComponents.parsers.multipartFormData) { request =>
      request.body.file("file") match {
        case Some(file) =>
          Future.successful(
            Ok(Json.toJson(rateService.getRatePerProvider(file.ref.path)))
          )
        case None =>
          Future.successful(BadRequest("file missing"))
      }
    }
}
