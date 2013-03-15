package com.gu.integration

import dispatch.classic._
import dispatch.classic.thread.ThreadSafeHttpClient
import org.apache.http.params.HttpParams
import org.apache.http.conn.params.ConnRouteParams

/**
* Convinence functions for HTTP based testing.
* Not to be used in production code.
*/
object HttpClient {

  def get(uri: String): String = apply(url(uri))

  def get(uri: String, headers: Map[String, String] = Map(), params: Traversable[(String, String)] = Map()): String = apply(url(uri) <:< headers <<? params)

  def post(uri: String) = apply(url(uri) << Map())

  def post(uri: String, params: Traversable[(String, String)]) = apply(url(uri) << params)

  def postWithBody(uri: String, body: String, params: Traversable[(String, String)] = Map(), headers: Map[String, String] = Map()) = apply(url(uri) <:< headers << body <<? params)

  def put(uri: String, params: Traversable[(String, String)] = Map()) = apply(url(uri) <<< params)

  def post(uri: String, params: Traversable[(String, String)] = Map(), headers: Map[String, String] = Map()) = apply(url(uri) <:< headers << params)

  def delete(uri: String) = apply(url(uri).DELETE)

  def apply(request: Request) = ThreadSafeHttp(request as_str)

  object ThreadSafeHttp extends Http with thread.Safety {
    // This is there to NOT go through the proxy, as that won't recognise localhost addresses
    override def make_client = new ThreadSafeHttpClient(new Http.CurrentCredentials(None), maxConnections = 50, maxConnectionsPerRoute = 50) {
      override protected def configureProxy(params: HttpParams) = {
        ConnRouteParams.setDefaultProxy(params, null)
        params
      }
    }
  }
}
