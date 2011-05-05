import sbt._

class SpecusProject(info:ProjectInfo) extends ParentProject(info) with IdeaProject{


  lazy val api = project("api","Specus API", new DefaultProject(_) with IdeaProject)
  lazy val server = project("server","Specus Server", new ServerProject(_),api)
  lazy val node = project("node","Specus Node",new NodeProject(_) with IdeaProject,api)

  val twitterRepo = "Twitter maven repository" at "http://maven.twttr.com/"
  val netty = "org.jboss.netty" % "netty" % "3.2.4.Final" //Apache2


  class ServerProject(info:ProjectInfo) extends DefaultProject(info) with IdeaProject with AkkaProject{
    val jbossRepo = "jBoss repository" at "https://repository.jboss.org/nexus/content/repositories/releases/"
    val configgy = "net.lag" % "configgy" %  "1.6.10" //from twitter repo, Apache2
    val akkaRemote = akkaModule("remote")
  }

  class NodeProject(info:ProjectInfo) extends DefaultProject(info) with IdeaProject with AkkaProject{
    val configgy = "net.lag" % "configgy" %  "1.6.10" //from twitter repo, Apache2
    val akkaRemote = akkaModule("remote")
  }

}

