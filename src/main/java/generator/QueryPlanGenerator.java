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
package generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import parser.RDFPathParser.RpContext;

public class QueryPlanGenerator {

	public static ArrayList<ArrayList<String>> predicates = new ArrayList<ArrayList<String>>();
	public static ArrayList<String> travoOPs = new ArrayList<String>();
	public static boolean extVP = false;
	public static Map<String, Double> Selectivity = new HashMap<String, Double>();;
	
	public static ArrayList<String> queries = new ArrayList<String>();
	public static int stepCounter = 0;
	public static int cloumnCounter = 0;
	
	public static ArrayList<BranchMetadata> branches = new ArrayList<BranchMetadata>();
	public static int currentBranch = -1;
	public static int branchStepLength = 0;
	
	public static int nestedFilterColumn = 0;
	public static String nestedFilterexpr = "";
	
	public static boolean filterRecursionFlag = false;
	public static boolean cnestedFilterFlag = false;
	public static int cnestedFilterColumn1 = 0;
	public static int cnestedFilterColumn2 = 0;
	public static String cnestedFilterop = "";

	
	public static boolean recursionFlag = false;
	public static BranchMetadata recursionMetaData = new BranchMetadata();
	public static String recursionUpBound = "";
	public static String recursionLowBound = "";
	public static boolean infiniteRecursionFlag = false;
	public static boolean markInifiniteChange = false;
	public static RpContext infiniteRecursionPath;
	public static boolean predicatesChangedFlag = false;
	public static boolean hasBranch = false;
	
	public static boolean hybridRecursion = false;
	public static boolean hasResultFunction = false;
	
	
	
	/*
	 * Checks if the query contains empty result, therefore no need to calculate.
	 */
	public static boolean hasEmpty()
	{
		
		boolean result = true;
		if(QueryPlanGenerator.predicates.size()==1)
		{
			return false;
		}
		int i=0;
		for(int j=1;j<QueryPlanGenerator.predicates.size();j++)
		{
			result = checkEmpty(i, j);
			if(result)
			{
				break;
			}
			i++;
		}
		return result;

	}
	public static boolean checkEmpty(int i, int j)
	{
		boolean result = true;

		if(QueryPlanGenerator.predicates.get(i).get(0).equals("*"))
		{
			if(QueryPlanGenerator.predicates.get(j).get(0).equals("*"))
			{
				return false;
				
			}
			else
			{
				String travOP = "";
				if(QueryPlanGenerator.travoOPs.get(i).equals("\\"))
				{
					travOP = "-PS";
				}
				for(String Predicate2:QueryPlanGenerator.predicates.get(j))
				{
					for(String tempPredicate:QueryPlanGenerator.Selectivity.keySet())
					{
						if(tempPredicate.endsWith("-"+Predicate2+travOP))
						{
								return false;
						}
					}
				}
			
			}
			
		}
		else if(QueryPlanGenerator.predicates.get(j).get(0).equals("*"))
		{
			if(!QueryPlanGenerator.predicates.get(i).get(0).equals("*"))
			{
				for(String Predicate1:QueryPlanGenerator.predicates.get(i))
				{
					for(String tempPredicate:QueryPlanGenerator.Selectivity.keySet())
					{
						if(tempPredicate.startsWith(Predicate1+"-"))
						{
							return false;
						}
					}
	
				}
			
			}
		}
		else
		{
			String travOP = "";
			if(QueryPlanGenerator.travoOPs.get(i).equals("\\"))
			{
				travOP = "-PS";
			}
			for(String Predicate1:QueryPlanGenerator.predicates.get(i))
			{
				for(String Predicate2:QueryPlanGenerator.predicates.get(j))
				{
				  if(QueryPlanGenerator.Selectivity.containsKey(Predicate1+"-"+Predicate2+travOP))
				  {
					  return false;
				  }
				}
			}
		}
		return result;
	}
	
	
}
