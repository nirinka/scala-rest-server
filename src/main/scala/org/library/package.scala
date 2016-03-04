package org

package object library {

  case class Document(title: String, documentType: String, tags: Set[String], content: String)

}
