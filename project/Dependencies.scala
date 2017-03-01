import DependenciesVersions._
import sbt._

object Dependencies {
  val akkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-contrib" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "org.json4s" %% "json4s-core" % json4s,
    "org.json4s" %% "json4s-native" % json4s,
    "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.5" % "test",
  "io.gatling"            % "gatling-test-framework"    % "2.1.5" % "test"
  )

  val additionalResolvers = Seq(
    Resolver.mavenLocal,
    "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"
  )
}
