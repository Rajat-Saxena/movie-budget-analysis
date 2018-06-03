package com.rajat.saxena

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object App {

  /* Objectives:
   + (adjust budget for inflation)
   ~ Budget of the movie wrt GDP of the country (most expensively made movies of all time?)
   * Awards vs Budget (do expensive movies win awards?)
   * Genre vs Budget (does a particular genre require high budget?)
   * Experience of director vs Budget (do new directors get high budgets?)
   * Best small budget movies (maybe budgets in the bottom 10%?)
   * Which production companies have maximum investments/awards/roi?
   */

  def main(args: Array[String]) {
    // Keep track of start time
    val appStartTime = System.nanoTime

    // Declare and initialize Spark variables
    val conf = new SparkConf().setAppName("Movie Budget Analysis").setMaster("local")
    val sc = new SparkContext(conf)

    // http://www.usinflationcalculator.com/inflation/consumer-price-index-and-annual-percent-changes-from-1913-to-2008/
    // Read data for Consumer Price Index (CPI) to track inflation rates
    val cpiData = sc.textFile("src/main/resources/CPI.txt")
        .map(_.split("\t"))
        .map(row => (row(0).trim, row(13).trim.toFloat))

    // Broadcast CPI rates and declare constant value for CPI in 2018
    val cpiBroadcast = sc.broadcast(cpiData.collectAsMap())
    val cpi2018 = 250.546

    // Read main movie data
    // Split by semicolon, but skip semicolon present in text
    // Filter such that release date of movie is present and status of movie is "Released"
    // Filter such that minimum 5 votes are logged for the movie
    val movieData = sc.textFile("src/main/resources/AllMoviesDetailsCleaned.csv")
      .map(_.split(";(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"))
      .filter(_(10).length == 10)
      .filter(_(14) == "Released")
      .filter(_(18).toInt >= 5)

    // Get first row from main movie data, contains column names and needs to be filtered
    val header = movieData.first()

    // Calculate adjusted budgets using CPI
    // Formula to calculate inflated value:
    // ((new CPI - oldCPI)/oldCPI + 1) * oldValue
    val convertedBudgets = movieData
      .filter(_ != header)
      .map(row => ((((cpi2018 - cpiBroadcast.value.getOrElse(row(10).substring(6,10), 9.9.toFloat))
      /cpiBroadcast.value.getOrElse(row(10).substring(6,10), 9.9.toFloat)) + 1) * row(1).toInt).toInt)

    // Merge adjusted budgets to main movie data and extract only the required columns
    val movieDataCleaned = movieData
      .filter(_ != header)
      .zip(convertedBudgets)
      .map(row => (row._1(16),              // String - Title
        row._1(10).substring(6,10).toInt,   // Int - Release Year
        row._1(17).toFloat,                 // Float - Avg Rating
        row._1(1).toInt,                    // Int - Budget
        row._2,                             // Int - Converted Budget
        row._1(8)                           // String - Production Companies
      ))

    // Objective 1: Find most expensive movies
    mostExpensiveMoviesOfAllTime(movieDataCleaned)

    // Keep track of end time
    val duration = (System.nanoTime - appStartTime) / 1e9d
    println("\n*** Program executed for: " + duration + " seconds ***")
  }

  def mostExpensiveMoviesOfAllTime(movieDataCleaned: RDD[(String, Int, Float, Int, Int, String)]) = {

    val formatter = java.text.NumberFormat.getCurrencyInstance

    println("********************************************")
    println("Most Expensive Movies of All Time")
    println("********************************************")
    val mostExpensiveMoviesOfAllTime = movieDataCleaned.sortBy(movie => (movie._4, movie._3), ascending = false)
      .map(row => row._1 + " (" + row._2 + ") | " + formatter.format(row._4) + " | " + formatter.format(row._5) + " | " + row._6)
    mostExpensiveMoviesOfAllTime.take(25).foreach(println)

    println("\n****************************************************************")
    println("Most Expensive Movies of All Time (Adjusted for Inflation)")
    println("****************************************************************")
    val mostExpensiveMoviesOfAllTimeAdjusted = movieDataCleaned.sortBy(movie => (movie._5, movie._3), ascending = false)
      .map(row => row._1 + " (" + row._2 + ") | " + formatter.format(row._4) + " | " + formatter.format(row._5) + " | " + row._6)
    mostExpensiveMoviesOfAllTimeAdjusted.take(25).foreach(println)
  }
}