Project goal:
	Search engine for:
	1) Support boolean query with "AND" and "OR".
	2) Support tf-idf ranking query.

How to run:
"scala -classpath commons-io-2.5.jar MainQuery.scala"
Please enter the path to input folder: C:\alt.atheism
Please enter the type of your query , TF-IDF or Boolean : Boolean
Please enter your query (for example 'science or religion': science or religion
Please enter the MAX number of results (-1  - means all results): 10

Possible improvements:
1)Correct terms for typos using a dictionary and metric such as the  Levenstein distance.
2)Remove the nonstop words
3)Extract the title, paragraph, sentences of each article and perform language identification using a  Markovian classifier
4)Improve query string parsing  to be able to read more complicated queries, for example "(science and religion) or philosophy"
5) Merge all files into one and submit it to HDFS, then change the code to be run in Spark, for example:
	val Conf = new SparkConf().setAppName("MainQuery_Spark").setMaster("local")
    val sc = new SparkContext(Conf)
	//      Read all the files in the path folder
     val wholeText = sc.wholeTextFiles(path_to_files+"/*")


