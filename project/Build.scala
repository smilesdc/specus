import collection.immutable.Seq
import sbt._
import Keys._

object SpecusBuild extends Build {
	import Resolvers._
	import Dependencies._	
	import BuildSettings._

	val commonDependencies = Seq(netty,specs2,mockito,lift_json,logback,akka_actor,akka_remote)
	val commonResolvers = Seq(akkaRepo,jbossRepo,guiceyFruitRepo)

	val commonSettings = buildSettings ++ Seq(
		resolvers := commonResolvers,
		libraryDependencies := commonDependencies
	)
	
	lazy val specus = Project("specus",file("."),settings = buildSettings) aggregate (server_api,server,node,node_api,common_api)
	
	lazy val server = Project(
		"server",
		file("server"),
		settings = commonSettings ++ Seq(name := "server", version := "0.0.0")
	) dependsOn (server_api,common_api)
	
	lazy val server_api = Project(
		"server_api",
		file("server_api"),
		settings = commonSettings ++ Seq(name := "server_api", version := "0.0.0")
	) dependsOn (common_api)
	
	lazy val node = Project(
		"node",
		file("node"),
		settings = commonSettings ++ Seq(name := "node", version := "0.0.0")
	) dependsOn (node_api, common_api)
	
	lazy val node_api = Project(
		"node",
		file("node_api"),
		settings = commonSettings ++ Seq(name := "node_api", version := "0.0.0")
	) dependsOn (common_api)

  lazy val common_api = Project(
    "common_api",
    file("common_api"),
    settings = buildSettings ++ Seq(name := "common_api", version := "0.0.0")
  )
}

object BuildSettings {
  val buildVersion      = "0.0.0"
  val buildScalaVersion = "2.9.0-1"

  val localMvnPublishId = "localMvn"
  val localMvnPublishDir = ".mvn-local-publish-dir"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := "net.tomasherman",
    version := "0.0.0-SNAPSHOT",

    scalaVersion := buildScalaVersion,
    scalacOptions := Seq("-deprecation", "-unchecked"),
    //code to force publish-local to generate maven stuff rather then ivy stuff
    otherResolvers := Seq(Resolver.file(localMvnPublishId, file(localMvnPublishDir))),
    publishLocalConfiguration <<= (packagedArtifacts, deliverLocal, ivyLoggingLevel) map {
      (arts, _, level) => new PublishConfiguration(None,localMvnPublishId,arts,level )
    }
   )
}


object Resolvers {
  val jbossRepo = "jBoss repository" at "https://repository.jboss.org/nexus/content/repositories/releases/"
  val akkaRepo  = "Akka Repo" at "http://akka.io/repository"
  val guiceyFruitRepo = "GuicyFruit Repository" at "http://guiceyfruit.googlecode.com/svn/repo/releases"

}

object Dependencies {

  val akka_version = "1.1.3"
  val netty = "org.jboss.netty" % "netty" % "3.2.4.Final" withSources //Apache2
  val lift_json = "net.liftweb" %% "lift-json" % "2.4-M2"
  val logback = "ch.qos.logback" % "logback-classic" % "0.9.29"
  val specs2 = "org.specs2" %% "specs2" % "1.4" % "test" //custom, bascially anything goes (https://github.com/etorreborre/specs2/blob/1.4/LICENSE.txt)
  val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test" //MIT
  val akka_actor = "se.scalablesolutions.akka" % "akka-actor" % akka_version
  val akka_remote = "se.scalablesolutions.akka" % "akka-remote" % akka_version

}