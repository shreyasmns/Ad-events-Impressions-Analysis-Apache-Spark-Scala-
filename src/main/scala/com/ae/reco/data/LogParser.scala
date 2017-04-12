package com.ae.reco.data

import java.net.URLDecoder
import java.text.SimpleDateFormat
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

import com.google.doubleclick.crypto.DoubleClickCrypto.{Keys, Price}
import com.netaporter.uri.Uri
import com.netaporter.uri.Uri._
import com.netaporter.uri.config.UriConfig
import com.netaporter.uri.decoding.NoopDecoder
import play.api.libs.json.JsValue

import scala.collection.mutable.Map
import scala.collection.immutable.{Map => Mapper}
import play.api.libs.json._
/**
  * Created by Shreyas MN on 18/2/17.
  */
case class Events(pvid:String,e:String){
}
case class Impressions(pvid:String){

}
object LogParser {
  val PATTERN = """^([\w\w\w\s\:\s\-\,]+) (\{.*\})""".r

  def parseEvents(log: String): Events = {
    val res = PATTERN.findFirstMatchIn(log)
    var event = "NA"
    var pvid = "NA"
    var dataToPull = ""
    if (res.isEmpty) {
      throw new RuntimeException("Cannot parse log line: " + log)
    }
    try {


      val m = res.get
      //    implicit val c = UriConfig(decoder = NoopDecoder)
      //    val uri: Uri = m.group(3)
      //    val parse_url = parse(uri.toStringRaw).query.paramMap
      val times = m.group(1)
      val jsonText = m.group(2)
      dataToPull = jsonText.toString.split("},",2)(1)
//      println(dataToPull)
      val parsedJson = Json.parse(dataToPull).as[Mapper[String,JsValue]]

      event = parsedJson.getOrElse("e", "NA").toString.replace("\"","").replace("\\","")
      pvid = parsedJson.getOrElse("pv", "NA").toString.replace("\"","").replace("\\","")
      Events(pvid, event)
    }
    catch {
      case e: Exception => {
        e.printStackTrace()

//        println("------------------------***************************->"+dataToPull)
        Events(pvid, event)
        //        event = "NA"
        //            println("json parsing issue:" + appdata)
      }

    }
  }

  def parseImpressions(log: String): Impressions = {
    val res = PATTERN.findFirstMatchIn(log)
//    var event = "NA"
    var pvid = "NA"
    if (res.isEmpty) {
      throw new RuntimeException("Cannot parse log line: " + log)
    }
    try {


      val m = res.get
      //    implicit val c = UriConfig(decoder = NoopDecoder)
      //    val uri: Uri = m.group(3)
      //    val parse_url = parse(uri.toStringRaw).query.paramMap
      val times = m.group(1)
      val jsonText = m.group(2)
      val dataToPull = jsonText.toString.split("},",2)(1)
      val parsedJson = Json.parse(dataToPull).as[Mapper[String, JsValue]]

//      event = parsedJson.getOrElse("e", "NA").toString
      pvid = parsedJson.getOrElse("pv", "NA").toString.replace("\"","").replace("\\","")
      Impressions(pvid)
    }
    catch {
      case e: Exception => {
        e.printStackTrace()
        Impressions(pvid)
        //        event = "NA"
        //            println("json parsing issue:" + appdata)
      }

    }
  }
}
