package services

import javax.inject.Singleton

import com.google.inject.Inject
import models.{Hash, ShortUrl, Url}
import persistance.dao.{EShortUrl, ShortUrlDAO}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Haoji on 2016-07-24.
  */
@Singleton
class UrlService @Inject() (shortUrlDAO: ShortUrlDAO) {

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

  def shortenUrl(url: Url)(implicit ec: ExecutionContext): Future[Hash] = {
    val computedHash = toHash(computeHash(getNextId))
    val shortenUrlModel = ShortUrl(computedHash, url)
    shortUrlDAO.insert(shortenUrlModel.entity).map{ eShortUrl =>
      ShortUrl(eShortUrl).hash
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

  // TODO: Use web service
  private var availableId = 0
  private def getNextId: Int = {
    val nextId = availableId
    // increment the id
    availableId += 1
    nextId
  }

  private final val HASH_INDEX = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
  private final val HASH_INDEX_LENGTH = HASH_INDEX.length

  private def computeHash(id: Int, acc: String = ""): String = {
    if (id < HASH_INDEX_LENGTH) {
      acc + HASH_INDEX.charAt(id).toString
    } else {
      computeHash(id / HASH_INDEX_LENGTH - 1, acc + HASH_INDEX.charAt(id % HASH_INDEX_LENGTH))
    }
  }

}
