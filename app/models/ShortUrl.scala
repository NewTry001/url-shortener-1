package models

import persistance.dao.EShortUrl

/**
  * Created by Haoji on 2016-07-24.
  */
/**
  * The ShortUrl Model
  * @param entity shortened hash entity
  */
case class ShortUrl(entity: EShortUrl) {
  def hash: Hash = Hash(entity.hash)
  def url: Url = Url(entity.url)
}

object ShortUrl {
  def apply(hash: Hash, url: Url): ShortUrl = {
    ShortUrl(EShortUrl(hash.hash, url.url))
  }
}

case class Hash(hash: String)

case class Url(url: String) {
  /**
    * Add back HTTP protocol if it does not contain.
    * Without this protocol, it will Redirect to a sub-directory, which will result a bad hash.
    * @return
    */
  def withProtocol: String = {
    if (url.startsWith("http://") || url.startsWith("https://")) {
      url
    } else {
      "http://" + url
    }
  }
}
