package com.rajat.saxena

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object App {

  def main(args: Array[String]) {
    val appStartTime = System.nanoTime

    val conf = new SparkConf().setAppName("Movie Budget Analysis").setMaster("local")
    val sc = new SparkContext(conf)

    // http://www.usinflationcalculator.com/inflation/consumer-price-index-and-annual-percent-changes-from-1913-to-2008/
    val cpiData = sc.textFile("src/main/resources/CPI.txt")
        .map(_.split("\t"))
        .map(row => (row(0).trim, row(13).trim.toFloat))
    val cpiBroadcast = sc.broadcast(cpiData.collectAsMap())
    val cpi2018 = 250.546

    val movieData = sc.textFile("src/main/resources/AllMoviesDetailsCleaned.csv")
      .map(_.split(";(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"))
      .filter(row => row(10).length == 10)
      .filter(row => row(14) == "Released")

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
      .map(row => ((((cpi2018 - cpiBroadcast.value.getOrElse(row(10).substring(6,10), 9.9.toFloat))
      /cpiBroadcast.value.getOrElse(row(10).substring(6,10), 9.9.toFloat)) + 1) * row(1).toInt).toInt)
    //convertedBudgets.foreach(println)

    val movieDataCleaned = movieData
      .filter(_ != header)
      .zip(convertedBudgets)
      .map(row => (row._1(16),             // String - Title
        row._1(10).substring(6,10).toInt, // Int - Release Year
        row._1(17).toFloat,               // Float - Avg Rating
        row._1(1).toInt,                  // Int - Budget
        row._2                            // Int - Converted Budget
      ))

    // Find most expensive movies
    mostExpensiveMoviesOfAllTime(movieDataCleaned)
    //movieDataCleaned.take(20).foreach(println)

    val duration = (System.nanoTime - appStartTime) / 1e9d
    println("Program executed for: " + duration + " seconds")
  }

  def mostExpensiveMoviesOfAllTime(movieDataCleaned: RDD[(String, Int, Float, Int, Int)]) = {

    val mostExpensiveMoviesOfAllTime = movieDataCleaned.sortBy(movie => (movie._5, movie._3), ascending = false)

    mostExpensiveMoviesOfAllTime.take(25).foreach(println)
  }
}