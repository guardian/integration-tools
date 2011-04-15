package com.gu.integration

import java.net.Socket
import java.io.IOException
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.HandlerCollection

/**
 * Represents an application server (jetty in this case but don't rely on it)
 * running on a particular port.
 *
 * Give the app server a list of webapps to run.
 *
 * Call start - and it will start until you call stop.
 */
abstract class AppServer extends Startable {
  protected def apps: List[WebApp]
  protected def port = 8080

  private lazy val jettyServer = {
    val handlers = new HandlerCollection
    handlers.setHandlers(apps.map(_.handler).toArray)

    val s = new Server(port)
    s setHandler handlers
    s
  }

  lazy val displayName = apps.map(_.displayName).mkString(", ") + " on port " + port

  def start() {
    if (portIsInUse)
      println("********* NOT STARTING " + displayName + " BECAUSE PORT IS IN USE! *********")
    else {
      println("********* STARTING " + displayName + " *********")
      apps.foreach(_.preStart())
      jettyServer.start()
      apps.foreach(_.postStart())
      println("********* STARTED " + displayName + " *********")
    }

  }

  def stop() {
    println("********* STOPPING " + displayName + " *********")
    jettyServer.stop()
    println("********* STOPPED " + displayName + " *********")
  }

  private def portIsInUse = {
    try {
      new Socket("localhost", port).close()
      true
    } catch {
      case e: IOException => false
    }
  }

}







/**
 * Class implements the code that can "find" a path in a sibling project.
 * This works around the complexity that build tools and ide's may set the
 * current directory either to the parent project or to a subdirectory underneath.
 *
 * Actually with sbt this may not be so necessary as it seems to agree with
 * IntelliJ and always sets the current directory to the root of the project,
 * but this may change if you do sbt sub projects or if you're using maven.
 */


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

