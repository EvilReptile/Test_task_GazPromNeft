package model

import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import java.util.UUID

case class Category(id: UUID = UUID.randomUUID(),
                    text: String)

object CategoryRepo {
    class CategoryTable(tag: Tag)
        extends Table[Category](tag, "category") {
        
        val id = column[UUID]("id", O.PrimaryKey)
        val text = column[String]("text")
        
        def * = (
            id,
            text
        ) <> ((Category.apply _).tupled, Category.unapply)
    }
    
    val tableQuery = TableQuery[CategoryTable]
}
