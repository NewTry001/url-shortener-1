package services

import models.{Hash, Url}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Haoji on 2016-07-24.
  */
class UrlService {

  /**
    * Given a shortened hash, returns a full url
    * @param hash The shortened hash.
    * @return
    */
  def lookUp(hash: Hash)(implicit ec: ExecutionContext): Future[Option[Url]] = {
    Future(Some(Url("http://www.google.ca")))
  }

  def shortenUrl(url: Url)(implicit ec: ExecutionContext): Future[Hash] = {
    Future(Hash(url.url))
  }

  /**
    * Convert to hash
    * @param hash
    * @return
    */
  def getHash(hash: String): Hash = {
    // TODO: move it to transfers
    Hash(hash)
  }

  /**
    * Convert to url
    * @param url
    * @return
    */
  def getUrl(url: String): Url = {
    // TODO: move it to transfers
    Url(url)
  }

}

object  UrlService extends UrlService
