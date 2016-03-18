package org

package object library {

  case class Document(title: String, documentType: String, tags: Set[String], content: String)

  sealed trait DocumentId

  case class EmptyId(errorMessage : String) extends DocumentId

  case class ValidId(id : String) extends DocumentId

}
