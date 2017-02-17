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

import executor.Settings;


public class IterationStepGenerator {
	
	/*
	 * Generate SQL query for one traversion step of a normal path.
	 */
	public static void generateNormalp(String travOp, int predicateType, String predicateValue,String filterPart)
	{
		/*
		 * Treat inifniteRecursionFlag
		 */
		if(QueryPlanGenerator.infiniteRecursionFlag)
		{
			return;
		}
		/*
		 * Keep Track of travOP
		 */
		QueryPlanGenerator.travoOPs.add(travOp);
		if(QueryPlanGenerator.stepCounter == 0)
		{
			FirstStepGenerator.generate(travOp,predicateType, predicateValue,filterPart);
		}
		else
		{
			
			String Query = "";
			String predicatePart = "";
			if(predicateType==1)
			{
				/*
				 * Generate Query Part.
				 */
				predicatePart = " AND g.predicate = '"+predicateValue+"' ";
				/*
				 * Keep track of predicates.
				 */
				ArrayList<String> currentPredicates = new ArrayList<String>();
				currentPredicates.add(predicateValue);
				QueryPlanGenerator.predicates.add(currentPredicates);
			}
			else if(predicateType==2)
			{
				predicateValue = predicateValue.substring(1,predicateValue.length()-1);
				String[] startNodes = predicateValue.split(",");
				Query = "";
				
				ArrayList<String> currentPredicates = new ArrayList<String>();
				for(String node:startNodes)
				{
					/*
					 * Generate Query Part.
					 */
					Query += "'"+node+"',";
					/*
					 * Keep track of predicates.
					 */				
					currentPredicates.add(node);
				}
				QueryPlanGenerator.predicates.add(currentPredicates);
				
				Query = Query.substring(0,Query.length()-1);
				Query = " predicate IN ("+Query+")";
				predicatePart = " AND ( "+Query+" ) ";
			}
			else if(predicateType==3)
			{
				/*
				 * Generate Query Part.
				 */
				predicatePart = " AND '0'='0' ";
				/*
				 * Keep track of predicates.
				 */
				ArrayList<String> currentPredicates = new ArrayList<String>();
				currentPredicates.add("*");
				QueryPlanGenerator.predicates.add(currentPredicates);
			}
			String firstPart = "SELECT "+getColumns(travOp)+
					   "  FROM st"+Integer.toString(QueryPlanGenerator.stepCounter)+" p, "+getGraph();
			if(QueryPlanGenerator.extVP)
			{
				predicatePart = " ";
			}
			/*
			 * Join on filterStart column if complex nested filter (part after operator)
			 */
			if(!QueryPlanGenerator.cnestedFilterFlag)
			{
				firstPart +=" WHERE p.c"+Integer.toString(QueryPlanGenerator.cloumnCounter)+" = g.subject ";
			}
			else
			{
				firstPart +=" WHERE p.c"+Integer.toString(QueryPlanGenerator.cnestedFilterColumn1)+" = g.subject ";
				QueryPlanGenerator.cnestedFilterFlag=false;
			}
			
			/*
			 * Avoid cycles
			 */
			String cyclesPart = " ";
			if(Settings.avoidCycles)
			{
				cyclesPart = avoidCycles2(travOp);
			}
			Query = firstPart+predicatePart+filterPart+cyclesPart+filterLiterals();
			/*
			 * Check if need to continue branch
			 */
			if(QueryPlanGenerator.branchStepLength>0)
			{
				QueryPlanGenerator.stepCounter += QueryPlanGenerator.branchStepLength;
				QueryPlanGenerator.branchStepLength = 0;
			}
			QueryPlanGenerator.stepCounter += 1;
			QueryPlanGenerator.cloumnCounter += 2;
			/*
			 * Check if traversing to predicate
			 */
			if(travOp.equals("\\"))
			{
				QueryPlanGenerator.cloumnCounter -= 1;
			}
			QueryPlanGenerator.queries.add(Query);
			QueryPlanGenerator.extVP = false;
		}
		
		
		
		
		
	}
	/*
	 * Jump to object or predicate based on traversing operator value.
	 */
	public static String getColumns(String travOp)
	{
		String result = "";
		
		for(int i=1;i<=QueryPlanGenerator.cloumnCounter;i++)
		{
			result += " p.c"+Integer.toString(i)+" AS c"+Integer.toString(i)+",";
		}
		if(travOp.equals("/"))
		{
			result += " g.predicate AS c"+Integer.toString(QueryPlanGenerator.cloumnCounter+1)+" ,g.object AS c"+Integer.toString(QueryPlanGenerator.cloumnCounter+2)+" "; 			
		}
		else
		{
			result += " g.predicate AS c"+Integer.toString(QueryPlanGenerator.cloumnCounter+1)+" "; 
			
		}
		
		
		return result;
		
	}
	/*
	 * Generate condition to avoid cycles
	 */
	public static String avoidCycles(String travOP)
	{
		String result = " AND ";
		int n = QueryPlanGenerator.cloumnCounter;
		for(int i =1;i<(n-1);i++)
		{
			result += " NOT( "+" C"+Integer.toString(i)+" ="+" C"+Integer.toString(n)+" AND "+" C"+Integer.toString(i+1)+" ="+" predicate ) AND";
		}
		if(travOP.equals("/"))
		{
			for(int i =1;i<n;i++)
			{
				result += " NOT( "+" C"+Integer.toString(i)+" ="+" predicate AND "+" C"+Integer.toString(i+1)+" ="+" object ) AND";
			}
		}
		result = result.substring(0,result.length()-4);
		
		return result;
		
	}
	
