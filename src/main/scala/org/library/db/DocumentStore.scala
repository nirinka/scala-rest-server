package org.library.db

import com.ibm.couchdb._
import com.typesafe.config.ConfigFactory
import org.library.{ValidId, EmptyId, DocumentId, Document}
import scalaz._

class DocumentStore(dbPort : Int, dbHost : String, dbName : String) {

  val typeMapping = TypeMapping(classOf[Document] -> "Document")

  // Create a CouchDB client instance
  val couch = CouchDb(dbHost, dbPort)

  // Get an instance of the DB API by name and type mapping
  val db = couch.db(dbName, typeMapping)

  def save(document: Document) : DocumentId = {
    val action = db.docs.create(document)
    action.attemptRun match {
      // In case of an error (left side of Either), print it
      case -\/(e) => EmptyId(e.getMessage)
      // In case of a success (right side of Either), print each object
      case \/-(a) => ValidId(a.id)
    }
  }

  def getAll: Seq[Document] = {
    val getAction = for {docs <- db.docs.getMany.queryIncludeDocs[Document]} yield docs.getDocsData
    getAction.attemptRun match {
      // In case of an error (left side of Either), print it
      case -\/(e) => Seq()
      // In case of a success (right side of Either), print each object
      case \/-(a) => a
    }
  }
}

object DocumentStore{

  val config = ConfigFactory.load()

  val dbPort = config.getInt("db.port")
  val dbHost = config.getString("db.host")
  val dbName = config.getString("db.name")

  var documentStore = new DocumentStore(dbPort, dbHost, dbName)

  def save(document : Document) = documentStore.save(document)

  def getAll : Seq[Document] = documentStore.getAll


}
