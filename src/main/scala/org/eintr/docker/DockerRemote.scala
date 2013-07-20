// Copyright (c) 2013. Daniel Lundin.

package org.eintr.docker

import org.json4s._
import org.json4s.native.JsonMethods._
import scalaj.http.Http
import java.net.URL

sealed case class Container(
    Id: String,
    Image: String,
    Command: String,
    Created: Long,
    Status: String,
    Ports: String,
    SizeRw: Long,
    SizeRootFs: Long)

sealed case class ContainerConfig(
    Image: String,
    Cmd: List[String],
    Hostname: String = "",
    User: String = "",
    Memory: Long = 0,
    MemorySwap: Long = 0,
    CpuShares: Long = 0,
    AttachStdin: Boolean = false,
    AttachStdout: Boolean = true,
    OpenStdin: Boolean = true,
    StdinOnce: Boolean = false,
    PortSpecs: List[String] = Nil,
    Tty: Boolean = false,
    Env: List[String] = Nil,
    Dns: List[String] = Nil,
    Volumes: Map[String, String] = Map.empty,
    VolumesFrom: String = "",
    EntryPoint: List[String] = Nil)

sealed case class Image(
    Id: String,
    Created: Long,
    Size: Long,
    VirtualSize: Long,
    Repository: Option[String],
    Tag: Option[String])

sealed case class ImageInfo(
    id: String,
    parent: Option[String],
    comment: Option[String],
    created: String,
    container: Option[String],
    container_config: Option[ContainerConfig],
    Size: Long,
    architecture: String,
    docker_version: String)

class DockerRemote(hostname: String, port: Int) {
  implicit val formats = DefaultFormats
  val baseURL = new URL("http", hostname, port, "/v1.3")

  private def url(spec: String) = new URL(baseURL, spec).toString

  private def get(spec: String) = Http(url(spec))

  def images(all: Boolean = false): List[Image] = {
    get("/images/json")
    .param("all", all.toString)
    .apply(parse(_).extract[List[Image]])
  }

  def inspectImage(id: String): Option[ImageInfo] = {
    get("/images/" + id + "/json") match {
      case req if req.responseCode == 200 => req(s => Some(parse(s).extract[ImageInfo]))
      case _                              => None
    }
  }

  def containers(
      all: Boolean = false,
      limit: Option[Int] = None,
      since: Option[String] = None,
      before: Option[String] = None): List[Container] = {
    val req = get("/containers/json").param("all", all.toString)
    since foreach (req.param("since", _))
    before foreach (req.param("before", _))
    limit foreach (i => req.param("limit", i.toString))
    req(parse(_).extract[List[Container]])
  }
}

object DockerRemote {
  def apply(hostname: String = "localhost", port: Int = 4243) = new DockerRemote(hostname, port)
}
