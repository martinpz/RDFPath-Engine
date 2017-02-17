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

public class ResultGenerator {

	public static void generate(String function)
	{
		if(QueryPlanGenerator.infiniteRecursionFlag)
		{
			return;
		}
		if(function.equals("count()"))
		{
			generateCount();
		}
		else if(function.equals("min()") || function.equals("max()") || function.equals("aAvg()"))
		{
			generateAggregate(function);
		}
		else if(function.equals("triple()"))
		{
			generateTriple();
		}
		else if(function.startsWith("limit"))
		{
			generateLimit(function);
		}
		else if(function.startsWith("project"))
		{
			generateProject(function);
		}
	}
	
	public static void generateProject(String function)
	{
		function = function.substring(8,function.length()-1);
		String[] indexes = function.split(",");
		String Selection = "";
		for(String index : indexes)
		{
			/*
			 * Simple leave out the column that is not available
			 */
			Selection +=" c"+index+",";
		}
		
		Selection = Selection.substring(0,Selection.length()-1);
		String Query = "SELECT "+Selection+" FROM st"+QueryPlanGenerator.stepCounter;
		
		QueryPlanGenerator.stepCounter+=1;
		QueryPlanGenerator.cloumnCounter = indexes.length;
		
		QueryPlanGenerator.queries.add(Query);
	}

	public static void generateLimit(String function)
	{
		function = function.replace("(", " ");
		function = function.replace(")", " ");
		
		String Query = QueryPlanGenerator.queries.get(QueryPlanGenerator.queries.size()-1);
		
		QueryPlanGenerator.queries.remove(QueryPlanGenerator.queries.size()-1);
		
		Query = Query+" "+function;
		
		QueryPlanGenerator.queries.add(Query);
	}
	public static void generateTriple()
	{
		String Query = "SELECT c1,c2, c"+Integer.toString(QueryPlanGenerator.cloumnCounter)+" FROM st"+QueryPlanGenerator.stepCounter;
		
		QueryPlanGenerator.stepCounter+=1;
		QueryPlanGenerator.cloumnCounter = 3;
		
		QueryPlanGenerator.queries.add(Query);
	}
	
	
	public static void generateAggregate(String function)
	{
		function = function.substring(0,3);
		String Query = "SELECT "+function+"(c"+Integer.toString(QueryPlanGenerator.cloumnCounter)+") FROM st"+QueryPlanGenerator.stepCounter;
		
		QueryPlanGenerator.stepCounter+=1;
		QueryPlanGenerator.cloumnCounter = 1;
		
		QueryPlanGenerator.queries.add(Query);
	}
	
	
	public static void generateCount()
	{
		String Query = "SELECT COUNT(*) FROM st"+QueryPlanGenerator.stepCounter;
		
		QueryPlanGenerator.stepCounter+=1;
		QueryPlanGenerator.cloumnCounter = 1;
		
		QueryPlanGenerator.queries.add(Query);
		
	}
	
	
}
