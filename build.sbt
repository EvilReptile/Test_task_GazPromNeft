import sbt.Keys.libraryDependencies

name := "Test_task"

version := "0.1"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % "10.2.7",
    "com.typesafe.akka" %% "akka-stream" % "2.6.8",
    "com.typesafe.play" %% "play-json" % "2.7.4",
    "de.heikoseeberger" %% "akka-http-play-json" % "1.38.2",
    "com.themillhousegroup" %% "scoup" % "1.0.0",
    "com.typesafe.slick" %% "slick" % "3.3.3",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
    "org.postgresql" % "postgresql" % "9.4-1206-jdbc42"
)

scalacOptions += "-language:postfixOps"