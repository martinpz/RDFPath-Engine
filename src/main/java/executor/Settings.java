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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

public class Settings {

	public static HashMap<String, String> listSettings = new HashMap<String, String>();
	/*
	 * Settings for execution mode 1 - Compositional 2 - Materializing 3 -
	 * Hybrid
	 */
	public static int execMode = 1;
	public static int hybridStep = 3;
	public static boolean compositionalLinear = false;
	public static boolean materializingSave = true;

	/*
	 * Settings for Extended Vertical Partitioning
	 */
	public static boolean useEXTVP = true;
	public static double extvpThreshold = 0.5;

	/*
	 * Settings for recursion
	 */
	public static boolean hybridRecursion = false;
	public static boolean hybridFilterRecursion = false;

	/*
	 * Settings for avoid cycles
	 */
	public static boolean avoidCycles = true;

	/*
	 * Settings for graph
	 */
	public static String Graph = "sib4000";
	public static String Database = "default";
	/*
	 * Settings for CountOnly Mode
	 */
	public static boolean countOnly = false;
	/*
	 * Other Settings
	 */
	public static String sparkMaster = "local";
	public static String sparkShufflePartitions = "63";	
	public static String impalaConnURL = "jdbc:hive2://dbisma04.informatik.privat:21050/default;auth=noSasl";
	public static String hiveMetastore = "hdfs://dbisma04.informatik.privat:8020/user/hive/warehouse/";
	public static String sparkStorage = "hdfs://localhost:8020/user/lavderim/sparkStore/";
	public static String resultsFolder = "Results/";
	

	public static void readSettings() {

		String line;
		try {
			InputStream fis = new FileInputStream("RDFPATH.conf");
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if(line.startsWith("#"))
				{
					continue;
				}
				else
				{
					String[] parts = line.split(",");
					listSettings.put(parts[0], parts[1]);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Configuration file not found.\nLoading default settings.");
		} catch (IOException e) {
			System.out.println("Error reading configuration file.\nLoading default settings.");
		}
		
	}
	public static void setSettings()
	{
		if(listSettings.containsKey("execMode"))
		{
			execMode=Integer.parseInt(listSettings.get("execMode"));
		}
		
		if(listSettings.containsKey("hybridStep"))
		{
			hybridStep=Integer.parseInt(listSettings.get("hybridStep"));
		}
		
		if(listSettings.containsKey("useEXTVP"))
		{
			useEXTVP = Boolean.parseBoolean(listSettings.get("useEXTVP"));
		}
		
		if(listSettings.containsKey("extvpThreshold"))
		{
			extvpThreshold = Double.parseDouble(listSettings.get("extvpThreshold"));
		}
		
		if(listSettings.containsKey("hybridRecursion"))
		{
			hybridRecursion = Boolean.parseBoolean(listSettings.get("hybridRecursion"));
		}
		
		if(listSettings.containsKey("hybridFilterRecursion"))
		{
			hybridFilterRecursion = Boolean.parseBoolean(listSettings.get("hybridFilterRecursion"));
		}
		
		if(listSettings.containsKey("avoidCycles"))
		{
			avoidCycles = Boolean.parseBoolean(listSettings.get("avoidCycles"));
		}
		
		if(listSettings.containsKey("Graph"))
		{
			Graph = listSettings.get("Graph");
		}
		
		if(listSettings.containsKey("sparkMaster"))
		{
			sparkMaster = listSettings.get("sparkMaster");
		}
		
		if(listSettings.containsKey("impalaConnURL"))
		{
			impalaConnURL = listSettings.get("impalaConnURL");
		}
		
		if(listSettings.containsKey("hiveMetastore"))
		{
			hiveMetastore = listSettings.get("hiveMetastore");
		}
		
		if(listSettings.containsKey("sparkStorage"))
		{
			sparkStorage = listSettings.get("sparkStorage");
		}
		
		if(listSettings.containsKey("resultsFolder"))
		{
			resultsFolder = listSettings.get("resultsFolder");
		}
		
		if(listSettings.containsKey("Database"))
		{
			Database = listSettings.get("Database");
		}
		
		if(listSettings.containsKey("countOnly"))
		{
			countOnly = Boolean.parseBoolean(listSettings.get("countOnly"));
		}
	     
		if(listSettings.containsKey("compositionalLinear"))
		{
			compositionalLinear = Boolean.parseBoolean(listSettings.get("compositionalLinear"));
		}
		
		if(listSettings.containsKey("sparkShufflePartitions"))
		{
			sparkShufflePartitions = listSettings.get("sparkShufflePartitions");
		}
		
		if(listSettings.containsKey("materializingSave"))
		{
			materializingSave = Boolean.parseBoolean(listSettings.get("materializingSave"));
		}
		
		

	}
}
