import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import controller.WikiInfoController
import service.{AuxiliaryTextService, CategoryService, WikiInfoService}
import slick.jdbc.JdbcBackend.Database

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object Server extends App {
    
    implicit val system: ActorSystem = ActorSystem("app")
    implicit val materializer: Materializer = Materializer.matFromSystem
    implicit val executionContext: ExecutionContext =
        ExecutionContext.fromExecutor(Executors.newFixedThreadPool(16))
    
    // В идеале настроить DI, чтобы такие моменты отсутствовали
    val db = Database.forConfig("postgres")
    val categoryService = new CategoryService(db)
    val auxiliaryTextService = new AuxiliaryTextService(db)
    val wikiInfoService = new WikiInfoService(db, auxiliaryTextService, categoryService)
    
    val route = new WikiInfoController(wikiInfoService).routes
    
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    
    println("Server running to http://localhost:8080")
}
