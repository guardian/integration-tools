import java.util.jar._

name := "integration-tools"

crossScalaVersions in ThisBuild := Seq("2.8.1", "2.9.0-1")

// doing "in ThisBuild" makes this default setting for all projects in this build
version in ThisBuild := "1.2"

organization in ThisBuild := "com.gu"

scalaVersion := "2.9.0-1"

resolvers in ThisBuild ++= Seq(
  "jboss" at "http://repository.jboss.org/maven2/"
)


libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
    val JETTY_VERSION = "7.3.1.v20110307"
    val ioVersionMap = Map("2.8.1" -> "0.1.1", "2.9.0-1" -> "0.1.2")
    val IO_VERSION = ioVersionMap.getOrElse(sv, error("Unsupported Scala version " + sv))
    Seq(
        "org.eclipse.jetty" % "jetty-webapp" % JETTY_VERSION,
        "org.eclipse.jetty" % "jetty-jsp-2.1" % JETTY_VERSION,
        "org.mortbay.jetty" % "jsp-2.1-glassfish" % "2.1.v20100127",
        "com.github.scala-incubator.io" %% "file" % IO_VERSION
    )
}

packageOptions in ThisBuild <+= (version, name) map { (v, n) =>
  Package.ManifestAttributes(
    Attributes.Name.IMPLEMENTATION_VERSION -> v,
    Attributes.Name.IMPLEMENTATION_TITLE -> n,
    Attributes.Name.IMPLEMENTATION_VENDOR -> "guardian.co.uk"
  )
}

publishArtifact := true

publishTo in ThisBuild <<= (version) { version: String =>
    val publishType = if (version.endsWith("SNAPSHOT")) "snapshots" else "releases"
    Some(
        Resolver.file(
            "guardian github " + publishType,
            file(System.getProperty("user.home") + "/guardian.github.com/maven/repo-" + publishType)
        )
    )
}

scalacOptions in ThisBuild += "-deprecation"



