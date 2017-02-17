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

public class FilterGenerator {

	public static String valueF(String op,String value)
	{
		return " g.object "+op+" '"+value+"'";
	}
	public static String funcvaluef(String op, String value)
	{
		value = value.replace("(","('");
		value = value.replace(")","')");
		return " g.object "+op+" "+value+" ";
	}
	public static String funcf(String value)
	{
		return " "+value.substring(0,value.length()-1)+"g.object) = 1";
	}
	
	/*
	 * Modify last query.
	 * Project only columns outside the filter.
	 * Add filter expression to the end of query.
	 * If recursion add current step to recursion branch. 
	 */
	public static void applyNestedFilter()
	{
		String oldQuery = QueryPlanGenerator.queries.get(QueryPlanGenerator.queries.size()-1);
		String newQuery = projectColumns(QueryPlanGenerator.nestedFilterColumn)+" "+oldQuery.substring(oldQuery.indexOf("FROM"))+" "+QueryPlanGenerator.nestedFilterexpr;
		QueryPlanGenerator.queries.set(QueryPlanGenerator.queries.size()-1,newQuery);
		/*
		 * Remove predicates from list
		 */
		removepreds(QueryPlanGenerator.cloumnCounter, QueryPlanGenerator.nestedFilterColumn);
		if(QueryPlanGenerator.filterRecursionFlag)
		{
			int[] resultsStep = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.nestedFilterColumn};
			QueryPlanGenerator.recursionMetaData.branchResults.add(resultsStep);
			QueryPlanGenerator.filterRecursionFlag=false;
		}
	}
	
	public static void applyCnestedFilterv1()
	{
		String newQuery = projectColumns(QueryPlanGenerator.cnestedFilterColumn1)+" ";
		
		/*
		 * Select last steps of two filter paths
		 */
		//newQuery += " FROM st"+QueryPlanGenerator.cnestedmidStep+" p, st"+QueryPlanGenerator.stepCounter+" r WHERE ";
		/*
		 * Join filter paths on last column of filter start step.
		 */
		newQuery += " p.c"+(QueryPlanGenerator.cnestedFilterColumn1)+"=r.c"+(QueryPlanGenerator.cnestedFilterColumn1);
		/*
		 * Apply filter operation
		 */
		newQuery += " AND p.c"+QueryPlanGenerator.cnestedFilterColumn2+QueryPlanGenerator.cnestedFilterop+"r.c"+QueryPlanGenerator.cloumnCounter+" ";
		/*
		 * Add Query to Queries, set new ColumnCounter and stepCounter 
		 */
		QueryPlanGenerator.queries.add(newQuery);
		QueryPlanGenerator.stepCounter++;
		QueryPlanGenerator.cloumnCounter=QueryPlanGenerator.cnestedFilterColumn1;
	}
	
	/*
	 * Modify last query, 
	 * Project distinct columns outside filter
	 * Apply filter.
	 * If recursion add current step to recursion branch. 
	 */
	public static void applyCnestedFilterv2()
	{
		FilterGenerator.removepreds(QueryPlanGenerator.cloumnCounter, QueryPlanGenerator.cnestedFilterColumn2);
		String oldQuery = QueryPlanGenerator.queries.get(QueryPlanGenerator.stepCounter-1);
		String newQuery = projectColumns(QueryPlanGenerator.cnestedFilterColumn1)+" "+oldQuery.substring(oldQuery.indexOf("FROM"));
		if(QueryPlanGenerator.travoOPs.get(QueryPlanGenerator.travoOPs.size()-1).equals("/"))
		{
			newQuery += " AND c"+Integer.toString(QueryPlanGenerator.cnestedFilterColumn2)+QueryPlanGenerator.cnestedFilterop+"g.object";
		}
		else
		{
			newQuery += " AND c"+Integer.toString(QueryPlanGenerator.cnestedFilterColumn2)+QueryPlanGenerator.cnestedFilterop+"g.predicate";
		}
				
		QueryPlanGenerator.queries.set(QueryPlanGenerator.stepCounter-1,newQuery);
		QueryPlanGenerator.cloumnCounter=QueryPlanGenerator.cnestedFilterColumn1;
		
		if(QueryPlanGenerator.filterRecursionFlag)
		{
			int[] resultsStep = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.cloumnCounter};
			QueryPlanGenerator.recursionMetaData.branchResults.add(resultsStep);
			QueryPlanGenerator.filterRecursionFlag=false;
		}
	}
	
	
	
	public static String projectColumns(int Limit)
	{
		String result = "SELECT DISTINCT ";
		for(int i=1;i<=Limit;i++)
		{
			result += " p.c"+Integer.toString(i)+" AS c"+Integer.toString(i)+", ";
		}
		result = result.substring(0,result.length()-2);
		return result;
	}
	
	public static void removepreds(int startColumn,int endColumn)
	{
		QueryPlanGenerator.predicatesChangedFlag = true;
		int i=startColumn;
		while(i>endColumn && i!=1)
		{
			QueryPlanGenerator.predicates.remove(QueryPlanGenerator.predicates.size()-1);
			i-=1;
			if(QueryPlanGenerator.travoOPs.get(QueryPlanGenerator.travoOPs.size()-1).equals("/"))
			{
				i-=1;
			}
			QueryPlanGenerator.travoOPs.remove(QueryPlanGenerator.travoOPs.size()-1);
		}
	}
}
