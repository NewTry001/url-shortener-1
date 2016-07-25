package transfers

import models.Url
import play.api.libs.json.Json

/**
  * Created by Haoji on 2016-07-24.
  */
case class TFullUrl(url: String) {
  def toUrl: Url = Url(url)
}

object TFullUrl {
  implicit val format = Json.format[TFullUrl]
}
