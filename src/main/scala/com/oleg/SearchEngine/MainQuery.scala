package com.oleg.SearchEngine

/**
  * Created by oleg.baydakov on 05/25/2016.
  */

//import java.util.Scanner
//import scala.math._
//import java.io.File
import scala.collection.JavaConversions._
import org.apache.commons.io.FileUtils._
import java.io.{File, IOException}
import scala.io.Source

object MainQuery extends App {

    val scn = new java.util.Scanner(System.in)

    val all_texts=InputFiles()
    val UserInput=InputParameters()
    val qry_type=UserInput._1
    val qry_string = UserInput._2
    var num_results = UserInput._3

   // Results in line with  the criteria
    if (qry_type == "Boolean"){
      val final_results = all_texts.map(l => Boolean_qry(qry_string, l._1, l._2)).filter(f => f != "")
      if (num_results == -1){
        final_results.foreach(println)
      }else{
        final_results.take(num_results).foreach(println)
      }
    }else if (qry_type == "TF-IDF"){
      val IDF=CalculateIDF(all_texts,qry_string)
      val final_results=CalculateTF_IDF(all_texts,qry_string,IDF)

      if (final_results.isEmpty){
        println("Nothing was found")
      }
      else{
        if (num_results == -1){
          final_results.foreach(println)
        }else{
          final_results.take(num_results).foreach(println)
        }
      }
      } else{
      println("There is no such query type "+qry_type)
      }

    // Read path to the folder with the files
    def InputFiles():List[(String,String)] = {
      print("Please enter the path to input folder: ")
      val path_to_folder = scn.nextLine()
      val all_texts=FileList(path_to_folder:String)
      all_texts
    }

    // Read all files   from the folder and create list
    def FileList(path_to_folder:String)={
      val main_dir = new File(path_to_folder)
      if (!main_dir.exists && !main_dir.isDirectory)
      {
        print("Folder does not exist")
        System.exit(1)
      }
      val all_files = listFiles(main_dir, null, true)
      val all_file_names = all_files.map(s => s.getAbsolutePath)
      val text = all_files.map(s => readFileToString(s, "UTF-8"))
      (all_file_names, text).zipped.toList
    }

    // Read input parameters - query type, query string and number of results)
    def InputParameters():(String,String,Int) ={
      print("Please enter the type of your query , TF-IDF or Boolean : ")
      val qry_type = scn.nextLine()

      print("Please enter your query (for example 'science or religion': ")
      val qry_string = scn.nextLine()

      print("Please enter the MAX number of results (-1  - means all results): ")
      val num_results = scn.nextLine().toInt
      (qry_type,qry_string, num_results)
    }

    // Boolean query function (and / or)
    def Boolean_qry(qry_string: String, file_name: String, message: String) = {
      val words = message.toLowerCase().split(" ").map(l => l.trim)
      val bool_list = qry_string.toLowerCase().filterNot(_ == '"').split(" or ") //split by " OR "
        .map(l => l.trim.split(" and ").map(ll => ll.trim))                      // split by " AND "
        .map(l => l.map(ll => words.contains(ll)))                               //  true if the message words include query word
        .map(l => if (l.contains(false)) false else true)
      val true_false = if (bool_list.contains(true)) true else false
      if ( true_false ){file_name}else {""}                                      // if the search is successful , return the filename, otherwise ""
    }

    //Calcualte IDF
    def CalculateIDF(all_texts:List[(String,String)], qry_string:String):Double={
      val ID_num = all_texts.map(l => l._2.split(" ").map(ll => ll.trim))
        .map(l => if (l.contains(qry_string)) 1 else 0)
        .reduce(_+_)
      val IDF = math.log(all_texts.length.toDouble / (ID_num.toDouble+1))
      IDF
    }

    def CalculateTF_IDF(all_texts:List[(String,String)], qry_string:String, IDF:Double)={
      val final_results = all_texts.map(l => TFIDF(qry_string, l._1, l._2, IDF))
        .filter(l => l._2 != 0)
        .sortBy(l => -1 * l._2)
        .map(l => l._1)
      final_results
    }


    //      TFIDF function
    def TFIDF(qry_string: String, file_name: String, content: String, IDF: Double) = {
      val query_string = qry_string.toLowerCase().filterNot(_ == '"')
      val words = content.toLowerCase().split(" ").map(l => l.trim) // split the words
      val count = words.count(l => l == query_string)                // how often qry_string in the document
      val TF = count / words.length.toDouble                        // Term frequency calculation
      val TF_IDF = TF * IDF // TF-IDF calculation
      (file_name, TF_IDF)
    }

}
