Integration tools provides you with your working webapp inside a Scala test!

- no build scripts to startup
- starts once, keeps running for all your tests
- same tests work in your IDE or build scripts!
- clear declaritive interface for your tests dependencies

This library contains stuff that we find helpful when integration testing
our projects. Currently, it addresses the step-one problem of firing up
multiple web application under test.



Doesn't this belong on your build scripts?
------------------------------------------

Our experience is that everything gets much simpler when your tests are
standalone, and don't require shell scripts or build scripts to start.
This external dependency means it's harder to run the tests reliably,
and therefore the tests get run less regularly.

By putting setup where it belongs - in the "before test" construct of your
test runner - you can run individual tests anywhere, including in your ide.
If you have non-trivial setup that needs to happen before your webapp will start
(though of course its A Very Good Thing to avoid this) it's much easier to
write this code in your "real" test code, not in the xml of your build scipt
or a build script plugin.

As a by-product, you can run your tests from anywhere, including in your
IDE of choice.

Tools like the [maven failsafe plugin](http://maven.apache.org/plugins/maven-failsafe-plugin/)
attempt to make it easy to add integration tests to your build script. Many 
people have great success with this; our experience is that it led to a focus
on build scripts over a focus of delivering tests.

Limitations
-----------

  * Webapps are started using jetty. Not other container is supported currently.
  * No class loader hacking is performed: the web apps are loaded into the same classloader
as your tests. This will cause problems if you web apps share libraries that use
statics for state. (See note below on Google Guice.)

Getting started with simple-build-tool and scalatest
====================================================

1. in your sbt build script include:

        val guardianGithub = "Guardian Github Releases" at "http://guardian.github.com/maven/repo-releases"
        val integrationTools = "com.gu" %% "integration-tools" % "1.0" % "test"
    
2. you'll also to include [scalatest](http://www.scalatest.org):

        val scalatest = "org.scalatest" % "scalatest" % "1.3" % "test"

3. define the webapps your tests need to have started:

        import com.gu.integration._
        import scalax.file.Path

        object ContentApi extends AppServer with LazyStop {
          override val port = 8700
          lazy val apps = List(ContentApiWebApp, ContentApiExplorerWebApp, SolrWebApp)

          object ContentApiWebApp extends ClasspathWebApp {
            lazy val srcPath = "content-api"
            override lazy val contextPath = "/content-api"

            override def postStart() {
              LocalApiBootstrap.ensureIndexPopulated()
            }
          }

          object ContentApiExplorerWebApp extends ClasspathWebApp {
            lazy val srcPath = "content-api-explorer"
            override lazy val contextPath = "/content-api-explorer"
          }

          object SolrWebApp extends WarWebApp {
            lazy val warPath = "solr-server/src/main/solr-server/webapps/solr.war"
            override lazy val contextPath = "/solr"

            override def preStart() {
              System.setProperty("solr.solr.home",
                SiblingProjectFile("solr-server/src/main/solr-server/solr").path)
              System.setProperty("solr.data.dir", Path("integration/target/dependency/solr/data").toAbsolute.path)
              System.setProperty("master.enable", "true")
            }
          }
        }

4. create a trait that calls this before and after all tests:

        trait RequiresRunningContentApi extends Suite with BeforeAndAfterAll  {
          override protected def beforeAll() { ContentApi.start() }
          override protected def afterAll() { ContentApi.stopUnlessSomeoneCallsStartAgainSoon() }
        }

        // stopUnlessSomeoneCallsStartAgainSoon -> will shutdown after (by default) 2 seconds
        // if noone calls start in this time period. Normally your test runner will do this
        // milliseconds later if its running another test.

5. mix this trait into each of your tests that require the app under test to be running


Special note on Guice 2.0
-------------------------

Guice 2.0 uses a static member to communicate between the bootstrap and
the filter. This fails miserably if you attempt to start two guice applications
in the same classloader, as this helper will do. It looks like guice 3.0
fixes this problem.
