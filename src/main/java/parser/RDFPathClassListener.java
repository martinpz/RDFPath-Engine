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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import executor.Settings;
import generator.BranchMerge;
import generator.BranchMetadata;
import generator.FilterGenerator;
import generator.IterationStepGenerator;
import generator.QueryPlanGenerator;
import generator.ResultGenerator;
import generator.StartNodeGenerator;
import parser.RDFPathParser.BranchopContext;
import parser.RDFPathParser.BranchpContext;
import parser.RDFPathParser.CnestedfContext;
import parser.RDFPathParser.CnestedopContext;
import parser.RDFPathParser.CompfContext;
import parser.RDFPathParser.CtxContext;
import parser.RDFPathParser.EdgeContext;
import parser.RDFPathParser.FilterContext;
import parser.RDFPathParser.FuncContext;
import parser.RDFPathParser.FuncfContext;
import parser.RDFPathParser.FuncvalueContext;
import parser.RDFPathParser.FuncvaluefContext;
import parser.RDFPathParser.IntvalueContext;
import parser.RDFPathParser.IntvaluetxtContext;
import parser.RDFPathParser.IriContext;
import parser.RDFPathParser.LogopContext;
import parser.RDFPathParser.NestedfContext;
import parser.RDFPathParser.NestedfexprContext;
import parser.RDFPathParser.NormalfContext;
import parser.RDFPathParser.NormalpContext;
import parser.RDFPathParser.OpContext;
import parser.RDFPathParser.PathContext;
import parser.RDFPathParser.QueryContext;
import parser.RDFPathParser.RecexprContext;
import parser.RDFPathParser.RecursionContext;
import parser.RDFPathParser.RepeatContext;
import parser.RDFPathParser.ResultContext;
import parser.RDFPathParser.RpContext;
import parser.RDFPathParser.SetINTContext;
import parser.RDFPathParser.SetIRIContext;
import parser.RDFPathParser.TextvalueContext;
import parser.RDFPathParser.TravopContext;
import parser.RDFPathParser.ValueContext;
import parser.RDFPathParser.ValuefContext;

public class RDFPathClassListener implements RDFPathListener {




