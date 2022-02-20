package model

import slick.jdbc.PostgresProfile.api._

import java.util.UUID

case class AuxiliaryText(id: UUID = UUID.randomUUID(),
                         text: String)

object AuxiliaryTextRepo {
    class AuxiliaryTextTable(tag: Tag)
        extends Table[AuxiliaryText](tag, "auxiliary_text") {
        
        val id = column[UUID]("id", O.PrimaryKey)
        val text = column[String]("text")
        
        def * = (
            id,
            text
        ) <> ((AuxiliaryText.apply _).tupled, AuxiliaryText.unapply)
    }
    
    val tableQuery = TableQuery[AuxiliaryTextTable]
}
