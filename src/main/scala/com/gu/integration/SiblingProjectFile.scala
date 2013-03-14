package com.gu.integration

import scalax.file.Path


/**
 * Class implements the code that can "find" a path in a sibling project.
 * This works around the complexity that build tools and ide's may set the
 * current directory either to the parent project or to a subdirectory underneath.
 *
 * Actually with sbt this may not be so necessary as it seems to agree with
 * IntelliJ and always sets the current directory to the root of the project,
 * but this may change if you do sbt sub projects or if you're using maven.
 */
object SiblingProjectFile {
  def apply(desiredPath: Path): Path = {
    val currentDirectory = (Path fromString ".").toAbsolute.normalize
    val directoriesToCheck = currentDirectory :: currentDirectory.parents.toList
    val possiblePaths = directoriesToCheck map { _ / desiredPath }
    possiblePaths.find(_ exists) getOrElse
      sys.error("Could not find absolute path for " + desiredPath.path + "\n searched in: " + possiblePaths.map(_.path) )
  }
}







