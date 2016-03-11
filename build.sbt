import AssemblyKeys._

name := "scala-rest-server"

version := "0.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.7.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.5",
  "io.spray" %% "spray-can" % "1.3.3",
  "io.spray" %% "spray-routing-shapeless2" % "1.3.3",
  "io.spray" %% "spray-json" % "1.3.2",
  "io.spray" %% "spray-testkit" % "1.3.3",
  "com.typesafe" % "config" % "1.2.1",
  "com.ibm" %% "couchdb-scala" % "0.7.0",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

testOptions in Test ++= Seq(
  // Output Scalatest html reports
  Tests.Argument(TestFrameworks.ScalaTest, "-h","target/html-test-report"),
  // Output JUnit-style xml reports for consumption by automated tooling (e.g. Jenkins)
  Tests.Argument(TestFrameworks.ScalaTest,"-u","target/unit-test-reports"),
  // Output test details to the console as they are executing
  Tests.Argument(TestFrameworks.ScalaTest,"-o")
)

resolvers ++= Seq(
  "Spray repository" at "http://repo.spray.io",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

assemblySettings