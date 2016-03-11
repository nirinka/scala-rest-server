package org.library.db

import com.ibm.couchdb._
import com.typesafe.config.ConfigFactory
import org.library.Document
import scalaz._

class DocumentStore(dbPort : Int, dbHost : String, dbName : String) {

  val typeMapping = TypeMapping(classOf[Document] -> "Document")

  val comment = Document("Good Comment", "Comment", Set("good", "comment"), "This is a good comment indeed")

  // Create a CouchDB client instance
  val couch = CouchDb(dbHost, dbPort)

  // Get an instance of the DB API by name and type mapping
  val db = couch.db(dbName, typeMapping)

  def save(document: Document) = {
    val action = db.docs.create(document)
    action.attemptRun match {
      // In case of an error (left side of Either), print it
      case -\/(e) => println(e)
      // In case of a success (right side of Either), print each object
      case \/-(a) => println(a)
    }
  }

  def getAll: Seq[Document] = {
    println("get all")
    val getAction = for {docs <- db.docs.getMany.queryIncludeDocs[Document]} yield docs.getDocsData
    getAction.attemptRun match {
      // In case of an error (left side of Either), print it
      case -\/(e) => println(e)
      // In case of a success (right side of Either), print each object
      case \/-(a) => println(a)
    }
    Seq(comment)

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