	@Override
	public void enterEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitErrorNode(ErrorNode arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitTerminal(TerminalNode arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterQuery(QueryContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitQuery(QueryContext ctx) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enterCtx(CtxContext ctx) {
		
		if(ctx.getText().equals("*"))
		{
			StartNodeGenerator.generate(3, "*");
		}
		else if(ctx.getText().startsWith("{"))
		{
			StartNodeGenerator.generate(2, ctx.getText());
		}
		else
		{
			StartNodeGenerator.generate(1, ctx.getText());
		}	
	}

	@Override
	public void exitCtx(CtxContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterPath(PathContext ctx) {
		// TODO Auto-generated method stub
		if(ctx.getChildCount()>2)
		{
			if(ctx.getChild(3).getChild(0).getClass().getName().equals("parser.RDFPathParser$RepeatContext"))
			{
				/*
				 * Walk the subtree for the number of repeats.
				 */
				for(int i = 1; i < Integer.parseInt(ctx.getChild(3).getChild(0).getChild(0).getText());i++)
				{
					ParseTreeWalker.DEFAULT.walk(this, ctx.getChild(1));
				}
			}
			else if(ctx.getChild(3).getChild(0).getClass().getName().equals("parser.RDFPathParser$RecursionContext"))
			{
				/*
				 * Handle Recursion enter
				 */
				QueryPlanGenerator.recursionFlag = true;
				QueryPlanGenerator.recursionMetaData.startStep = QueryPlanGenerator.stepCounter;
				QueryPlanGenerator.recursionMetaData.startColumnNumber = QueryPlanGenerator.cloumnCounter;
				QueryPlanGenerator.recursionLowBound = ctx.getChild(3).getChild(0).getChild(0).getText();
				QueryPlanGenerator.recursionUpBound = ctx.getChild(3).getChild(0).getChild(2).getText();
				
				
				/*
				 * Treat (* - Lower Bound)
				 */
				if(QueryPlanGenerator.recursionLowBound.equals("*") && QueryPlanGenerator.stepCounter!=0)
				{
					int[] resultsStep = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.cloumnCounter};
					QueryPlanGenerator.recursionMetaData.branchResults.add(resultsStep); 
				}
				/*
				 * Treat (* - Upper Bound)
				 */
				if(QueryPlanGenerator.recursionUpBound.equals("*"))
				{
					/*
					 * Check if it is a resume of walk
					 */
					if(QueryPlanGenerator.infiniteRecursionFlag)
					{
						QueryPlanGenerator.markInifiniteChange = true;
					}
					else
					{
						ParseTreeWalker.DEFAULT.walk(this, ctx.getChild(1));
						QueryPlanGenerator.infiniteRecursionPath = (RpContext) ctx.getChild(1);
						QueryPlanGenerator.infiniteRecursionFlag = true; 
						/*
						 * Treat execution mode
						 */
						if(Settings.execMode==1)
						{
							Settings.execMode = 2;
							System.out.println("NOTE: Infinite recursion is not possible in compositional execution mode\n "
									+ "changed to materializing execution mode.");
						}
					}
				}
				else
				{
					/*
					 * Set hasBranch flag
					 */
					QueryPlanGenerator.hasBranch = true;
					/*
					 * Walk the subtree for the number of repeats.
					 */
					for(int i = 1; i < Integer.parseInt(ctx.getChild(3).getChild(0).getChild(2).getText());i++)
					{
						ParseTreeWalker.DEFAULT.walk(this, ctx.getChild(1));
					}
				}
				
			}
		}
	}

	@Override
	public void exitPath(PathContext ctx) {
		// TODO Auto-generated method stub
		if(QueryPlanGenerator.markInifiniteChange && QueryPlanGenerator.nestedFilterColumn==0 && ctx.getChildCount()>2)
		{
			QueryPlanGenerator.markInifiniteChange = false;
			QueryPlanGenerator.infiniteRecursionFlag = false;
		}
		if(QueryPlanGenerator.nestedFilterColumn==0 && QueryPlanGenerator.recursionFlag && !QueryPlanGenerator.infiniteRecursionFlag)
		{
			/*
			 * Handle recursion exit
			 */
			BranchMerge.mergeBranch(QueryPlanGenerator.recursionMetaData.branchResults);
			
			QueryPlanGenerator.recursionFlag = false;
			QueryPlanGenerator.recursionMetaData = new BranchMetadata();
		}
	}

	@Override
	public void enterTravop(TravopContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTravop(TravopContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterRp(RpContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitRp(RpContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterNormalp(NormalpContext ctx) {
		// TODO Auto-generated method stub
		
		String travOP = ctx.getParent().getParent().getChild(0).getText();
			
		String filterPart = "";
		if(ctx.getChildCount()>1)
		{
			
			if(ctx.getChild(2).getChild(0).getClass().getName().equals("parser.RDFPathParser$NormalfContext"))
			{
				filterPart = " AND ";
				filterPart += enterNormalf((NormalfContext) ctx.getChild(2).getChild(0));
			}
			else if(ctx.getChild(2).getChild(0).getClass().getName().equals("parser.RDFPathParser$CompfContext"))
			{
				filterPart = " AND ";
				filterPart += enterCompf((CompfContext) ctx.getChild(2).getChild(0));	
			}
		}
		
		
		
		if(ctx.edge().getText().equals("(*)"))
		{
			IterationStepGenerator.generateNormalp(travOP,3, "(*)",filterPart);
		}
		else if(ctx.edge().getText().startsWith("{"))
		{
			IterationStepGenerator.generateNormalp(travOP,2, ctx.edge().getText(),filterPart);
		}
		else
		{
			IterationStepGenerator.generateNormalp(travOP,1, ctx.edge().getText(),filterPart);
		}
		
		/*
		 * Treat recursion
		 */
		if(QueryPlanGenerator.recursionFlag)
		{
			
			if( QueryPlanGenerator.recursionLowBound.equals("+") || QueryPlanGenerator.recursionLowBound.equals("*"))
			{
				int[] resultsStep = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.cloumnCounter};
				if(!QueryPlanGenerator.hybridRecursion || Settings.execMode!=3)
				{
					/*
					 * Check if path has filter no filter and is not inside filter.
					 * Add path to recursion branch if it has only normal filter.
					 * Else mark end of filter to be added as branch result
					 */
						if(ctx.getChildCount()==1 && QueryPlanGenerator.nestedFilterColumn==0 && QueryPlanGenerator.cnestedFilterColumn1==0)
						{
							QueryPlanGenerator.recursionMetaData.branchResults.add(resultsStep);
						}
						else if(ctx.getChildCount()>1)
						{
							if(ctx.getChild(2).getChild(0).getClass().getName().equals("parser.RDFPathParser$NormalfContext"))
							{
								QueryPlanGenerator.recursionMetaData.branchResults.add(resultsStep); 
							}	
						}
						else
						{
							QueryPlanGenerator.filterRecursionFlag=true;
						}
						

				}
					
			}
			else 
			{
				if(QueryPlanGenerator.stepCounter - QueryPlanGenerator.recursionMetaData.startStep >= 
						Integer.parseInt(QueryPlanGenerator.recursionLowBound))
						{
							int[] resultsStep = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.cloumnCounter};
							if(!QueryPlanGenerator.hybridRecursion || Settings.execMode!=3)
							{
								/*
								 * Check if path has filter no filter and is not inside filter.
								 * Add path to recursion branch if it has only normal filter.
								 * Else mark end of filter to be added as branch result
								 */
									if(ctx.getChildCount()==1 && QueryPlanGenerator.nestedFilterColumn==0 && QueryPlanGenerator.cnestedFilterColumn1==0)
									{
										QueryPlanGenerator.recursionMetaData.branchResults.add(resultsStep);
									}
									else if(ctx.getChildCount()>1)
									{
										if(ctx.getChild(2).getChild(0).getClass().getName().equals("parser.RDFPathParser$NormalfContext"))
										{
											QueryPlanGenerator.recursionMetaData.branchResults.add(resultsStep); 
										}	
									}
									else
									{
										QueryPlanGenerator.filterRecursionFlag=true;
									}
							} 
						}
				
			}
		}
		
		
	}

	@Override
	public void exitNormalp(NormalpContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterEdge(EdgeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitEdge(EdgeContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFilter(FilterContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFilter(FilterContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String enterNormalf(NormalfContext ctx) {
		// TODO Auto-generated method stub
		String fType = ctx.getChild(0).getClass().getName();
		

		if (fType.equals("parser.RDFPathParser$FuncvaluefContext"))
		{
			return  FilterGenerator.funcvaluef(ctx.getText().substring(0, 1),ctx.getText().substring(1));
		}
		else if (fType.equals("parser.RDFPathParser$ValuefContext"))
		{
			
			return FilterGenerator.valueF(ctx.getText().substring(0, 1),ctx.getText().substring(1));
		}
		else 
		{
			return FilterGenerator.funcf(ctx.getText());
		}
		
	}

	@Override
	public void exitNormalf(NormalfContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterValuef(ValuefContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitValuef(ValuefContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFuncvaluef(FuncvaluefContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFuncvaluef(FuncvaluefContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFuncf(FuncfContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFuncf(FuncfContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterOp(OpContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitOp(OpContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterValue(ValueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitValue(ValueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFuncvalue(FuncvalueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFuncvalue(FuncvalueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterFunc(FuncContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitFunc(FuncContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterLogop(LogopContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitLogop(LogopContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterResult(ResultContext ctx) {
		// TODO Auto-generated method stub
		ResultGenerator.generate(ctx.getText());
		QueryPlanGenerator.hasResultFunction = true;
	}

	@Override
	public void exitResult(ResultContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterIri(IriContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIri(IriContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSetIRI(SetIRIContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSetIRI(SetIRIContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterSetINT(SetINTContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitSetINT(SetINTContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterTextvalue(TextvalueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitTextvalue(TextvalueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterIntvalue(IntvalueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIntvalue(IntvalueContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterRepeat(RepeatContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitRepeat(RepeatContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterBranchp(BranchpContext ctx) {
		// TODO Auto-generated method stub
	QueryPlanGenerator.branches.add(new BranchMetadata());	
	QueryPlanGenerator.currentBranch += 1;
	QueryPlanGenerator.branches.get(QueryPlanGenerator.currentBranch).startStep = QueryPlanGenerator.stepCounter;
	QueryPlanGenerator.branches.get(QueryPlanGenerator.currentBranch).startColumnNumber = QueryPlanGenerator.cloumnCounter;
	/*
	 * Set hasBranch flag
	 */
	QueryPlanGenerator.hasBranch = true;

	}

	@Override
	public void exitBranchp(BranchpContext ctx) {
		// TODO Auto-generated method stub
		int[] resultsStep = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.cloumnCounter};
		QueryPlanGenerator.branches.get(QueryPlanGenerator.currentBranch).branchResults.add(resultsStep);
		BranchMerge.mergeBranch(QueryPlanGenerator.branches.get(QueryPlanGenerator.currentBranch).branchResults);
		QueryPlanGenerator.branches.remove(QueryPlanGenerator.currentBranch);
		QueryPlanGenerator.currentBranch -= 1;
	}

	@Override
	public void enterBranchop(BranchopContext ctx) {
		// TODO Auto-generated method stub
		/*
		 * Remove predicates when iterating from one branch to the other.
		 */
		FilterGenerator.removepreds(QueryPlanGenerator.cloumnCounter, QueryPlanGenerator.branches.get(QueryPlanGenerator.currentBranch).startColumnNumber);
		
		int[] resultsStep = {QueryPlanGenerator.stepCounter,QueryPlanGenerator.cloumnCounter};
		QueryPlanGenerator.branches.get(QueryPlanGenerator.currentBranch).branchResults.add(resultsStep);
		QueryPlanGenerator.branchStepLength = QueryPlanGenerator.stepCounter - QueryPlanGenerator.branches.get(QueryPlanGenerator.currentBranch).startStep;
		QueryPlanGenerator.stepCounter = QueryPlanGenerator.branches.get(QueryPlanGenerator.currentBranch).startStep;
		QueryPlanGenerator.cloumnCounter = QueryPlanGenerator.branches.get(QueryPlanGenerator.currentBranch).startColumnNumber;
	}

	@Override
	public void exitBranchop(BranchopContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String enterCompf(CompfContext ctx) {
		// TODO Auto-generated method stub
		/*
		 * Generate composite filter part by calculating 
		 * each normal filter part and merge with sepcified operator.
		 */
	
		String filterPart = " (";
		for(int i =0;i<ctx.getChildCount();i++)
		{
			if(i%2==0)
			{
				filterPart += enterNormalf((NormalfContext) ctx.getChild(i));
			}
			else
			{
				if(ctx.getChild(i).getText().equals("&&"))
				{
					filterPart += " AND ";
				}
				else
				{
					filterPart += " OR ";
				}
			}
		}
		filterPart += " )";
		return filterPart;
	}

	@Override
	public void exitCompf(CompfContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterNestedf(NestedfContext ctx) {
		// TODO Auto-generated method stub
		QueryPlanGenerator.nestedFilterColumn=QueryPlanGenerator.cloumnCounter;
	}

	@Override
	public void exitNestedf(NestedfContext ctx) {
		// TODO Auto-generated method stub
		FilterGenerator.applyNestedFilter();
		QueryPlanGenerator.cloumnCounter = QueryPlanGenerator.nestedFilterColumn;
		QueryPlanGenerator.nestedFilterColumn=0;
		QueryPlanGenerator.nestedFilterexpr = "";
	}

	@Override
	public void enterNestedfexpr(NestedfexprContext ctx) {
		// TODO Auto-generated method stub
		String filterPart = " AND ";
		if(ctx.getChild(0).getClass().getName().equals("parser.RDFPathParser$NormalfContext"))
		{
			filterPart += enterNormalf((NormalfContext) ctx.getChild(0));
		}
		else if(ctx.getChild(0).getClass().getName().equals("parser.RDFPathParser$CompfContext"))
		{

			filterPart += enterCompf((CompfContext) ctx.getChild(0));	
		}
		QueryPlanGenerator.nestedFilterexpr = filterPart;
	}

	@Override
	public void exitNestedfexpr(NestedfexprContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterCnestedf(CnestedfContext ctx) {
		// TODO Auto-generated method stub
		QueryPlanGenerator.cnestedFilterColumn1= QueryPlanGenerator.cloumnCounter;
	}

	@Override
	public void exitCnestedf(CnestedfContext ctx) {
		// TODO Auto-generated method stub
		FilterGenerator.applyCnestedFilterv2();
		QueryPlanGenerator.cnestedFilterColumn1 = 0;
		QueryPlanGenerator.cnestedFilterColumn2 = 0;
		QueryPlanGenerator.cnestedFilterop = "";
	}

	@Override
	public void enterCnestedop(CnestedopContext ctx) {
		// TODO Auto-generated method stub
		FilterGenerator.removepreds(QueryPlanGenerator.cloumnCounter, QueryPlanGenerator.cnestedFilterColumn1);
		QueryPlanGenerator.cnestedFilterFlag=true;
		QueryPlanGenerator.cnestedFilterColumn2=QueryPlanGenerator.cloumnCounter;
		QueryPlanGenerator.cnestedFilterop = ctx.getText();
	}

	@Override
	public void exitCnestedop(CnestedopContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterRecexpr(RecexprContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitRecexpr(RecexprContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enterRecursion(RecursionContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitRecursion(RecursionContext ctx) {
		// TODO Auto-generated method stub
		
	
	}

	@Override
	public void enterIntvaluetxt(IntvaluetxtContext ctx) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exitIntvaluetxt(IntvaluetxtContext ctx) {
		// TODO Auto-generated method stub
		
	}







}
