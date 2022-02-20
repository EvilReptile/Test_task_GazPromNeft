package service

import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}
import scala.language.reflectiveCalls

trait IBaseMethodService[Entity <: {def id: UUID; def text: String}, EntityRel <: {def wikiInfoId: UUID; def entityId: UUID}] {
    
    def db: Database
    
    implicit val ec: ExecutionContext
    
    private type WikiInfoAndEntity = {def wikiInfoId: Rep[UUID]; def entityId: Rep[UUID]}
    private type Id = {def id: Rep[UUID]; def text: Rep[String]}
    
    protected type TableType <: Table[Entity] with Id
    protected type TableRelType <: Table[EntityRel] with WikiInfoAndEntity
    
    protected def tableQuery: slick.lifted.TableQuery[TableType]
    protected def tableRelQuery: slick.lifted.TableQuery[TableRelType]
    
    def findWikiInfoByEntity(wikiInfoId: UUID): Future[Seq[Entity]] = {
        db.run {
            tableRelQuery.join(tableQuery).on(_.entityId === _.id)
                .filter { case (rel, _) => rel.wikiInfoId === wikiInfoId }
                .map { case (_, entity) => entity }
                .result
        }
    }
    
    def addNewEntity(wikiInfoId: UUID, entities: Seq[String]): Future[Unit] = {
        for {
            oldEntities <- db.run {
                tableQuery.filter(_.text inSetBind entities)
                    .result
            }
            newEntities = entities.filterNot { entity => oldEntities.map(_.text).contains(entity) }.map(generateEntity)
            _ <- db.run(tableQuery ++= newEntities)
            _ <- db.run(tableRelQuery ++= genarateEntityRel(wikiInfoId, (oldEntities ++ newEntities).map(_.id)))
        } yield {
            // okay
        }
    }
    
    protected def generateEntity(text: String): Entity
    
    protected def genarateEntityRel(wikiInfoId: UUID, entityIds: Seq[UUID]): Seq[EntityRel]
}
