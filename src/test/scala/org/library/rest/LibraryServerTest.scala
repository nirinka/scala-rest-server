package org.library.rest

import org.library.Document
import org.library.db.DocumentStore
import org.library.rest.LibraryResponse.AllDocumentsResponse
import org.scalatest.mock.MockitoSugar
import org.scalatest.{GivenWhenThen, FeatureSpec}
import spray.httpx.SprayJsonSupport
import spray.testkit.ScalatestRouteTest
import org.mockito.Mockito


class LibraryServerTest extends FeatureSpec with GivenWhenThen with ScalatestRouteTest with SprayJsonSupport with MockitoSugar{
  import org.library.rest.Protocols._

  val documentEndpoint = "/document"
  val documentStore = mock[DocumentStore]
  val document = Document("Good Comment", "Comment", Set("good", "comment"), "This is a good comment indeed")

  var libraryServer: LibraryServer = {
    val server: LibraryServer = new LibraryServer(8000, "localhost")
    server.start()
    server
  }

  feature("Get Documents"){
    scenario("Get all documents") {
      Given("Library has one document")
      Mockito.when(documentStore.getAll).thenReturn(Seq(document))
      When("Get all documents request receved")
      Get(documentEndpoint) ~> libraryServer.routes() ~> check {
        Then("Response with the document from library is returned")
        assertResult(AllDocumentsResponse(Seq(document)), "Should get expected documents")(responseAs[AllDocumentsResponse])
      }
    }
  }

}
