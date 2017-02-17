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


public class StartNodeGenerator {

	public static String startNode = "";
	/*
	 * Types:  
	 * 1 - iri  
	 * 2 - setiri  
 	 * 3 - *    
	 */
	public static void generate(int type, String value)
	{

		if(type==1)
		{
			startNode = " '0'='0' AND g.subject = '"+value+"' ";
		}
		else if(type==2)
		{
			value = value.substring(1,value.length()-1);
			String[] startNodes = value.split(",");
			String Query = "";
			for(String node:startNodes)
			{
				Query += "'"+node+"',";
			}
			Query = Query.substring(0,Query.length()-1);
			Query = " '0'='0' AND g.subject IN ("+Query+")";
			startNode = " ( "+Query+" ) ";
		}
		else if(type==3)
		{
			startNode = " '0'='0' ";
		}
		
		
		
		
	}
	
	
}
