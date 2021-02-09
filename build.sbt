name := "sat"

version := "1.0"

scalaVersion := "2.12.0"

assemblyJarName in assembly := "sat.jar"

mainClass in assembly := Some("frank.app.MainApp")

libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.0.0" % "test")
