package service

import model.{Category, CategoryRepo, WikiInfoByCategoryRel, WikiInfoByCategoryRelRepo}
import slick.jdbc.JdbcBackend.Database

import java.util.UUID
import scala.concurrent.ExecutionContext

class CategoryService(val db: Database)(implicit val ec: ExecutionContext) extends IBaseMethodService[Category, WikiInfoByCategoryRel] {
    
    override protected type TableType = CategoryRepo.CategoryTable
    override protected type TableRelType = WikiInfoByCategoryRelRepo.WikiInfoByCategoryRelTable
    
    override protected def tableQuery = CategoryRepo.tableQuery
    
    override protected def tableRelQuery = WikiInfoByCategoryRelRepo.tableQuery
    
    override protected def generateEntity(text: String): Category = {
        Category(text = text)
    }
    
    override protected def genarateEntityRel(wikiInfoId: UUID, entityIds: Seq[UUID]): Seq[WikiInfoByCategoryRel] = {
        entityIds.map { id =>
            WikiInfoByCategoryRel(wikiInfoId = wikiInfoId, entityId = id)
        }
    }
}