	/*
	 * Another way to
	 * Generate condition to avoid cycles
	 */
	public static String avoidCycles2(String travOP)
	{
		String result = " AND ";
		int n = QueryPlanGenerator.cloumnCounter;
		for(int i =1;i<(n-2);i++)
		{
			result += " NOT( "+" c"+Integer.toString(i)+" ="+" predicate AND "+" c"+Integer.toString(i+1)+" =c"+Integer.toString(n)+" ) AND";
		}
		if(travOP.equals("/"))
		{
			for(int i =1;i<(n-1);i++)
			{
				result += " NOT( "+" c"+Integer.toString(i)+" ="+" object AND "+" c"+Integer.toString(i+1)+" ="+" predicate ) AND";
			}
		}
		result = result.substring(0,result.length()-4);
		
		return result;
		
	}
	
	/*
	 * Deal with use of extended vertical partitioning for the graph.
	 */
	public static String getGraph()
	{
		int lastIndex = QueryPlanGenerator.predicates.size()-1;
		String Result = " ( ";
		String travOP = "";
		if(!Settings.useEXTVP)
		{
			return Settings.Graph+" g";
		}
		if(QueryPlanGenerator.travoOPs.get(lastIndex-1).equals("\\"))
		{
			travOP ="-PS";
		}
		if(QueryPlanGenerator.predicates.get(lastIndex).get(0).equals("*"))
		{
			if(QueryPlanGenerator.predicates.get(lastIndex-1).get(0).equals("*"))
			{
				/*
				 * Change flag to true
				 */
				QueryPlanGenerator.extVP = true;
				/*
				 *Read left part which contains only nodes with out degree>0
				 */
				Result += " SELECT subject, predicate, object FROM "+Settings.Graph+"_extvpdata "
						+ "WHERE pair='STAR-STAR"+travOP+"') g";
			}
			else
			{
				/*
				 * Change flag to true
				 */
				QueryPlanGenerator.extVP = true;
				/*
				 *Get on right only parts that will have a match with left part 
				 */
				Result += " SELECT subject, predicate, object FROM "+Settings.Graph+"_extvpdata "
						+ "WHERE ";
				for(String predicate1:QueryPlanGenerator.predicates.get(lastIndex-1))
				{
					
					Result+= " pair LIKE '"+predicate1+"-%"+travOP+"' OR ";
					
				}
				Result = Result.substring(0,Result.length()-3)+" ) g";
			}							
			return Result;
		}
		else if(QueryPlanGenerator.predicates.get(lastIndex-1).get(0).equals("*"))
		{
			if(!QueryPlanGenerator.predicates.get(lastIndex).get(0).equals("*"))
			{
				return Settings.Graph+" g";
			}
		}
		else
		{
			/*
			 * used to check whether is it worth to go with extvp or not
			 */
			boolean efficient = false;
			/*
			 * Change flag to true
			 */
			QueryPlanGenerator.extVP = true;
			/*
			 * Check whether the right part should be checked for distinct.
			 */
			String Distinct = "";
			if(QueryPlanGenerator.predicates.get(lastIndex-1).size()>1)
			{
			  Distinct = " DISTINCT ";	
			}
			/*
			 *Iterate through cross join of L and R predicates 
			 */
			Result += " SELECT"+Distinct+" subject, predicate, object FROM "+Settings.Graph+"_extvpdata "
					+ "WHERE pair IN (";
			for(String predicate1:QueryPlanGenerator.predicates.get(lastIndex-1))
			{
				
				for(String predicate2:QueryPlanGenerator.predicates.get(lastIndex))
				{
					Result +="'"+predicate1+"-"+predicate2+travOP+"',";
					if(QueryPlanGenerator.Selectivity.containsKey(predicate1+"-"+predicate2+travOP))
					{
						if(QueryPlanGenerator.Selectivity.get(predicate1+"-"+predicate2+travOP)<=Settings.extvpThreshold)
						{
							efficient = true;
						}
					}
					else
					{
						efficient = true;
					}
				}
				
			}
			Result = Result.substring(0,Result.length()-1)+") ) g";
			/*
			 * If only union all should be used then it is automatically efficient.
			 */
			if(QueryPlanGenerator.predicates.get(lastIndex-1).size()==1)
			{
				efficient=true;
			}
			if(efficient)
			{
				/*
				 * Change flag to true
				 */
				QueryPlanGenerator.extVP = true;
				return Result;
			}
			else
			{
				return Settings.Graph+" g";
			}
		}
		return Result;
		
	}
	
	/*
	 * Checks whether to remove literals from string.
	 */
	public static String filterLiterals()
	{
		int lastIndex = QueryPlanGenerator.predicates.size()-1;
		if(QueryPlanGenerator.travoOPs.get(lastIndex-1).equals("\\") || !Settings.useEXTVP)
		{
			return ""; 
		}
		else if(Settings.compositionalLinear)
		{
			return "";
		}
		else if(QueryPlanGenerator.predicates.get(lastIndex-1).get(0).equals("*"))
		{
			return " AND p.c"+QueryPlanGenerator.cloumnCounter+" NOT LIKE '\"%'";
		}
		else
		{
			boolean literalPredicate = false;
			for(String predicate1:QueryPlanGenerator.predicates.get(lastIndex-1))
			{
				/*
				 * Check if predicate can result literals!
				 */
				if(QueryPlanGenerator.Selectivity.containsKey(predicate1))
				{
					literalPredicate = true;
					break;
				}
				
			}
			if(literalPredicate)
			{
				return " AND p.c"+QueryPlanGenerator.cloumnCounter+" NOT LIKE '\"%'";
			}
			else
			{
				return "";
			}
		}

	}

	public static String columns()
	{
		String result = "";
		for(int i=1;i<=QueryPlanGenerator.cloumnCounter;i++)
		{
			result += "c"+i+", ";
		}
		result = result.substring(0,result.length()-2);
		return result;
	}
}
