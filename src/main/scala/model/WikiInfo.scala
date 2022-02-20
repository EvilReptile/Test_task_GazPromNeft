package model

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.time.Instant
import java.util.UUID

case class WikiInfo(id: UUID = UUID.randomUUID(),
                    createTimestamp: Instant,
                    timestamp: Instant,
                    language: String,
                    wiki: String,
                    title: String)

object WikiInfoRepo {
    class WikiInfoTable(tag: Tag)
        extends Table[WikiInfo](tag, "wiki_info") {
        
        val id = column[UUID]("id", O.PrimaryKey)
        val createTimestamp = column[Instant]("create_timestamp")
        val timestamp = column[Instant]("timestamp")
        val language = column[String]("language")
        val wiki = column[String]("wiki")
        val title = column[String]("title")
        
        def * = (
            id,
            createTimestamp,
            timestamp,
            language,
            wiki,
            title
        ) <> ((WikiInfo.apply _).tupled, WikiInfo.unapply)
    }
    
    val tableQuery = TableQuery[WikiInfoTable]
}
