import ReleaseTransformations._

name := "integration-tools"

organization := "com.gu"

scalaVersion := "2.11.7"

scalacOptions += "-deprecation"

resolvers ++= Seq(
  "jboss" at "http://repository.jboss.org/maven2/"
)

libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
    val JETTY_VERSION = "8.1.11.v20130520"
    Seq(
        "org.eclipse.jetty" % "jetty-webapp" % JETTY_VERSION,
        "org.eclipse.jetty" % "jetty-jsp" % JETTY_VERSION,
        "net.databinder" %% "dispatch-http" % "0.8.10",
        "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
    )
}

publishArtifact := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  ReleaseStep(action = Command.process("publishSigned", _)),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
  pushChanges
)
