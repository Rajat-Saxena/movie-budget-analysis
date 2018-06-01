package com.rajat.saxena

import org.apache.spark.{SparkConf, SparkContext}

object App {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Movie Budget Analysis").setMaster("local")
    val sc = new SparkContext(conf)

    val movieData = sc.textFile("src/main/resources/AllMoviesDetailsCleaned.csv")
    println("Row count:" + movieData.count())
  }
}