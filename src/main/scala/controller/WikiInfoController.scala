package controller

import akka.http.scaladsl.model.StatusCodes.{InternalServerError, NotFound, OK}
import akka.http.scaladsl.server.{Directives, Route}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport._
import play.api.libs.json.Json
import service.WikiInfoService
import utils.exception.EntityNotFound

import java.io.File
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class WikiInfoController(wikiInfoService: WikiInfoService)
                        (implicit ec: ExecutionContext)
    extends Directives {
    
    private val updateData = path("updateData") {
        (post & storeUploadedFile("file", fileInfo => File.createTempFile(fileInfo.fileName, ".tmp"))) { case (_, file) =>
            onComplete(
                wikiInfoService
                    .updateData(file)
                    .map { res =>
                        file.delete()
                        res
                    }
            ) {
                case Success(_) => complete(OK)
                case Failure(_) => complete(InternalServerError, "Ошибка при обработке файла")
            }
        }
    }
    
    private val wiki = path("wiki" / Segment) { wikiTitle =>
        (get & parameter("pretty".?)) { pretty =>
            onComplete(
                wikiInfoService
                    .getWikiInfo(wikiTitle)
            ) {
                case Success(value) => pretty match {
                    case Some(_) => complete(OK, Json.toJson(value).toString())
                    case None => complete(OK, value)
                }
                case Failure(ex: EntityNotFound) => complete(NotFound, ex.getMessage)
                case Failure(ex) => complete(InternalServerError, ex.getMessage)
            }
        }
    }
    
    val routes: Route =
        updateData ~
            wiki
}
