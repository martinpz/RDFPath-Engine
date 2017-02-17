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
package loader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import executor.Settings;
import executor.impalaDaemon;

public class VerticalPartitioner2 {
	
	public static void partition(String inputPath,String Graph)
	{
		long lStartTime = System.nanoTime();
		try
		{
			/*
			 * Create rawTable(Unpartitioned table) from file
			 */
			String rawtableQuery = "create table rawgraph(subject string, predicate string, object string)"
					+ " row format delimited"
					+ " fields terminated by '\\t'"
					+ " location '"+inputPath+"';";
			impalaDaemon.noReturn(rawtableQuery);
			/*
			 * Create Table Mask
			 */
			String table1Query = "CREATE TABLE mask (subject string, object string) PARTITIONED BY (predicate string)";
			impalaDaemon.noReturn(table1Query);
			/*
			 * Create Table Graph
			 */
			String table2Query = "CREATE TABLE "+Graph+" LIKE mask STORED AS parquet";
			impalaDaemon.noReturn(table2Query);
			/*
			 * Get all predicates
			 */
			String predicatesQuery = "SELECT predicate FROM rawgraph WHERE subject != '@prefix' GROUP BY predicate";
			ResultSet getPredicates = impalaDaemon.main(predicatesQuery);
			ArrayList<String> predicates = new ArrayList<String>();
			while(getPredicates.next())
			{
			  predicates.add(getPredicates.getString(1));
			}
			for(String predicate:predicates)
			{
				String Query = "INSERT INTO "+Graph+" partition(predicate='"+predicate+"') SELECT subject,object FROM rawgraph WHERE predicate ='"+predicate+"'";
				impalaDaemon.noReturn(Query);
			}
			/*
			 * Compute stats for table
			 */
			impalaDaemon.noReturn("COMPUTE STATS "+Graph+" ");
			/*
			 * Drop rawTable
			 */
			impalaDaemon.noReturn("DROP TABLE rawgraph");
			/*
			 * Drop maskTable
			 */
			impalaDaemon.noReturn("DROP TABLE mask");
			/*
			 * Close connection.
			 */
			impalaDaemon.closeConnection();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
		
		long lEndTime = System.nanoTime();
		long difference = lEndTime - lStartTime;

		System.out.println("Partitioning complete.\nElapsed milliseconds: " + difference/1000000);
		
		if(Settings.useEXTVP)
		{
			//extpartition();
		}
		
	}

	public static void extpartition(String Graph)
	{
		long lStartTime = System.nanoTime();
		try
		{
			/*
			 * Create Table Mask1
			 */
			String table1Query = "CREATE TABLE mask1 (pair string,selectivity double)";
			impalaDaemon.noReturn(table1Query);
			/*
			 * Create Table extvpsel
			 */
			String table2Query = "CREATE TABLE extvpsel LIKE mask1 STORED AS parquet";
			impalaDaemon.noReturn(table2Query);
			
			/*
			 * Create Table Mask2
			 */
			table1Query = "CREATE TABLE mask2 (subject string, predicate string, object string) PARTITIONED BY (pair string)";
			impalaDaemon.noReturn(table1Query);
			/*
			 * Create Table extvpdata
			 */
			table2Query = "CREATE TABLE "+Graph+"_extvpdata LIKE mask2 STORED AS parquet";
			impalaDaemon.noReturn(table2Query);
			/*
			 * Create Table Mask3
			 */
			String table3Query = "CREATE TABLE mask3 (pair string,selectivity double)";
			impalaDaemon.noReturn(table3Query);
			/*
			 * Create Table Mask3
			 */
			table3Query = "CREATE TABLE mask3 (pair string,selectivity double)";
			impalaDaemon.noReturn(table3Query);
			/*
			 * Create Table extvpstats
			 */
			table3Query = "CREATE TABLE "+Graph+"_extvpstats  LIKE mask3 STORED AS parquet";
			impalaDaemon.noReturn(table3Query);
			
			/*
			 * Get all predicates
			 */
			String predicatesQuery = "SELECT predicate FROM "+Graph+" WHERE subject != '@prefix' GROUP BY predicate";
			ResultSet getPredicates = impalaDaemon.main(predicatesQuery);
			ArrayList<String> predicates = new ArrayList<String>();
			while(getPredicates.next())
			{
			  predicates.add(getPredicates.getString(1));
			}
			/*
			 * For each predicate pair, Join graph filtered by predicates on O-S.
			 * Store semi-join of right predicate, and distinct values of join parameter.
			 */
			int count =0;
			for(String predicate1:predicates)
			{	
				for(String predicate2:predicates)
				{
					count++;
					/*
					 * Calculate extVP for all predicate pairs, join on OS
					 */
					System.out.println("Current Pair: "+"'"+predicate1+"-"+predicate2+"'");
					System.out.println("Progress: "+count+"/"+(predicates.size()*predicates.size()));
					String Query1 = "INSERT INTO "+Graph+"_extvpdata partition(pair='"+predicate1+"-"+predicate2+"') "
							+ "SELECT DISTINCT g2.subject, g2.predicate, g2.object FROM "+Graph+" g1, "+Graph+" g2 "
							+ "WHERE g1.predicate ='"+predicate1+"' "
							+ "AND g2.predicate ='"+predicate2+"' "
							+ "AND g1.object = g2.subject";
					impalaDaemon.noReturn(Query1);
					
					/*
					 * Calculate Selectivity for OS
					 */
					String Selectivity1 = "WITH ext AS (SELECT Count(*) as cnt FROM "+Graph+"_extvpdata where pair='"+predicate1+"-"+predicate2+"'),"+ 
											   " vp AS (SELECT Count(*) as cnt FROM "+Graph+" where predicate='"+predicate2+"') "+
											   " INSERT INTO extvpsel Select '"+predicate1+"-"+predicate2+"' as pair, "
											   + " cast(ext.cnt as double)/cast(vp.cnt as double) as selectivity From ext, vp";
					impalaDaemon.noReturn(Selectivity1);
					/*
					 * Calculate extVP for all predicate pairs, join on PS
					 */
					Query1 = "INSERT INTO "+Graph+"_extvpdata partition(pair='"+predicate1+"-"+predicate2+"-PS') "
							+ "SELECT DISTINCT g2.subject, g2.predicate, g2.object FROM "+Graph+" g1, "+Graph+" g2 "
							+ "WHERE g1.predicate ='"+predicate1+"' "
							+ "AND g2.predicate ='"+predicate2+"' "
							+ "AND g1.predicate = g2.subject";
					impalaDaemon.noReturn(Query1);
					/*
					 * Calculate Selectivity for PS
					 */
					Selectivity1 = "WITH ext AS (SELECT Count(*) as cnt FROM "+Graph+"_extvpdata where pair='"+predicate1+"-"+predicate2+"-PS'),"+ 
							   " vp AS (SELECT Count(*) as cnt FROM "+Graph+" where predicate='"+predicate2+"') "+
							   " INSERT INTO extvpsel Select '"+predicate1+"-"+predicate2+"-PS' as pair, "
							   + " cast(ext.cnt as double)/cast(vp.cnt as double) as selectivity From ext, vp";
					impalaDaemon.noReturn(Selectivity1);
					
					
				}
				
			}
			/*
			 * Predicates that produce literals
			 */
			String predicateLiteral = " INSERT INTO extvpsel Select DISTINCT predicate as pair, cast(2.0 as double) as selectivity FROM "+Graph+" WHERE object LIKE '\"%'";
			impalaDaemon.noReturn(predicateLiteral);
			/*
			 * Get only selectivities >0
			 */
			String selectivities = " INSERT INTO "+Graph+"_extvpstats  Select pair, selectivity FROM extvpsel WHERE selectivity>0";
			impalaDaemon.noReturn(selectivities);
			/*
			 * Star-Star case PS.
			 */
			String Query1 = "INSERT INTO "+Graph+"_extvpdata partition(pair='STAR-STAR-PS') "
							+ "SELECT DISTINCT g2.subject, g2.predicate, g2.object FROM "+Graph+" g1, "+Graph+" g2 "
							+ "WHERE g1.predicate = g2.subject";
			impalaDaemon.noReturn(Query1);
			/*
			 * Star-Star case OS.
			 */
			Query1 = "INSERT INTO "+Graph+"_extvpdata partition(pair='STAR-STAR') "
							+ "SELECT DISTINCT g2.subject, g2.predicate, g2.object FROM "+Graph+" g1, "+Graph+" g2 "
							+ "WHERE g1.object = g2.subject";
			impalaDaemon.noReturn(Query1);
			/*
			 * Compute stats for table
			 */
			impalaDaemon.noReturn("COMPUTE STATS "+Graph+"_extvpstats ");
			/*
			 * Compute data for table
			 */
			impalaDaemon.noReturn("COMPUTE STATS "+Graph+"_extvpdata ");
			/*
			 * Drop rawTable
			 */
			impalaDaemon.noReturn("DROP TABLE mask1");
			/*
			 * Drop maskTable
			 */
			impalaDaemon.noReturn("DROP TABLE mask2");
			/*
			 * Drop maskTable
			 */
			impalaDaemon.noReturn("DROP TABLE mask3");
			/*
			 * Drop maskTable
			 */
			//impalaDaemon.noReturn("DROP TABLE extvpsel");
			/*
			 * Close connection.
			 */
			impalaDaemon.closeConnection();
			
			
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
		
		long lEndTime = System.nanoTime();
		long difference = lEndTime - lStartTime;
	
		System.out.println("Extended Partitioning complete.\nElapsed milliseconds: " + difference/1000000);
			
			
		}
}
