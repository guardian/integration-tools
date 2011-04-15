import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val JETTY_VERSION = "7.3.1.v20110307"

  val jettyWebapp = "org.eclipse.jetty" % "jetty-webapp" % JETTY_VERSION
  val jettyJsp = "org.eclipse.jetty" % "jetty-jsp-2.1" % JETTY_VERSION
  val moreJettyJsp = "org.mortbay.jetty" % "jsp-2.1-glassfish" % "2.1.v20100127"

  val ioFile = "com.github.scala-incubator.io" %% "file" % "0.1.1"

  override def managedStyle = ManagedStyle.Maven

  lazy val sourceArtifact = Artifact.sources(artifactID)
  override def packageSrcJar = defaultJarPath("-sources.jar")
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)

  val publishTo =
    Resolver.file("Guardian github Releases", Path.userHome / "guardian.github.com" / "maven" / "repo-releases" asFile)

}
