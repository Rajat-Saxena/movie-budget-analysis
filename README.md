# Movie Budget Analysis

An analysis on movie budgets through the years, and the impact it has on overall rating and popularity.

The dataset that I have used can be found [here](https://www.kaggle.com/stephanerappeneau/350-000-movies-from-themoviedborg). It was posted by Kaggle user [Stephanerappeneau](https://www.kaggle.com/stephanerappeneau) and contains data about ~350,000 movies from [themoviedb.org](themoviedb.org).

## Preface
This analysis covers the below points:
* Finding the most expensive movies of all time.
* Finding genre of expensive movies.
* Finding the best small budget movies.
* Identifying production company of most expensive movies.
* Relation between high budget and a good movie.

Since this analysis involves budgets from the past 80 years, I have made adjustments for inflation and scaled all budgets to reflect appropriate values as of 2018. However there can certainly be some deviations, as you will see. The adjustment for inflation was done with the help of [Consumer Price Index on US Inflation Calculator](http://www.usinflationcalculator.com/inflation/consumer-price-index-and-annual-percent-changes-from-1913-to-2008). All movie budgets are in US Dollars ($).

## Analysis
Let's begin.

I will only post the top 25 results here. The full list can be found in `output/` directory in the repo.

### Most expensive movies of all time
Unsurprisingly, only recent movies made this list, with the oldest movie in Top 25 being _Superman Returns_ from 2006.

Rank | Movie | Year | Budget | Adjust Budget | Production Company
-----|-------|------|--------|---------------|-------------------
1|Pirates of the Caribbean: On Stranger Tides |2011 | $380,000,000.00 | $423,259,114.00 | Walt Disney Pictures
2|Pirates of the Caribbean: At World's End |2007 | $300,000,000.00 | $362,584,654.00 | Walt Disney Pictures
3|Avengers: Age of Ultron |2015 | $280,000,000.00 | $295,982,484.00 | Marvel Studios
4|Superman Returns |2006 | $270,000,000.00 | $335,552,668.00 | DC Comics
5|Tangled |2010 | $260,000,000.00 | $298,739,590.00 | Walt Disney Pictures
6|Transformers: The Last Knight |2017 | $260,000,000.00 | $265,755,390.00 | Paramount Pictures
7|John Carter |2012 | $260,000,000.00 | $283,726,759.00 | Walt Disney Pictures
8|Spider-Man 3 |2007 | $258,000,000.00 | $311,822,802.00 | Columbia Pictures
9|The Lone Ranger |2013 | $255,000,000.00 | $274,253,316.00 | Walt Disney Pictures
10|X-Men: Days of Future Past |2014 | $250,000,000.00 | $264,583,764.00 | Twentieth Century Fox Film Corporation
11|Harry Potter and the Deathly Hallows: Part 1 |2010 | $250,000,000.00 | $287,249,605.00 | Warner Bros.
12|The Dark Knight Rises |2012 | $250,000,000.00 | $272,814,192.00 | Legendary Pictures
13|The Hobbit: The Desolation of Smaug |2013 | $250,000,000.00 | $268,875,800.00 | WingNut Films
14|Harry Potter and the Half-Blood Prince |2009 | $250,000,000.00 | $291,961,289.00 | Warner Bros.
15|The Hobbit: The Battle of the Five Armies |2014 | $250,000,000.00 | $264,583,764.00 | WingNut Films
16|The Hobbit: An Unexpected Journey |2012 | $250,000,000.00 | $272,814,192.00 | WingNut Films
17|Captain America: Civil War |2016 | $250,000,000.00 | $260,977,800.00 | Studio Babelsberg
18|The Fate of the Furious |2017 | $250,000,000.00 | $255,534,029.00 | Universal Pictures
19|Batman v Superman: Dawn of Justice |2016 | $250,000,000.00 | $260,977,800.00 | DC Comics
20|Star Wars: The Force Awakens |2015 | $245,000,000.00 | $258,984,673.00 | Lucasfilm
21|Spectre |2015 | $245,000,000.00 | $258,984,673.00 | Columbia Pictures
22|Avatar |2009 | $237,000,000.00 | $276,779,302.00 | Ingenious Film Partners
23|Pirates of the Caribbean: Dead Men Tell No Tales |2017 | $230,000,000.00 | $235,091,306.00 | Walt Disney Pictures
24|Man of Steel |2013 | $225,000,000.00 | $241,988,220.00 | Legendary Pictures
25|The Chronicles of Narnia: Prince Caspian |2008 | $225,000,000.00 | $261,830,311.00 | Walt Disney

### Most expensive movies of all time adjusted for inflation
Things might get slightly more interesting on finding the most expensive movies sorted by adjust budget (where adjusted budget is original budget adjusted for inflation). While it may be interesting, there are certainly some aberrations. For instance, I personally consider _Metropolis (1927)_ to be an anomaly since it is difficult to believe a budget of $92M in 1927 which becomes over $1.3B after adjustment.

Rank | Movie | Year | Budget | Adjust Budget | Production Company
-----|-------|------|--------|---------------|-------------------
1|Metropolis |1927 | $92,620,000.00 | $1,333,653,507.00 | Paramount Pictures
2|War and Peace |1966 | $100,000,000.00 | $773,290,087.00 | Mosfilm
3|The Manchurian Candidate |1962 | $80,000,000.00 | $663,697,996.00 | United Artists
4|Pirates of the Caribbean: On Stranger Tides |2011 | $380,000,000.00 | $423,259,114.00 | Walt Disney Pictures
5|Pirates of the Caribbean: At World's End |2007 | $300,000,000.00 | $362,584,654.00 | Walt Disney Pictures
6|Superman Returns |2006 | $270,000,000.00 | $335,552,668.00 | DC Comics
7|Titanic |1997 | $200,000,000.00 | $312,206,853.00 | Paramount Pictures
8|Spider-Man 3 |2007 | $258,000,000.00 | $311,822,802.00 | Columbia Pictures
9|Tangled |2010 | $260,000,000.00 | $298,739,590.00 | Walt Disney Pictures
10|Avengers: Age of Ultron |2015 | $280,000,000.00 | $295,982,484.00 | Marvel Studios
11|Harry Potter and the Half-Blood Prince |2009 | $250,000,000.00 | $291,961,289.00 | Warner Bros.
12|Waterworld |1995 | $175,000,000.00 | $287,700,470.00 | Universal Pictures
13|Harry Potter and the Deathly Hallows: Part 1 |2010 | $250,000,000.00 | $287,249,605.00 | Warner Bros.
14|John Carter |2012 | $260,000,000.00 | $283,726,759.00 | Walt Disney Pictures
15|Avatar |2009 | $237,000,000.00 | $276,779,302.00 | Ingenious Film Partners
16|The Lone Ranger |2013 | $255,000,000.00 | $274,253,316.00 | Walt Disney Pictures
17|The Dark Knight Rises |2012 | $250,000,000.00 | $272,814,192.00 | Legendary Pictures
18|The Hobbit: An Unexpected Journey |2012 | $250,000,000.00 | $272,814,192.00 | WingNut Films
19|Terminator 3: Rise of the Machines |2003 | $200,000,000.00 | $272,332,608.00 | Columbia Pictures
20|The Hobbit: The Desolation of Smaug |2013 | $250,000,000.00 | $268,875,800.00 | WingNut Films
21|Transformers: The Last Knight |2017 | $260,000,000.00 | $265,755,390.00 | Paramount Pictures
22|King Kong |2005 | $207,000,000.00 | $265,555,664.00 | WingNut Films
23|Spider-Man 2 |2004 | $200,000,000.00 | $265,268,404.00 | Columbia Pictures
24|X-Men: Days of Future Past |2014 | $250,000,000.00 | $264,583,764.00 | Twentieth Century Fox Film Corporation
25|The Hobbit: The Battle of the Five Armies |2014 | $250,000,000.00 | $264,583,764.00 | WingNut Films

### Genre of expensive movies
In order to not distort the results, I picked the 500 most expensive movies and counted the occurrences of various genres. I certainly expected _War_ movies to be higher up, but perhaps there aren't many such movies in the top 500. I am surprised that there are so many _Comedy_ movies in the list.

Rank | Genre | Count
-----|-------|------
1|Action|284
2|Adventure|280
3|Science Fiction|145
4|Fantasy|140
5|Thriller|133
6|Comedy|122
7|Drama|122
8|Family|120
9|Animation|75
10|Crime|42
11|Romance|37
12|Mystery|31
13|History|30
14|War|23
15|Horror|13
16|Western|10
17|Music|9

### Best small budget movies
For this finding, I restricted movies with budget in the first quartile (without adjustment). I also added a filter for movies to have a minimum rating of 6.0 and having at least 10 votes.

Popular movies in this list are _Whiplash, 12 Angry Men, Pulp Fiction_, and _One Flew Over the Cuckoo's Nest_.

Rank | Movie | Year | Budget | Rating | Votes
-----|-------|------|--------|--------|-------
1|The Book of Henry | 2017 | $10,221,361.00 | 8.8 | 11
2|Stop Making Sense | 1984 | $2,893,697.00 | 8.6 | 40
3|Human | 2015 | $13,742,043.00 | 8.6 | 88
4|Parched | 2015 | $2,854,116.00 | 8.5 | 20
5|Guten Tag, Ramón | 2013 | $4,302,012.00 | 8.4 | 16
6|The Five(ish) Doctors Reboot | 2013 | $32,265.00 | 8.3 | 14
7|Scenes from a Marriage | 1973 | $846,439.00 | 8.3 | 42
8|Michael Jackson's Thriller | 1983 | $2,767,074.00 | 8.3 | 67
9|Whiplash | 2014 | $3,492,505.00 | 8.3 | 3880
10|Radio Day | 2008 | $4,654,761.00 | 8.3 | 18
11|The Visual Bible: The Gospel of John | 2003 | $13,616,630.00 | 8.3 | 10
12|Satantango | 1994 | $2,535,890.00 | 8.2 | 49
13|12 Angry Men | 1957 | $3,031,517.00 | 8.2 | 1947
14|Mommy | 2014 | $5,185,841.00 | 8.2 | 643
15|Psycho | 1960 | $6,830,324.00 | 8.2 | 2080
16|Generation War | 2013 | $12,906,038.00 | 8.2 | 66
17|Pulp Fiction | 1994 | $13,524,750.00 | 8.2 | 7761
18|One Flew Over the Cuckoo's Nest | 1975 | $13,970,966.00 | 8.2 | 2650
19|The Intouchables | 2011 | $14,479,917.00 | 8.2 | 4744
20|Modern Times | 1936 | $18.00 | 8.1 | 781
21|One Minute Time Machine | 2014 | $1,058.00 | 8.1 | 15
22|HyperNormalisation | 2016 | $38,103.00 | 8.1 | 22
23|Louis C.K.: Live at the Beacon Theater | 2011 | $278,459.00 | 8.1 | 73
24|Who's Singin' Over There? | 1980 | $364,872.00 | 8.1 | 30
25|Carrossel - O Filme | 2015 | $2,194,517.00 | 8.1 | 19

### Production company of most expensive movies
From the 500 most expensive movies of all time, I extracted the production company and the number of movies produced by it, along with the aggregate money pumped into them. No real surprises here.

Rank | Production Company | Count | Aggregate Spending
-----|--------------------|-------|----------------
1|Walt Disney Pictures | 57 | $10,777,976,401.00
2|Paramount Pictures | 49 | $9,266,424,661.00
3|Columbia Pictures | 40 | $6,740,078,866.00
4|Universal Pictures | 34 | $5,394,587,153.00
5|Twentieth Century Fox Film Corporation | 30 | $4,665,011,910.00
6|Village Roadshow Pictures | 23 | $3,480,891,143.00
7|Warner Bros. | 13 | $2,377,873,553.00
8|DreamWorks SKG | 12 | $1,754,124,813.00
9|DreamWorks Animation | 12 | $1,972,371,970.00
10|Ingenious Film Partners | 11 | $1,816,334,130.00
11|Marvel Studios | 10 | $1,906,902,881.00
12|New Line Cinema | 10 | $1,445,345,001.00
13|Imagine Entertainment | 9 | $1,394,623,863.00
14|Jerry Bruckheimer Films | 8 | $1,246,051,015.00
15|TriStar Pictures | 8 | $1,102,809,256.00
16|Legendary Pictures | 7 | $1,366,543,968.00
17|Columbia Pictures Corporation | 7 | $1,079,644,246.00
18|Lucasfilm | 6 | $1,168,084,212.00
19|Summit Entertainment | 6 | $749,035,172.00
20|WingNut Films | 6 | $1,331,394,222.00
21|DC Comics | 5 | $1,186,929,268.00
22|Eon Productions | 5 | $988,870,871.00
23|Amblin Entertainment | 5 | $809,069,802.00
24|Silver Pictures | 4 | $585,819,916.00
25|Studio Babelsberg | 4 | $742,782,358.00

### High budget and a good movie
Does an expensively made movie guarantee success? Or is it always a low-budget movie that gains popularity? Here are two visualizations comparing movie budget and movie rating for the top 500 most expensive movies. I have removed outliers for better comprehension.

#### Movie Rating vs Movie Budget
<img src="https://plot.ly/~rajatsaxena/28.png?share_key=Yr1RPypXEtjti1NgZTPKnj" alt="Movie Rating vs Movie Budget" style="width:100%;" onerror="this.onerror=null;this.src='https://plot.ly/404.png';" />
#### Movie Budget vs Movie Rating
<img src="https://plot.ly/~rajatsaxena/26.png?share_key=7Rpnoxxd1tOcz1tSwYLgMN" alt="Movie Budget vs Movie Rating" style="width:100%" onerror="this.onerror=null;this.src='https://plot.ly/404.png';" />

It seems difficult to find a conclusion from the scatter plots.

### Conclusion
While there seems to be no direct correlation between budget and rating, I am going to make a conclusion:
> For any decent movie (rating > 7), the optimum production budget is $200M.

Of course, there will always be exceptions.
