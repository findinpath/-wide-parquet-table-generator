import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

import scala.collection.JavaConversions._

object SparkApplication {

  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().appName("sample-spark").master("local[*]").getOrCreate()


    sparkSession.conf.set("mapreduce.fileoutputcommitter.marksuccessfuljobs", "false")
    val r = scala.util.Random
    val rowcount = 5000
    val colcount = 3000
    val partitioncount = 2000
    val schema = StructType(List(StructField("deptid", IntegerType), StructField("clientid", IntegerType)) ++ (1 to colcount).map( i =>  StructField("col" +  i.toString, StringType, true )))
    val data = List.range(1, rowcount).map(rowid => Row.fromSeq( ( List(r.nextInt(partitioncount), rowid)  ++ (1 to colcount).map(colid => if (r.nextBoolean) "row" + rowid + "col" + colid else null))))


    val df = sparkSession.createDataFrame(data, schema)
    df.write.partitionBy("deptid").parquet("/tmp/depts")
  }
}
