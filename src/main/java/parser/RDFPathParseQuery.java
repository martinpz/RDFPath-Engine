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
package parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.gui.TreeViewer;


public class RDFPathParseQuery {

		/*
		 * Parses the query read from input file or input query string.
		 */
		public static ParseTree parse(String Query, boolean file) throws IOException
		{
			RDFPathLexer lexer; 
			/*
			 * Check whether input is string or query.
			 */
			if(file)
			{
				lexer = new RDFPathLexer(new ANTLRFileStream(Query));
			}
			else
			{
				lexer = new RDFPathLexer(new ANTLRInputStream(Query));
			}
				
			/*
			 *  wrap a token-stream around the lexer
			 */
			CommonTokenStream tokens = new CommonTokenStream(lexer);


			/*
			 *  create the parser
			 */
			RDFPathParser parser = new RDFPathParser(tokens);

			/*
			 *  invoke the entry point of our parser
			 */
			ParseTree tree = parser.query();
			/*
			 * Show tree
			 */
			final List<String> ruleNames =  Arrays.asList(RDFPathParser.ruleNames);
			final TreeViewer view = new TreeViewer(ruleNames, tree);
		    view.open();
			
			
			/*
			 *  Return the query tree
			 */
			return tree;
			
		}
		
		public static void visualize(RDFPathParser parser, ParseTree tree)
		{

		
		}
	
	
}
