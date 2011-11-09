import java.util.jar._

name := "integration-tools"

organization := "com.gu"

version:= "1.4-SNAPSHOT"

crossScalaVersions := Seq("2.8.1", "2.9.0-1", "2.9.1")

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "jboss" at "http://repository.jboss.org/maven2/"
)


libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
    val JETTY_VERSION = "7.3.1.v20110307"
    val scalaIncubatorIO =
      sv match {
        case "2.8.1" => "com.github.scala-incubator.io" %% "file" % "0.1.1"
        case "2.9.0-1" => "com.github.scala-incubator.io" %% "file" % "0.1.2"
        case "2.9.1" => "com.github.scala-incubator.io" %% "scala-io-file" % "0.2.0"
      }
    Seq(
        "org.eclipse.jetty" % "jetty-webapp" % JETTY_VERSION,
        "org.eclipse.jetty" % "jetty-jsp-2.1" % JETTY_VERSION,
        "org.mortbay.jetty" % "jsp-2.1-glassfish" % "2.1.v20100127",
        scalaIncubatorIO
    )
}

packageOptions <+= (version, name) map { (v, n) =>
  Package.ManifestAttributes(
    Attributes.Name.IMPLEMENTATION_VERSION -> v,
    Attributes.Name.IMPLEMENTATION_TITLE -> n,
    Attributes.Name.IMPLEMENTATION_VENDOR -> "guardian.co.uk"
  )
}

publishArtifact := true

publishTo <<= (version) { version: String =>
    val publishType = if (version.endsWith("SNAPSHOT")) "snapshots" else "releases"
    Some(
        Resolver.file(
            "guardian github " + publishType,
            file(System.getProperty("user.home") + "/guardian.github.com/maven/repo-" + publishType)
        )
    )
}

scalacOptions += "-deprecation"



