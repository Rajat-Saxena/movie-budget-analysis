package com.rajat

import java.text.SimpleDateFormat
import java.util.Calendar

import co.theasi.plotly._
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object App {

  /**
    * Objectives:
    * 0. Adjust movie budget for inflation
    * 1. Most expensively made movies of all time
    * 2. Genre of most expensive movies
    * 3. Best small budget movies
    * 4. Production company of most expensive movies
    * 5. Does high budget mean a good movie?
    * @param args
    */
  def main(args: Array[String]) {
    // Keep track of start time
    val appStartTime = System.nanoTime

    // Timestamp for output directory
    val timestampFormat = new SimpleDateFormat("yyyyMMdd_hhmmss")
    val timestamp = timestampFormat.format(Calendar.getInstance.getTime)
    println("Timestamp: " + timestamp)

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
    // Filter such that budget is greater than 0
    val movieData = sc.textFile("src/main/resources/AllMoviesDetailsCleaned.csv")
      .map(_.split(";(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)"))
      .filter(_(10).length == 10)
      .filter(_(14) == "Released")
      .filter(_(18).toInt >= 5)
      .filter(_(1).toInt > 0)

    // Get first row from main movie data, contains column names and needs to be filtered
    val header = movieData.first()

    // Objective 0: Calculate adjusted budgets using CPI
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
      .map(row => (row._1(16),              // _1 String  Title
        row._1(10).substring(6,10).toInt,   // _2 Int     Release Year
        row._1(17).toFloat,                 // _3 Float   Avg Rating
        row._1(1).toInt,                    // _4 Int     Budget
        row._2,                             // _5 Int     Converted Budget
        row._1(8),                          // _6 String  Production Companies
        row._1(2),                          // _7 String  Genre
        row._1(18).toInt                    // _8 Int     Number of votes
      ))

    val movieDataSortedByBudget = movieDataCleaned.sortBy(movie => (movie._5, movie._3), ascending = false).persist()

    // Objective 1: Find most expensive movies
    /*mostExpensiveMoviesOfAllTime(movieDataSortedByBudget, timestamp)

    // Objective 2: Find genres that are most expensive
    mostExpensiveGenres(movieDataSortedByBudget, timestamp)

    // Objective 3: Find best small budget movies
    bestSmallBudgetMovies(movieDataSortedByBudget, timestamp)

    // Objective 4: Production companies and expensive movies
    productionCompaniesAndExpensiveMovies(movieDataSortedByBudget, timestamp)*/

    // Objective 5:
    budgetVersusMovieRating(movieDataSortedByBudget, timestamp)

    sc.stop()
    // Keep track of end time
    val duration = (System.nanoTime - appStartTime) / 1e9d
    println("\n*** Program executed for: " + duration + " seconds ***")
  }

  /**
    * Function to return the most expensive movies of all time
    * Prints top 25 movies in terms of budget (both adjusted for inflation and without)
    * @param movieData
    */
  def mostExpensiveMoviesOfAllTime(movieData: RDD[(String, Int, Float, Int, Int, String, String, Int)],
                                   timestamp: String) = {

    // Formatter to print budget in readable amount format
    val formatter = java.text.NumberFormat.getCurrencyInstance

    // Sort by budget to find most expensive movies of all time
    println("Most Expensive Movies of All Time:")
    val mostExpensiveMoviesOfAllTime = movieData
      .sortBy(movie => (movie._4, movie._3), ascending = false)
      .map(row => row._1 + " (" + row._2 + ") | " + formatter.format(row._4) + " | " + formatter.format(row._5) + " | " + row._6)

    // Save to file
    mostExpensiveMoviesOfAllTime
      .coalesce(1)
      .saveAsTextFile("target/output/" + timestamp + "/mostExpensiveMoviesOfAllTime")

    // Print results to console
    mostExpensiveMoviesOfAllTime.take(50).foreach(println)

    // Sort by adjusted budget to find most expensive movies of all time
    println("Most Expensive Movies of All Time (Adjusted for Inflation):")
    val mostExpensiveMoviesOfAllTimeAdjusted = movieData
      .map(row => row._1 + " (" + row._2 + ") | " + formatter.format(row._4) + " | " + formatter.format(row._5) + " | " + row._6)

    // Save to file
    mostExpensiveMoviesOfAllTimeAdjusted
        .coalesce(1)
        .saveAsTextFile("target/output/" + timestamp + "/mostExpensiveMoviesOfAllTimeAdjusted")

    // Print results to console
    mostExpensiveMoviesOfAllTimeAdjusted.take(50).foreach(println)
  }

  /**
    * Function to return the genres of most expensive movies
    * Prints genres of the top 100 expensive movies of all time
    * @param movieData
    */
  def mostExpensiveGenres(movieData: RDD[(String, Int, Float, Int, Int, String, String, Int)],
                          timestamp: String) = {

    // Get most common genres for the top 500 most expensive movies
    val genreCountOfTop500ExpensiveMovies = movieData
      .map(_._7).map(_.trim())
      .zipWithIndex()
      .filter(_._2 < 500)
      .map(_._1)
      .flatMap(_.split("\\|"))
      .map((_, 1))
      .reduceByKey(_+_)
      .sortBy(_._2, ascending = false)
      .map(row => (row._1 + "\t" + row._2))

    // Save to file
    genreCountOfTop500ExpensiveMovies
      .coalesce(1)
      .saveAsTextFile("target/output/" + timestamp + "/genreCountOfTop500ExpensiveMovies")

    // Print results to console
    genreCountOfTop500ExpensiveMovies.foreach(println)
  }

  /**
    * Function to return the best small budget movies
    * Prints best movies made with budget in first quartile (25th percentile)
    * @param movieData
    */
  def bestSmallBudgetMovies(movieData: RDD[(String, Int, Float, Int, Int, String, String, Int)],
                            timestamp: String) = {

    // Formatter to print budget in readable amount format
    val formatter = java.text.NumberFormat.getCurrencyInstance

    // Find value marking the first quartile
    val firstQuartile = movieData.count()/4
    println("First quartile marked at: "  + firstQuartile)

    // Find movies with budget in the first quartile,
    // and having a rating of at least 6.0
    // and having at least 10 votes
    val bestSmallBudgetMovies = movieData
      .filter(_._3 >= 6.0)
      .filter(_._8 >= 10)
      .sortBy(movie => (movie._5, movie._3), ascending = true)
      .zipWithIndex()
      .filter(_._2 <= firstQuartile)
      .map(_._1)
      .sortBy(_._3, ascending = false)
      .map(row => row._1 + " (" + row._2 + ") | " + formatter.format(row._5) + " | " + row._3 + " | " + row._8)

    // Save to file
    bestSmallBudgetMovies
      .coalesce(1)
      .saveAsTextFile("target/output/" + timestamp + "/bestSmallBudgetMovies")

    // Print results to console
    bestSmallBudgetMovies.foreach(println)
  }

  def productionCompaniesAndExpensiveMovies(movieData: RDD[(String, Int, Float, Int, Int, String, String, Int)],
                                            timestamp: String) = {

    // Formatter to print budget in readable amount format
    val formatter = java.text.NumberFormat.getCurrencyInstance

    // Get the 500 top rated movies
    val top500 = movieData.zipWithIndex().filter(_._2 < 500).map(_._1)

    // Get count of production companies along with their gross expense for top 500 movies
    val productionCompaniesAggregate = top500
      .map(row => (row._6, (1, row._5.toLong)))
      .reduceByKey(
        {
          case ((accCount, accGross), (count, gross)) => (accCount + count, accGross + gross)
        }
      )
      .sortBy(_._2._1, ascending = false)
      .map(row => row._1 + " | " + row._2._1 + " | " + formatter.format(row._2._2))

    // Save to file
    productionCompaniesAggregate
      .coalesce(1)
      .saveAsTextFile("target/output/" + timestamp + "/productionCompaniesAggregate")
    productionCompaniesAggregate.foreach(println)
  }

  def budgetVersusMovieRating(movieData: RDD[(String, Int, Float, Int, Int, String, String, Int)], timestamp: String) = {

    // Get the 500 most expensive movies
    val top500Budget = movieData.zipWithIndex().filter(_._2 < 500).map(_._1)

    //val top500Rated = movieData.sortBy(_._3, ascending = false)

    // Plot graph of budget (x) vs rating (y)
    val xs0 = top500Budget.map(_._5).collect().toList
    val ys0 = top500Budget.map(_._3.toDouble).collect().toList
    val plot0 = Plot().withScatter(xs0, ys0, ScatterOptions().mode(ScatterMode.Marker))
    draw(plot0, "budget-vs-rating", writer.FileOptions(overwrite=true))

    // Plot graph of budget (x) vs rating (y)
    val plot1 = Plot().withScatter(ys0, xs0, ScatterOptions().mode(ScatterMode.Marker))
    draw(plot1, "rating-vs-budget", writer.FileOptions(overwrite=true))
  }
}