import sbt._
import Keys._

object SpecusBuild extends Build {
	import Resolvers._
	import Dependencies._	
	import BuildSettings._

	val commonDependencies = Seq(netty,specs2,mockito,lift_json,slf4s)
	val commonResolvers = Seq(akkaRepo,jbossRepo)

	val commonSettings = buildSettings ++ Seq(
		resolvers := commonResolvers,
		libraryDependencies := commonDependencies
	)
	
	lazy val specus = Project("specus",file("."),settings = buildSettings) aggregate (server_api,server,node,node_api)
	
	lazy val server = Project(
		"server",
		file("server"),
		settings = commonSettings
	) dependsOn (server_api,common_api)
	
	lazy val server_api = Project(
		"server_api",
		file("server_api"),
		settings = commonSettings
	) dependsOn (common_api)
	
	lazy val node = Project(
		"node",
		file("node"),
		settings = commonSettings
	) dependsOn (node_api, common_api)
	
	lazy val node_api = Project(
		"node",
		file("node_api"),
		settings = commonSettings
	) dependsOn (common_api)

  lazy val common_api = Project(
    "common_api",
    file("common_api"),
    settings = buildSettings
  )
}

object BuildSettings {
  val buildVersion      = "0.0.0"
  val buildScalaVersion = "2.9.0-1"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    version      := buildVersion,
    scalaVersion := buildScalaVersion
  )
}


object Resolvers {
  val jbossRepo = "jBoss repository" at "https://repository.jboss.org/nexus/content/repositories/releases/"
  val akkaRepo  = "Akka Repo" at "http://akka.io/repository"
}

object Dependencies {
  val netty = "org.jboss.netty" % "netty" % "3.2.4.Final" //Apache2
  val slf4s = "com.weiglewilczek.slf4s" %% "slf4s" % "1.0.6"
  val lift_json = "net.liftweb" %% "lift-json" % "2.4-M2"

  val specs2 = "org.specs2" %% "specs2" % "1.3" % "test" //custom, bascially anything goes (https://github.com/etorreborre/specs2/blob/1.4/LICENSE.txt)
  val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test" //MIT
	
}