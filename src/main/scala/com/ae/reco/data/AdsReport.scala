package com.ae.reco.data

import breeze.linalg.*
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.storage.StorageLevel
//import com.ae.reco.data.Events

/**
  * Created by Shreyas MN on 20/2/17.
  */
object AdsReport {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder.appName("Adelemet User Recommendation").getOrCreate()
    val conf = sparkSession.conf
    val sc = sparkSession.sparkContext
    //val conf = new SparkConf().setAppName("Adelemet User Recommendation")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.set("spark.rdd.compress", "true")
    conf.set("spark.kryoserializer.buffer.max", "512")
    conf.set("mapreduce.input.fileinputformat.input.dir.recursive", "true")
    //    conf.set("spark.memory.storageFraction","0.3")
    //    conf.set("spark.default.parallelism","8900")

    val eventsPath = args(0)
    val impressionsPath = args(1)
    import sparkSession.implicits._

    val eventsData = sc.textFile(eventsPath).map(line => LogParser.parseEvents(line)).toDF().persist(StorageLevel.MEMORY_AND_DISK_SER)

    val imprData = sc.textFile(impressionsPath).map(line => LogParser.parseImpressions(line)).toDF().persist(StorageLevel.MEMORY_AND_DISK_SER)


    val imprAgg = imprData
      .filter($"pvid"=!="NA")
      .groupBy(
        imprData("pvid")
      )
      .count()
      .select("*")
      .persist(StorageLevel.MEMORY_AND_DISK_SER)

    imprAgg.show(10)
    eventsData.filter($"pvid".notEqual(lit("NA")))
      .filter($"e".notEqual(lit("NA"))).show(10)
    val eventsView = eventsData
                    .filter($"pvid".notEqual(lit("NA")))
                    .filter($"e".notEqual(lit("NA")))
                    .filter($"e".equalTo(lit("view")))
                    .groupBy($"pvid",$"e")
                    .count()
                    .select($"pvid",$"count")
                    .persist(StorageLevel.MEMORY_AND_DISK_SER)


    eventsView.show(10)

    val eventsClick = eventsData
                      .filter($"pvid".notEqual(lit("NA")))
                      .filter($"e".notEqual(lit("NA")))
                      .filter($"e".equalTo(lit("click")))
                      .groupBy($"pvid",$"e")
                      .count()
                      .select($"pvid",$"count")
                      .persist(StorageLevel.MEMORY_AND_DISK_SER)

    eventsClick.show(10)


    val eventsDf = eventsView.as("eventsView").join(eventsClick.as("eventsClick"),eventsView("pvid")===eventsClick("pvid"),"outer").select(coalesce(eventsClick("pvid"),eventsView("pvid")).alias("pvid"),eventsView("count").alias("view"),eventsClick("count").alias("click")).na.fill(0).persist(StorageLevel.MEMORY_AND_DISK_SER)

    eventsDf.show()

    val finalData = imprAgg.join(eventsDf,imprAgg("pvid")===eventsDf("pvid"),"left_outer").select(coalesce(imprAgg("pvid"),eventsDf("pvid")).alias("pvid"),imprAgg("count").alias("impressions"),eventsDf("view"),eventsDf("click")).na.fill(0).write
      .format("com.databricks.spark.csv")
      .option("header", "true")
      .option("delimiter", ",")
      .save(args(2))

//    finalData.show(10)

  }
}
