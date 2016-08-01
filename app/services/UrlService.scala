package services

import javax.inject.Singleton

import com.google.inject.Inject
import models.{Hash, ShortUrl, Url}
import persistance.dao.{EShortUrl, ShortUrlDAO}
import play.api.Logger
import play.api.libs.json._
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Haoji on 2016-07-24.
  */
@Singleton
class UrlService @Inject() (shortUrlDAO: ShortUrlDAO, ws: WSClient) {

  private val logger = Logger(this.getClass)

  /**
    * Given a shortened hash, returns a full url
    * @param hash The shortened hash.
    * @return
    */
  def lookUp(hash: Hash)(implicit ec: ExecutionContext): Future[Option[Url]] = {
    shortUrlDAO.findByHash(hash).map{ eShortUrlOpt =>
      val urlOpt: Option[Url] = eShortUrlOpt.map(ShortUrl(_).url)
      urlOpt
    }
  }

  /**
    * Given a full url, check if it already exists in DB,
    * if exists, return that hash
    * if not, computed unique shorten hash, and store the hash along with the full url into db
    * @param url URL to shorten.
    * @param ec
    * @return The Hash stored into db
    */
  def shortenUrl(url: Url)(implicit ec: ExecutionContext): Future[Hash] = {
    shortUrlDAO.findByUrl(url).flatMap {
      case Some(eShortUrl) =>
        Future(ShortUrl(eShortUrl).hash)
      case None =>
        val currentTimeStamp: Future[BigInt] = getCurrentTimeStamp
        currentTimeStamp map { timeStamp =>
          val computedHash = toHash(computeHash(timeStamp))
          ShortUrl(computedHash, url)
        } flatMap { shortUrlModel =>
          val futureHash = shortUrlDAO.insert(shortUrlModel.entity).map{ eShortUrl =>
            ShortUrl(eShortUrl).hash
          }
          futureHash
        }
    }
  }

  /**
    * Convert to hash
    * @param hash
    * @return
    */
  def toHash(hash: String): Hash = {
    Hash(hash)
  }

  private final val TIMESTAMP_SERVICE_URL = "http://www.convert-unix-time.com/api?timestamp=now"
  private val timestampRequestHolder = ws.url(TIMESTAMP_SERVICE_URL)

  // TODO: Move this into another service
  private def getCurrentTimeStamp(implicit ec: ExecutionContext): Future[BigInt] = {
    // return type will be a json with a field called timestamp
    timestampRequestHolder.post("") map { response =>
      val responseAsJson: JsValue = Json.parse(response.body)
      val timeStampAsString = (responseAsJson \ "timestamp").get.toString

      logger.info(s"Received timestamp response from external timestamp web service with timestamp=$timeStampAsString")
      BigInt(timeStampAsString)
    }
  }

  private final val HASH_INDEX = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
  private final val HASH_INDEX_LENGTH = HASH_INDEX.length

  private def computeHash(id: BigInt, acc: String = ""): String = {
    if (id < HASH_INDEX_LENGTH) {
      acc + HASH_INDEX.charAt(id.toInt).toString
    } else {
      computeHash(id / HASH_INDEX_LENGTH - 1, acc + HASH_INDEX.charAt((id % HASH_INDEX_LENGTH).toInt))
    }
  }

}
