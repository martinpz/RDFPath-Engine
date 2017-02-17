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

public class FirstStepGenerator {
	
	public static void generate(String travOp, int predicateType, String predicateValue,String filterPart)
	{
		String predicatePart = "";
		/*
		 * Keep Track of travOP
		 */
		QueryPlanGenerator.travoOPs.add(travOp);
		if(predicateType==1)
		{
			/*
			 * Generate Query Part.
			 */
			predicatePart = " AND g.predicate = '"+predicateValue+"' ";
			/*
			 * Keep track of predicates
			 */
			ArrayList<String> currentPredicates = new ArrayList<String>();
			currentPredicates.add(predicateValue);
			QueryPlanGenerator.predicates.add(currentPredicates);
		}
		else if(predicateType==2)
		{
			predicateValue = predicateValue.substring(1,predicateValue.length()-1);
			String[] startNodes = predicateValue.split(",");
			String Query = "";
			
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
			Query = " g.predicate IN ("+Query+")";
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
		
		String Query = "";
		/*
		 * Check whether to traverse to predicate or object
		 */
		if(travOp.equals("\\"))
		{
			Query += "SELECT subject as c1, predicate as c2 ";
			QueryPlanGenerator.cloumnCounter = 2;
		}
		else
		{
			Query += "SELECT subject as c1, predicate as c2, object as c3 ";
			QueryPlanGenerator.cloumnCounter = 3;
		}
		Query += "  FROM "+Settings.Graph+" g "+
				 " WHERE "+StartNodeGenerator.startNode+predicatePart+filterPart;
		/*
		 * Check if need to continue branch
		 */
		if(QueryPlanGenerator.branchStepLength>0)
		{
			QueryPlanGenerator.stepCounter += QueryPlanGenerator.branchStepLength;
			QueryPlanGenerator.branchStepLength = 0;
		}
		QueryPlanGenerator.stepCounter += 1;
		QueryPlanGenerator.queries.add(Query);
		
	}
	
	
	
}
