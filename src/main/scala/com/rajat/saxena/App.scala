package com.rajat.saxena

import org.apache.spark.{SparkConf, SparkContext}

object App {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Movie Budget Analysis").setMaster("local")
    val sc = new SparkContext(conf)

    // http://www.usinflationcalculator.com/inflation/consumer-price-index-and-annual-percent-changes-from-1913-to-2008/
    val cpiData = sc.textFile("src/main/resources/CPI.txt")
        .map(_.split("\t"))
        .map(row => (row(0).trim.toInt, row(13).trim.toFloat))
    val cpiBroadcast = sc.broadcast(cpiData.collect())
    val cpi2018 = 250.546

    val movieData = sc.textFile("src/main/resources/AllMoviesDetailsCleaned.csv")

    /* Objective:
     - (adjust budget for inflation)
     * Budget of the movie wrt GDP of the country (most expensively made movies of all time?)
     * Awards vs Budget (do expensive movies win awards?)
     * Genre vs Budget (does a particular genre require high budget?)
     * Experience of director vs Budget (do new directors get high budgets?)
     * Best small budget movies
     */

    // Get movie title and budget from main data

    val header = movieData.first()
    val convertedBudgets = movieData
      .filter(_ != header)
      .map(_.split(";(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"))
      .filter(row => row(10) != null)
      .map(row => ((((cpi2018 - cpiBroadcast.value.toMap.get(row(10).substring(6,10).toInt).get)
        /cpiBroadcast.value.toMap.get(row(10).substring(6,10).toInt).get) + 1) * row(1).toInt).toInt)
    convertedBudgets.take(20).foreach(println)

    val movieDataCleaned = movieData
      .filter(_ != header)
      .map(_.split(";(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"))
      .filter(row => row(10) != null)
      .zip(convertedBudgets)
      .map(row => (row._1(5),             // Title
        row._1(10).substring(6,10).toInt, // Release Year
        row._1(17),                       // Avg Rating
        row._1(1).toInt,                  // Budget
        row._2                            // Converted Budget
      ))

    movieDataCleaned.take(20).foreach(println)
  }
}