// Generated from grammar/SQLGrammar.g4 by ANTLR 4.7.2
package grammar;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SQLGrammarParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SQLGrammarVisitor<T> extends ParseTreeVisitor<T> {

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#program}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(SQLGrammarParser.ProgramContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#statement}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(SQLGrammarParser.StatementContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#echo}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEcho(SQLGrammarParser.EchoContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#createDatabase}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDatabase(SQLGrammarParser.CreateDatabaseContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#alterDatabase}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterDatabase(SQLGrammarParser.AlterDatabaseContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#dropDatabase}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDatabase(SQLGrammarParser.DropDatabaseContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#showDatabases}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDatabases(SQLGrammarParser.ShowDatabasesContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#useDatabase}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseDatabase(SQLGrammarParser.UseDatabaseContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#columnType}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumnType(SQLGrammarParser.ColumnTypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#idList}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdList(SQLGrammarParser.IdListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#constraintType}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintType(SQLGrammarParser.ConstraintTypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#constraintList}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstraintList(SQLGrammarParser.ConstraintListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#createTable}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTable(SQLGrammarParser.CreateTableContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#action}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(SQLGrammarParser.ActionContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#alterTable}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTable(SQLGrammarParser.AlterTableContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#dropTable}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTable(SQLGrammarParser.DropTableContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#showTables}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTables(SQLGrammarParser.ShowTablesContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#showColumns}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowColumns(SQLGrammarParser.ShowColumnsContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#valueList}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueList(SQLGrammarParser.ValueListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#idValue}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdValue(SQLGrammarParser.IdValueContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#idValueList}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdValueList(SQLGrammarParser.IdValueListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#insertInto}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertInto(SQLGrammarParser.InsertIntoContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#update}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate(SQLGrammarParser.UpdateContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#deleteFrom}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteFrom(SQLGrammarParser.DeleteFromContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#orderType}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderType(SQLGrammarParser.OrderTypeContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#orderExp}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderExp(SQLGrammarParser.OrderExpContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#orderList}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrderList(SQLGrammarParser.OrderListContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#select}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect(SQLGrammarParser.SelectContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#location}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocation(SQLGrammarParser.LocationContext ctx);

	/**
	 * Visit a parse tree produced by the {@code eqExpr} labeled alternative in
	 * {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExpr(SQLGrammarParser.EqExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code notExpr} labeled alternative in
	 * {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(SQLGrammarParser.NotExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code locationExpr} labeled
	 * alternative in {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocationExpr(SQLGrammarParser.LocationExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code multdivExpr} labeled
	 * alternative in {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultdivExpr(SQLGrammarParser.MultdivExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code negExpr} labeled alternative in
	 * {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegExpr(SQLGrammarParser.NegExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code literalExpr} labeled
	 * alternative in {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpr(SQLGrammarParser.LiteralExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code orExpr} labeled alternative in
	 * {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(SQLGrammarParser.OrExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code addsubExpr} labeled alternative
	 * in {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddsubExpr(SQLGrammarParser.AddsubExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code relExpr} labeled alternative in
	 * {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExpr(SQLGrammarParser.RelExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code parenthesisExpr} labeled
	 * alternative in {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesisExpr(SQLGrammarParser.ParenthesisExprContext ctx);

	/**
	 * Visit a parse tree produced by the {@code andExpr} labeled alternative in
	 * {@link SQLGrammarParser#expression}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(SQLGrammarParser.AndExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#eq_op}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq_op(SQLGrammarParser.Eq_opContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#literal}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(SQLGrammarParser.LiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#int_literal}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt_literal(SQLGrammarParser.Int_literalContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#string_literal}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_literal(SQLGrammarParser.String_literalContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#real_literal}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReal_literal(SQLGrammarParser.Real_literalContext ctx);

	/**
	 * Visit a parse tree produced by {@link SQLGrammarParser#null_literal}.
	 *
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull_literal(SQLGrammarParser.Null_literalContext ctx);
}
