package model

import slick.jdbc.PostgresProfile.api._

import java.util.UUID

case class WikiInfoByAuxiliaryTextRel(id: UUID = UUID.randomUUID(),
                                      wikiInfoId: UUID,
                                      entityId: UUID)

object WikiInfoByAuxiliaryTextRelRepo {
    class WikiInfoByAuxiliaryTextRelTable(tag: Tag)
        extends Table[WikiInfoByAuxiliaryTextRel](tag, "wiki_info_by_auxiliary_text_rel") {
        
        val id = column[UUID]("id", O.PrimaryKey)
        val wikiInfoId = column[UUID]("wiki_info_id")
        val entityId = column[UUID]("entity_id")
        
        override def * = (
            id,
            wikiInfoId,
            entityId
        ) <> ((WikiInfoByAuxiliaryTextRel.apply _).tupled, WikiInfoByAuxiliaryTextRel.unapply)
    }
    
    val tableQuery = TableQuery[WikiInfoByAuxiliaryTextRelTable]
}
