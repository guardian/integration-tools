package com.gu.integration


/**
 * This class represents your end to end application, which may
 * be a mix of muliple AppServers with multiple WebApps within
 * each.
 */
abstract class MultiAppServer extends Startable {
  def appServers: List[AppServer]

  def start() {
    appServers.foreach(_.start())
  }

  def stop() {
    appServers.reverse.foreach(_.stop())
  }
}