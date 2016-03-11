package org.library.rest

import org.library.rest.LibraryResponse.{AllDocumentsResponse, Success}
import spray.json.DefaultJsonProtocol
import scala.collection.JavaConverters._

import org.library.Document

object LibraryResponse {

  sealed trait LibraryResponse

  case class Success(message: String) extends LibraryResponse

  case class AllDocumentsResponse(documents: Seq[Document]) extends LibraryResponse

}

object Protocols extends DefaultJsonProtocol {
  implicit val successRequestFormat = jsonFormat1(Success)
  implicit val documentResponseFormat = jsonFormat4(Document)
  implicit val allDocumentResponseFormat = jsonFormat1(AllDocumentsResponse)
}
