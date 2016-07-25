package controllers

import javax.inject.{Inject, Singleton}

import models.Url
import play.api.libs.json.{JsResult, JsValue, Json}
import play.api.mvc.{Action, AnyContent, Controller}
import services.UrlService
import transfers.{TFullUrl, TShortUrl}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by Haoji on 2016-07-24.
  */
@Singleton
class UrlController @Inject() extends Controller {
  def redirect(hash: String): Action[AnyContent] = Action.async {
    val serverHash = UrlService.getHash(hash)
    UrlService.lookUp(serverHash) map {
      case Some(fullUrl) => Redirect(fullUrl.url, 301)
      case None => NotFound("Bad hash")
    }
  }

  def shorten: Action[AnyContent] = Action.async { request =>
    val urlOpt: Option[Url] = request.body.asJson flatMap { (jsValue: JsValue) =>
     Json.fromJson[TFullUrl](jsValue).asOpt
    } map (_.toUrl)

    if (urlOpt.isDefined) {
      val serverUrl = urlOpt.get
      UrlService.shortenUrl(serverUrl) map { hash =>
        Ok(Json.toJson(TShortUrl.fromHash(hash)))
      }
    } else {
      Future.successful(BadRequest("Bad json format"))
    }

  }

}
