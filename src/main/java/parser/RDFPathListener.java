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
// Generated from RDFPath.g4 by ANTLR 4.5
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RDFPathParser}.
 */
public interface RDFPathListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(RDFPathParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(RDFPathParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#ctx}.
	 * @param ctx the parse tree
	 */
	void enterCtx(RDFPathParser.CtxContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#ctx}.
	 * @param ctx the parse tree
	 */
	void exitCtx(RDFPathParser.CtxContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(RDFPathParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(RDFPathParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#travop}.
	 * @param ctx the parse tree
	 */
	void enterTravop(RDFPathParser.TravopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#travop}.
	 * @param ctx the parse tree
	 */
	void exitTravop(RDFPathParser.TravopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#rp}.
	 * @param ctx the parse tree
	 */
	void enterRp(RDFPathParser.RpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#rp}.
	 * @param ctx the parse tree
	 */
	void exitRp(RDFPathParser.RpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#recexpr}.
	 * @param ctx the parse tree
	 */
	void enterRecexpr(RDFPathParser.RecexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#recexpr}.
	 * @param ctx the parse tree
	 */
	void exitRecexpr(RDFPathParser.RecexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#recursion}.
	 * @param ctx the parse tree
	 */
	void enterRecursion(RDFPathParser.RecursionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#recursion}.
	 * @param ctx the parse tree
	 */
	void exitRecursion(RDFPathParser.RecursionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#repeat}.
	 * @param ctx the parse tree
	 */
	void enterRepeat(RDFPathParser.RepeatContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#repeat}.
	 * @param ctx the parse tree
	 */
	void exitRepeat(RDFPathParser.RepeatContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#normalp}.
	 * @param ctx the parse tree
	 */
	void enterNormalp(RDFPathParser.NormalpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#normalp}.
	 * @param ctx the parse tree
	 */
	void exitNormalp(RDFPathParser.NormalpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#edge}.
	 * @param ctx the parse tree
	 */
	void enterEdge(RDFPathParser.EdgeContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#edge}.
	 * @param ctx the parse tree
	 */
	void exitEdge(RDFPathParser.EdgeContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#branchp}.
	 * @param ctx the parse tree
	 */
	void enterBranchp(RDFPathParser.BranchpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#branchp}.
	 * @param ctx the parse tree
	 */
	void exitBranchp(RDFPathParser.BranchpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#branchop}.
	 * @param ctx the parse tree
	 */
	void enterBranchop(RDFPathParser.BranchopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#branchop}.
	 * @param ctx the parse tree
	 */
	void exitBranchop(RDFPathParser.BranchopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilter(RDFPathParser.FilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilter(RDFPathParser.FilterContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#cnestedf}.
	 * @param ctx the parse tree
	 */
	void enterCnestedf(RDFPathParser.CnestedfContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#cnestedf}.
	 * @param ctx the parse tree
	 */
	void exitCnestedf(RDFPathParser.CnestedfContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#cnestedop}.
	 * @param ctx the parse tree
	 */
	void enterCnestedop(RDFPathParser.CnestedopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#cnestedop}.
	 * @param ctx the parse tree
	 */
	void exitCnestedop(RDFPathParser.CnestedopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#nestedf}.
	 * @param ctx the parse tree
	 */
	void enterNestedf(RDFPathParser.NestedfContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#nestedf}.
	 * @param ctx the parse tree
	 */
	void exitNestedf(RDFPathParser.NestedfContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#nestedfexpr}.
	 * @param ctx the parse tree
	 */
	void enterNestedfexpr(RDFPathParser.NestedfexprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#nestedfexpr}.
	 * @param ctx the parse tree
	 */
	void exitNestedfexpr(RDFPathParser.NestedfexprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#compf}.
	 * @param ctx the parse tree
	 * @return 
	 */
	String enterCompf(RDFPathParser.CompfContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#compf}.
	 * @param ctx the parse tree
	 */
	void exitCompf(RDFPathParser.CompfContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#normalf}.
	 * @param ctx the parse tree
	 * @return 
	 */
	String enterNormalf(RDFPathParser.NormalfContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#normalf}.
	 * @param ctx the parse tree
	 */
	void exitNormalf(RDFPathParser.NormalfContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#valuef}.
	 * @param ctx the parse tree
	 */
	void enterValuef(RDFPathParser.ValuefContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#valuef}.
	 * @param ctx the parse tree
	 */
	void exitValuef(RDFPathParser.ValuefContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#funcvaluef}.
	 * @param ctx the parse tree
	 */
	void enterFuncvaluef(RDFPathParser.FuncvaluefContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#funcvaluef}.
	 * @param ctx the parse tree
	 */
	void exitFuncvaluef(RDFPathParser.FuncvaluefContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#funcf}.
	 * @param ctx the parse tree
	 */
	void enterFuncf(RDFPathParser.FuncfContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#funcf}.
	 * @param ctx the parse tree
	 */
	void exitFuncf(RDFPathParser.FuncfContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#op}.
	 * @param ctx the parse tree
	 */
	void enterOp(RDFPathParser.OpContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#op}.
	 * @param ctx the parse tree
	 */
	void exitOp(RDFPathParser.OpContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(RDFPathParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(RDFPathParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#funcvalue}.
	 * @param ctx the parse tree
	 */
	void enterFuncvalue(RDFPathParser.FuncvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#funcvalue}.
	 * @param ctx the parse tree
	 */
	void exitFuncvalue(RDFPathParser.FuncvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#func}.
	 * @param ctx the parse tree
	 */
	void enterFunc(RDFPathParser.FuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#func}.
	 * @param ctx the parse tree
	 */
	void exitFunc(RDFPathParser.FuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#logop}.
	 * @param ctx the parse tree
	 */
	void enterLogop(RDFPathParser.LogopContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#logop}.
	 * @param ctx the parse tree
	 */
	void exitLogop(RDFPathParser.LogopContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#result}.
	 * @param ctx the parse tree
	 */
	void enterResult(RDFPathParser.ResultContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#result}.
	 * @param ctx the parse tree
	 */
	void exitResult(RDFPathParser.ResultContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#iri}.
	 * @param ctx the parse tree
	 */
	void enterIri(RDFPathParser.IriContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#iri}.
	 * @param ctx the parse tree
	 */
	void exitIri(RDFPathParser.IriContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#setIRI}.
	 * @param ctx the parse tree
	 */
	void enterSetIRI(RDFPathParser.SetIRIContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#setIRI}.
	 * @param ctx the parse tree
	 */
	void exitSetIRI(RDFPathParser.SetIRIContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#setINT}.
	 * @param ctx the parse tree
	 */
	void enterSetINT(RDFPathParser.SetINTContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#setINT}.
	 * @param ctx the parse tree
	 */
	void exitSetINT(RDFPathParser.SetINTContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#intvaluetxt}.
	 * @param ctx the parse tree
	 */
	void enterIntvaluetxt(RDFPathParser.IntvaluetxtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#intvaluetxt}.
	 * @param ctx the parse tree
	 */
	void exitIntvaluetxt(RDFPathParser.IntvaluetxtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#intvalue}.
	 * @param ctx the parse tree
	 */
	void enterIntvalue(RDFPathParser.IntvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#intvalue}.
	 * @param ctx the parse tree
	 */
	void exitIntvalue(RDFPathParser.IntvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link RDFPathParser#textvalue}.
	 * @param ctx the parse tree
	 */
	void enterTextvalue(RDFPathParser.TextvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link RDFPathParser#textvalue}.
	 * @param ctx the parse tree
	 */
	void exitTextvalue(RDFPathParser.TextvalueContext ctx);
}