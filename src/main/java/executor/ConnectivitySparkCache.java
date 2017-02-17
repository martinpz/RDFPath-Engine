/*
* Copyright (C) 2017 University of Freiburg.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* The RDFPath Engine is a research project at the department of 
* computer science, University of Freiburg. 
*
* More information on the project:
* http://dbis.informatik.uni-freiburg.de/forschung/projekte/DiPoS/
* zablocki@informatik.uni-freiburg.de
*/
package executor;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

public class ConnectivitySparkCache {
	
	public static SparkConf sparkConf;
	public static JavaSparkContext ctx;
	public static SQLContext sqlContext;
	
	public static void main(String[] args)
	{
		String result="";
		/*
		 * Read Settings
		 */
		Settings.readSettings();
		Settings.setSettings();
		/*
		 * Check if input is passed
		 */
		if(args.length<2)
		{
			System.exit(0);
		}
		else if(args.length>3)
		{
			Settings.Graph=args[3];
		}
		
		 /*
		 * Initialize SPARK
		 */
		sparkConf = new SparkConf().setAppName("RDFPath").setMaster(Settings.sparkMaster);
		sparkConf.set("spark.sql.parquet.binaryAsString", "true");
	    sparkConf.set("spark.sql.parquet.filterPushdown", "true");
		sparkConf.set("spark.executor.memory", "24g");
		sparkConf.set("spark.default.parallelism", "108");
		sparkConf.set("spark.sql.inMemoryColumnarStorage.compressed", "true");                              
        sparkConf.set("spark.sql.autoBroadcastJoinThreshold", "-1");
        sparkConf.set("spark.sql.inMemoryColumnarStorage.batchSize", "20000");
        sparkConf.set("spark.sql.shuffle.partitions", Settings.sparkShufflePartitions);

        
		ctx = new JavaSparkContext(sparkConf);
		sqlContext = new SQLContext(ctx);
		/*
		 * Read graph from parquet 
		 */
		String location = Settings.Database+".db/"+Settings.Graph;
		if(Settings.Database.equals("default"))
		{
			location = Settings.Graph;
		}
		/*
		 * Cache only used predicates.
		 */
		DataFrame schemaRDF = sqlContext.parquetFile(Settings.hiveMetastore+location);		
		String currentPredicates =" WHERE predicate IN('snvoc:knows','snvoc:hasPerson') ";
		
		schemaRDF.registerTempTable("raw"+Settings.Graph);
		DataFrame graphFrame = sqlContext.sql("SELECT subject, predicate, object FROM raw"+Settings.Graph+currentPredicates);
		graphFrame.cache().registerTempTable(Settings.Graph);
		sqlContext.sql("SELECT COUNT() FROM "+Settings.Graph).collect();
		/*
		 *	For each distance search for connection 
		 */
		long lStartTime = System.nanoTime();
		result = friend(args[1],args[2]);
		result="0";
		if(result.equals("0"))
		{
			result = foaf(args[1],args[2]);
		}	
		if(result.equals("0"))
		{
			result =foaf3(args[1],args[2]);
		}
		if(result.equals("0"))
		{
			result =foaf4(args[1],args[2]);
		}
		
		long lEndTime = System.nanoTime();
		long difference = lEndTime - lStartTime;
		
		System.out.println("Number of Results: "+result);
		System.out.println("Elapsed milliseconds: " + difference / 1000000);
		AppSpark.writeResult(args[1]+"---"+args[2]+","+(difference/1000000)+","+result+"\n"); 
		
	}
	public static String friend(String Person1, String Person2)
	{
		String result="";
		String Query = "SELECT g1.subject as c1, g2.object as c2  FROM "+Settings.Graph+" g1, "+Settings.Graph+" g2 "
				+ "WHERE g1.predicate='snvoc:knows' "
				+ "AND g2.predicate='snvoc:hasPerson' "
				+ "AND g1.object=g2.subject "
				+ "AND g1.subject='"+Person1+"' AND g2.object='"+Person2+"'";
		
		
		DataFrame queryFrame = sqlContext.sql(Query);
		result = Long.toString(queryFrame.count());
		
		return result;

	}

	
	public static String foaf(String Person1, String Person2)
	{
		String result="";
		String st1 = "SELECT g1.subject as c1, g1.predicate as c2, g1.object as c3, g2.predicate as c4, g2.object as c5  FROM "+Settings.Graph+" g1, "+Settings.Graph+" g2 "
				+ "WHERE g1.predicate='snvoc:knows' "
				+ "AND g2.predicate='snvoc:hasPerson' "
				+ "AND g1.object=g2.subject "
				+ "AND g1.subject='"+Person1+"' ";
		sqlContext.sql(st1).cache().registerTempTable("st1");
		
		String st2 = "SELECT g1.subject as c1, g1.predicate as c2, g1.object as c3, g2.predicate as c4, g2.object as c5  FROM "+Settings.Graph+" g1, "+Settings.Graph+" g2 "
				+ "WHERE g1.predicate='snvoc:knows' "
				+ "AND g2.predicate='snvoc:hasPerson' "
				+ "AND g1.object=g2.subject "
				+ "AND g2.object='"+Person2+"' ";
		sqlContext.sql(st2).cache().registerTempTable("st2");
		
		String Query = "SELECT l.c1 as c1, r.c5 as c2 FROM st1 l, st2 r "
						+ "WHERE l.c5 = r.c1 ";
		
		DataFrame queryFrame = sqlContext.sql(Query);
		result = Long.toString(queryFrame.count());
		
		return result;
	}
	
