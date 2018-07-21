# Movie Budget Analysis

An analysis on movie budgets through the years, and the impact it has on overall rating and popularity.

The dataset that I have used can be found [here](https://www.kaggle.com/stephanerappeneau/350-000-movies-from-themoviedborg). It was posted by Kaggle user [Stephanerappeneau](https://www.kaggle.com/stephanerappeneau) and contains data about ~350,000 movies from [themoviedb.org](themoviedb.org).

## Preface
This analysis covers the below points:
* Finding the most expensive movies of all time.
* Finding genre of the most expensive movies.
* Finding the best small budget movies.
* Identifying production company of most expensive movies.
* Relation between high budget and a good movie.

Since this analysis involves budgets from the past 80 years, I have made adjustments for inflation and scaled all budgets to reflect appropriate values as of 2018. However there can certainly be some deviations, as you will see. The adjustment for inflation was done with the help of [Consumer Price Index on US Inflation Calculator](http://www.usinflationcalculator.com/inflation/consumer-price-index-and-annual-percent-changes-from-1913-to-2008). All movie budgets are in US Dollars ($).

## Analysis
Let's begin.

### 1. Most expensive movies of all time
Unsurprisingly, only recent movies made this list, with the oldest movie in Top 25 being _Superman Returns_ from 2006.

Rank | Movie | Budget | Adjust Budget | Production Company
-------|-------|---------------|-------------------|----
1|Pirates of the Caribbean: On Stranger Tides (2011) | $380,000,000.00 | $423,259,114.00 | Walt Disney Pictures
2|Pirates of the Caribbean: At World's End (2007) | $300,000,000.00 | $362,584,654.00 | Walt Disney Pictures
3|Avengers: Age of Ultron (2015) | $280,000,000.00 | $295,982,484.00 | Marvel Studios
4|Superman Returns (2006) | $270,000,000.00 | $335,552,668.00 | DC Comics
5|Tangled (2010) | $260,000,000.00 | $298,739,590.00 | Walt Disney Pictures
6|Transformers: The Last Knight (2017) | $260,000,000.00 | $265,755,390.00 | Paramount Pictures
7|John Carter (2012) | $260,000,000.00 | $283,726,759.00 | Walt Disney Pictures
8|Spider-Man 3 (2007) | $258,000,000.00 | $311,822,802.00 | Columbia Pictures
9|The Lone Ranger (2013) | $255,000,000.00 | $274,253,316.00 | Walt Disney Pictures
10|X-Men: Days of Future Past (2014) | $250,000,000.00 | $264,583,764.00 | Twentieth Century Fox Film Corporation
11|Harry Potter and the Deathly Hallows: Part 1 (2010) | $250,000,000.00 | $287,249,605.00 | Warner Bros.
12|The Dark Knight Rises (2012) | $250,000,000.00 | $272,814,192.00 | Legendary Pictures
13|The Hobbit: The Desolation of Smaug (2013) | $250,000,000.00 | $268,875,800.00 | WingNut Films
14|Harry Potter and the Half-Blood Prince (2009) | $250,000,000.00 | $291,961,289.00 | Warner Bros.
15|The Hobbit: The Battle of the Five Armies (2014) | $250,000,000.00 | $264,583,764.00 | WingNut Films
16|The Hobbit: An Unexpected Journey (2012) | $250,000,000.00 | $272,814,192.00 | WingNut Films
17|Captain America: Civil War (2016) | $250,000,000.00 | $260,977,800.00 | Studio Babelsberg
18|The Fate of the Furious (2017) | $250,000,000.00 | $255,534,029.00 | Universal Pictures
19|Batman v Superman: Dawn of Justice (2016) | $250,000,000.00 | $260,977,800.00 | DC Comics
20|Star Wars: The Force Awakens (2015) | $245,000,000.00 | $258,984,673.00 | Lucasfilm
21|Spectre (2015) | $245,000,000.00 | $258,984,673.00 | Columbia Pictures
22|Avatar (2009) | $237,000,000.00 | $276,779,302.00 | Ingenious Film Partners
23|Pirates of the Caribbean: Dead Men Tell No Tales (2017) | $230,000,000.00 | $235,091,306.00 | Walt Disney Pictures
24|Man of Steel (2013) | $225,000,000.00 | $241,988,220.00 | Legendary Pictures
25|The Chronicles of Narnia: Prince Caspian (2008) | $225,000,000.00 | $261,830,311.00 | Walt Disney
