package org.library.rest

import org.library.rest.LibraryResponse.AllDocumentsResponse
import org.scalatest.{GivenWhenThen, FeatureSpec}
import spray.httpx.SprayJsonSupport
import spray.testkit.ScalatestRouteTest


class LibraryServerTest extends FeatureSpec with GivenWhenThen with ScalatestRouteTest with SprayJsonSupport{
  import org.library.rest.Protocols._

  val documentEndpoint = "/document"

  var libraryServer: LibraryServer = {
    val server: LibraryServer = new LibraryServer(8000, "localhost")
    server.start()
    server
  }

  feature("Get Documents"){
    scenario("Get all documents") {
      Given("")
      When("Get all documents request receved")
      Get(documentEndpoint) ~> libraryServer.routes() ~> check {
        Then("Response with the list of all document is returned")
        assertResult(AllDocumentsResponse)
      }
    }
  }

}
