package org.library.rest

import org.library.rest.LibraryResponse._
import spray.json.DefaultJsonProtocol
import scala.collection.JavaConverters._

import org.library.Document

object LibraryResponse {

  sealed trait LibraryResponse

  case class Success(message: String) extends LibraryResponse

  case class AllDocumentsResponse(documents: Seq[Document]) extends LibraryResponse

  case class AddDocumentSuccess(id : String) extends LibraryResponse

  case class AddDocumentFailed(message : String) extends LibraryResponse

  sealed trait LibraryRequest

  case class AddDocumentRequest(document: Document) extends LibraryRequest

}

object Protocols extends DefaultJsonProtocol {
  implicit val successRequestFormat = jsonFormat1(Success)
  implicit val documentResponseFormat = jsonFormat4(Document)
  implicit val allDocumentResponseFormat = jsonFormat1(AllDocumentsResponse)
  implicit val addDocumentRequest = jsonFormat1(AddDocumentRequest)
  implicit val addDocumentSuccess = jsonFormat1(AddDocumentSuccess)
  implicit val addDocumentFailed = jsonFormat1(AddDocumentFailed)
}
