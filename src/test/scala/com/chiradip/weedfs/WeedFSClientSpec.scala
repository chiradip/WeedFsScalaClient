package com.chiradip.weedfs

import org.scalatest.{GivenWhenThen, FeatureSpec}
import java.io.{FileInputStream, File}
import org.apache.http.entity.ContentType
import org.apache.commons.io.IOUtils
import scala.collection.mutable.ListBuffer

/**
 * Created with IntelliJ IDEA.
 * User: chiradip
 * Date: 11/23/13
 * Time: 9:55 AM
 * To change this template use File | Settings | File Templates.
 */
class WeedFSClientSpec extends FeatureSpec with GivenWhenThen {
  val weedFSMaster = "67.169.5.73:9333"
  val volumeServer = "67.169.5.73:8080"
  val file = new File("/Users/chiradip/Documents/Ultra Large Scale Systems .pdf")
  val contentType = ContentType.parse("application/pdf")
  info(s"File Name: ${file.getName}")
  var listOfUploadedFiles = new ListBuffer[String]

  feature("Different scenarios of file upload to WeedFS with previously retrieved FID")    {
    scenario("Upload file as file object")   {
      Given("Get weed volume URL and get a file id assigned")
      lazy val fileAssignment = new WeedFSClient().getFileAssignment(weedFSMaster)
      val uploadUrl = fileAssignment.get("publicUrl").getOrElse("NONE")
      info(s"Upload/Public URL: $uploadUrl")
      val url = fileAssignment.get("url").getOrElse("NONE")
      info(s"URL: $url")
      val fid = fileAssignment.get("fid").getOrElse("NONE")
      info(s"FID: $fid")
      assert(!uploadUrl.equalsIgnoreCase("NONE"))
      assert(!fid.equalsIgnoreCase("NONE"))
      When("Upload is requested")
      val result = new WeedFSClient().uploadWithFid(file, uploadUrl, fid, contentType, file.getName )
      Then("Result shold be a non-empty map")
      assert(result != null)
      assert(result match {
        case _: scala.collection.mutable.Map[String, String]  => true
        case _    => false
      })
      info(s"result.get(size).get.toInt: ${result.get("size").get.toInt}")
      assert(result.get("size").get.toInt>0)
      if((!uploadUrl.equalsIgnoreCase("NONE")) && (!fid.equalsIgnoreCase("NONE"))) {
        listOfUploadedFiles+=s"http://$uploadUrl/$fid"
      }
    }
    scenario("Upload file as input stream") {
      Given("Get weed volume URL and get a file id assigned")
      lazy val fileAssignment = new WeedFSClient().getFileAssignment(weedFSMaster)
      val uploadUrl = fileAssignment.get("publicUrl").getOrElse("NONE")
      info(s"Upload/Public URL: $uploadUrl")
      val url = fileAssignment.get("url").getOrElse("NONE")
      info(s"URL: $url")
      val fid = fileAssignment.get("fid").getOrElse("NONE")
      info(s"FID: $fid")
      assert(!uploadUrl.equalsIgnoreCase("NONE"))
      assert(!fid.equalsIgnoreCase("NONE"))
      When("Upload is requested")
      val result = new WeedFSClient().uploadWithFid(new FileInputStream(file), uploadUrl, fid, ContentType.parse("image/jpeg"),file.getName )
      Then("Result shold be a non-empty map")
      assert(result != null)
      assert(result match {
        case _: scala.collection.mutable.Map[String, String]  => true
        case _    => false
      })
      info(s"result.get(size).get.toInt: ${result.get("size").get.toInt}")
      assert(result.get("size").get.toInt>0)
      if((!uploadUrl.equalsIgnoreCase("NONE")) && (!fid.equalsIgnoreCase("NONE"))) {
        listOfUploadedFiles+=s"http://$uploadUrl/$fid"
      }
    }
    scenario("Upload file as ByteArray") {
      Given("Get weed volume URL and get a file id assigned")
      lazy val fileAssignment = new WeedFSClient().getFileAssignment(weedFSMaster)
      val uploadUrl = fileAssignment.get("publicUrl").getOrElse("NONE")
      info(s"Upload/Public URL: $uploadUrl")
      val url = fileAssignment.get("url").getOrElse("NONE")
      info(s"URL: $url")
      val fid = fileAssignment.get("fid").getOrElse("NONE")
      info(s"FID: $fid")
      assert(!uploadUrl.equalsIgnoreCase("NONE"))
      assert(!fid.equalsIgnoreCase("NONE"))
      When("Upload is requested")
      val result = new WeedFSClient().uploadWithFid(IOUtils.toByteArray(new FileInputStream(file)), uploadUrl, fid, ContentType.parse("image/jpeg"),file.getName )
      Then("Result shold be a non-empty map")
      assert(result != null)
      assert(result match {
        case _: scala.collection.mutable.Map[String, String]  => true
        case _    => false
      })
      info(s"result.get(size).get.toInt: ${result.get("size").get.toInt}")
      assert(result.get("size").get.toInt>0)
      if((!uploadUrl.equalsIgnoreCase("NONE")) && (!fid.equalsIgnoreCase("NONE"))) {
        listOfUploadedFiles+=s"http://$uploadUrl/$fid"
      }
    }
  }

  feature("Different scenarios of file upload to WeedFS without a previously retrieved FID")    {
    scenario("Upload file as file object")   {pending}
    scenario("Upload file as input stream") {pending}
    scenario("Upload file as ByteArray") {
      val result = new WeedFSClient().uploadWithoutFid(IOUtils.toByteArray(new FileInputStream(file)), volumeServer, contentType,file.getName )
      info(s"Result: $result")
      val fid = result.get("fid").getOrElse("NONE")
      if(!fid.equalsIgnoreCase("NONE"))   {
        listOfUploadedFiles+=s"http://$volumeServer/$fid"
      }
    }
  }
  feature("Delete a specific file - also helps to cleanup uplaoded space after testing")  {
    scenario("Delete all the uploaded file") {
      for( file <- listOfUploadedFiles) {
        val result = new WeedFSClient().deleteFile(file)
        info(s"DELETE Result $result")

      }
    }
  }
}
