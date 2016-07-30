package persistance.dao

import javax.inject.Singleton

import com.google.inject.Inject
import models.Hash
import play.api.Logger
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.ProvenShape

import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by Haoji on 2016-07-24.
  */
case class EShortUrl(hash: String, url: String)

class ShortUrlTable(tag: Tag) extends Table[EShortUrl](tag, "UrlShortener") {
  def hash: Rep[String] = column[String]("hash", O.PrimaryKey)
  def url: Rep[String] = column[String]("url")
  override def * : ProvenShape[EShortUrl] = {
    (hash, url) <> (EShortUrl.tupled, EShortUrl.unapply)
  }
}

@Singleton
class ShortUrlDAO @Inject() (dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfig[JdbcProfile] {
  override val dbConfig = dbConfigProvider.get[JdbcProfile]
  val tableQuery = TableQuery[ShortUrlTable]

  private val logger = Logger(this.getClass)

  def insert(shortUrl: EShortUrl)(implicit ex: ExecutionContext): Future[EShortUrl] = {
    Logger.info(s"ShortUrl insert starts for hash=${shortUrl.hash} and url=${shortUrl.url}")
    db.run((tableQuery += shortUrl).transactionally).map { _ =>
      logger.info(s"ShortUrl insert succeeded for hash=${shortUrl.hash} and url=${shortUrl.url}")
      shortUrl
    }.recover{
      case e: Exception =>
        logger.error(s"ShortUrl insert failed for hash=${shortUrl.hash} and url=${shortUrl.url}, " +
          s"exception message=${e.getMessage}")
        shortUrl
    }
  }

  def findByHash(hash: Hash)(implicit ex: ExecutionContext): Future[Option[EShortUrl]] = {
    logger.info(s"ShortUrl findByHash starts for hash=${hash.hash}")
    val query = for {
      entity <- tableQuery if entity.hash === hash.hash
    } yield entity
    db.run(query.result.headOption)
  }
}
