package com.gu.integration

import scalax.file.Path._
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.server.Handler


trait WebApp {
  def handler: Handler
  def displayName: String

  def preStart() {}
  def postStart() {}
}


abstract class WarWebApp extends WebApp {
  def warPath: String
  // todo: should default to the name of the war
  def contextPath: String

  lazy val displayName = contextPath
  private lazy val war = SiblingProjectFile(warPath)

  lazy val handler = {
    val context = new WebAppContext
    context setWar war.path
    context setContextPath contextPath
    context
  }
}

/**
 * This class is designed to make it easy to startup the output war of another
 * project, typically for integration testing
 *
 * pathToProject should be something like "identity-webapp/identity"
 * this class will then hunt for the src/main/webapp/WEB-INF/web.xml
 * starting at from the current directory and trying all parent directories
 * until it finds it.
 *
 * It will then start jetty with this web.xml. You'd normally put a
 * build-tool level dependency on the classes referenced by web.xml
 * on the calling project, so all other data is found by classpath
 * lookup.
 *
 * See http://stackoverflow.com/questions/805280/loading-up-a-web-xml-for-integration-tests-with-jetty
 * for the inspiration for this code.
 */
abstract class ClasspathWebApp extends WebApp {
  protected def srcPath: String
  protected def contextPath = "/" + srcPath

  lazy val displayName = contextPath

  private val webXmlPath = "WEB-INF/web.xml"
  private lazy val webXmlLocation = SiblingProjectFile(srcPath / "src/main/webapp" / webXmlPath)
  private lazy val projectRoot = webXmlLocation.path.stripSuffix(webXmlPath)

  lazy val handler = {
    val context = new WebAppContext
    context setWar projectRoot
    context setContextPath contextPath
    context
  }

}
