import sbt._

class SpecusProject(info:ProjectInfo) extends ParentProject(info) with IdeaProject{

  lazy val api = project("api","Specus API", new DefaultProject(_) with IdeaProject)

  lazy val server = project("server","Specus Server",new DefaultProject(_) with IdeaProject,api)
  lazy val node = project("node","Specus Node",new DefaultProject(_) with IdeaProject,api)


}
