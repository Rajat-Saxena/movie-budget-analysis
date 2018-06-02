package com.rajat.saxena

import org.apache.spark.rdd.RDD
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
    //cpiData.foreach(println)

    val movieData = sc.textFile("src/main/resources/AllMoviesDetailsCleaned.csv")
    //println("Row count:" + movieData.count())

    /*
     * (adjust budget for inflation)
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

    /*val movieDataCleaned = movieData
      .filter(_ != header)
      .map(_.split(";(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"))
      .filter(row => row(10) != null)
      .map(row => (row(5),                          // Title
                    row(10).substring(6,10).toInt,  // Release Year
                    row(17),                        // Avg Rating
                    row(1).toInt,                   // Budget
                    getCurrentValue(row(1).toInt, row(10).substring(6,10).toInt, cpiData),
        (cpi2018 - cpiBroadcast.value.toMap.get(row(10).substring(6,10).toInt).get) * 100 * row(1).toInt
      ))*/

    movieDataCleaned.take(20).foreach(println)
  }

  def getCurrentValue(budget: Int, originalYear: Int, cpiData: RDD[(Int, Float)]): Double = {
    val cpi2018 = 250.546
    val cpiOriginalYear = cpiData.filter(_._1 == originalYear).map(_._2)

    val coefficient = (cpi2018 - cpiOriginalYear.first()) * 100

    budget * coefficient
  }
}