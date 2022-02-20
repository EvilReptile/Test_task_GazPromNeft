package model

import slick.jdbc.PostgresProfile.api._

import java.util.UUID

case class WikiInfoByCategoryRel(id: UUID = UUID.randomUUID(),
                                 wikiInfoId: UUID,
                                 entityId: UUID)

object WikiInfoByCategoryRelRepo {
    class WikiInfoByCategoryRelTable(tag: Tag)
        extends Table[WikiInfoByCategoryRel](tag, "wiki_info_by_category_rel") {
        
        val id = column[UUID]("id", O.PrimaryKey)
        val wikiInfoId = column[UUID]("wiki_info_id")
        val entityId = column[UUID]("entity_id")
        
        override def * = (
            id,
            wikiInfoId,
            entityId
        ) <> ((WikiInfoByCategoryRel.apply _).tupled, WikiInfoByCategoryRel.unapply)
    }
    
    val tableQuery = TableQuery[WikiInfoByCategoryRelTable]
}
