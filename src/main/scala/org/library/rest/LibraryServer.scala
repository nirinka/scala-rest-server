package org.library.rest

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import org.library.{EmptyId, ValidId}
import org.library.db.DocumentStore
import org.library.rest.LibraryResponse._
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport
import spray.routing.SimpleRoutingApp

class LibraryServer(port : Int, host : String, documentStore: DocumentStore) extends SimpleRoutingApp with SprayJsonSupport {
  implicit val actorSystem = ActorSystem("library-service")

  def start(): Unit = {
    startServer(host, port)(routes())
  }

  def routes() = {
    import Protocols._
    logRequestResponse("libraryservice") {
      path("document") {
        get {
          complete(AllDocumentsResponse(documentStore.getAll))
        }~
          (post & entity(as[AddDocumentRequest])) { addDocumentRequest => {
            documentStore.save(addDocumentRequest.document) match {
              case ValidId(documentId) => complete(AddDocumentSuccess(documentId))
              case EmptyId(message) => complete{StatusCodes.BadRequest -> AddDocumentFailed(message)}
            }
          }
        }
      }
    }
  }

}

object LibraryServer extends SimpleRoutingApp{

  var server : LibraryServer = null
  val documentStore = DocumentStore

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()

    val port = config.getInt("libraryService.port")
    val host = config.getString("libraryService.host")

    server = new LibraryServer(port, host, documentStore.documentStore)
    server.start()
  }

}
