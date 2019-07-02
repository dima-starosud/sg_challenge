name := "sg_challenge"

version := "0.1"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-feature", "-language:higherKinds")

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.33.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.3",
  "org.jsoup" % "jsoup" % "1.8.3"
)
