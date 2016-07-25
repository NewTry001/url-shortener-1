package transfers

import models.Hash
import play.api.libs.json.Json

/**
  * Created by Haoji on 2016-07-24.
  */
case class TShortUrl(hash: String)

object TShortUrl {
  implicit val format = Json.format[TShortUrl]
  def fromHash(hash: Hash): TShortUrl = TShortUrl(hash.hash)
}
