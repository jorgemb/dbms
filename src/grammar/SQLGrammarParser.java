// Generated from grammar/SQLGrammar.g4 by ANTLR 4.7.2
package grammar;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SQLGrammarParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, CREATE=2, DATABASE=3, DATABASES=4, ALTER=5, DROP=6, SHOW=7, USE=8, 
		CONSTRAINT=9, TABLE=10, TABLES=11, PRIMARY=12, FOREIGN=13, KEY=14, REFERENCES=15, 
		CHECK=16, AND=17, OR=18, NOT=19, RENAME=20, TO=21, ADD=22, COLUMN=23, 
		COLUMNS=24, FROM=25, INSERT=26, INTO=27, VALUES=28, UPDATE=29, SET=30, 
		WHERE=31, DELETE=32, SELECT=33, ORDER=34, BY=35, ASC=36, DESC=37, NULL=38, 
		ECHO=39, ENABLED=40, DISABLED=41, END_LINE=42, DOT_OP=43, SUBTRACT_OP=44, 
		SUM_OP=45, TIMES_OP=46, DIV_OP=47, MOD_OP=48, RIGHT_PAR=49, LEFT_PAR=50, 
		REL_OP=51, EQUALITY_OP=52, INEQUALITY_OP=53, INT=54, FLOAT=55, DATE=56, 
		CHAR=57, ID=58, NUM=59, STRING=60, REAL=61, WS=62, COMMENT=63;
	public static final int
		RULE_program = 0, RULE_statement = 1, RULE_echo = 2, RULE_createDatabase = 3, 
		RULE_alterDatabase = 4, RULE_dropDatabase = 5, RULE_showDatabases = 6, 
		RULE_useDatabase = 7, RULE_columnType = 8, RULE_idList = 9, RULE_constraintType = 10, 
		RULE_constraintList = 11, RULE_createTable = 12, RULE_action = 13, RULE_alterTable = 14, 
		RULE_dropTable = 15, RULE_showTables = 16, RULE_showColumns = 17, RULE_valueList = 18, 
		RULE_idValue = 19, RULE_idValueList = 20, RULE_insertInto = 21, RULE_update = 22, 
		RULE_deleteFrom = 23, RULE_orderType = 24, RULE_orderExp = 25, RULE_orderList = 26, 
		RULE_select = 27, RULE_location = 28, RULE_expression = 29, RULE_eq_op = 30, 
		RULE_literal = 31, RULE_int_literal = 32, RULE_string_literal = 33, RULE_real_literal = 34, 
		RULE_null_literal = 35;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "statement", "echo", "createDatabase", "alterDatabase", "dropDatabase", 
			"showDatabases", "useDatabase", "columnType", "idList", "constraintType", 
			"constraintList", "createTable", "action", "alterTable", "dropTable", 
			"showTables", "showColumns", "valueList", "idValue", "idValueList", "insertInto", 
			"update", "deleteFrom", "orderType", "orderExp", "orderList", "select", 
			"location", "expression", "eq_op", "literal", "int_literal", "string_literal", 
			"real_literal", "null_literal"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "','", null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, "';'", "'.'", "'-'", "'+'", "'*'", 
			"'/'", "'%'", "')'", "'('", null, "'='", "'<>'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "CREATE", "DATABASE", "DATABASES", "ALTER", "DROP", "SHOW", 
			"USE", "CONSTRAINT", "TABLE", "TABLES", "PRIMARY", "FOREIGN", "KEY", 
			"REFERENCES", "CHECK", "AND", "OR", "NOT", "RENAME", "TO", "ADD", "COLUMN", 
			"COLUMNS", "FROM", "INSERT", "INTO", "VALUES", "UPDATE", "SET", "WHERE", 
			"DELETE", "SELECT", "ORDER", "BY", "ASC", "DESC", "NULL", "ECHO", "ENABLED", 
			"DISABLED", "END_LINE", "DOT_OP", "SUBTRACT_OP", "SUM_OP", "TIMES_OP", 
			"DIV_OP", "MOD_OP", "RIGHT_PAR", "LEFT_PAR", "REL_OP", "EQUALITY_OP", 
			"INEQUALITY_OP", "INT", "FLOAT", "DATE", "CHAR", "ID", "NUM", "STRING", 
			"REAL", "WS", "COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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
	public String getGrammarFileName() { return "SQLGrammar.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SQLGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public List<TerminalNode> END_LINE() { return getTokens(SQLGrammarParser.END_LINE); }
		public TerminalNode END_LINE(int i) {
			return getToken(SQLGrammarParser.END_LINE, i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			statement();
			setState(77);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(73);
					match(END_LINE);
					setState(74);
					statement();
					}
					} 
				}
				setState(79);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==END_LINE) {
				{
				setState(80);
				match(END_LINE);
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

	public static class StatementContext extends ParserRuleContext {
		public CreateDatabaseContext createDatabase() {
			return getRuleContext(CreateDatabaseContext.class,0);
		}
		public AlterDatabaseContext alterDatabase() {
			return getRuleContext(AlterDatabaseContext.class,0);
		}
		public DropDatabaseContext dropDatabase() {
			return getRuleContext(DropDatabaseContext.class,0);
		}
		public ShowDatabasesContext showDatabases() {
			return getRuleContext(ShowDatabasesContext.class,0);
		}
		public UseDatabaseContext useDatabase() {
			return getRuleContext(UseDatabaseContext.class,0);
		}
		public CreateTableContext createTable() {
			return getRuleContext(CreateTableContext.class,0);
		}
		public AlterTableContext alterTable() {
			return getRuleContext(AlterTableContext.class,0);
		}
		public DropTableContext dropTable() {
			return getRuleContext(DropTableContext.class,0);
		}
		public ShowTablesContext showTables() {
			return getRuleContext(ShowTablesContext.class,0);
		}
		public ShowColumnsContext showColumns() {
			return getRuleContext(ShowColumnsContext.class,0);
		}
		public InsertIntoContext insertInto() {
			return getRuleContext(InsertIntoContext.class,0);
		}
		public UpdateContext update() {
			return getRuleContext(UpdateContext.class,0);
		}
		public DeleteFromContext deleteFrom() {
			return getRuleContext(DeleteFromContext.class,0);
		}
		public SelectContext select() {
			return getRuleContext(SelectContext.class,0);
		}
		public EchoContext echo() {
			return getRuleContext(EchoContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_statement);
		try {
			setState(98);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(83);
				createDatabase();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(84);
				alterDatabase();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(85);
				dropDatabase();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(86);
				showDatabases();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(87);
				useDatabase();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(88);
				createTable();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(89);
				alterTable();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(90);
				dropTable();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(91);
				showTables();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(92);
				showColumns();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(93);
				insertInto();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(94);
				update();
				}
				break;
			case 13:
				enterOuterAlt(_localctx, 13);
				{
				setState(95);
				deleteFrom();
				}
				break;
			case 14:
				enterOuterAlt(_localctx, 14);
				{
				setState(96);
				select();
				}
				break;
			case 15:
				enterOuterAlt(_localctx, 15);
				{
				setState(97);
				echo();
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

	public static class EchoContext extends ParserRuleContext {
		public TerminalNode ECHO() { return getToken(SQLGrammarParser.ECHO, 0); }
		public TerminalNode ENABLED() { return getToken(SQLGrammarParser.ENABLED, 0); }
		public TerminalNode DISABLED() { return getToken(SQLGrammarParser.DISABLED, 0); }
		public EchoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_echo; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitEcho(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EchoContext echo() throws RecognitionException {
		EchoContext _localctx = new EchoContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_echo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(ECHO);
			setState(101);
			_la = _input.LA(1);
			if ( !(_la==ENABLED || _la==DISABLED) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
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

	public static class CreateDatabaseContext extends ParserRuleContext {
		public TerminalNode CREATE() { return getToken(SQLGrammarParser.CREATE, 0); }
		public TerminalNode DATABASE() { return getToken(SQLGrammarParser.DATABASE, 0); }
		public TerminalNode ID() { return getToken(SQLGrammarParser.ID, 0); }
		public CreateDatabaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createDatabase; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitCreateDatabase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateDatabaseContext createDatabase() throws RecognitionException {
		CreateDatabaseContext _localctx = new CreateDatabaseContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_createDatabase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(CREATE);
			setState(104);
			match(DATABASE);
			setState(105);
			match(ID);
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

	public static class AlterDatabaseContext extends ParserRuleContext {
		public TerminalNode ALTER() { return getToken(SQLGrammarParser.ALTER, 0); }
		public TerminalNode DATABASE() { return getToken(SQLGrammarParser.DATABASE, 0); }
		public List<TerminalNode> ID() { return getTokens(SQLGrammarParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SQLGrammarParser.ID, i);
		}
		public TerminalNode RENAME() { return getToken(SQLGrammarParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(SQLGrammarParser.TO, 0); }
		public AlterDatabaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alterDatabase; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitAlterDatabase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AlterDatabaseContext alterDatabase() throws RecognitionException {
		AlterDatabaseContext _localctx = new AlterDatabaseContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_alterDatabase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			match(ALTER);
			setState(108);
			match(DATABASE);
			setState(109);
			match(ID);
			setState(110);
			match(RENAME);
			setState(111);
			match(TO);
			setState(112);
			match(ID);
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

	public static class DropDatabaseContext extends ParserRuleContext {
		public TerminalNode DROP() { return getToken(SQLGrammarParser.DROP, 0); }
		public TerminalNode DATABASE() { return getToken(SQLGrammarParser.DATABASE, 0); }
		public TerminalNode ID() { return getToken(SQLGrammarParser.ID, 0); }
		public DropDatabaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dropDatabase; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitDropDatabase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DropDatabaseContext dropDatabase() throws RecognitionException {
		DropDatabaseContext _localctx = new DropDatabaseContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_dropDatabase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			match(DROP);
			setState(115);
			match(DATABASE);
			setState(116);
			match(ID);
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

	public static class ShowDatabasesContext extends ParserRuleContext {
		public TerminalNode SHOW() { return getToken(SQLGrammarParser.SHOW, 0); }
		public TerminalNode DATABASES() { return getToken(SQLGrammarParser.DATABASES, 0); }
		public ShowDatabasesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_showDatabases; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitShowDatabases(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShowDatabasesContext showDatabases() throws RecognitionException {
		ShowDatabasesContext _localctx = new ShowDatabasesContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_showDatabases);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			match(SHOW);
			setState(119);
			match(DATABASES);
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

	public static class UseDatabaseContext extends ParserRuleContext {
		public TerminalNode USE() { return getToken(SQLGrammarParser.USE, 0); }
		public TerminalNode DATABASE() { return getToken(SQLGrammarParser.DATABASE, 0); }
		public TerminalNode ID() { return getToken(SQLGrammarParser.ID, 0); }
		public UseDatabaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_useDatabase; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitUseDatabase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UseDatabaseContext useDatabase() throws RecognitionException {
		UseDatabaseContext _localctx = new UseDatabaseContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_useDatabase);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(USE);
			setState(122);
			match(DATABASE);
			setState(123);
			match(ID);
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

	public static class ColumnTypeContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(SQLGrammarParser.INT, 0); }
		public TerminalNode FLOAT() { return getToken(SQLGrammarParser.FLOAT, 0); }
		public TerminalNode DATE() { return getToken(SQLGrammarParser.DATE, 0); }
		public TerminalNode CHAR() { return getToken(SQLGrammarParser.CHAR, 0); }
		public TerminalNode LEFT_PAR() { return getToken(SQLGrammarParser.LEFT_PAR, 0); }
		public Int_literalContext int_literal() {
			return getRuleContext(Int_literalContext.class,0);
		}
		public TerminalNode RIGHT_PAR() { return getToken(SQLGrammarParser.RIGHT_PAR, 0); }
		public ColumnTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_columnType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitColumnType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColumnTypeContext columnType() throws RecognitionException {
		ColumnTypeContext _localctx = new ColumnTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_columnType);
		try {
			setState(133);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
				enterOuterAlt(_localctx, 1);
				{
				setState(125);
				match(INT);
				}
				break;
			case FLOAT:
				enterOuterAlt(_localctx, 2);
				{
				setState(126);
				match(FLOAT);
				}
				break;
			case DATE:
				enterOuterAlt(_localctx, 3);
				{
				setState(127);
				match(DATE);
				}
				break;
			case CHAR:
				enterOuterAlt(_localctx, 4);
				{
				setState(128);
				match(CHAR);
				setState(129);
				match(LEFT_PAR);
				setState(130);
				int_literal();
				setState(131);
				match(RIGHT_PAR);
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

	public static class IdListContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(SQLGrammarParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SQLGrammarParser.ID, i);
		}
		public IdListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitIdList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdListContext idList() throws RecognitionException {
		IdListContext _localctx = new IdListContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_idList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(135);
			match(ID);
			}
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(136);
				match(T__0);
				setState(137);
				match(ID);
				}
				}
				setState(142);
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

	public static class ConstraintTypeContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(SQLGrammarParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SQLGrammarParser.ID, i);
		}
		public TerminalNode PRIMARY() { return getToken(SQLGrammarParser.PRIMARY, 0); }
		public TerminalNode KEY() { return getToken(SQLGrammarParser.KEY, 0); }
		public List<TerminalNode> LEFT_PAR() { return getTokens(SQLGrammarParser.LEFT_PAR); }
		public TerminalNode LEFT_PAR(int i) {
			return getToken(SQLGrammarParser.LEFT_PAR, i);
		}
		public List<IdListContext> idList() {
			return getRuleContexts(IdListContext.class);
		}
		public IdListContext idList(int i) {
			return getRuleContext(IdListContext.class,i);
		}
		public List<TerminalNode> RIGHT_PAR() { return getTokens(SQLGrammarParser.RIGHT_PAR); }
		public TerminalNode RIGHT_PAR(int i) {
			return getToken(SQLGrammarParser.RIGHT_PAR, i);
		}
		public TerminalNode FOREIGN() { return getToken(SQLGrammarParser.FOREIGN, 0); }
		public TerminalNode REFERENCES() { return getToken(SQLGrammarParser.REFERENCES, 0); }
		public TerminalNode CHECK() { return getToken(SQLGrammarParser.CHECK, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ConstraintTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraintType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitConstraintType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstraintTypeContext constraintType() throws RecognitionException {
		ConstraintTypeContext _localctx = new ConstraintTypeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_constraintType);
		try {
			setState(168);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(143);
				match(ID);
				setState(144);
				match(PRIMARY);
				setState(145);
				match(KEY);
				setState(146);
				match(LEFT_PAR);
				setState(147);
				idList();
				setState(148);
				match(RIGHT_PAR);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(150);
				match(ID);
				setState(151);
				match(FOREIGN);
				setState(152);
				match(KEY);
				setState(153);
				match(LEFT_PAR);
				setState(154);
				idList();
				setState(155);
				match(RIGHT_PAR);
				setState(156);
				match(REFERENCES);
				setState(157);
				match(ID);
				setState(158);
				match(LEFT_PAR);
				setState(159);
				idList();
				setState(160);
				match(RIGHT_PAR);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(162);
				match(ID);
				setState(163);
				match(CHECK);
				{
				setState(164);
				match(LEFT_PAR);
				setState(165);
				expression(0);
				setState(166);
				match(RIGHT_PAR);
				}
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

	public static class ConstraintListContext extends ParserRuleContext {
		public List<TerminalNode> CONSTRAINT() { return getTokens(SQLGrammarParser.CONSTRAINT); }
		public TerminalNode CONSTRAINT(int i) {
			return getToken(SQLGrammarParser.CONSTRAINT, i);
		}
		public List<ConstraintTypeContext> constraintType() {
			return getRuleContexts(ConstraintTypeContext.class);
		}
		public ConstraintTypeContext constraintType(int i) {
			return getRuleContext(ConstraintTypeContext.class,i);
		}
		public ConstraintListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constraintList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitConstraintList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstraintListContext constraintList() throws RecognitionException {
		ConstraintListContext _localctx = new ConstraintListContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_constraintList);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(170);
			match(CONSTRAINT);
			setState(171);
			constraintType();
			}
			setState(178);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(173);
					match(T__0);
					setState(174);
					match(CONSTRAINT);
					setState(175);
					constraintType();
					}
					} 
				}
				setState(180);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
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

	public static class CreateTableContext extends ParserRuleContext {
		public TerminalNode CREATE() { return getToken(SQLGrammarParser.CREATE, 0); }
		public TerminalNode TABLE() { return getToken(SQLGrammarParser.TABLE, 0); }
		public List<TerminalNode> ID() { return getTokens(SQLGrammarParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SQLGrammarParser.ID, i);
		}
		public TerminalNode LEFT_PAR() { return getToken(SQLGrammarParser.LEFT_PAR, 0); }
		public TerminalNode RIGHT_PAR() { return getToken(SQLGrammarParser.RIGHT_PAR, 0); }
		public List<ColumnTypeContext> columnType() {
			return getRuleContexts(ColumnTypeContext.class);
		}
		public ColumnTypeContext columnType(int i) {
			return getRuleContext(ColumnTypeContext.class,i);
		}
		public List<ConstraintListContext> constraintList() {
			return getRuleContexts(ConstraintListContext.class);
		}
		public ConstraintListContext constraintList(int i) {
			return getRuleContext(ConstraintListContext.class,i);
		}
		public CreateTableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_createTable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitCreateTable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CreateTableContext createTable() throws RecognitionException {
		CreateTableContext _localctx = new CreateTableContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_createTable);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			match(CREATE);
			setState(182);
			match(TABLE);
			setState(183);
			match(ID);
			setState(203);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_PAR) {
				{
				setState(184);
				match(LEFT_PAR);
				{
				{
				setState(185);
				match(ID);
				setState(186);
				columnType();
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CONSTRAINT) {
					{
					setState(187);
					constraintList();
					}
				}

				}
				setState(198);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__0) {
					{
					{
					setState(190);
					match(T__0);
					setState(191);
					match(ID);
					setState(192);
					columnType();
					setState(194);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==CONSTRAINT) {
						{
						setState(193);
						constraintList();
						}
					}

					}
					}
					setState(200);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				setState(201);
				match(RIGHT_PAR);
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

	public static class ActionContext extends ParserRuleContext {
		public TerminalNode ADD() { return getToken(SQLGrammarParser.ADD, 0); }
		public TerminalNode COLUMN() { return getToken(SQLGrammarParser.COLUMN, 0); }
		public TerminalNode ID() { return getToken(SQLGrammarParser.ID, 0); }
		public ColumnTypeContext columnType() {
			return getRuleContext(ColumnTypeContext.class,0);
		}
		public ConstraintListContext constraintList() {
			return getRuleContext(ConstraintListContext.class,0);
		}
		public TerminalNode CONSTRAINT() { return getToken(SQLGrammarParser.CONSTRAINT, 0); }
		public ConstraintTypeContext constraintType() {
			return getRuleContext(ConstraintTypeContext.class,0);
		}
		public TerminalNode DROP() { return getToken(SQLGrammarParser.DROP, 0); }
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitAction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_action);
		int _la;
		try {
			setState(221);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(205);
				match(ADD);
				setState(206);
				match(COLUMN);
				setState(207);
				match(ID);
				setState(208);
				columnType();
				setState(210);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CONSTRAINT) {
					{
					setState(209);
					constraintList();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(212);
				match(ADD);
				setState(213);
				match(CONSTRAINT);
				setState(214);
				constraintType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(215);
				match(DROP);
				setState(216);
				match(COLUMN);
				setState(217);
				match(ID);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(218);
				match(DROP);
				setState(219);
				match(CONSTRAINT);
				setState(220);
				match(ID);
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

	public static class AlterTableContext extends ParserRuleContext {
		public TerminalNode ALTER() { return getToken(SQLGrammarParser.ALTER, 0); }
		public TerminalNode TABLE() { return getToken(SQLGrammarParser.TABLE, 0); }
		public List<TerminalNode> ID() { return getTokens(SQLGrammarParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SQLGrammarParser.ID, i);
		}
		public TerminalNode RENAME() { return getToken(SQLGrammarParser.RENAME, 0); }
		public TerminalNode TO() { return getToken(SQLGrammarParser.TO, 0); }
		public List<ActionContext> action() {
			return getRuleContexts(ActionContext.class);
		}
		public ActionContext action(int i) {
			return getRuleContext(ActionContext.class,i);
		}
		public AlterTableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_alterTable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitAlterTable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AlterTableContext alterTable() throws RecognitionException {
		AlterTableContext _localctx = new AlterTableContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_alterTable);
		int _la;
		try {
			setState(240);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(223);
				match(ALTER);
				setState(224);
				match(TABLE);
				setState(225);
				match(ID);
				setState(226);
				match(RENAME);
				setState(227);
				match(TO);
				setState(228);
				match(ID);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(229);
				match(ALTER);
				setState(230);
				match(TABLE);
				setState(231);
				match(ID);
				{
				setState(232);
				action();
				}
				setState(237);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__0) {
					{
					{
					setState(233);
					match(T__0);
					setState(234);
					action();
					}
					}
					setState(239);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class DropTableContext extends ParserRuleContext {
		public TerminalNode DROP() { return getToken(SQLGrammarParser.DROP, 0); }
		public TerminalNode TABLE() { return getToken(SQLGrammarParser.TABLE, 0); }
		public TerminalNode ID() { return getToken(SQLGrammarParser.ID, 0); }
		public DropTableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dropTable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitDropTable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DropTableContext dropTable() throws RecognitionException {
		DropTableContext _localctx = new DropTableContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_dropTable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			match(DROP);
			setState(243);
			match(TABLE);
			setState(244);
			match(ID);
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

	public static class ShowTablesContext extends ParserRuleContext {
		public TerminalNode SHOW() { return getToken(SQLGrammarParser.SHOW, 0); }
		public TerminalNode TABLES() { return getToken(SQLGrammarParser.TABLES, 0); }
		public ShowTablesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_showTables; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitShowTables(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShowTablesContext showTables() throws RecognitionException {
		ShowTablesContext _localctx = new ShowTablesContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_showTables);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(SHOW);
			setState(247);
			match(TABLES);
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

	public static class ShowColumnsContext extends ParserRuleContext {
		public TerminalNode SHOW() { return getToken(SQLGrammarParser.SHOW, 0); }
		public TerminalNode COLUMNS() { return getToken(SQLGrammarParser.COLUMNS, 0); }
		public TerminalNode FROM() { return getToken(SQLGrammarParser.FROM, 0); }
		public TerminalNode ID() { return getToken(SQLGrammarParser.ID, 0); }
		public ShowColumnsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_showColumns; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitShowColumns(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShowColumnsContext showColumns() throws RecognitionException {
		ShowColumnsContext _localctx = new ShowColumnsContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_showColumns);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(249);
			match(SHOW);
			setState(250);
			match(COLUMNS);
			setState(251);
			match(FROM);
			setState(252);
			match(ID);
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

	public static class ValueListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ValueListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valueList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitValueList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueListContext valueList() throws RecognitionException {
		ValueListContext _localctx = new ValueListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_valueList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(254);
			expression(0);
			}
			setState(259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(255);
				match(T__0);
				setState(256);
				expression(0);
				}
				}
				setState(261);
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

	public static class IdValueContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SQLGrammarParser.ID, 0); }
		public TerminalNode EQUALITY_OP() { return getToken(SQLGrammarParser.EQUALITY_OP, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public IdValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idValue; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitIdValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdValueContext idValue() throws RecognitionException {
		IdValueContext _localctx = new IdValueContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_idValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			match(ID);
			setState(263);
			match(EQUALITY_OP);
			setState(264);
			expression(0);
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

	public static class IdValueListContext extends ParserRuleContext {
		public List<IdValueContext> idValue() {
			return getRuleContexts(IdValueContext.class);
		}
		public IdValueContext idValue(int i) {
			return getRuleContext(IdValueContext.class,i);
		}
		public IdValueListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_idValueList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitIdValueList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdValueListContext idValueList() throws RecognitionException {
		IdValueListContext _localctx = new IdValueListContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_idValueList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(266);
			idValue();
			}
			setState(271);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(267);
				match(T__0);
				setState(268);
				idValue();
				}
				}
				setState(273);
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

	public static class InsertIntoContext extends ParserRuleContext {
		public TerminalNode INSERT() { return getToken(SQLGrammarParser.INSERT, 0); }
		public TerminalNode INTO() { return getToken(SQLGrammarParser.INTO, 0); }
		public TerminalNode ID() { return getToken(SQLGrammarParser.ID, 0); }
		public TerminalNode VALUES() { return getToken(SQLGrammarParser.VALUES, 0); }
		public List<TerminalNode> LEFT_PAR() { return getTokens(SQLGrammarParser.LEFT_PAR); }
		public TerminalNode LEFT_PAR(int i) {
			return getToken(SQLGrammarParser.LEFT_PAR, i);
		}
		public ValueListContext valueList() {
			return getRuleContext(ValueListContext.class,0);
		}
		public List<TerminalNode> RIGHT_PAR() { return getTokens(SQLGrammarParser.RIGHT_PAR); }
		public TerminalNode RIGHT_PAR(int i) {
			return getToken(SQLGrammarParser.RIGHT_PAR, i);
		}
		public IdListContext idList() {
			return getRuleContext(IdListContext.class,0);
		}
		public InsertIntoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_insertInto; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitInsertInto(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InsertIntoContext insertInto() throws RecognitionException {
		InsertIntoContext _localctx = new InsertIntoContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_insertInto);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(274);
			match(INSERT);
			setState(275);
			match(INTO);
			setState(276);
			match(ID);
			setState(281);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LEFT_PAR) {
				{
				setState(277);
				match(LEFT_PAR);
				setState(278);
				idList();
				setState(279);
				match(RIGHT_PAR);
				}
			}

			setState(283);
			match(VALUES);
			setState(284);
			match(LEFT_PAR);
			setState(285);
			valueList();
			setState(286);
			match(RIGHT_PAR);
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

	public static class UpdateContext extends ParserRuleContext {
		public TerminalNode UPDATE() { return getToken(SQLGrammarParser.UPDATE, 0); }
		public TerminalNode ID() { return getToken(SQLGrammarParser.ID, 0); }
		public TerminalNode SET() { return getToken(SQLGrammarParser.SET, 0); }
		public IdValueListContext idValueList() {
			return getRuleContext(IdValueListContext.class,0);
		}
		public TerminalNode WHERE() { return getToken(SQLGrammarParser.WHERE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateContext update() throws RecognitionException {
		UpdateContext _localctx = new UpdateContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_update);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(288);
			match(UPDATE);
			setState(289);
			match(ID);
			setState(290);
			match(SET);
			setState(291);
			idValueList();
			setState(294);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(292);
				match(WHERE);
				setState(293);
				expression(0);
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

	public static class DeleteFromContext extends ParserRuleContext {
		public TerminalNode DELETE() { return getToken(SQLGrammarParser.DELETE, 0); }
		public TerminalNode FROM() { return getToken(SQLGrammarParser.FROM, 0); }
		public TerminalNode ID() { return getToken(SQLGrammarParser.ID, 0); }
		public TerminalNode WHERE() { return getToken(SQLGrammarParser.WHERE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public DeleteFromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_deleteFrom; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitDeleteFrom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeleteFromContext deleteFrom() throws RecognitionException {
		DeleteFromContext _localctx = new DeleteFromContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_deleteFrom);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			match(DELETE);
			setState(297);
			match(FROM);
			setState(298);
			match(ID);
			setState(301);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(299);
				match(WHERE);
				setState(300);
				expression(0);
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

	public static class OrderTypeContext extends ParserRuleContext {
		public TerminalNode ASC() { return getToken(SQLGrammarParser.ASC, 0); }
		public TerminalNode DESC() { return getToken(SQLGrammarParser.DESC, 0); }
		public OrderTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderType; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitOrderType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderTypeContext orderType() throws RecognitionException {
		OrderTypeContext _localctx = new OrderTypeContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_orderType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			_la = _input.LA(1);
			if ( !(_la==ASC || _la==DESC) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
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

	public static class OrderExpContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public OrderTypeContext orderType() {
			return getRuleContext(OrderTypeContext.class,0);
		}
		public OrderExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderExp; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitOrderExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderExpContext orderExp() throws RecognitionException {
		OrderExpContext _localctx = new OrderExpContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_orderExp);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			expression(0);
			setState(307);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASC || _la==DESC) {
				{
				setState(306);
				orderType();
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

	public static class OrderListContext extends ParserRuleContext {
		public List<OrderExpContext> orderExp() {
			return getRuleContexts(OrderExpContext.class);
		}
		public OrderExpContext orderExp(int i) {
			return getRuleContext(OrderExpContext.class,i);
		}
		public OrderListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_orderList; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitOrderList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrderListContext orderList() throws RecognitionException {
		OrderListContext _localctx = new OrderListContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_orderList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(309);
			orderExp();
			}
			setState(314);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(310);
				match(T__0);
				setState(311);
				orderExp();
				}
				}
				setState(316);
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

	public static class SelectContext extends ParserRuleContext {
		public TerminalNode SELECT() { return getToken(SQLGrammarParser.SELECT, 0); }
		public TerminalNode FROM() { return getToken(SQLGrammarParser.FROM, 0); }
		public List<IdListContext> idList() {
			return getRuleContexts(IdListContext.class);
		}
		public IdListContext idList(int i) {
			return getRuleContext(IdListContext.class,i);
		}
		public TerminalNode TIMES_OP() { return getToken(SQLGrammarParser.TIMES_OP, 0); }
		public TerminalNode WHERE() { return getToken(SQLGrammarParser.WHERE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode ORDER() { return getToken(SQLGrammarParser.ORDER, 0); }
		public TerminalNode BY() { return getToken(SQLGrammarParser.BY, 0); }
		public OrderListContext orderList() {
			return getRuleContext(OrderListContext.class,0);
		}
		public SelectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitSelect(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SelectContext select() throws RecognitionException {
		SelectContext _localctx = new SelectContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_select);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(317);
			match(SELECT);
			setState(320);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case TIMES_OP:
				{
				setState(318);
				match(TIMES_OP);
				}
				break;
			case ID:
				{
				setState(319);
				idList();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(322);
			match(FROM);
			setState(323);
			idList();
			setState(326);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==WHERE) {
				{
				setState(324);
				match(WHERE);
				setState(325);
				expression(0);
				}
			}

			setState(331);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ORDER) {
				{
				setState(328);
				match(ORDER);
				setState(329);
				match(BY);
				setState(330);
				orderList();
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

	public static class LocationContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(SQLGrammarParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SQLGrammarParser.ID, i);
		}
		public TerminalNode DOT_OP() { return getToken(SQLGrammarParser.DOT_OP, 0); }
		public LocationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_location; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitLocation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocationContext location() throws RecognitionException {
		LocationContext _localctx = new LocationContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_location);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(333);
			match(ID);
			}
			setState(336);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,25,_ctx) ) {
			case 1:
				{
				setState(334);
				match(DOT_OP);
				setState(335);
				match(ID);
				}
				break;
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

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class EqExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public Eq_opContext eq_op() {
			return getRuleContext(Eq_opContext.class,0);
		}
		public EqExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitEqExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotExprContext extends ExpressionContext {
		public TerminalNode NOT() { return getToken(SQLGrammarParser.NOT, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NotExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitNotExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LocationExprContext extends ExpressionContext {
		public LocationContext location() {
			return getRuleContext(LocationContext.class,0);
		}
		public LocationExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitLocationExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MultdivExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode TIMES_OP() { return getToken(SQLGrammarParser.TIMES_OP, 0); }
		public TerminalNode DIV_OP() { return getToken(SQLGrammarParser.DIV_OP, 0); }
		public TerminalNode MOD_OP() { return getToken(SQLGrammarParser.MOD_OP, 0); }
		public MultdivExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitMultdivExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NegExprContext extends ExpressionContext {
		public TerminalNode SUBTRACT_OP() { return getToken(SQLGrammarParser.SUBTRACT_OP, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public NegExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitNegExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LiteralExprContext extends ExpressionContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public LiteralExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitLiteralExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode OR() { return getToken(SQLGrammarParser.OR, 0); }
		public OrExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddsubExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode SUM_OP() { return getToken(SQLGrammarParser.SUM_OP, 0); }
		public TerminalNode SUBTRACT_OP() { return getToken(SQLGrammarParser.SUBTRACT_OP, 0); }
		public AddsubExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitAddsubExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class RelExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode REL_OP() { return getToken(SQLGrammarParser.REL_OP, 0); }
		public RelExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitRelExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenthesisExprContext extends ExpressionContext {
		public TerminalNode LEFT_PAR() { return getToken(SQLGrammarParser.LEFT_PAR, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RIGHT_PAR() { return getToken(SQLGrammarParser.RIGHT_PAR, 0); }
		public ParenthesisExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitParenthesisExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndExprContext extends ExpressionContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode AND() { return getToken(SQLGrammarParser.AND, 0); }
		public AndExprContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 58;
		enterRecursionRule(_localctx, 58, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(349);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case ID:
				{
				_localctx = new LocationExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(339);
				location();
				}
				break;
			case NULL:
			case NUM:
			case STRING:
			case REAL:
				{
				_localctx = new LiteralExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(340);
				literal();
				}
				break;
			case LEFT_PAR:
				{
				_localctx = new ParenthesisExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(341);
				match(LEFT_PAR);
				setState(342);
				expression(0);
				setState(343);
				match(RIGHT_PAR);
				}
				break;
			case SUBTRACT_OP:
				{
				_localctx = new NegExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(345);
				match(SUBTRACT_OP);
				setState(346);
				expression(8);
				}
				break;
			case NOT:
				{
				_localctx = new NotExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(347);
				match(NOT);
				setState(348);
				expression(5);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(372);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(370);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
					case 1:
						{
						_localctx = new MultdivExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(351);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(352);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TIMES_OP) | (1L << DIV_OP) | (1L << MOD_OP))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(353);
						expression(8);
						}
						break;
					case 2:
						{
						_localctx = new AddsubExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(354);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(355);
						_la = _input.LA(1);
						if ( !(_la==SUBTRACT_OP || _la==SUM_OP) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(356);
						expression(7);
						}
						break;
					case 3:
						{
						_localctx = new RelExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(357);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(358);
						match(REL_OP);
						setState(359);
						expression(5);
						}
						break;
					case 4:
						{
						_localctx = new EqExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(360);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(361);
						eq_op();
						setState(362);
						expression(4);
						}
						break;
					case 5:
						{
						_localctx = new AndExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(364);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(365);
						match(AND);
						setState(366);
						expression(3);
						}
						break;
					case 6:
						{
						_localctx = new OrExprContext(new ExpressionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(367);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(368);
						match(OR);
						setState(369);
						expression(2);
						}
						break;
					}
					} 
				}
				setState(374);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class Eq_opContext extends ParserRuleContext {
		public TerminalNode EQUALITY_OP() { return getToken(SQLGrammarParser.EQUALITY_OP, 0); }
		public TerminalNode INEQUALITY_OP() { return getToken(SQLGrammarParser.INEQUALITY_OP, 0); }
		public Eq_opContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eq_op; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitEq_op(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Eq_opContext eq_op() throws RecognitionException {
		Eq_opContext _localctx = new Eq_opContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_eq_op);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(375);
			_la = _input.LA(1);
			if ( !(_la==EQUALITY_OP || _la==INEQUALITY_OP) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
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

	public static class LiteralContext extends ParserRuleContext {
		public Int_literalContext int_literal() {
			return getRuleContext(Int_literalContext.class,0);
		}
		public String_literalContext string_literal() {
			return getRuleContext(String_literalContext.class,0);
		}
		public Real_literalContext real_literal() {
			return getRuleContext(Real_literalContext.class,0);
		}
		public Null_literalContext null_literal() {
			return getRuleContext(Null_literalContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_literal);
		try {
			setState(381);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NUM:
				enterOuterAlt(_localctx, 1);
				{
				setState(377);
				int_literal();
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(378);
				string_literal();
				}
				break;
			case REAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(379);
				real_literal();
				}
				break;
			case NULL:
				enterOuterAlt(_localctx, 4);
				{
				setState(380);
				null_literal();
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

	public static class Int_literalContext extends ParserRuleContext {
		public TerminalNode NUM() { return getToken(SQLGrammarParser.NUM, 0); }
		public Int_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_int_literal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitInt_literal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Int_literalContext int_literal() throws RecognitionException {
		Int_literalContext _localctx = new Int_literalContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_int_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(383);
			match(NUM);
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

	public static class String_literalContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(SQLGrammarParser.STRING, 0); }
		public String_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string_literal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitString_literal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final String_literalContext string_literal() throws RecognitionException {
		String_literalContext _localctx = new String_literalContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_string_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(385);
			match(STRING);
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

	public static class Real_literalContext extends ParserRuleContext {
		public TerminalNode REAL() { return getToken(SQLGrammarParser.REAL, 0); }
		public Real_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_real_literal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitReal_literal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Real_literalContext real_literal() throws RecognitionException {
		Real_literalContext _localctx = new Real_literalContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_real_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			match(REAL);
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

	public static class Null_literalContext extends ParserRuleContext {
		public TerminalNode NULL() { return getToken(SQLGrammarParser.NULL, 0); }
		public Null_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_null_literal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SQLGrammarVisitor ) return ((SQLGrammarVisitor<? extends T>)visitor).visitNull_literal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Null_literalContext null_literal() throws RecognitionException {
		Null_literalContext _localctx = new Null_literalContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_null_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(389);
			match(NULL);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 29:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		case 2:
			return precpred(_ctx, 4);
		case 3:
			return precpred(_ctx, 3);
		case 4:
			return precpred(_ctx, 2);
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3A\u018a\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\3\2\3\2\3\2\7\2N\n\2\f\2\16\2Q\13\2\3\2"+
		"\5\2T\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\5\3e\n\3\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7"+
		"\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\n\5\n\u0088\n\n\3\13\3\13\3\13\7\13\u008d\n\13\f\13\16\13\u0090\13\13"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00ab\n\f\3\r\3\r\3\r\3\r\3\r\3\r\7"+
		"\r\u00b3\n\r\f\r\16\r\u00b6\13\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5"+
		"\16\u00bf\n\16\3\16\3\16\3\16\3\16\5\16\u00c5\n\16\7\16\u00c7\n\16\f\16"+
		"\16\16\u00ca\13\16\3\16\3\16\5\16\u00ce\n\16\3\17\3\17\3\17\3\17\3\17"+
		"\5\17\u00d5\n\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00e0"+
		"\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\7\20"+
		"\u00ee\n\20\f\20\16\20\u00f1\13\20\5\20\u00f3\n\20\3\21\3\21\3\21\3\21"+
		"\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\7\24\u0104\n\24"+
		"\f\24\16\24\u0107\13\24\3\25\3\25\3\25\3\25\3\26\3\26\3\26\7\26\u0110"+
		"\n\26\f\26\16\26\u0113\13\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u011c"+
		"\n\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\5\30\u0129"+
		"\n\30\3\31\3\31\3\31\3\31\3\31\5\31\u0130\n\31\3\32\3\32\3\33\3\33\5\33"+
		"\u0136\n\33\3\34\3\34\3\34\7\34\u013b\n\34\f\34\16\34\u013e\13\34\3\35"+
		"\3\35\3\35\5\35\u0143\n\35\3\35\3\35\3\35\3\35\5\35\u0149\n\35\3\35\3"+
		"\35\3\35\5\35\u014e\n\35\3\36\3\36\3\36\5\36\u0153\n\36\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\5\37\u0160\n\37\3\37\3\37\3\37"+
		"\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37"+
		"\3\37\3\37\7\37\u0175\n\37\f\37\16\37\u0178\13\37\3 \3 \3!\3!\3!\3!\5"+
		"!\u0180\n!\3\"\3\"\3#\3#\3$\3$\3%\3%\3%\2\3<&\2\4\6\b\n\f\16\20\22\24"+
		"\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFH\2\7\3\2*+\3\2&\'\3\2\60"+
		"\62\3\2./\3\2\66\67\2\u019e\2J\3\2\2\2\4d\3\2\2\2\6f\3\2\2\2\bi\3\2\2"+
		"\2\nm\3\2\2\2\ft\3\2\2\2\16x\3\2\2\2\20{\3\2\2\2\22\u0087\3\2\2\2\24\u0089"+
		"\3\2\2\2\26\u00aa\3\2\2\2\30\u00ac\3\2\2\2\32\u00b7\3\2\2\2\34\u00df\3"+
		"\2\2\2\36\u00f2\3\2\2\2 \u00f4\3\2\2\2\"\u00f8\3\2\2\2$\u00fb\3\2\2\2"+
		"&\u0100\3\2\2\2(\u0108\3\2\2\2*\u010c\3\2\2\2,\u0114\3\2\2\2.\u0122\3"+
		"\2\2\2\60\u012a\3\2\2\2\62\u0131\3\2\2\2\64\u0133\3\2\2\2\66\u0137\3\2"+
		"\2\28\u013f\3\2\2\2:\u014f\3\2\2\2<\u015f\3\2\2\2>\u0179\3\2\2\2@\u017f"+
		"\3\2\2\2B\u0181\3\2\2\2D\u0183\3\2\2\2F\u0185\3\2\2\2H\u0187\3\2\2\2J"+
		"O\5\4\3\2KL\7,\2\2LN\5\4\3\2MK\3\2\2\2NQ\3\2\2\2OM\3\2\2\2OP\3\2\2\2P"+
		"S\3\2\2\2QO\3\2\2\2RT\7,\2\2SR\3\2\2\2ST\3\2\2\2T\3\3\2\2\2Ue\5\b\5\2"+
		"Ve\5\n\6\2We\5\f\7\2Xe\5\16\b\2Ye\5\20\t\2Ze\5\32\16\2[e\5\36\20\2\\e"+
		"\5 \21\2]e\5\"\22\2^e\5$\23\2_e\5,\27\2`e\5.\30\2ae\5\60\31\2be\58\35"+
		"\2ce\5\6\4\2dU\3\2\2\2dV\3\2\2\2dW\3\2\2\2dX\3\2\2\2dY\3\2\2\2dZ\3\2\2"+
		"\2d[\3\2\2\2d\\\3\2\2\2d]\3\2\2\2d^\3\2\2\2d_\3\2\2\2d`\3\2\2\2da\3\2"+
		"\2\2db\3\2\2\2dc\3\2\2\2e\5\3\2\2\2fg\7)\2\2gh\t\2\2\2h\7\3\2\2\2ij\7"+
		"\4\2\2jk\7\5\2\2kl\7<\2\2l\t\3\2\2\2mn\7\7\2\2no\7\5\2\2op\7<\2\2pq\7"+
		"\26\2\2qr\7\27\2\2rs\7<\2\2s\13\3\2\2\2tu\7\b\2\2uv\7\5\2\2vw\7<\2\2w"+
		"\r\3\2\2\2xy\7\t\2\2yz\7\6\2\2z\17\3\2\2\2{|\7\n\2\2|}\7\5\2\2}~\7<\2"+
		"\2~\21\3\2\2\2\177\u0088\78\2\2\u0080\u0088\79\2\2\u0081\u0088\7:\2\2"+
		"\u0082\u0083\7;\2\2\u0083\u0084\7\64\2\2\u0084\u0085\5B\"\2\u0085\u0086"+
		"\7\63\2\2\u0086\u0088\3\2\2\2\u0087\177\3\2\2\2\u0087\u0080\3\2\2\2\u0087"+
		"\u0081\3\2\2\2\u0087\u0082\3\2\2\2\u0088\23\3\2\2\2\u0089\u008e\7<\2\2"+
		"\u008a\u008b\7\3\2\2\u008b\u008d\7<\2\2\u008c\u008a\3\2\2\2\u008d\u0090"+
		"\3\2\2\2\u008e\u008c\3\2\2\2\u008e\u008f\3\2\2\2\u008f\25\3\2\2\2\u0090"+
		"\u008e\3\2\2\2\u0091\u0092\7<\2\2\u0092\u0093\7\16\2\2\u0093\u0094\7\20"+
		"\2\2\u0094\u0095\7\64\2\2\u0095\u0096\5\24\13\2\u0096\u0097\7\63\2\2\u0097"+
		"\u00ab\3\2\2\2\u0098\u0099\7<\2\2\u0099\u009a\7\17\2\2\u009a\u009b\7\20"+
		"\2\2\u009b\u009c\7\64\2\2\u009c\u009d\5\24\13\2\u009d\u009e\7\63\2\2\u009e"+
		"\u009f\7\21\2\2\u009f\u00a0\7<\2\2\u00a0\u00a1\7\64\2\2\u00a1\u00a2\5"+
		"\24\13\2\u00a2\u00a3\7\63\2\2\u00a3\u00ab\3\2\2\2\u00a4\u00a5\7<\2\2\u00a5"+
		"\u00a6\7\22\2\2\u00a6\u00a7\7\64\2\2\u00a7\u00a8\5<\37\2\u00a8\u00a9\7"+
		"\63\2\2\u00a9\u00ab\3\2\2\2\u00aa\u0091\3\2\2\2\u00aa\u0098\3\2\2\2\u00aa"+
		"\u00a4\3\2\2\2\u00ab\27\3\2\2\2\u00ac\u00ad\7\13\2\2\u00ad\u00ae\5\26"+
		"\f\2\u00ae\u00b4\3\2\2\2\u00af\u00b0\7\3\2\2\u00b0\u00b1\7\13\2\2\u00b1"+
		"\u00b3\5\26\f\2\u00b2\u00af\3\2\2\2\u00b3\u00b6\3\2\2\2\u00b4\u00b2\3"+
		"\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\31\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b7"+
		"\u00b8\7\4\2\2\u00b8\u00b9\7\f\2\2\u00b9\u00cd\7<\2\2\u00ba\u00bb\7\64"+
		"\2\2\u00bb\u00bc\7<\2\2\u00bc\u00be\5\22\n\2\u00bd\u00bf\5\30\r\2\u00be"+
		"\u00bd\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c8\3\2\2\2\u00c0\u00c1\7\3"+
		"\2\2\u00c1\u00c2\7<\2\2\u00c2\u00c4\5\22\n\2\u00c3\u00c5\5\30\r\2\u00c4"+
		"\u00c3\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c7\3\2\2\2\u00c6\u00c0\3\2"+
		"\2\2\u00c7\u00ca\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9"+
		"\u00cb\3\2\2\2\u00ca\u00c8\3\2\2\2\u00cb\u00cc\7\63\2\2\u00cc\u00ce\3"+
		"\2\2\2\u00cd\u00ba\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\33\3\2\2\2\u00cf"+
		"\u00d0\7\30\2\2\u00d0\u00d1\7\31\2\2\u00d1\u00d2\7<\2\2\u00d2\u00d4\5"+
		"\22\n\2\u00d3\u00d5\5\30\r\2\u00d4\u00d3\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5"+
		"\u00e0\3\2\2\2\u00d6\u00d7\7\30\2\2\u00d7\u00d8\7\13\2\2\u00d8\u00e0\5"+
		"\26\f\2\u00d9\u00da\7\b\2\2\u00da\u00db\7\31\2\2\u00db\u00e0\7<\2\2\u00dc"+
		"\u00dd\7\b\2\2\u00dd\u00de\7\13\2\2\u00de\u00e0\7<\2\2\u00df\u00cf\3\2"+
		"\2\2\u00df\u00d6\3\2\2\2\u00df\u00d9\3\2\2\2\u00df\u00dc\3\2\2\2\u00e0"+
		"\35\3\2\2\2\u00e1\u00e2\7\7\2\2\u00e2\u00e3\7\f\2\2\u00e3\u00e4\7<\2\2"+
		"\u00e4\u00e5\7\26\2\2\u00e5\u00e6\7\27\2\2\u00e6\u00f3\7<\2\2\u00e7\u00e8"+
		"\7\7\2\2\u00e8\u00e9\7\f\2\2\u00e9\u00ea\7<\2\2\u00ea\u00ef\5\34\17\2"+
		"\u00eb\u00ec\7\3\2\2\u00ec\u00ee\5\34\17\2\u00ed\u00eb\3\2\2\2\u00ee\u00f1"+
		"\3\2\2\2\u00ef\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\u00f3\3\2\2\2\u00f1"+
		"\u00ef\3\2\2\2\u00f2\u00e1\3\2\2\2\u00f2\u00e7\3\2\2\2\u00f3\37\3\2\2"+
		"\2\u00f4\u00f5\7\b\2\2\u00f5\u00f6\7\f\2\2\u00f6\u00f7\7<\2\2\u00f7!\3"+
		"\2\2\2\u00f8\u00f9\7\t\2\2\u00f9\u00fa\7\r\2\2\u00fa#\3\2\2\2\u00fb\u00fc"+
		"\7\t\2\2\u00fc\u00fd\7\32\2\2\u00fd\u00fe\7\33\2\2\u00fe\u00ff\7<\2\2"+
		"\u00ff%\3\2\2\2\u0100\u0105\5<\37\2\u0101\u0102\7\3\2\2\u0102\u0104\5"+
		"<\37\2\u0103\u0101\3\2\2\2\u0104\u0107\3\2\2\2\u0105\u0103\3\2\2\2\u0105"+
		"\u0106\3\2\2\2\u0106\'\3\2\2\2\u0107\u0105\3\2\2\2\u0108\u0109\7<\2\2"+
		"\u0109\u010a\7\66\2\2\u010a\u010b\5<\37\2\u010b)\3\2\2\2\u010c\u0111\5"+
		"(\25\2\u010d\u010e\7\3\2\2\u010e\u0110\5(\25\2\u010f\u010d\3\2\2\2\u0110"+
		"\u0113\3\2\2\2\u0111\u010f\3\2\2\2\u0111\u0112\3\2\2\2\u0112+\3\2\2\2"+
		"\u0113\u0111\3\2\2\2\u0114\u0115\7\34\2\2\u0115\u0116\7\35\2\2\u0116\u011b"+
		"\7<\2\2\u0117\u0118\7\64\2\2\u0118\u0119\5\24\13\2\u0119\u011a\7\63\2"+
		"\2\u011a\u011c\3\2\2\2\u011b\u0117\3\2\2\2\u011b\u011c\3\2\2\2\u011c\u011d"+
		"\3\2\2\2\u011d\u011e\7\36\2\2\u011e\u011f\7\64\2\2\u011f\u0120\5&\24\2"+
		"\u0120\u0121\7\63\2\2\u0121-\3\2\2\2\u0122\u0123\7\37\2\2\u0123\u0124"+
		"\7<\2\2\u0124\u0125\7 \2\2\u0125\u0128\5*\26\2\u0126\u0127\7!\2\2\u0127"+
		"\u0129\5<\37\2\u0128\u0126\3\2\2\2\u0128\u0129\3\2\2\2\u0129/\3\2\2\2"+
		"\u012a\u012b\7\"\2\2\u012b\u012c\7\33\2\2\u012c\u012f\7<\2\2\u012d\u012e"+
		"\7!\2\2\u012e\u0130\5<\37\2\u012f\u012d\3\2\2\2\u012f\u0130\3\2\2\2\u0130"+
		"\61\3\2\2\2\u0131\u0132\t\3\2\2\u0132\63\3\2\2\2\u0133\u0135\5<\37\2\u0134"+
		"\u0136\5\62\32\2\u0135\u0134\3\2\2\2\u0135\u0136\3\2\2\2\u0136\65\3\2"+
		"\2\2\u0137\u013c\5\64\33\2\u0138\u0139\7\3\2\2\u0139\u013b\5\64\33\2\u013a"+
		"\u0138\3\2\2\2\u013b\u013e\3\2\2\2\u013c\u013a\3\2\2\2\u013c\u013d\3\2"+
		"\2\2\u013d\67\3\2\2\2\u013e\u013c\3\2\2\2\u013f\u0142\7#\2\2\u0140\u0143"+
		"\7\60\2\2\u0141\u0143\5\24\13\2\u0142\u0140\3\2\2\2\u0142\u0141\3\2\2"+
		"\2\u0143\u0144\3\2\2\2\u0144\u0145\7\33\2\2\u0145\u0148\5\24\13\2\u0146"+
		"\u0147\7!\2\2\u0147\u0149\5<\37\2\u0148\u0146\3\2\2\2\u0148\u0149\3\2"+
		"\2\2\u0149\u014d\3\2\2\2\u014a\u014b\7$\2\2\u014b\u014c\7%\2\2\u014c\u014e"+
		"\5\66\34\2\u014d\u014a\3\2\2\2\u014d\u014e\3\2\2\2\u014e9\3\2\2\2\u014f"+
		"\u0152\7<\2\2\u0150\u0151\7-\2\2\u0151\u0153\7<\2\2\u0152\u0150\3\2\2"+
		"\2\u0152\u0153\3\2\2\2\u0153;\3\2\2\2\u0154\u0155\b\37\1\2\u0155\u0160"+
		"\5:\36\2\u0156\u0160\5@!\2\u0157\u0158\7\64\2\2\u0158\u0159\5<\37\2\u0159"+
		"\u015a\7\63\2\2\u015a\u0160\3\2\2\2\u015b\u015c\7.\2\2\u015c\u0160\5<"+
		"\37\n\u015d\u015e\7\25\2\2\u015e\u0160\5<\37\7\u015f\u0154\3\2\2\2\u015f"+
		"\u0156\3\2\2\2\u015f\u0157\3\2\2\2\u015f\u015b\3\2\2\2\u015f\u015d\3\2"+
		"\2\2\u0160\u0176\3\2\2\2\u0161\u0162\f\t\2\2\u0162\u0163\t\4\2\2\u0163"+
		"\u0175\5<\37\n\u0164\u0165\f\b\2\2\u0165\u0166\t\5\2\2\u0166\u0175\5<"+
		"\37\t\u0167\u0168\f\6\2\2\u0168\u0169\7\65\2\2\u0169\u0175\5<\37\7\u016a"+
		"\u016b\f\5\2\2\u016b\u016c\5> \2\u016c\u016d\5<\37\6\u016d\u0175\3\2\2"+
		"\2\u016e\u016f\f\4\2\2\u016f\u0170\7\23\2\2\u0170\u0175\5<\37\5\u0171"+
		"\u0172\f\3\2\2\u0172\u0173\7\24\2\2\u0173\u0175\5<\37\4\u0174\u0161\3"+
		"\2\2\2\u0174\u0164\3\2\2\2\u0174\u0167\3\2\2\2\u0174\u016a\3\2\2\2\u0174"+
		"\u016e\3\2\2\2\u0174\u0171\3\2\2\2\u0175\u0178\3\2\2\2\u0176\u0174\3\2"+
		"\2\2\u0176\u0177\3\2\2\2\u0177=\3\2\2\2\u0178\u0176\3\2\2\2\u0179\u017a"+
		"\t\6\2\2\u017a?\3\2\2\2\u017b\u0180\5B\"\2\u017c\u0180\5D#\2\u017d\u0180"+
		"\5F$\2\u017e\u0180\5H%\2\u017f\u017b\3\2\2\2\u017f\u017c\3\2\2\2\u017f"+
		"\u017d\3\2\2\2\u017f\u017e\3\2\2\2\u0180A\3\2\2\2\u0181\u0182\7=\2\2\u0182"+
		"C\3\2\2\2\u0183\u0184\7>\2\2\u0184E\3\2\2\2\u0185\u0186\7?\2\2\u0186G"+
		"\3\2\2\2\u0187\u0188\7(\2\2\u0188I\3\2\2\2 OSd\u0087\u008e\u00aa\u00b4"+
		"\u00be\u00c4\u00c8\u00cd\u00d4\u00df\u00ef\u00f2\u0105\u0111\u011b\u0128"+
		"\u012f\u0135\u013c\u0142\u0148\u014d\u0152\u015f\u0174\u0176\u017f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}