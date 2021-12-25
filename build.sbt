name := """annualcalculator"""
organization := "anchor"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.7"

libraryDependencies += guice
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.12.0"
libraryDependencies += "org.specs2" %% "specs2-core" % "4.8.3" % "test"
libraryDependencies += "org.specs2" %% "specs2-junit" % "4.8.3" % "test"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

