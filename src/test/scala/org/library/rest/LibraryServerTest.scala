package org.library.rest

import org.http4s.blaze.http.http_parser.BaseExceptions.BadRequest
import org.library.{EmptyId, ValidId, Document}
import org.library.db.DocumentStore
import org.library.rest.LibraryResponse._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{GivenWhenThen, FeatureSpec}
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport
import spray.testkit.ScalatestRouteTest
import org.mockito.Mockito


class LibraryServerTest extends FeatureSpec with GivenWhenThen with ScalatestRouteTest with SprayJsonSupport with MockitoSugar{
  import org.library.rest.Protocols._

  val documentEndpoint = "/document"
  val documentStore = mock[DocumentStore]
  val document = Document("Good Comment", "Comment", Set("good", "comment"), "This is a good comment indeed")

  var libraryServer: LibraryServer = {
    val server: LibraryServer = new LibraryServer(8000, "localhost", documentStore)
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

  feature("Add Document"){
    scenario("Return newly created document id on create document request"){
      val document = Document("test", "note", Set("test"), "Test note")
      Mockito.when(documentStore.save(document)).thenReturn(ValidId("id"))
      When("Request to create a document recieved")
      Post(documentEndpoint, AddDocumentRequest(document)) ~> libraryServer.routes() ~> check {
        Then("Response should return document id")
        assertResult(AddDocumentSuccess("id"), "Should return id")(responseAs[AddDocumentSuccess])
      }
    }

    scenario("Return status code 400 if document creation failed"){
      val document = Document("test", "note", Set("test"), "Test note")
      Mockito.when(documentStore.save(document)).thenReturn(EmptyId("Error message"))
      When("Request to create a document recieved")
      Post(documentEndpoint, AddDocumentRequest(document)) ~> libraryServer.routes() ~> check {
        Then("Response should return error message and status code 400")
        status === StatusCodes.BadRequest
        assertResult(AddDocumentFailed("Error message"), "Should return error message")(responseAs[AddDocumentFailed])
      }
    }
  }

}
