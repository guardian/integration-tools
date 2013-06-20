import java.util.jar._

name := "integration-tools"

organization := "com.gu"

version:= "1.5-SNAPSHOT"

crossScalaVersions := Seq("2.9.2", "2.10.1")

scalaVersion := "2.10.1"

resolvers ++= Seq(
  "jboss" at "http://repository.jboss.org/maven2/"
)


libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
    val JETTY_VERSION = "8.1.11.v20130520"
    Seq(
        "org.eclipse.jetty" % "jetty-webapp" % JETTY_VERSION,
        "org.eclipse.jetty" % "jetty-jsp" % JETTY_VERSION,
        "net.databinder" %% "dispatch-http" % "0.8.9",
        "com.github.scala-incubator.io" %% "scala-io-file" % (if (sv.startsWith("2.9.")) "0.4.1" else "0.4.2")
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



