package org.library.rest

import org.library.rest.LibraryResponse.Success
import spray.json.DefaultJsonProtocol

object LibraryResponse {

  sealed trait LibraryResponse

  case class Success(message: String) extends LibraryResponse

}

object Protocols extends DefaultJsonProtocol {
  implicit val successRequestFormat = jsonFormat1(Success)
}
