package models

import persistance.EShortUrl

/**
  * Created by Haoji on 2016-07-24.
  */
/**
  * The ShortUrl Model
  * @param entity shortened hash entity
  */
class ShortUrl(entity: EShortUrl) {
  def hash: Hash = Hash(entity.hash)
  def url: Url = Url(entity.url)
}

case class Hash(hash: String)

case class Url(url: String)
