import play.PlayJava

name := "wanzi-sys"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  "com.google.inject" % "guice" % "3.0",
  "net.vz.mongodb.jackson" % "play-mongo-jackson-mapper_2.10" % "1.1.0",
  "com.thoughtworks.xstream" % "xstream" % "1.4.3",
  "com.google.zxing" % "core" % "3.3.0",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2"
)

libraryDependencies += "com.google.code.gson" % "gson" % "2.2"

libraryDependencies += "com.typesafe.play" % "play-json_2.11" % "2.3.10"

libraryDependencies += "com.typesafe.play" %% "play-mailer" % "2.4.1"

javaOptions in Test += "-Dconfig.file=conf/test.conf"
