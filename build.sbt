import io.gatling.sbt.GatlingPlugin

name := "cb-monitoring"
version := "0.3"
scalaVersion := "2.11.8"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
enablePlugins(GatlingPlugin)

lazy val `cb-monitoring` = project.in(file("."))
  .settings(resolvers ++= Dependencies.additionalResolvers)
  .settings(libraryDependencies ++= Dependencies.akkaDependencies)
