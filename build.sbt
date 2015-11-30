import ReleaseTransformations._

name := "integration-tools"

organization := "com.gu"

scalaVersion := "2.11.7"

scalacOptions += "-deprecation"

resolvers ++= Seq(
  "jboss" at "http://repository.jboss.org/maven2/"
)

libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
    val JETTY_VERSION = "9.3.6.v20151106"
    Seq(
        "org.eclipse.jetty" % "jetty-webapp" % JETTY_VERSION,
        "org.eclipse.jetty" % "apache-jsp" % JETTY_VERSION,
        "net.databinder" %% "dispatch-http" % "0.8.10",
        "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
    )
}

publishArtifact := true

scmInfo := Some(ScmInfo(url("https://github.com/guardian/integration-tools"),
  "scm:git:git@github.com:guardian/integration-tools.git"))

description := "Simple scala wrappers to make it easy to kick up web apps under jetty, ideal for integration tests"

licenses := Seq("Apache V2" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

pomExtra := {
  <url>https://github.com/guardian/feature-switching</url>
    <developers>
      <developer>
        <id>theguardian</id>
        <name>The Guardian</name>
        <url>https://github.com/guardian</url>
      </developer>
    </developers>
}

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
