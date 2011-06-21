import sbt._

object Plugins extends Build {
  lazy val root = Project("root", file(".")) dependsOn(
	  uri("git://github.com/eed3si9n/sbt-assembly.git"),
	  uri("git://github.com/ijuma/sbt-idea.git#sbt-0.10")
    )
}
