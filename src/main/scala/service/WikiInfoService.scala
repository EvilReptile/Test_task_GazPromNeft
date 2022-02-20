package service

import model._
import model.dto.{ParsingDTO, ResultDTO}
import play.api.libs.json.{JsValue, Json}
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import utils.JsonHelper
import utils.exception.EntityNotFound

import java.io.File
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

class WikiInfoService(db: Database,
                      auxiliaryTextService: AuxiliaryTextService,
                      categoryService: CategoryService)
                     (implicit ec: ExecutionContext) {
    
    private val wikiInfoQuery = WikiInfoRepo.tableQuery
    
    def getWikiInfo(wikiTitle: String): Future[ResultDTO] = {
        for {
            wikiInfo <- db.run {
                wikiInfoQuery
                    .filter(_.title === wikiTitle)
                    .result
                    .headOption
            }.map(_.getOrElse(throw EntityNotFound(s"Сущности WikiInfo( title = $wikiTitle ) не найдена")))
            categories <- categoryService.findWikiInfoByEntity(wikiInfo.id)
            auxiliaryTexts <- auxiliaryTextService.findWikiInfoByEntity(wikiInfo.id)
        } yield {
            ResultDTO(
                createTimestamp = wikiInfo.createTimestamp.toEpochMilli,
                timestamp = wikiInfo.timestamp.toEpochMilli,
                title = wikiInfo.title,
                language = wikiInfo.language,
                wiki = wikiInfo.wiki,
                categories = categories.map(_.text),
                auxilaryTexts = auxiliaryTexts.map(_.text)
            )
        }
    }
    
    def updateData(file: File): Future[Unit] = {
        Future.sequence(
            Source.fromFile(file, "UTF-8")
                .getLines()
                .flatMap { line =>
                    findDTO(Json.parse(line).as[JsValue])
                        .map(updateWikiInfo)
                }.toSeq
        ).map(_ => ())
    }
    
    private def findDTO(json: JsValue): Option[ParsingDTO] = {
        (
            JsonHelper.findInstant(json, "create_timestamp"),
            JsonHelper.findInstant(json, "timestamp"),
            JsonHelper.findString(json, "title"),
            JsonHelper.findString(json, "language"),
            JsonHelper.findString(json, "wiki"),
            JsonHelper.findStrings(json, "category"),
            JsonHelper.findStrings(json, "auxiliary_text")
        ) match {
            case (Some(createTimestamp),
            Some(timestamp),
            Some(title),
            Some(language),
            Some(wiki),
            Some(categories),
            Some(auxilaryTexts)) => Some(
                ParsingDTO(
                    createTimestamp = createTimestamp,
                    timestamp = timestamp,
                    title = title,
                    language = language,
                    wiki = wiki,
                    categories = categories,
                    auxilaryTexts = auxilaryTexts
                )
            )
            case _ => None
        }
    }
    
    private def updateWikiInfo(dto: ParsingDTO): Future[Unit] = {
        val newWikiInfo = WikiInfo(
            createTimestamp = dto.createTimestamp,
            timestamp = dto.timestamp,
            language = dto.language,
            wiki = dto.wiki,
            title = dto.title
        )
        for {
            _ <- db.run {
                wikiInfoQuery += newWikiInfo
            }
            _ <- auxiliaryTextService.addNewEntity(newWikiInfo.id, dto.auxilaryTexts)
            _ <- categoryService.addNewEntity(newWikiInfo.id, dto.categories)
        } yield {
            // okay
        }
    }
}
