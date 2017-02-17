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
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class RDFPathParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		INT=32, TEXTVALUE=33, LEGALCHAR=34, Whitespace=35, Comment=36;
	public static final int
		RULE_query = 0, RULE_ctx = 1, RULE_path = 2, RULE_travop = 3, RULE_rp = 4, 
		RULE_recexpr = 5, RULE_recursion = 6, RULE_repeat = 7, RULE_normalp = 8, 
		RULE_edge = 9, RULE_branchp = 10, RULE_branchop = 11, RULE_filter = 12, 
		RULE_nestedf = 13, RULE_nestedfexpr = 14, RULE_compf = 15, RULE_cnestedf = 16, 
		RULE_cnestedop = 17, RULE_normalf = 18, RULE_valuef = 19, RULE_funcvaluef = 20, 
		RULE_funcf = 21, RULE_op = 22, RULE_value = 23, RULE_funcvalue = 24, RULE_func = 25, 
		RULE_logop = 26, RULE_result = 27, RULE_iri = 28, RULE_setIRI = 29, RULE_setINT = 30, 
		RULE_intvaluetxt = 31, RULE_intvalue = 32, RULE_textvalue = 33;
	public static final String[] ruleNames = {
		"query", "ctx", "path", "travop", "rp", "recexpr", "recursion", "repeat", 
		"normalp", "edge", "branchp", "branchop", "filter", "nestedf", "nestedfexpr", 
		"compf", "cnestedf", "cnestedop", "normalf", "valuef", "funcvaluef", "funcf", 
		"op", "value", "funcvalue", "func", "logop", "result", "iri", "setIRI", 
		"setINT", "intvaluetxt", "intvalue", "textvalue"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'.'", "'*'", "'('", "')'", "'/'", "'\\'", "','", "'['", "']'", 
		"'(*)'", "'||'", "'='", "'>'", "'<'", "'!='", "'>='", "'<='", "'()'", 
		"'&&'", "'project('", "'nodes()'", "'count()'", "'avg()'", "'sum()'", 
		"'max()'", "'min()'", "'triple()'", "'limit('", "'{'", "'}'", "'+'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, "INT", "TEXTVALUE", "LEGALCHAR", 
		"Whitespace", "Comment"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "RDFPath.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RDFPathParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class QueryContext extends ParserRuleContext {
		public CtxContext ctx() {
			return getRuleContext(CtxContext.class,0);
		}
		public List<PathContext> path() {
			return getRuleContexts(PathContext.class);
		}
		public PathContext path(int i) {
			return getRuleContext(PathContext.class,i);
		}
		public ResultContext result() {
			return getRuleContext(ResultContext.class,0);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitQuery(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_query);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			ctx();
			setState(69);
			match(T__0);
			setState(71); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(70);
				path();
				}
				}
				setState(73); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__4 || _la==T__5 );
			setState(76);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(75);
				match(T__0);
				}
			}

			setState(79);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__19) | (1L << T__20) | (1L << T__21) | (1L << T__22) | (1L << T__23) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27))) != 0)) {
				{
				setState(78);
				result();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CtxContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public SetIRIContext setIRI() {
			return getRuleContext(SetIRIContext.class,0);
		}
		public CtxContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ctx; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterCtx(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitCtx(this);
		}
	}

	public final CtxContext ctx() throws RecognitionException {
		CtxContext _localctx = new CtxContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_ctx);
		try {
			setState(84);
			switch (_input.LA(1)) {
			case INT:
			case TEXTVALUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(81);
				iri();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(82);
				match(T__1);
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 3);
				{
				setState(83);
				setIRI();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PathContext extends ParserRuleContext {
		public TravopContext travop() {
			return getRuleContext(TravopContext.class,0);
		}
		public RpContext rp() {
			return getRuleContext(RpContext.class,0);
		}
		public RecexprContext recexpr() {
			return getRuleContext(RecexprContext.class,0);
		}
		public PathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitPath(this);
		}
	}

	public final PathContext path() throws RecognitionException {
		PathContext _localctx = new PathContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_path);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86);
			travop();
			setState(87);
			rp();
			setState(92);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(88);
				match(T__2);
				setState(89);
				recexpr();
				setState(90);
				match(T__3);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TravopContext extends ParserRuleContext {
		public TravopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_travop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterTravop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitTravop(this);
		}
	}

	public final TravopContext travop() throws RecognitionException {
		TravopContext _localctx = new TravopContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_travop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			_la = _input.LA(1);
			if ( !(_la==T__4 || _la==T__5) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RpContext extends ParserRuleContext {
		public NormalpContext normalp() {
			return getRuleContext(NormalpContext.class,0);
		}
		public BranchpContext branchp() {
			return getRuleContext(BranchpContext.class,0);
		}
		public RpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterRp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitRp(this);
		}
	}

	public final RpContext rp() throws RecognitionException {
		RpContext _localctx = new RpContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_rp);
		try {
			setState(98);
			switch (_input.LA(1)) {
			case T__9:
			case T__28:
			case INT:
			case TEXTVALUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				normalp();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 2);
				{
				setState(97);
				branchp();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RecexprContext extends ParserRuleContext {
		public RepeatContext repeat() {
			return getRuleContext(RepeatContext.class,0);
		}
		public RecursionContext recursion() {
			return getRuleContext(RecursionContext.class,0);
		}
		public RecexprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recexpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterRecexpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitRecexpr(this);
		}
	}

	public final RecexprContext recexpr() throws RecognitionException {
		RecexprContext _localctx = new RecexprContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_recexpr);
		try {
			setState(102);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(100);
				repeat();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(101);
				recursion();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RecursionContext extends ParserRuleContext {
		public List<IntvaluetxtContext> intvaluetxt() {
			return getRuleContexts(IntvaluetxtContext.class);
		}
		public IntvaluetxtContext intvaluetxt(int i) {
			return getRuleContext(IntvaluetxtContext.class,i);
		}
		public RecursionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_recursion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterRecursion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitRecursion(this);
		}
	}

	public final RecursionContext recursion() throws RecognitionException {
		RecursionContext _localctx = new RecursionContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_recursion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			intvaluetxt();
			setState(105);
			match(T__6);
			setState(106);
			intvaluetxt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RepeatContext extends ParserRuleContext {
		public IntvalueContext intvalue() {
			return getRuleContext(IntvalueContext.class,0);
		}
		public RepeatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_repeat; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterRepeat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitRepeat(this);
		}
	}

	public final RepeatContext repeat() throws RecognitionException {
		RepeatContext _localctx = new RepeatContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_repeat);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(108);
			intvalue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NormalpContext extends ParserRuleContext {
		public EdgeContext edge() {
			return getRuleContext(EdgeContext.class,0);
		}
		public FilterContext filter() {
			return getRuleContext(FilterContext.class,0);
		}
		public NormalpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_normalp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterNormalp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitNormalp(this);
		}
	}

	public final NormalpContext normalp() throws RecognitionException {
		NormalpContext _localctx = new NormalpContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_normalp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			edge();
			setState(115);
			_la = _input.LA(1);
			if (_la==T__7) {
				{
				setState(111);
				match(T__7);
				setState(112);
				filter();
				setState(113);
				match(T__8);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EdgeContext extends ParserRuleContext {
		public IriContext iri() {
			return getRuleContext(IriContext.class,0);
		}
		public SetIRIContext setIRI() {
			return getRuleContext(SetIRIContext.class,0);
		}
		public EdgeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_edge; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterEdge(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitEdge(this);
		}
	}

	public final EdgeContext edge() throws RecognitionException {
		EdgeContext _localctx = new EdgeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_edge);
		try {
			setState(120);
			switch (_input.LA(1)) {
			case INT:
			case TEXTVALUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(117);
				iri();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 2);
				{
				setState(118);
				match(T__9);
				}
				break;
			case T__28:
				enterOuterAlt(_localctx, 3);
				{
				setState(119);
				setIRI();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BranchpContext extends ParserRuleContext {
		public List<PathContext> path() {
			return getRuleContexts(PathContext.class);
		}
		public PathContext path(int i) {
			return getRuleContext(PathContext.class,i);
		}
		public List<BranchopContext> branchop() {
			return getRuleContexts(BranchopContext.class);
		}
		public BranchopContext branchop(int i) {
			return getRuleContext(BranchopContext.class,i);
		}
		public BranchpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_branchp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterBranchp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitBranchp(this);
		}
	}

	public final BranchpContext branchp() throws RecognitionException {
		BranchpContext _localctx = new BranchpContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_branchp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(122);
			match(T__2);
			setState(124); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(123);
				path();
				}
				}
				setState(126); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__4 || _la==T__5 );
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__10) {
				{
				{
				setState(128);
				branchop();
				setState(130); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(129);
					path();
					}
					}
					setState(132); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__4 || _la==T__5 );
				}
				}
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(139);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BranchopContext extends ParserRuleContext {
		public BranchopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_branchop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterBranchop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitBranchop(this);
		}
	}

	public final BranchopContext branchop() throws RecognitionException {
		BranchopContext _localctx = new BranchopContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_branchop);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(T__10);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FilterContext extends ParserRuleContext {
		public NormalfContext normalf() {
			return getRuleContext(NormalfContext.class,0);
		}
		public CompfContext compf() {
			return getRuleContext(CompfContext.class,0);
		}
		public NestedfContext nestedf() {
			return getRuleContext(NestedfContext.class,0);
		}
		public CnestedfContext cnestedf() {
			return getRuleContext(CnestedfContext.class,0);
		}
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitFilter(this);
		}
	}

	public final FilterContext filter() throws RecognitionException {
		FilterContext _localctx = new FilterContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_filter);
		try {
			setState(147);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(143);
				normalf();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(144);
				compf();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(145);
				nestedf();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(146);
				cnestedf();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NestedfContext extends ParserRuleContext {
		public NestedfexprContext nestedfexpr() {
			return getRuleContext(NestedfexprContext.class,0);
		}
		public List<PathContext> path() {
			return getRuleContexts(PathContext.class);
		}
		public PathContext path(int i) {
			return getRuleContext(PathContext.class,i);
		}
		public NestedfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterNestedf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitNestedf(this);
		}
	}

	public final NestedfContext nestedf() throws RecognitionException {
		NestedfContext _localctx = new NestedfContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_nestedf);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(150); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(149);
				path();
				}
				}
				setState(152); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__4 || _la==T__5 );
			setState(154);
			nestedfexpr();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NestedfexprContext extends ParserRuleContext {
		public NormalfContext normalf() {
			return getRuleContext(NormalfContext.class,0);
		}
		public CompfContext compf() {
			return getRuleContext(CompfContext.class,0);
		}
		public NestedfexprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedfexpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterNestedfexpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitNestedfexpr(this);
		}
	}

	public final NestedfexprContext nestedfexpr() throws RecognitionException {
		NestedfexprContext _localctx = new NestedfexprContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_nestedfexpr);
		try {
			setState(159);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(156);
				normalf();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(158);
				compf();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CompfContext extends ParserRuleContext {
		public List<NormalfContext> normalf() {
			return getRuleContexts(NormalfContext.class);
		}
		public NormalfContext normalf(int i) {
			return getRuleContext(NormalfContext.class,i);
		}
		public List<LogopContext> logop() {
			return getRuleContexts(LogopContext.class);
		}
		public LogopContext logop(int i) {
			return getRuleContext(LogopContext.class,i);
		}
		public CompfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterCompf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitCompf(this);
		}
	}

	public final CompfContext compf() throws RecognitionException {
		CompfContext _localctx = new CompfContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_compf);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			normalf();
			setState(165); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(162);
				logop();
				setState(163);
				normalf();
				}
				}
				setState(167); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__10 || _la==T__18 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CnestedfContext extends ParserRuleContext {
		public CnestedopContext cnestedop() {
			return getRuleContext(CnestedopContext.class,0);
		}
		public List<PathContext> path() {
			return getRuleContexts(PathContext.class);
		}
		public PathContext path(int i) {
			return getRuleContext(PathContext.class,i);
		}
		public CnestedfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cnestedf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterCnestedf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitCnestedf(this);
		}
	}

	public final CnestedfContext cnestedf() throws RecognitionException {
		CnestedfContext _localctx = new CnestedfContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_cnestedf);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(169);
				path();
				}
				}
				setState(172); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__4 || _la==T__5 );
			setState(174);
			cnestedop();
			setState(176); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(175);
				path();
				}
				}
				setState(178); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__4 || _la==T__5 );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CnestedopContext extends ParserRuleContext {
		public OpContext op() {
			return getRuleContext(OpContext.class,0);
		}
		public CnestedopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cnestedop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterCnestedop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitCnestedop(this);
		}
	}

	public final CnestedopContext cnestedop() throws RecognitionException {
		CnestedopContext _localctx = new CnestedopContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_cnestedop);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			op();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NormalfContext extends ParserRuleContext {
		public ValuefContext valuef() {
			return getRuleContext(ValuefContext.class,0);
		}
		public FuncvaluefContext funcvaluef() {
			return getRuleContext(FuncvaluefContext.class,0);
		}
		public FuncfContext funcf() {
			return getRuleContext(FuncfContext.class,0);
		}
		public NormalfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_normalf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterNormalf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitNormalf(this);
		}
	}

	public final NormalfContext normalf() throws RecognitionException {
		NormalfContext _localctx = new NormalfContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_normalf);
		try {
			setState(185);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(182);
				valuef();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(183);
				funcvaluef();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(184);
				funcf();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValuefContext extends ParserRuleContext {
		public OpContext op() {
			return getRuleContext(OpContext.class,0);
		}
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public ValuefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valuef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterValuef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitValuef(this);
		}
	}

	public final ValuefContext valuef() throws RecognitionException {
		ValuefContext _localctx = new ValuefContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_valuef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			op();
			setState(188);
			value();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncvaluefContext extends ParserRuleContext {
		public OpContext op() {
			return getRuleContext(OpContext.class,0);
		}
		public FuncvalueContext funcvalue() {
			return getRuleContext(FuncvalueContext.class,0);
		}
		public FuncvaluefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcvaluef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterFuncvaluef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitFuncvaluef(this);
		}
	}

	public final FuncvaluefContext funcvaluef() throws RecognitionException {
		FuncvaluefContext _localctx = new FuncvaluefContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_funcvaluef);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			op();
			setState(191);
			funcvalue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncfContext extends ParserRuleContext {
		public FuncContext func() {
			return getRuleContext(FuncContext.class,0);
		}
		public FuncfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcf; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterFuncf(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitFuncf(this);
		}
	}

	public final FuncfContext funcf() throws RecognitionException {
		FuncfContext _localctx = new FuncfContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_funcf);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			func();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OpContext extends ParserRuleContext {
		public OpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterOp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitOp(this);
		}
	}

	public final OpContext op() throws RecognitionException {
		OpContext _localctx = new OpContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public TextvalueContext textvalue() {
			return getRuleContext(TextvalueContext.class,0);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitValue(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			textvalue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncvalueContext extends ParserRuleContext {
		public List<TextvalueContext> textvalue() {
			return getRuleContexts(TextvalueContext.class);
		}
		public TextvalueContext textvalue(int i) {
			return getRuleContext(TextvalueContext.class,i);
		}
		public FuncvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_funcvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterFuncvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitFuncvalue(this);
		}
	}

	public final FuncvalueContext funcvalue() throws RecognitionException {
		FuncvalueContext _localctx = new FuncvalueContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_funcvalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			textvalue();
			setState(200);
			match(T__2);
			setState(201);
			textvalue();
			setState(202);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FuncContext extends ParserRuleContext {
		public TextvalueContext textvalue() {
			return getRuleContext(TextvalueContext.class,0);
		}
		public FuncContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_func; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterFunc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitFunc(this);
		}
	}

	public final FuncContext func() throws RecognitionException {
		FuncContext _localctx = new FuncContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_func);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			textvalue();
			setState(205);
			match(T__17);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LogopContext extends ParserRuleContext {
		public LogopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterLogop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitLogop(this);
		}
	}

	public final LogopContext logop() throws RecognitionException {
		LogopContext _localctx = new LogopContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_logop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			_la = _input.LA(1);
			if ( !(_la==T__10 || _la==T__18) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResultContext extends ParserRuleContext {
		public SetINTContext setINT() {
			return getRuleContext(SetINTContext.class,0);
		}
		public IntvalueContext intvalue() {
			return getRuleContext(IntvalueContext.class,0);
		}
		public ResultContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_result; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterResult(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitResult(this);
		}
	}

	public final ResultContext result() throws RecognitionException {
		ResultContext _localctx = new ResultContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_result);
		try {
			setState(224);
			switch (_input.LA(1)) {
			case T__19:
				enterOuterAlt(_localctx, 1);
				{
				setState(209);
				match(T__19);
				setState(210);
				setINT();
				setState(211);
				match(T__3);
				}
				break;
			case T__20:
				enterOuterAlt(_localctx, 2);
				{
				setState(213);
				match(T__20);
				}
				break;
			case T__21:
				enterOuterAlt(_localctx, 3);
				{
				setState(214);
				match(T__21);
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 4);
				{
				setState(215);
				match(T__22);
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 5);
				{
				setState(216);
				match(T__23);
				}
				break;
			case T__24:
				enterOuterAlt(_localctx, 6);
				{
				setState(217);
				match(T__24);
				}
				break;
			case T__25:
				enterOuterAlt(_localctx, 7);
				{
				setState(218);
				match(T__25);
				}
				break;
			case T__26:
				enterOuterAlt(_localctx, 8);
				{
				setState(219);
				match(T__26);
				}
				break;
			case T__27:
				enterOuterAlt(_localctx, 9);
				{
				setState(220);
				match(T__27);
				setState(221);
				intvalue();
				setState(222);
				match(T__3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IriContext extends ParserRuleContext {
		public TextvalueContext textvalue() {
			return getRuleContext(TextvalueContext.class,0);
		}
		public IriContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iri; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterIri(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitIri(this);
		}
	}

	public final IriContext iri() throws RecognitionException {
		IriContext _localctx = new IriContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_iri);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			textvalue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SetIRIContext extends ParserRuleContext {
		public List<IriContext> iri() {
			return getRuleContexts(IriContext.class);
		}
		public IriContext iri(int i) {
			return getRuleContext(IriContext.class,i);
		}
		public SetIRIContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setIRI; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterSetIRI(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitSetIRI(this);
		}
	}

	public final SetIRIContext setIRI() throws RecognitionException {
		SetIRIContext _localctx = new SetIRIContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_setIRI);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(228);
			match(T__28);
			setState(229);
			iri();
			setState(234);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(230);
				match(T__6);
				setState(231);
				iri();
				}
				}
				setState(236);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(237);
			match(T__29);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SetINTContext extends ParserRuleContext {
		public List<IntvalueContext> intvalue() {
			return getRuleContexts(IntvalueContext.class);
		}
		public IntvalueContext intvalue(int i) {
			return getRuleContext(IntvalueContext.class,i);
		}
		public SetINTContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setINT; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterSetINT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitSetINT(this);
		}
	}

	public final SetINTContext setINT() throws RecognitionException {
		SetINTContext _localctx = new SetINTContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_setINT);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(239);
			intvalue();
			setState(244);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(240);
				match(T__6);
				setState(241);
				intvalue();
				}
				}
				setState(246);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntvaluetxtContext extends ParserRuleContext {
		public IntvalueContext intvalue() {
			return getRuleContext(IntvalueContext.class,0);
		}
		public IntvaluetxtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intvaluetxt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterIntvaluetxt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitIntvaluetxt(this);
		}
	}

	public final IntvaluetxtContext intvaluetxt() throws RecognitionException {
		IntvaluetxtContext _localctx = new IntvaluetxtContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_intvaluetxt);
		try {
			setState(250);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(247);
				intvalue();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(248);
				match(T__1);
				}
				break;
			case T__30:
				enterOuterAlt(_localctx, 3);
				{
				setState(249);
				match(T__30);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IntvalueContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(RDFPathParser.INT, 0); }
		public IntvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterIntvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitIntvalue(this);
		}
	}

	public final IntvalueContext intvalue() throws RecognitionException {
		IntvalueContext _localctx = new IntvalueContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_intvalue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(252);
			match(INT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TextvalueContext extends ParserRuleContext {
		public TerminalNode TEXTVALUE() { return getToken(RDFPathParser.TEXTVALUE, 0); }
		public IntvalueContext intvalue() {
			return getRuleContext(IntvalueContext.class,0);
		}
		public TextvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_textvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).enterTextvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RDFPathListener ) ((RDFPathListener)listener).exitTextvalue(this);
		}
	}

	public final TextvalueContext textvalue() throws RecognitionException {
		TextvalueContext _localctx = new TextvalueContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_textvalue);
		try {
			setState(256);
			switch (_input.LA(1)) {
			case TEXTVALUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(254);
				match(TEXTVALUE);
				}
				break;
			case INT:
				enterOuterAlt(_localctx, 2);
				{
				setState(255);
				intvalue();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3&\u0105\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\3\2\3\2\3\2\6\2J\n\2\r\2\16\2K\3\2\5\2O\n\2\3\2\5\2"+
		"R\n\2\3\3\3\3\3\3\5\3W\n\3\3\4\3\4\3\4\3\4\3\4\3\4\5\4_\n\4\3\5\3\5\3"+
		"\6\3\6\5\6e\n\6\3\7\3\7\5\7i\n\7\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3"+
		"\n\3\n\5\nv\n\n\3\13\3\13\3\13\5\13{\n\13\3\f\3\f\6\f\177\n\f\r\f\16\f"+
		"\u0080\3\f\3\f\6\f\u0085\n\f\r\f\16\f\u0086\7\f\u0089\n\f\f\f\16\f\u008c"+
		"\13\f\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\5\16\u0096\n\16\3\17\6\17\u0099"+
		"\n\17\r\17\16\17\u009a\3\17\3\17\3\20\3\20\3\20\5\20\u00a2\n\20\3\21\3"+
		"\21\3\21\3\21\6\21\u00a8\n\21\r\21\16\21\u00a9\3\22\6\22\u00ad\n\22\r"+
		"\22\16\22\u00ae\3\22\3\22\6\22\u00b3\n\22\r\22\16\22\u00b4\3\23\3\23\3"+
		"\24\3\24\3\24\5\24\u00bc\n\24\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27"+
		"\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\34\3\34"+
		"\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\35"+
		"\3\35\5\35\u00e3\n\35\3\36\3\36\3\37\3\37\3\37\3\37\7\37\u00eb\n\37\f"+
		"\37\16\37\u00ee\13\37\3\37\3\37\3 \3 \3 \7 \u00f5\n \f \16 \u00f8\13 "+
		"\3!\3!\3!\5!\u00fd\n!\3\"\3\"\3#\3#\5#\u0103\n#\3#\2\2$\2\4\6\b\n\f\16"+
		"\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BD\2\5\3\2\7\b\3\2\16"+
		"\23\4\2\r\r\25\25\u0108\2F\3\2\2\2\4V\3\2\2\2\6X\3\2\2\2\b`\3\2\2\2\n"+
		"d\3\2\2\2\fh\3\2\2\2\16j\3\2\2\2\20n\3\2\2\2\22p\3\2\2\2\24z\3\2\2\2\26"+
		"|\3\2\2\2\30\u008f\3\2\2\2\32\u0095\3\2\2\2\34\u0098\3\2\2\2\36\u00a1"+
		"\3\2\2\2 \u00a3\3\2\2\2\"\u00ac\3\2\2\2$\u00b6\3\2\2\2&\u00bb\3\2\2\2"+
		"(\u00bd\3\2\2\2*\u00c0\3\2\2\2,\u00c3\3\2\2\2.\u00c5\3\2\2\2\60\u00c7"+
		"\3\2\2\2\62\u00c9\3\2\2\2\64\u00ce\3\2\2\2\66\u00d1\3\2\2\28\u00e2\3\2"+
		"\2\2:\u00e4\3\2\2\2<\u00e6\3\2\2\2>\u00f1\3\2\2\2@\u00fc\3\2\2\2B\u00fe"+
		"\3\2\2\2D\u0102\3\2\2\2FG\5\4\3\2GI\7\3\2\2HJ\5\6\4\2IH\3\2\2\2JK\3\2"+
		"\2\2KI\3\2\2\2KL\3\2\2\2LN\3\2\2\2MO\7\3\2\2NM\3\2\2\2NO\3\2\2\2OQ\3\2"+
		"\2\2PR\58\35\2QP\3\2\2\2QR\3\2\2\2R\3\3\2\2\2SW\5:\36\2TW\7\4\2\2UW\5"+
		"<\37\2VS\3\2\2\2VT\3\2\2\2VU\3\2\2\2W\5\3\2\2\2XY\5\b\5\2Y^\5\n\6\2Z["+
		"\7\5\2\2[\\\5\f\7\2\\]\7\6\2\2]_\3\2\2\2^Z\3\2\2\2^_\3\2\2\2_\7\3\2\2"+
		"\2`a\t\2\2\2a\t\3\2\2\2be\5\22\n\2ce\5\26\f\2db\3\2\2\2dc\3\2\2\2e\13"+
		"\3\2\2\2fi\5\20\t\2gi\5\16\b\2hf\3\2\2\2hg\3\2\2\2i\r\3\2\2\2jk\5@!\2"+
		"kl\7\t\2\2lm\5@!\2m\17\3\2\2\2no\5B\"\2o\21\3\2\2\2pu\5\24\13\2qr\7\n"+
		"\2\2rs\5\32\16\2st\7\13\2\2tv\3\2\2\2uq\3\2\2\2uv\3\2\2\2v\23\3\2\2\2"+
		"w{\5:\36\2x{\7\f\2\2y{\5<\37\2zw\3\2\2\2zx\3\2\2\2zy\3\2\2\2{\25\3\2\2"+
		"\2|~\7\5\2\2}\177\5\6\4\2~}\3\2\2\2\177\u0080\3\2\2\2\u0080~\3\2\2\2\u0080"+
		"\u0081\3\2\2\2\u0081\u008a\3\2\2\2\u0082\u0084\5\30\r\2\u0083\u0085\5"+
		"\6\4\2\u0084\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0084\3\2\2\2\u0086"+
		"\u0087\3\2\2\2\u0087\u0089\3\2\2\2\u0088\u0082\3\2\2\2\u0089\u008c\3\2"+
		"\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008d\3\2\2\2\u008c"+
		"\u008a\3\2\2\2\u008d\u008e\7\6\2\2\u008e\27\3\2\2\2\u008f\u0090\7\r\2"+
		"\2\u0090\31\3\2\2\2\u0091\u0096\5&\24\2\u0092\u0096\5 \21\2\u0093\u0096"+
		"\5\34\17\2\u0094\u0096\5\"\22\2\u0095\u0091\3\2\2\2\u0095\u0092\3\2\2"+
		"\2\u0095\u0093\3\2\2\2\u0095\u0094\3\2\2\2\u0096\33\3\2\2\2\u0097\u0099"+
		"\5\6\4\2\u0098\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u0098\3\2\2\2\u009a"+
		"\u009b\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009d\5\36\20\2\u009d\35\3\2"+
		"\2\2\u009e\u00a2\5&\24\2\u009f\u00a2\3\2\2\2\u00a0\u00a2\5 \21\2\u00a1"+
		"\u009e\3\2\2\2\u00a1\u009f\3\2\2\2\u00a1\u00a0\3\2\2\2\u00a2\37\3\2\2"+
		"\2\u00a3\u00a7\5&\24\2\u00a4\u00a5\5\66\34\2\u00a5\u00a6\5&\24\2\u00a6"+
		"\u00a8\3\2\2\2\u00a7\u00a4\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00a7\3\2"+
		"\2\2\u00a9\u00aa\3\2\2\2\u00aa!\3\2\2\2\u00ab\u00ad\5\6\4\2\u00ac\u00ab"+
		"\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af"+
		"\u00b0\3\2\2\2\u00b0\u00b2\5$\23\2\u00b1\u00b3\5\6\4\2\u00b2\u00b1\3\2"+
		"\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5"+
		"#\3\2\2\2\u00b6\u00b7\5.\30\2\u00b7%\3\2\2\2\u00b8\u00bc\5(\25\2\u00b9"+
		"\u00bc\5*\26\2\u00ba\u00bc\5,\27\2\u00bb\u00b8\3\2\2\2\u00bb\u00b9\3\2"+
		"\2\2\u00bb\u00ba\3\2\2\2\u00bc\'\3\2\2\2\u00bd\u00be\5.\30\2\u00be\u00bf"+
		"\5\60\31\2\u00bf)\3\2\2\2\u00c0\u00c1\5.\30\2\u00c1\u00c2\5\62\32\2\u00c2"+
		"+\3\2\2\2\u00c3\u00c4\5\64\33\2\u00c4-\3\2\2\2\u00c5\u00c6\t\3\2\2\u00c6"+
		"/\3\2\2\2\u00c7\u00c8\5D#\2\u00c8\61\3\2\2\2\u00c9\u00ca\5D#\2\u00ca\u00cb"+
		"\7\5\2\2\u00cb\u00cc\5D#\2\u00cc\u00cd\7\6\2\2\u00cd\63\3\2\2\2\u00ce"+
		"\u00cf\5D#\2\u00cf\u00d0\7\24\2\2\u00d0\65\3\2\2\2\u00d1\u00d2\t\4\2\2"+
		"\u00d2\67\3\2\2\2\u00d3\u00d4\7\26\2\2\u00d4\u00d5\5> \2\u00d5\u00d6\7"+
		"\6\2\2\u00d6\u00e3\3\2\2\2\u00d7\u00e3\7\27\2\2\u00d8\u00e3\7\30\2\2\u00d9"+
		"\u00e3\7\31\2\2\u00da\u00e3\7\32\2\2\u00db\u00e3\7\33\2\2\u00dc\u00e3"+
		"\7\34\2\2\u00dd\u00e3\7\35\2\2\u00de\u00df\7\36\2\2\u00df\u00e0\5B\"\2"+
		"\u00e0\u00e1\7\6\2\2\u00e1\u00e3\3\2\2\2\u00e2\u00d3\3\2\2\2\u00e2\u00d7"+
		"\3\2\2\2\u00e2\u00d8\3\2\2\2\u00e2\u00d9\3\2\2\2\u00e2\u00da\3\2\2\2\u00e2"+
		"\u00db\3\2\2\2\u00e2\u00dc\3\2\2\2\u00e2\u00dd\3\2\2\2\u00e2\u00de\3\2"+
		"\2\2\u00e39\3\2\2\2\u00e4\u00e5\5D#\2\u00e5;\3\2\2\2\u00e6\u00e7\7\37"+
		"\2\2\u00e7\u00ec\5:\36\2\u00e8\u00e9\7\t\2\2\u00e9\u00eb\5:\36\2\u00ea"+
		"\u00e8\3\2\2\2\u00eb\u00ee\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec\u00ed\3\2"+
		"\2\2\u00ed\u00ef\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ef\u00f0\7 \2\2\u00f0"+
		"=\3\2\2\2\u00f1\u00f6\5B\"\2\u00f2\u00f3\7\t\2\2\u00f3\u00f5\5B\"\2\u00f4"+
		"\u00f2\3\2\2\2\u00f5\u00f8\3\2\2\2\u00f6\u00f4\3\2\2\2\u00f6\u00f7\3\2"+
		"\2\2\u00f7?\3\2\2\2\u00f8\u00f6\3\2\2\2\u00f9\u00fd\5B\"\2\u00fa\u00fd"+
		"\7\4\2\2\u00fb\u00fd\7!\2\2\u00fc\u00f9\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fc"+
		"\u00fb\3\2\2\2\u00fdA\3\2\2\2\u00fe\u00ff\7\"\2\2\u00ffC\3\2\2\2\u0100"+
		"\u0103\7#\2\2\u0101\u0103\5B\"\2\u0102\u0100\3\2\2\2\u0102\u0101\3\2\2"+
		"\2\u0103E\3\2\2\2\32KNQV^dhuz\u0080\u0086\u008a\u0095\u009a\u00a1\u00a9"+
		"\u00ae\u00b4\u00bb\u00e2\u00ec\u00f6\u00fc\u0102";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}