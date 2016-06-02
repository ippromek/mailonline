
import com.oleg.SearchEngine._
import org.scalatest.FunSuite

class MainQueryTest extends FunSuite{
  val path_to_folder = "C:\\alt.atheism"
  val qry_string_boolean="science or religion"
  val qry_string_tfidf="science"
  val FileList = MainQuery.FileList(path_to_folder)
  test("Boolean function"){
    val final_results = FileList.map(l => MainQuery.Boolean_qry(qry_string_boolean, l._1, l._2)).filter(f => f != "")
    assert(final_results.length == 189)
  }
  test("Calculate IDF"){
    val IDF=MainQuery.CalculateIDF(FileList,qry_string_tfidf)
    assert(Math.round(IDF) ==3)
  }
  test("TF-IDF function"){
    val IDF=7
    val final_results = FileList.map(l => MainQuery.TFIDF(qry_string_tfidf, l._1, l._2, IDF))
      .filter(l => l._2 != 0)
      .sortBy(l => -1 * l._2).map(l => l._1)
    assert(final_results.length==98)
  }
}
