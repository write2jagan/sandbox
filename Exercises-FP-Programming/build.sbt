
organization := "it.balyfix"

name := "efpi"

scalaVersion := "2.11.4"

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "2.3.12" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