	public static String foaf3(String Person1, String Person2)
	{
		String result="";
		
		String st3 = "SELECT g1.c1 as c1, g1.c2 as c2, g1.c3 as c3, g1.c4 as c4, g1.c5 as c5, g2.predicate as c6, g2.object as c7  FROM st1 g1, "+Settings.Graph+" g2 "
				+ "WHERE g2.predicate='snvoc:knows' "
				+ "AND g1.c5=g2.subject ";
		
		String st4 = "WITH st3 as ( "+st3+" )"
				+ "SELECT g1.c1 as c1, g1.c2 as c2, g1.c3 as c3, g1.c4 as c4, g1.c5 as c5, g1.c6 as c6, g1.c7 as c7, g2.predicate as c8, g2.object as c9  FROM st3 g1, "+Settings.Graph+" g2 "
				+ "WHERE g2.predicate='snvoc:hasPerson' "
				+ "AND g1.c7=g2.subject "
				+ "AND g1.c5!=g2.object";
		sqlContext.sql(st4).cache().registerTempTable("st4");
		
		
		String Query = "SELECT l.c1 as c1, r.c5 as c2 FROM st4 l, st2 r "
						+ "WHERE l.c9 = r.c1 ";
		
		
		DataFrame queryFrame = sqlContext.sql(Query);
		result = Long.toString(queryFrame.count());

		return result;
	}
	
	public static String foaf4(String Person1, String Person2)
	{
		String result="";
		
		String st5 = "SELECT g1.subject as c1, g1.predicate as c2, g2.c1 as c3, g2.c2 as c4, g2.c3 as c5, g2.c4 as c6, g2.c5 as c7 FROM "+Settings.Graph+" g1, st2 g2 "
				+ "WHERE g1.predicate='snvoc:hasPerson' "
				+ "AND g1.object=g2.c1 ";
		
		String st6 = "WITH st5 as ( "+st5+" ) "
				+ "SELECT g1.subject as c1, g1.predicate as c2, g2.c1 as c3, g2.c2 as c4, g2.c3 as c5, g2.c4 as c6, g2.c5 as c7, g2.c6 as c8, g2.c7 as c9 FROM "+Settings.Graph+" g1, st5 g2 "
				+ "WHERE g1.predicate='snvoc:knows' "
				+ "AND g1.object=g2.c1 AND g1.subject != g2.c3 ";
		sqlContext.sql(st6).cache().registerTempTable("st6");	
				
		String Query = "SELECT l.c1 as c1, r.c9 as c2 FROM st4 l, st6 r "
						+ "WHERE l.c9 = r.c1 ";
		
		
		DataFrame queryFrame = sqlContext.sql(Query);
		result = Long.toString(queryFrame.count());

		return result;
	}
	
	
	
	

}
