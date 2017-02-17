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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*
 * This class merges branch results. 
 * If results are of different columns sizes they are converted to same size.
 */
public class BranchMerge {
 
	public static void mergeBranch(List<int[]> branchResults)
	{
		/*
		 * Put steps and corresponding column size to two lists.
		 */
		ArrayList<Integer> steps = new ArrayList<Integer>();
		ArrayList<Integer> columnSizes = new ArrayList<Integer>();
		for(int[] result:branchResults)
		{
			if(!steps.contains(result[0]))
			{
				steps.add(result[0]);
				columnSizes.add(result[1]);
			}		
		}
		String Query = "";
		int maxColumnSize = Collections.max(columnSizes);
		for(int i = 0 ; i<steps.size();i++)
		{
			if(steps.size()==1)
			{
				return;
			}
		 Query +="SELECT "+getColumns(maxColumnSize, columnSizes.get(i))+" FROM st"+Integer.toString(steps.get(i))+" UNION ALL ";	
		}
		Query = Query.substring(0, Query.length()-10);
		
		QueryPlanGenerator.queries.add(Query);
		QueryPlanGenerator.stepCounter += 1;
		QueryPlanGenerator.cloumnCounter = maxColumnSize;
		/*
		 * Treat predicates
		 */
		ArrayList<String> predicates = new ArrayList<String>();
		for(int step :steps)
		{
			try
			{
				predicates.addAll(QueryPlanGenerator.predicates.get(step-1));
			}
			catch(Exception e)
			{
				
			}
		}
		/*
		 * Remove duplicates
		 */
		Set<String> hs = new HashSet<>();
		hs.addAll(predicates);
		predicates.clear();
		predicates.addAll(hs);
		
		QueryPlanGenerator.predicates.add(predicates);
		QueryPlanGenerator.travoOPs.add("/");
		
	}
	public static String getColumns(int maxColumnSize,int columnSize)
	{
		String result = "";
		for(int i =1;i<=maxColumnSize;i++)
		{
			if(i<=columnSize-1)
			{
				result += "c"+Integer.toString(i)+", ";
			}
			else if(i>columnSize-1 && i<maxColumnSize)
			{
				result += "'##' as c"+Integer.toString(i)+", ";
			}
			else if(i==maxColumnSize)
			{
				result += "c"+Integer.toString(columnSize)+" as c"+Integer.toString(i);
			}
		}
		
		
		return result;
	}
}
