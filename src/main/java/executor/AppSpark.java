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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;


import org.apache.hadoop.fs.*;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import generator.BranchMerge;
import generator.QueryPlanGenerator;
import parser.RDFPathClassListener;
import parser.RDFPathParseQuery;

public class AppSpark {
	
	public static SparkConf sparkConf;
	public static JavaSparkContext ctx;
	public static SQLContext sqlContext;
	static ParseTree tree;
	static RDFPathClassListener extractor;
	static int recursionLast;
	static int beforeRecursion;
	static List<Integer> hybridSteps = new ArrayList<Integer>();
	static long nrResults;
	static long Nettodifference =0;
	static long lStartTime=0;
	
	public static void main(String args[]) throws IOException
	{

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
		else if(args.length>2)
		{
			Settings.Graph=args[2];
			Settings.sparkShufflePartitions=args[3];
			Settings.useEXTVP=Boolean.parseBoolean(args[4]);
			Settings.execMode=Integer.parseInt(args[5]);
		}
		/*
		 * Check if input is string query or file.
		 */
		boolean file = false;
		if(args[0].equals("-f"))
		{
			file = true;
		}
		/*
		 * Parse query to tree
		 */
		tree = RDFPathParseQuery.parse(args[1], file);
		
		/*
		 * Visit tree to generate query plan
		 */
	    extractor = new RDFPathClassListener();
	    ParseTreeWalker.DEFAULT.walk(extractor, tree);

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
		String currentPredicates =getPredicates();
		if(!currentPredicates.equals(""))
		{
			schemaRDF.registerTempTable("raw"+Settings.Graph);
			DataFrame graphFrame = sqlContext.sql("SELECT subject, predicate, object FROM raw"+Settings.Graph+currentPredicates);
			graphFrame.cache().registerTempTable(Settings.Graph);
		}
		else
		{
			schemaRDF.cache().registerTempTable(Settings.Graph);	
		}
		sqlContext.sql("SELECT COUNT() FROM "+Settings.Graph).collect();

		/*
		 * Read Selectivity values from table
		 */
		if (Settings.useEXTVP) {
			schemaRDF = sqlContext.parquetFile(Settings.hiveMetastore+location+"_extvpdata");
			schemaRDF.registerTempTable("raw"+Settings.Graph+"_extvpdata");
			DataFrame graphFrame = sqlContext.sql("SELECT pair, subject, predicate, object FROM raw"+Settings.Graph+"_extvpdata WHERE "+getPairs());
			graphFrame.cache().registerTempTable(Settings.Graph+"_extvpdata");
			schemaRDF = sqlContext.parquetFile(Settings.hiveMetastore+location+"_extvpstats");
			schemaRDF.registerTempTable(Settings.Graph+"_extvpstats");
			sqlContext.sql("SELECT Count() FROM "+Settings.Graph+"_extvpdata").collect();
			readSelectivity();
		}	
		
		
	    /*
		 * Check for empty result
		 */
		if (Settings.useEXTVP && !QueryPlanGenerator.predicatesChangedFlag) {
			if (QueryPlanGenerator.hasEmpty()) {
				System.out.println("Result is empty! No need to calculate.\n");
				try {
					PrintWriter writer = new PrintWriter(Settings.resultsFolder+"result.txt", "UTF-8");
					writer.println(" ");
					writer.close();
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				System.exit(0);

			}
		}
		/*
		 * Check if Hybrid is applicable
		 */
		if(Settings.hybridStep==1 && Settings.execMode==3)
		{
			Settings.execMode=2;
			System.out.println("Hybrid with step 1 = materialising");
		}
		else if(QueryPlanGenerator.hasBranch && Settings.execMode==3 && !Settings.hybridFilterRecursion)
		{
			Settings.execMode=2;
			System.out.println("Hybrid execution is not available for branches/bounded recursion\n"
					+ "Changed to materializing");
		}
		lStartTime = System.nanoTime();
		
		/*
		 * Execute queries based on execution mode.
		 */
		if (Settings.execMode == 1) {
			if(Settings.compositionalLinear)
			{
				if(QueryPlanGenerator.hasBranch || QueryPlanGenerator.recursionFlag)
				{
					System.out.println("compositionalLinear is not possible with branches!\n"
							+ "Switched to normal compostional executor");
					calculateALL();
				}
				else
				{
				  String fromPart = "FROM (";
				  /*
				   * Treat Result at END
				   */
				  int limit = 1;
				  if(QueryPlanGenerator.hasResultFunction)
				  {
					  limit +=1;
				  }
				  /*
				   * Generate fromPart of big Query
				   */
				  for(int i=0;i<=QueryPlanGenerator.queries.size()-limit;i++)
				  {
					fromPart += "SELECT subject, predicate, object FROM "+subGraphName(QueryPlanGenerator.queries.get(i))+" WHERE"+subGraphWhere(QueryPlanGenerator.queries.get(i))+" ) st"+(i+1)+", \n(";
				  }
				  fromPart = fromPart.substring(0, fromPart.length()-4);
				  /*
				   * Generate SELECT and WHERE part of Big Query
				   * Merge all parts of Big Query
				   */
				  String Query = generateFullQuery(fromPart,limit);
				  /*
				   * Treat Result at END
				   */
					  if(QueryPlanGenerator.hasResultFunction)
					  {
						  String resultQuery = QueryPlanGenerator.queries.get(QueryPlanGenerator.queries.size()-limit+1);
						  Query = resultQuery.substring(0,resultQuery.indexOf("FROM")+4)+" ( "+Query+" ) res ";
					  }
	
					  
					  finalQuery(Query);

				}
			  
			}
			else
			{
			 calculateALL();
			}
		} else if (Settings.execMode == 2) {
			if (QueryPlanGenerator.infiniteRecursionFlag) {
				QueryPlanGenerator.queries.add("");
				beforeRecursion = QueryPlanGenerator.queries.size();
				/*
				 * Calculate intermediate results
				 */
				calculateIntermediate();
				/*
				 * Back to normal mode.
				 */
				QueryPlanGenerator.infiniteRecursionFlag = false;
				/*
				 * Iterate recursive step
				 */
				infiniteRecursion();

				/*
				 * Check if there were more queries produced after recursion.
				 */
				if (QueryPlanGenerator.queries.size() > recursionLast) {
					for (int i = 0; i < QueryPlanGenerator.queries.size() - 1; i++) {
						if (i > recursionLast - 1) {
							String tableName = "st" + Integer.toString(i + 1);
							String Query = QueryPlanGenerator.queries.get(i);
							DataFrame Table = sqlContext.sql(Query);
							//Table.write().parquet(Settings.sparkStorage+tableName);
							Table.registerTempTable(tableName);
						}
					}
					calculateFinalResult();
				} else {
					QueryPlanGenerator.queries
							.add("SELECT "+columns()+" FROM st" + Integer.toString(QueryPlanGenerator.stepCounter));
					calculateFinalResult();
				}
				
			} else {
				/*
				 * Calculate intermediate results
				 */
				calculateIntermediate();

				/*
				 * Calculate and print final result
				 */
				calculateFinalResult();
			
			}
			
		} else if (Settings.execMode == 3) {
			if (QueryPlanGenerator.infiniteRecursionFlag) {
				int length = QueryPlanGenerator.queries.size();
				String Query = "";
				for (int i = 0; i < length; i += Settings.hybridStep) {
					/*
					 * Treat parts to be saved and last part differently
					 */
					if (i + Settings.hybridStep < length) {
						Query = "WITH ";
						for (int j = 0; j < Settings.hybridStep - 1; j++) {
							Query += " st" + Integer.toString(i + j + 1) + " AS ("
									+ QueryPlanGenerator.queries.get(i + j) + ") \n,";
						}
						Query = Query.substring(0, Query.length() - 1);
						Query += QueryPlanGenerator.queries.get(i + Settings.hybridStep - 1);
						String tableName = "st" + Integer.toString(i + Settings.hybridStep);
						DataFrame Table = sqlContext.sql(Query);
						Table.write().parquet(Settings.sparkStorage+tableName);
						sqlContext.read().parquet(Settings.sparkStorage+tableName).registerTempTable(tableName);
						hybridSteps.add(i + Settings.hybridStep);
					} else {
						if (length - i > 1) {
							Query = "WITH ";
							for (int j = 0; j < length - i - 1; j++) {
								Query += " st" + Integer.toString(i + j + 1) + " AS ("
										+ QueryPlanGenerator.queries.get(i + j) + ") \n,";
							}
							Query = Query.substring(0, Query.length() - 1) + QueryPlanGenerator.queries.get(length - 1);
							String tableName = "st" + Integer.toString(length);
							DataFrame Table = sqlContext.sql(Query);
							Table.write().parquet(Settings.sparkStorage+tableName);
							sqlContext.read().parquet(Settings.sparkStorage+tableName).registerTempTable(tableName);
							hybridSteps.add(length);
						} else {
							String tableName = "st" + Integer.toString(length);
							Query = QueryPlanGenerator.queries.get(length - 1);
							DataFrame Table = sqlContext.sql(Query);
							Table.write().parquet(Settings.sparkStorage+tableName);
							sqlContext.read().parquet(Settings.sparkStorage+tableName).registerTempTable(tableName);
							hybridSteps.add(length);
							
							
						}
					}

				}

				/*
				 * Back to normal mode.
				 */
				QueryPlanGenerator.infiniteRecursionFlag = false;
				/*
				 * Iterate recursive step
				 */
				QueryPlanGenerator.hybridRecursion = Settings.hybridRecursion;
				if(Settings.hybridRecursion)
				{
					hybridRecursion();
				}
				else
				{
					infiniteRecursion();
				}
				/*
				 * Check if there were more queries produced after recursion.
				 */
				if (QueryPlanGenerator.queries.size() > recursionLast) {
					length = QueryPlanGenerator.queries.size();
					Query = "";
					for (int i = recursionLast; i < length; i += Settings.hybridStep) {
						/*
						 * Treat parts to be saved and last part differently
						 */
						if (i + Settings.hybridStep < length) {
							Query = "WITH ";
							for (int j = 0; j < Settings.hybridStep - 1; j++) {
								Query += " st" + Integer.toString(i + j + 1) + " AS ("
										+ QueryPlanGenerator.queries.get(i + j) + ") \n,";
							}
							Query = Query.substring(0, Query.length() - 1);
							Query += QueryPlanGenerator.queries.get(i + Settings.hybridStep - 1);
							String tableName = "st" + Integer.toString(i + Settings.hybridStep);
							DataFrame Table = sqlContext.sql(Query);
							Table.write().parquet(Settings.sparkStorage+tableName);
							sqlContext.read().parquet(Settings.sparkStorage+tableName).registerTempTable(tableName);
							hybridSteps.add(i + Settings.hybridStep);
						} else {
							if (length - i > 1) {
								Query = "WITH ";
								for (int j = 0; j < length - i - 1; j++) {
									Query += " st" + Integer.toString(i + j + 1) + " AS ("
											+ QueryPlanGenerator.queries.get(i + j) + ") \n,";
								}
								Query = Query.substring(0, Query.length() - 1)
										+ QueryPlanGenerator.queries.get(length - 1);
								finalQuery(Query);
							} else {
								calculateFinalResult();
							}
						}

					}
				} else {
					QueryPlanGenerator.queries
							.add("SELECT "+columns()+" FROM st" + Integer.toString(QueryPlanGenerator.stepCounter));
					calculateFinalResult();
				}

			} else {

				int length = QueryPlanGenerator.queries.size();
				String Query = "";
				for (int i = 0; i < length; i += Settings.hybridStep) {
					/*
					 * Treat parts to be saved and last part differently
					 */
					if (i + Settings.hybridStep < length) {
						Query = "WITH ";
						for (int j = 0; j < Settings.hybridStep - 1; j++) {
							Query += " st" + Integer.toString(i + j + 1) + " AS ("
									+ QueryPlanGenerator.queries.get(i + j) + ") \n,";
						}
						Query = Query.substring(0, Query.length() - 1);
						Query += QueryPlanGenerator.queries.get(i + Settings.hybridStep - 1);
						String tableName = "st" + Integer.toString(i + Settings.hybridStep);
						DataFrame Table = sqlContext.sql(Query);
						Table.write().parquet(Settings.sparkStorage+tableName);
						sqlContext.read().parquet(Settings.sparkStorage+tableName).registerTempTable(tableName);
						hybridSteps.add(i + Settings.hybridStep);
					} else {
						if (length - i > 1) {
							Query = "WITH ";
							for (int j = 0; j < length - i - 1; j++) {
								Query += " st" + Integer.toString(i + j + 1) + " AS ("
										+ QueryPlanGenerator.queries.get(i + j) + ") \n,";
							}
							Query = Query.substring(0, Query.length() - 1) + QueryPlanGenerator.queries.get(length - 1);
							finalQuery(Query);
						} else {
							calculateFinalResult();
						}
					}

				}

			}
		
		}

	    long lEndTime = System.nanoTime();
		long difference = lEndTime - lStartTime;
		if(Settings.countOnly)
		{
			System.out.println("Elapsed milliseconds: " + Nettodifference/1000000);
			writeResult(args[1]+","+(Nettodifference/1000000)+","+Long.toString(nrResults)+"\n");
		}
		else
		{
			System.out.println("Elapsed milliseconds: " + difference/1000000);
			writeResult(args[1]+","+(difference/1000000)+","+Long.toString(nrResults)+"\n");
		}
		
		/*
	     * Save Queries to file
	     */
	    saveQueries();
		/*
		 * If materializing or hybrid delete intermediate results.
		 */
		if (Settings.execMode == 2) {
			clearIntermediate();
		} else if (Settings.execMode == 3) {
			for (int i : hybridSteps) {
				deleteGraph("st" + Integer.toString(i));
			}
		}
		/*
		 * Clear cache
		 */
		sqlContext.uncacheTable(Settings.Graph);
		if(Settings.useEXTVP)
		{
			sqlContext.uncacheTable(Settings.Graph+"_extvpdata");
		}
		sqlContext.cacheManager().clearCache();
		/*
		 * Close connection.
		 */
		ctx.close();
	}
	public static void calculateALL()
	{
		try
		{

			/*
			 * If more than two steps merge all queries to one single query using WITH clause
			 */
			String Query = QueryPlanGenerator.queries.get(0);
			if(QueryPlanGenerator.queries.size()>1)
			{
				Query = "WITH ";
				for(int i=0;i<QueryPlanGenerator.queries.size()-1;i++)
				{
					Query += " st"+Integer.toString(i+1)+" AS ("+QueryPlanGenerator.queries.get(i)+") \n,";		
				}	
				Query = Query.substring(0, Query.length()-1) + QueryPlanGenerator.queries.get(QueryPlanGenerator.queries.size()-1);
			}
			/*
			 * Run the query.
			 */
			DataFrame resultFrame = sqlContext.sql(Query);
			if(Settings.countOnly)
			{
				long lStartTime = System.nanoTime();
				nrResults = resultFrame.count();		
			    long lEndTime = System.nanoTime();
				Nettodifference = lEndTime - lStartTime;
				System.out.println("Number of Results: " + nrResults);
				return;
			}
			else
			{
				/*
				 * Read and print results.
				 */
				Row[] results = resultFrame.collect();
				PrintWriter writer = new PrintWriter(Settings.resultsFolder+"result.txt", "UTF-8");
				for(Row r :results)
				{
					String result = "";
					for(int i=0;i<=QueryPlanGenerator.cloumnCounter-1;i++)
					{
						if(r.get(i).equals("##"))
						{
							continue;
						}
						result +=r.get(i)+"\t";
					}
				  writer.println(result);
				}	
				writer.close();
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void saveQueries()
	{
		try
		{
			PrintWriter writer = new PrintWriter(Settings.resultsFolder+"queries.txt", "UTF-8");
			
			for(String Query:QueryPlanGenerator.queries)
			{
				writer.println(Query);
			}
			writer.close();
		}
		catch(FileNotFoundException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * Read selectivity from table to main memory
	 */
	public static void readSelectivity() {
		try {
			/*
			 * Read the selectivity from table
			 */
			String Query = "SELECT pair, selectivity FROM "+Settings.Graph+"_extvpstats";
			/*
			 * Run the query.
			 */
			DataFrame resultFrame = sqlContext.sql(Query);
			
			/*
			 * Read and print results.
			 */
			Row[] results = resultFrame.collect();
			for(Row r:results)
			{
				QueryPlanGenerator.Selectivity.put(r.getString(0), r.getDouble(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Execute queries with materializing scemantics
	 */
	public static void calculateIntermediate() {
		for (int i = 0; i < QueryPlanGenerator.queries.size() - 1; i++) {
			String tableName = "st" + Integer.toString(i + 1);
			String Query = QueryPlanGenerator.queries.get(i);
			DataFrame Table = sqlContext.sql(Query);
			if(Settings.materializingSave)
			{
				Table.write().parquet(Settings.sparkStorage+tableName);
				sqlContext.read().parquet(Settings.sparkStorage+tableName).registerTempTable(tableName);
			}
			else
			{
				Table.registerTempTable(tableName);
			}
			
		}

	}

	public static void calculateFinalResult() {
		try {
			/*
			 * Run the query.
			 */
			DataFrame Table = sqlContext.sql(QueryPlanGenerator.queries.get(QueryPlanGenerator.queries.size() - 1));
			if(Settings.countOnly)
			{
	
				nrResults = Table.count();		
			    long lEndTime = System.nanoTime();
				Nettodifference = lEndTime - lStartTime;
				System.out.println("Number of Results: " + nrResults);
				return;
			}
			/*
			 * Read and print results.
			 */
			
			PrintWriter writer = new PrintWriter(Settings.resultsFolder+"result.txt", "UTF-8");
			Row[] results = Table.collect();
			for(Row r:results)
			{
				String result = "";
				for (int i = 0; i < QueryPlanGenerator.cloumnCounter; i++) {
					if (r.get(i).equals("##")) {
						continue;
					}
					result += r.get(i) + "\t";
				}
				writer.println(result);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearIntermediate() {
		try {
			for (int i = 1; i < QueryPlanGenerator.queries.size(); i++) {
				deleteGraph("st" + Integer.toString(i));
			}
		} catch (Exception e) {

		}
	}
	
	public static void deleteGraph(String graphName) 
	{
		String Storage = Settings.sparkStorage.substring(0,Settings.sparkStorage.indexOf(":8020")+5);
		String pathtoRoot = Settings.sparkStorage.substring(Storage.length());
		try
		{
			FileSystem fs = FileSystem.get(new URI(Storage),ctx.hadoopConfiguration());	
			fs.delete(new Path(pathtoRoot+graphName), true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void infiniteRecursion() {

		/*
		 * Walk recursive path
		 */
		while (isNotEmpty(QueryPlanGenerator.stepCounter,"")) {
			int beforeWalk = QueryPlanGenerator.queries.size();
			ParseTreeWalker.DEFAULT.walk(extractor, QueryPlanGenerator.infiniteRecursionPath);
			while(beforeWalk<QueryPlanGenerator.queries.size())
			{
				beforeWalk++;
				String tableName = "st" + Integer.toString(beforeWalk-1);
				String Query = QueryPlanGenerator.queries.get(beforeWalk-1);
				DataFrame Table = sqlContext.sql(Query);
				//Table.write().parquet(Settings.sparkStorage+tableName);
				Table.registerTempTable(tableName);
			}
			if (Settings.execMode == 3) {
				hybridSteps.add(QueryPlanGenerator.stepCounter);
			}
		}
		/*
		 * Remove last step of recursion
		 */
		int removedStep = QueryPlanGenerator.recursionMetaData.branchResults
				.get(QueryPlanGenerator.recursionMetaData.branchResults.size()-1)[0];
		QueryPlanGenerator.recursionMetaData.branchResults
		.remove(QueryPlanGenerator.recursionMetaData.branchResults.size() - 1);
		int newStep = QueryPlanGenerator.recursionMetaData.branchResults.get(QueryPlanGenerator.recursionMetaData.branchResults.size()-1)[0];
		
		for(int i=removedStep;i>newStep;i--)
		{
			deleteGraph("st" + i);
			QueryPlanGenerator.queries.remove(QueryPlanGenerator.queries.size()-1);
			try
			{
				QueryPlanGenerator.predicates.remove(QueryPlanGenerator.predicates.size()-1);
				QueryPlanGenerator.travoOPs.remove(QueryPlanGenerator.travoOPs.size()-1);
			}
			catch (Exception e)
			{
				
			}
		}
	

		if (Settings.execMode == 3) {
			hybridSteps.remove(hybridSteps.size() - 1);
		}
		
		QueryPlanGenerator.stepCounter = newStep;
		QueryPlanGenerator.cloumnCounter = QueryPlanGenerator.recursionMetaData.branchResults.get(QueryPlanGenerator.recursionMetaData.branchResults.size()-1)[1];

		
		if (Settings.execMode != 3) {
			QueryPlanGenerator.queries.remove(beforeRecursion - 1);
		}
		
		/*
		 * Continue walking original tree, store current step.
		 */
		recursionLast = QueryPlanGenerator.queries.size();
		QueryPlanGenerator.infiniteRecursionFlag = true;
		ParseTreeWalker.DEFAULT.walk(extractor, tree);
		
		

	}
	
	/*
	 * Check if given step has >0 rows.
	 */
	public static boolean isNotEmpty(int step,String Condition) {
		boolean result = true;
		try {
			DataFrame QueryResult = sqlContext.sql("SELECT c1 FROM st" + Integer.toString(step)+Condition);
			if(QueryResult.count()== (long)0)
			{
				result = false;
			}
		} catch (Exception e) {
			// result = false;
			e.printStackTrace();
		}
		return result;
	}
	
	public static String columns()
	{
		String result ="";
		for(int i=1;i<=QueryPlanGenerator.cloumnCounter;i++)
		{
			result += "c"+Integer.toString(i)+", ";
		}
		result=result.substring(0,result.length()-2);
		return result;
	}
	
	public static void finalQuery(String Query) {
		try {
			/*
			 * Run the query.
			 */
			DataFrame Table = sqlContext.sql(Query);
			if(Settings.countOnly)
			{
				nrResults = Table.count();		
			    long lEndTime = System.nanoTime();
				Nettodifference = lEndTime - lStartTime;
				System.out.println("Number of Results: " + nrResults);
				return;
			}
			else
			{
				/*
				 * Read and print results.
				 */
				
				PrintWriter writer = new PrintWriter(Settings.resultsFolder+"result.txt", "UTF-8");
				Row[] results = Table.collect();
				for(Row r:results)
				{
					String result = "";
					for (int i = 0; i < QueryPlanGenerator.cloumnCounter; i++) {
						if (r.get(i).equals("##")) {
							continue;
						}
						result += r.get(i) + "\t";
					}
					writer.println(result);
				}
				writer.close();
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void hybridRecursion()
	{
		if(QueryPlanGenerator.recursionLowBound.equals("*") || QueryPlanGenerator.recursionLowBound.equals("+"))
		{
			int[] resultsStep4 = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.cloumnCounter};
			QueryPlanGenerator.recursionMetaData.branchResults.add(resultsStep4);
		}
		/*
		 * Walk recursive path up to desired path to be taken
		 */
		int startLength = QueryPlanGenerator.queries.size(); 
		if(isNotEmpty(QueryPlanGenerator.stepCounter,"") && !QueryPlanGenerator.recursionLowBound.equals("*") && !QueryPlanGenerator.recursionLowBound.equals("+"))
		{
			for(int i=0;i<Integer.parseInt(QueryPlanGenerator.recursionLowBound)-1;i++)
			{
				ParseTreeWalker.DEFAULT.walk(extractor, QueryPlanGenerator.infiniteRecursionPath);
			}
			int queriesSize = QueryPlanGenerator.queries.size();
			
			hybridPart(startLength,queriesSize);
			
			int[] resultsStep = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.cloumnCounter};
			QueryPlanGenerator.recursionMetaData.branchResults.add(resultsStep);
			
		}
		/*
		 * Walk paths to be saved using hybrid executor and store their union.
		 */
			while(isNotEmpty(QueryPlanGenerator.stepCounter," WHERE c"+(QueryPlanGenerator.cloumnCounter-1)+"!='##'"))
			{
				startLength = QueryPlanGenerator.queries.size();
				int startColumn = QueryPlanGenerator.cloumnCounter;
				List<int[]> mergeSteps = new ArrayList<int[]>();
				
				for(int i=0;i<Settings.hybridStep;i++)
				{
					ParseTreeWalker.DEFAULT.walk(extractor, QueryPlanGenerator.infiniteRecursionPath);
					int[] resultsStep2 = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.cloumnCounter};
					mergeSteps.add(resultsStep2);
					if(Settings.hybridFilterRecursion)
					{
					 break;
					}
				}
				/*
				 * Treat hybrid recursion with nested filters.
				 */
				if(Settings.hybridFilterRecursion)
				{
					int queriesSize = QueryPlanGenerator.queries.size();
					hybridPart(startLength,queriesSize);
				}
				else
				{
					String tempQuery = QueryPlanGenerator.queries.get(startLength)+" AND p.c"+(startColumn-1)+"!='##'";
					QueryPlanGenerator.queries.set(startLength, tempQuery);
					BranchMerge.mergeBranch(mergeSteps);
					int queriesSize = QueryPlanGenerator.queries.size();
					Settings.hybridStep+=1;
					hybridPart(startLength,queriesSize);
					Settings.hybridStep-=1;
				}
				
				int[] resultsStep3 = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.cloumnCounter};
				QueryPlanGenerator.recursionMetaData.branchResults.add(resultsStep3);
			}
			/*
			 * If last step is empty remove it.
			 */
			if(!isNotEmpty(QueryPlanGenerator.stepCounter," "))
			{
				QueryPlanGenerator.recursionMetaData.branchResults.remove(QueryPlanGenerator.recursionMetaData.branchResults.size()-1);
			}
		/*
		 * Continue walking original tree, store current step.
		 */
		recursionLast = QueryPlanGenerator.queries.size();
		QueryPlanGenerator.infiniteRecursionFlag = true;
		ParseTreeWalker.DEFAULT.walk(extractor, tree);
	}


	public static void hybridPart(int startLength,int queriesSize)
	{
		String Query = "";
		for (int i = startLength; i < queriesSize; i += Settings.hybridStep) {
			
			/*
			 * Treat parts to be saved and last part differently
			 */
			if (i + Settings.hybridStep < queriesSize) {
				Query = "WITH ";
				for (int j = 0; j < Settings.hybridStep - 1; j++) {
					Query += " st" + Integer.toString(i + j + 1) + " AS ("
							+ QueryPlanGenerator.queries.get(i + j) + ") \n,";
				}
				Query = Query.substring(0, Query.length() - 1);
				Query += QueryPlanGenerator.queries.get(i + Settings.hybridStep - 1);
				String tableName = "st" + Integer.toString(i + Settings.hybridStep);
				DataFrame Table = sqlContext.sql(Query);
				Table.write().parquet(Settings.sparkStorage+tableName);
				sqlContext.read().parquet(Settings.sparkStorage+tableName).registerTempTable(tableName);
				hybridSteps.add(i + Settings.hybridStep);
			} else {
				if (queriesSize - i > 1) {
					Query = "WITH ";
					for (int j = 0; j < queriesSize - i - 1; j++) {
						Query += " st" + Integer.toString(i + j + 1) + " AS ("
								+ QueryPlanGenerator.queries.get(i + j) + ") \n,";
					}
					Query = Query.substring(0, Query.length() - 1) + QueryPlanGenerator.queries.get(queriesSize - 1);
					String tableName = "st" + Integer.toString(queriesSize);
					DataFrame Table = sqlContext.sql(Query);
					Table.write().parquet(Settings.sparkStorage+tableName);
					sqlContext.read().parquet(Settings.sparkStorage+tableName).registerTempTable(tableName);
					hybridSteps.add(queriesSize);
				} else {
					Query = QueryPlanGenerator.queries.get(queriesSize - 1);
					String tableName = "st" + Integer.toString(queriesSize);
					DataFrame Table = sqlContext.sql(Query);
					Table.write().parquet(Settings.sparkStorage+tableName);
					sqlContext.read().parquet(Settings.sparkStorage+tableName).registerTempTable(tableName);
					hybridSteps.add(queriesSize);
				}
			}

		}
	}
	
	public static String subGraphName(String Query)
	{
		String result = "";
		
		if(Query.indexOf(" p,")!=-1)
		{
			result = Query.substring(Query.indexOf(" p,")+3,Query.lastIndexOf("WHERE"));
		}
		else
		{
			result = Query.substring(Query.indexOf("FROM")+4,Query.lastIndexOf("WHERE"));
		}
	
		return result;
	}
	public static String subGraphWhere(String Query)
	{
		String result = "";
		
		if(Query.indexOf("AND")!=-1)
		{
		result = Query.substring(Query.indexOf("AND")+3);
		}
		else
		{
			result = " '0'='0' ";
		}
		
		return result;
		
	}
	public static String generateFullQuery(String fromPart,int limit)
	{
		int columnCounter = 2;
		String result = "";
		String selectPart = "SELECT st1.subject as c1, st1.predicate as c2";
		String wherePart = " WHERE '0'='0' ";
		/*
		 * Treat first step 
		 */
		if(QueryPlanGenerator.travoOPs.get(0).equals("/"))
		{
			columnCounter++;
			selectPart += ", st1.object as c"+columnCounter;
		}

		for(int i=1;i<=QueryPlanGenerator.queries.size()-limit;i++)
		{
			if(QueryPlanGenerator.travoOPs.get(i).equals("/"))
			{
				columnCounter+=2;
				selectPart += ", st"+(i+1)+".predicate as c"+(columnCounter-1);
				selectPart += ", st"+(i+1)+".object as c"+columnCounter;
			}
			else
			{
				columnCounter+=1;
				selectPart += ", st"+(i+1)+".predicate as c"+(columnCounter);
			}
			
			if(QueryPlanGenerator.travoOPs.get(i-1).equals("/"))
			{
				wherePart += " AND st"+(i)+".object=st"+(i+1)+".subject";
			}
			else
			{
				wherePart += " AND st"+(i)+".predicate=st"+(i+1)+".subject";
			}
			
		}
		
		result = selectPart+" "+fromPart+" "+wherePart;
		
		
		return result;
		
	}

	public static String getPairs()
	{
		/*
		 * Get pairs when infinite recursion using regex.
		 */
		if(QueryPlanGenerator.infiniteRecursionFlag || QueryPlanGenerator.predicatesChangedFlag)
		{
			return getPairs2();
		}
		String result = "";
		String result1 ="";
		String result2 ="";
		
		for(int i=0;i<QueryPlanGenerator.predicates.size()-1;i++)
		{
			for(String predicate1:QueryPlanGenerator.predicates.get(i))
			{
				if(predicate1.equals("*"))
				{
					predicate1="%";
				}
				for(String predicate2:QueryPlanGenerator.predicates.get(i+1))
				{
					if(predicate2.equals("*"))
					{
						predicate2="%";
					}
					if(predicate1.equals("%") && predicate2.equals("%"))
					{
						predicate1 = "STAR";
						predicate2 = "STAR";
					}
					String travPart = "";
					if(QueryPlanGenerator.travoOPs.get(i).equals("\\"))
					{
						travPart="-PS";
					}
					if(predicate1.equals("%") || predicate2.equals("%"))
					{
						result2 += " OR pair LIKE '"+predicate1+"-"+predicate2+travPart+"' ";
					}
					else
					{
					 result1 += "'"+predicate1+"-"+predicate2+travPart+"',";
					}
				}
			}
		}
		result1 = result1.substring(0,result1.length()-1);
		result = "pair IN ("+result1+") "+result2; 
		
		return result;
	}
	public static String getPairs2()
	{
		String result = " pair IN (";
		
		for(int i=0;i<QueryPlanGenerator.queries.size();i++)
		{
			String Query = QueryPlanGenerator.queries.get(i);
			if(Query.contains("pair IN ("))
			{
				String part = Query.substring(Query.indexOf("pair IN (")+9);
				part = part.substring(0, part.indexOf(")"));
				result +=part+",";	
				
			}
		}
		/*
		 * Treat first predicate.
		 */
		if(QueryPlanGenerator.infiniteRecursionFlag)
		{
			result +="'"+QueryPlanGenerator.predicates.get(0).get(0)+"-"+QueryPlanGenerator.predicates.get(0).get(0)+"',";
		}		
		result = result.substring(0,result.length()-1)+")";
		
		return result;
	}

	public static String getPredicates()
	{
		if(QueryPlanGenerator.infiniteRecursionFlag || QueryPlanGenerator.predicatesChangedFlag)
		{
			return getPredicates2();
		}
		String currentPredicates =" WHERE predicate IN ( ";
		for(List<String>predicates:QueryPlanGenerator.predicates)
		{
			for(String Predicate:predicates)
			{
				if(Predicate.equals("*"))
				{
					currentPredicates ="";
					break;
				}
				currentPredicates+="'"+Predicate+"', ";
			}
			if(currentPredicates.equals(""))
			{
				break;
			}
		}
		currentPredicates = currentPredicates.substring(0,currentPredicates.length()-2)+" )";
		if(Settings.useEXTVP && Settings.Database.equals("watdiv"))
		{
			return " WHERE predicate IN ( '"+QueryPlanGenerator.predicates.get(0).get(0)+"' )";
		}
		
		return currentPredicates;
	}
	
	public static String getPredicates2()
	{
		
		String currentPredicates =" WHERE predicate IN ( ";
		for(int i=0;i<QueryPlanGenerator.queries.size();i++)
		{
			String Query = QueryPlanGenerator.queries.get(i);
			if(Query.contains("g.predicate = '"))
			{
				String part = Query.substring(Query.indexOf("g.predicate = '")+15);
				part = part.substring(0, part.indexOf("'"));
				currentPredicates +="'"+part+"',";	
				
			}
			else if(Query.contains("predicate IN ("))
			{
				//predicate IN ('snvoc:firstName','snvoc:lastName')
				String part = Query.substring(Query.indexOf("predicate IN (")+14);
				part = part.substring(0, part.indexOf(")"));
				currentPredicates +=part+",";	
			}
		}
		currentPredicates = currentPredicates.substring(0,currentPredicates.length()-1)+" )";
		
		
		return currentPredicates;
	}
	public static void writeResult(String Result)
	{
		try {
		    Files.write(Paths.get("restimes.txt"), Result.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}

}
