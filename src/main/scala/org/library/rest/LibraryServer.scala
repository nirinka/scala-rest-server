package org.library.rest

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import org.library.db.DocumentStore
import org.library.rest.LibraryResponse.Success
import spray.httpx.SprayJsonSupport
import spray.routing.SimpleRoutingApp

class LibraryServer(port : Int, host : String) extends SimpleRoutingApp with SprayJsonSupport {
  implicit val actorSystem = ActorSystem("library-service")

  val documentStore = DocumentStore

  def start(): Unit = {
    startServer(host, port)(routes())
  }

  def routes() = {
    import Protocols._
    logRequestResponse("libraryservice") {
      path("document") {
        get {
          complete(Success("Successful GET"))
        }~
        post {
          complete(Success("Successful POST"))
        }
      }
    }
  }

}

object LibraryServer extends SimpleRoutingApp{

  var server : LibraryServer = null

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()

    val port = config.getInt("libraryService.port")
    val host = config.getString("libraryService.host")

    server = new LibraryServer(port, host)
    server.start()
  }

}
