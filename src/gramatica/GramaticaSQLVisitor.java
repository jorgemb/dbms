// Generated from /Users/eddycastro/Desktop/DBMS/GramaticaSQL.g4 by ANTLR 4.1
package gramatica;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link GramaticaSQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface GramaticaSQLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#update}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUpdate(@NotNull GramaticaSQLParser.UpdateContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#andExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(@NotNull GramaticaSQLParser.AndExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#locationExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocationExpr(@NotNull GramaticaSQLParser.LocationExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#createTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateTable(@NotNull GramaticaSQLParser.CreateTableContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#location}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocation(@NotNull GramaticaSQLParser.LocationContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#alterDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterDatabase(@NotNull GramaticaSQLParser.AlterDatabaseContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#listaConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaConstraint(@NotNull GramaticaSQLParser.ListaConstraintContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#alterTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlterTable(@NotNull GramaticaSQLParser.AlterTableContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#literalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpr(@NotNull GramaticaSQLParser.LiteralExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#tipoColumna}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoColumna(@NotNull GramaticaSQLParser.TipoColumnaContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#listaOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaOrder(@NotNull GramaticaSQLParser.ListaOrderContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#dropDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropDatabase(@NotNull GramaticaSQLParser.DropDatabaseContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#listaIDValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaIDValue(@NotNull GramaticaSQLParser.ListaIDValueContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#showColumns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowColumns(@NotNull GramaticaSQLParser.ShowColumnsContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#deleteFrom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteFrom(@NotNull GramaticaSQLParser.DeleteFromContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#echo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEcho(@NotNull GramaticaSQLParser.EchoContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#listaValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaValue(@NotNull GramaticaSQLParser.ListaValueContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#null_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull_literal(@NotNull GramaticaSQLParser.Null_literalContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#showTables}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowTables(@NotNull GramaticaSQLParser.ShowTablesContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#createDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreateDatabase(@NotNull GramaticaSQLParser.CreateDatabaseContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#real_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReal_literal(@NotNull GramaticaSQLParser.Real_literalContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#eqExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExpr(@NotNull GramaticaSQLParser.EqExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#insertInto}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInsertInto(@NotNull GramaticaSQLParser.InsertIntoContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#notExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(@NotNull GramaticaSQLParser.NotExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#listaID}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListaID(@NotNull GramaticaSQLParser.ListaIDContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#idValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdValue(@NotNull GramaticaSQLParser.IdValueContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#select}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelect(@NotNull GramaticaSQLParser.SelectContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#int_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt_literal(@NotNull GramaticaSQLParser.Int_literalContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#multdivExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultdivExpr(@NotNull GramaticaSQLParser.MultdivExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#showDatabases}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShowDatabases(@NotNull GramaticaSQLParser.ShowDatabasesContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#relExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelExpr(@NotNull GramaticaSQLParser.RelExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#addsubExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddsubExpr(@NotNull GramaticaSQLParser.AddsubExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#parenthesisExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesisExpr(@NotNull GramaticaSQLParser.ParenthesisExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#accion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAccion(@NotNull GramaticaSQLParser.AccionContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#orExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(@NotNull GramaticaSQLParser.OrExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(@NotNull GramaticaSQLParser.StatementContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#dropTable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDropTable(@NotNull GramaticaSQLParser.DropTableContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#tipoOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoOrder(@NotNull GramaticaSQLParser.TipoOrderContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#useDatabase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUseDatabase(@NotNull GramaticaSQLParser.UseDatabaseContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(@NotNull GramaticaSQLParser.ProgramContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#negExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegExpr(@NotNull GramaticaSQLParser.NegExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#eq_op}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq_op(@NotNull GramaticaSQLParser.Eq_opContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#string_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_literal(@NotNull GramaticaSQLParser.String_literalContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(@NotNull GramaticaSQLParser.LiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#expOrder}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpOrder(@NotNull GramaticaSQLParser.ExpOrderContext ctx);

	/**
	 * Visit a parse tree produced by {@link GramaticaSQLParser#tipoConstraint}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipoConstraint(@NotNull GramaticaSQLParser.TipoConstraintContext ctx);
}