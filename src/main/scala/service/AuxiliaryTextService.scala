package service

import model.{AuxiliaryText, AuxiliaryTextRepo, WikiInfoByAuxiliaryTextRel, WikiInfoByAuxiliaryTextRelRepo}
import slick.jdbc.JdbcBackend.Database

import java.util.UUID
import scala.concurrent.ExecutionContext

class AuxiliaryTextService(val db: Database)(implicit val ec: ExecutionContext) extends IBaseMethodService[AuxiliaryText, WikiInfoByAuxiliaryTextRel] {
    
    override protected type TableType = AuxiliaryTextRepo.AuxiliaryTextTable
    override protected type TableRelType = WikiInfoByAuxiliaryTextRelRepo.WikiInfoByAuxiliaryTextRelTable
    
    override protected def tableQuery = AuxiliaryTextRepo.tableQuery
    
    override protected def tableRelQuery = WikiInfoByAuxiliaryTextRelRepo.tableQuery
    
    override protected def generateEntity(text: String): AuxiliaryText = {
        AuxiliaryText(text = text)
    }
    
    override protected def genarateEntityRel(wikiInfoId: UUID, entityIds: Seq[UUID]): Seq[WikiInfoByAuxiliaryTextRel] = {
        entityIds.map { id =>
            WikiInfoByAuxiliaryTextRel(wikiInfoId = wikiInfoId, entityId = id)
        }
    }
}
