package com.rajat.saxena

import org.apache.spark.SparkConf

object App {

  val conf = new SparkConf().setAppName("Movie Budget Analysis").setMaster("local")
}