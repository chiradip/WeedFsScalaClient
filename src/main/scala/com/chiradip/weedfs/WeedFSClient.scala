package com.chiradip.weedfs

/**
 * Created with IntelliJ IDEA.
 * User: chiradip
 * Date: 11/23/13
 * Time: 9:40 AM
 * To change this template use File | Settings | File Templates.
 */
import com.google.gson.reflect.TypeToken
import java.io.{File, InputStream}
import java.util
import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.{HttpDelete, HttpGet, CloseableHttpResponse}
import org.apache.http.entity.ContentType
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.HttpEntity
import scala.collection.mutable.Map
import org.slf4j.LoggerFactory

class WeedFSClient {
  import org.apache.http.client.methods.HttpPost
  import org.apache.http.entity.mime.HttpMultipartMode
  import org.apache.http.entity.mime.content.AbstractContentBody
  import org.apache.http.entity.mime.content.ByteArrayBody
  import org.apache.http.entity.mime.content.InputStreamBody
  import org.apache.http.entity.mime.content.StringBody
  import org.apache.http.impl.client.HttpClients
  import org.apache.http.util.EntityUtils
  import com.google.gson.Gson
  import org.apache.http.entity.mime.content.FileBody

  val logger = LoggerFactory.getLogger(this.getClass)

  def uploadWithFid(data: AnyRef, url: String, fid: String, contentType: ContentType, fileName: String): Map[String, String] = {
    uploadCommon(data, url + "/" + fid, contentType, fileName)
  }

  def uploadWithoutFid(data: AnyRef, url: String, contentType: ContentType, fileName: String): Map[String, String] = {
    uploadCommon(data, url + "/submit", contentType, fileName)
  }

  def uploadCommon(data: AnyRef, url: String, contentType: ContentType, fileName: String): Map[String, String] = {
    import scala.collection.JavaConversions._
    val client = HttpClients.createDefault()

    val post = new HttpPost("http://" + url)
    try {
      lazy val fileBody: AbstractContentBody = data match {
        case is: InputStream => new InputStreamBody(is, contentType, fileName)
        case byteArr: Array[Byte] => new ByteArrayBody(byteArr, contentType, fileName)
        case file: File => new FileBody(file, contentType, fileName)
        case _ => throw new Exception("Not a valid file, data or stream")
      }

      val reqEntity: HttpEntity = MultipartEntityBuilder.create()
        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
        .addPart("fileBody", fileBody)
        .addPart("fileName", new StringBody(fileName,ContentType.TEXT_PLAIN))
        .build()

      post.setEntity(reqEntity)
      logger.debug(s"executing request ${post.getRequestLine}")
      val response: CloseableHttpResponse = client.execute(post)

      try {
        var res = "{code: error}"
        val resEntity = response.getEntity
        if(resEntity != null) {
          logger.debug(s"Response content length: ${resEntity.getContentLength}")
          res = IOUtils.toString(resEntity.getContent)
          logger.debug(s"Response: ${res}")
        }
        EntityUtils.consume(resEntity)
        val mapType = new TypeToken[util.HashMap[String, String]]() {}.getType
        val map: util.HashMap[String, String] = new Gson().fromJson(res, mapType)
        val scalaMap: Map[String, String] = map
        scalaMap
      }finally {
        response.close
      }
    }finally {
      client.close
    }
  }

  def getFileAssignment(masterServer: String): Map[String, String] = {
    import scala.collection.JavaConversions._
    val client = HttpClients.createDefault()
    try {
      val get = new HttpGet("http://" + masterServer + "/dir/assign")
      val response = client.execute(get)
      try {
        var res = "{code: error}"
        val resEntity = response.getEntity
        if(resEntity != null) {
          res = IOUtils.toString(resEntity.getContent)
        }
        EntityUtils.consume(resEntity)
        val mapType = new TypeToken[util.HashMap[String, String]]() {}.getType
        val map: util.HashMap[String, String] = new Gson().fromJson(res, mapType)
        val scalaMap: Map[String, String] = map
        scalaMap
      }finally {
        response.close
      }
    }finally {
      client.close
    }
  }
  def deleteFile(fileLocation: String): Map[String, String] = {
    import scala.collection.JavaConversions._
    val client = HttpClients.createDefault()
    try {
      val get = new HttpDelete(fileLocation)
      val response = client.execute(get)
      try {
        var res = "{code: error}"
        val resEntity = response.getEntity
        if(resEntity != null) {
          res = IOUtils.toString(resEntity.getContent)
        }
        EntityUtils.consume(resEntity)
        val mapType = new TypeToken[util.HashMap[String, String]]() {}.getType
        val map: util.HashMap[String, String] = new Gson().fromJson(res, mapType)
        val scalaMap: Map[String, String] = map
        scalaMap
      }finally {
        response.close
      }
    }finally {
      client.close
    }
  }
}