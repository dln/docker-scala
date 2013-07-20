seq(lsSettings: _*)

organization := "org.eintr.docker"

name := "docker-remote"

version := "0.0.1-SNAPSHOT"

description := "Scala client library for the Docker remote API"

licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

scalaVersion := "2.10.2"

crossScalaVersions := Seq("2.9.3", "2.10.2")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-encoding", "utf8")

libraryDependencies := Seq(
  "org.json4s"         %% "json4s-native"      % "3.2.4",
  "org.scalaj"         %% "scalaj-http"        % "0.3.9" exclude("junit", "junit")
)

publishMavenStyle := true

publishArtifact in Test := false

publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { x => false }

pomExtra := (
  <url>http://github.com/dln/docker-scala</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:dln/docker-scala.git</url>
    <connection>scm:git:git@github.com:dln/docker-scala.git</connection>
  </scm>
  <developers>
    <developer>
      <id>dln</id>
      <name>Daniel Lundin</name>
      <url>http://github.com/dln</url>
    </developer>
  </developers>
)
