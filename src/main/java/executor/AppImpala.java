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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import generator.BranchMerge;
import generator.QueryPlanGenerator;

import parser.RDFPathClassListener;

import parser.RDFPathParseQuery;

public class AppImpala {
	static ParseTree tree;
	static RDFPathClassListener extractor;
	static int recursionLast;
	static int beforeRecursion;
	static List<Integer> hybridSteps = new ArrayList<Integer>();
	static String nrResults = "";

	public static void main(String[] args) throws IOException, SQLException {
		
		/*
		 * Read Settings
		 */
		Settings.readSettings();
		Settings.setSettings();
		/*
		 * Check if input is passed
		 */
		if (args.length < 2) {
			System.exit(0);
		}
		else if(args.length>2)
		{
			Settings.Graph=args[2];
			Settings.useEXTVP=Boolean.parseBoolean(args[4]);
			Settings.execMode=Integer.parseInt(args[5]);
		}

		/*
		 * Check if input is string query or file.
		 */
		boolean file = false;
		if (args[0].equals("-f")) {
			file = true;
		}
		/*
		 * Fix Graph name for non-root database
		 */
		Settings.Graph = Settings.Database+"."+Settings.Graph;
		/*
		 * Read Selectivity values from table
		 */
		if (Settings.useEXTVP) {
			readSelectivity();
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
		/*
		 * Handle Count Only Mode!
		 */
		if(Settings.countOnly)
		{
			QueryPlanGenerator.queries.add("SELECT COUNT(*) FROM st"+QueryPlanGenerator.queries.size());
			QueryPlanGenerator.cloumnCounter = 1;
		}
		long lStartTime = System.nanoTime();
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
				   * Treat Result at END, and Count Mode
				   */
				  int limit = 1;
				  if(QueryPlanGenerator.hasResultFunction)
				  {
					  limit +=1;
				  }
				  if(Settings.countOnly)
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
				   * Treat Result at END, and Count Mode
				   */
	
					  if(QueryPlanGenerator.hasResultFunction)
					  {
						  String resultQuery = QueryPlanGenerator.queries.get(QueryPlanGenerator.queries.size()-limit+1);
						  Query = resultQuery.substring(0,resultQuery.indexOf("FROM")+4)+" ( "+Query+" ) res ";
					  }
					  if(Settings.countOnly)
					  {
						  Query = "SELECT COUNT(*) FROM ( "+Query+" ) cnt ";
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
							String Query = "CREATE TABLE st" + Integer.toString(i + 1) + " AS "
									+ QueryPlanGenerator.queries.get(i);
							ResultSet rs = impalaDaemon.main(Query);
							try {while (rs.next()) {}} catch (SQLException e) {}
						}
					}
					calculateFinalResult();
				} else {
					QueryPlanGenerator.queries
							.add("SELECT * FROM st" + Integer.toString(QueryPlanGenerator.stepCounter));
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
						Query = " CREATE TABLE st" + Integer.toString(i + Settings.hybridStep) + " AS " + Query;
						hybridSteps.add(i + Settings.hybridStep);
						ResultSet rs = impalaDaemon.main(Query);
						try {rs.next();} catch (SQLException e) {}
					} else {
						if (length - i > 1) {
							Query = "WITH ";
							for (int j = 0; j < length - i - 1; j++) {
								Query += " st" + Integer.toString(i + j + 1) + " AS ("
										+ QueryPlanGenerator.queries.get(i + j) + ") \n,";
							}
							Query = Query.substring(0, Query.length() - 1) + QueryPlanGenerator.queries.get(length - 1);
							Query = "CREATE TABLE st" + Integer.toString(length) + " AS " + Query;
							hybridSteps.add(length);
							ResultSet rs = impalaDaemon.main(Query);
							try {rs.next();} catch (SQLException e) {}
						} else {
							Query = "CREATE TABLE st" + Integer.toString(length) + " AS "
									+ QueryPlanGenerator.queries.get(length - 1);
							hybridSteps.add(length);
							ResultSet rs = impalaDaemon.main(Query);
							try {rs.next();} catch (SQLException e) {}
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
							Query = " CREATE TABLE st" + Integer.toString(i + Settings.hybridStep) + " AS " + Query;
							hybridSteps.add(i + Settings.hybridStep);
							ResultSet rs = impalaDaemon.main(Query);
							try {
								rs.next();
							} catch (SQLException e) {
							}
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
							.add("SELECT * FROM st" + Integer.toString(QueryPlanGenerator.stepCounter));
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
						Query = " CREATE TABLE st" + Integer.toString(i + Settings.hybridStep) + " AS " + Query;
						hybridSteps.add(i + Settings.hybridStep);
						ResultSet rs = impalaDaemon.main(Query);
						try {
							rs.next();
						} catch (SQLException e) {
						}
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

		System.out.println("Elapsed milliseconds: " + difference / 1000000);
		writeResult(args[1]+","+(difference/1000000)+","+nrResults+"\n");
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
				impalaDaemon.noReturn("DROP TABLE st" + Integer.toString(i));
			}
		}
		/*
		 * Close connection
		 */
		impalaDaemon.closeConnection();

	}

	/*
	 * Execute queries with compositional scemantics.
	 */
	public static void calculateALL() {
		try {
			
			/*
			 * If more than two steps merge all queries to one single query
			 * using WITH clause
			 */
			
			String Query = QueryPlanGenerator.queries.get(0);
			if (QueryPlanGenerator.queries.size() > 1) {
				Query = "WITH ";
				for (int i = 0; i < QueryPlanGenerator.queries.size() - 1; i++) {
					Query += " st" + Integer.toString(i + 1) + " AS (" + QueryPlanGenerator.queries.get(i) + ") \n,";
				}
				Query = Query.substring(0, Query.length() - 1)
						+ QueryPlanGenerator.queries.get(QueryPlanGenerator.queries.size() - 1);
			}
			
			/*
			 * Run the query.
			 */
			ResultSet r = impalaDaemon.main(Query);
			
			if(Settings.countOnly)
			{
				
				nrResults = "";
				while(r.next())
				{
				nrResults = r.getString(1);
				}
				System.out.println("Number of Results: " + nrResults);
				return;

			}
			/*
			 * Read and print results.
			 */

			PrintWriter writer = new PrintWriter(Settings.resultsFolder+"result.txt", "UTF-8");

			while (r.next()) {
				String result = "";
				for (int i = 1; i <= QueryPlanGenerator.cloumnCounter; i++) {
					if (r.getString(i).equals("##")) {
						continue;
					}
					result += r.getString(i) + "\t";
				}
				writer.println(result);
			}
			writer.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Execute queries with materializing scemantics
	 */
	public static void calculateIntermediate() {
		for (int i = 0; i < QueryPlanGenerator.queries.size() - 1; i++) {
			String Query = "CREATE TABLE st" + Integer.toString(i + 1) + " AS " + QueryPlanGenerator.queries.get(i);
			ResultSet rs = impalaDaemon.main(Query);
			try {
				while (rs.next()) {
				}
			} catch (SQLException e) {
			}
		}

	}

	public static void calculateFinalResult() {
		try {
			/*
			 * Run the query.
			 */
			ResultSet r = impalaDaemon.main(QueryPlanGenerator.queries.get(QueryPlanGenerator.queries.size() - 1));
			if(Settings.countOnly)
			{
				
				nrResults = "";
				while(r.next())
				{
				nrResults = r.getString(1);
				}

				System.out.println("Number of Results: " + nrResults);
				return;

			}
			/*
			 * Read and print results.
			 */

			PrintWriter writer = new PrintWriter(Settings.resultsFolder+"result.txt", "UTF-8");
			while (r.next()) {
				String result = "";
				for (int i = 1; i <= QueryPlanGenerator.cloumnCounter; i++) {
					if (r.getString(i).equals("##")) {
						continue;
					}
					result += r.getString(i) + "\t";
				}
				writer.println(result);
				if(QueryPlanGenerator.cloumnCounter==1)
				{
					nrResults=result;
				}
			}
			writer.close();

		} catch (SQLException | FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static void clearIntermediate() {
		try {
			for (int i = 1; i < QueryPlanGenerator.queries.size(); i++) {
				impalaDaemon.noReturn("DROP TABLE st" + Integer.toString(i));
			}
		} catch (Exception e) {

		}

	}

	/*
	 * Save Queries to disk
	 */
	public static void saveQueries() {
		try {
			PrintWriter writer = new PrintWriter(Settings.resultsFolder+"queries.txt", "UTF-8");

			for (String Query : QueryPlanGenerator.queries) {
				writer.println(Query);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
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
			ResultSet r = impalaDaemon.main(Query);
			/*
			 * Read results and fill the HashMap.
			 */
			while (r.next()) {
				QueryPlanGenerator.Selectivity.put(r.getString(1), r.getDouble(2));
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Check if given step has >0 rows.
	 */
	public static boolean isNotEmpty(int step,String Condition) {
		boolean result = true;
		try {
			ResultSet rs = impalaDaemon.main("SELECT Count(*) FROM st" + Integer.toString(step)+Condition);

			while (rs.next()) {
				if (rs.getLong(1) == (long) 0) {
					result = false;
				}
			}
		} catch (Exception e) {
			// result = false;
			e.printStackTrace();
		}
		return result;
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
				String Query = "CREATE TABLE st" + Integer.toString(beforeWalk-1) + " AS "
						+ QueryPlanGenerator.queries.get(beforeWalk-1);
				
				ResultSet rs = impalaDaemon.main(Query);
				try {
					while (rs.next()) {
					}
				} catch (SQLException e) {
				}
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
			try
			{
				impalaDaemon.noReturn("Drop Table st"+i);
				QueryPlanGenerator.queries.remove(QueryPlanGenerator.queries.size()-1);
			}
			catch(Exception e)
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

	public static void finalQuery(String Query) {
		try {
			/*
			 * Run the query.
			 */
			ResultSet r = impalaDaemon.main(Query);
			if(Settings.countOnly)
			{
				
				nrResults = "";
				while(r.next())
				{
				nrResults = r.getString(1);
				}

				System.out.println("Number of Results: " + nrResults);
				return;

			}
			/*
			 * Read and print results.
			 */

			PrintWriter writer = new PrintWriter(Settings.resultsFolder+"result.txt", "UTF-8");
			while (r.next()) {
				String result = "";
				for (int i = 1; i <= QueryPlanGenerator.cloumnCounter; i++) {
					if (r.getString(i).equals("##")) {
						continue;
					}
					result += r.getString(i) + "\t";
				}
				writer.println(result);
				if(QueryPlanGenerator.cloumnCounter==1)
				{
					nrResults=result;
				}
			}
			writer.close();

		} catch (SQLException | FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
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
				Query = " CREATE TABLE st" + Integer.toString(i + Settings.hybridStep) + " AS " + Query;
				hybridSteps.add(i + Settings.hybridStep);
				ResultSet rs = impalaDaemon.main(Query);
				try {rs.next();} catch (SQLException e) {}
			} else {
				if (queriesSize - i > 1) {
					Query = "WITH ";
					for (int j = 0; j < queriesSize - i - 1; j++) {
						Query += " st" + Integer.toString(i + j + 1) + " AS ("
								+ QueryPlanGenerator.queries.get(i + j) + ") \n,";
					}
					Query = Query.substring(0, Query.length() - 1) + QueryPlanGenerator.queries.get(queriesSize - 1);
					Query = "CREATE TABLE st" + Integer.toString(queriesSize) + " AS " + Query;
					hybridSteps.add(queriesSize);
					ResultSet rs = impalaDaemon.main(Query);
					try {rs.next();} catch (SQLException e) {}
				} else {
					Query = "CREATE TABLE st" + Integer.toString(queriesSize) + " AS "
							+ QueryPlanGenerator.queries.get(queriesSize - 1);
					hybridSteps.add(queriesSize);
					ResultSet rs = impalaDaemon.main(Query);
					try {rs.next();} catch (SQLException e) {}
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
	
	public static void writeResult(String Result)
	{
		try {
		    Files.write(Paths.get("restimes.txt"), Result.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
		    //exception handling left as an exercise for the reader
		}
	}
	

}
