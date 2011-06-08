import sbt._

class SpecusProject(info:ProjectInfo) extends ParentProject(info) with IdeaProject{


  lazy val sapi = project("server_api","Specus server API", new ServerApiProject(_))
  lazy val napi = project("node_api","Specus node API", new ServerApiProject(_))
  lazy val server = project("server","Specus Server", new ServerProject(_),sapi)
  lazy val node = project("node","Specus Node",new NodeProject(_) with IdeaProject,napi)

  override def shouldCheckOutputDirectories = false

  class ServerApiProject(info:ProjectInfo) extends DefaultProject(info) with IdeaProject with AkkaProject{
    val jbossRepo = "jBoss repository" at "https://repository.jboss.org/nexus/content/repositories/releases/"
    val netty = "org.jboss.netty" % "netty" % "3.2.4.Final" //Apache2
    val specs2 = "org.specs2" %% "specs2" % "1.3" % "test"
    val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test"

    def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
    override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)
  }

  class NodeApiProject(info:ProjectInfo) extends DefaultProject(info) with IdeaProject with AkkaProject{
    val jbossRepo = "jBoss repository" at "https://repository.jboss.org/nexus/content/repositories/releases/"
    val netty = "org.jboss.netty" % "netty" % "3.2.4.Final" //Apache2
    val specs2 = "org.specs2" %% "specs2" % "1.3" % "test"
    val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test"

    def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
    override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)
  }


  class ServerProject(info:ProjectInfo) extends DefaultProject(info) with IdeaProject with AkkaProject{
    val jbossRepo = "jBoss repository" at "https://repository.jboss.org/nexus/content/repositories/releases/"
    val netty = "org.jboss.netty" % "netty" % "3.2.4.Final" //Apache2

    val configgy = "net.lag" % "configgy" %  "1.6.10" //from twitter repo, Apache2
    val akkaRemote = akkaModule("remote")
    val specs2 = "org.specs2" %% "specs2" % "1.3" % "test"

    def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
    override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)
  }

  class NodeProject(info:ProjectInfo) extends DefaultProject(info) with IdeaProject with AkkaProject{
    val twitterRepo = "Twitter maven repository" at "http://maven.twttr.com/"

    val configgy = "net.lag" % "configgy" %  "1.6.10" //from twitter repo, Apache2
    val akkaRemote = akkaModule("remote")
  }

}

