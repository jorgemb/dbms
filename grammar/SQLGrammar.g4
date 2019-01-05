grammar SQLGrammar;

// *** LEXER ***
// Fragments are included in order to make reserved works case insensitive
fragment A_ :	'a' | 'A';
fragment B_ :	'b' | 'B';
fragment C_ :	'c' | 'C';
fragment D_ :	'd' | 'D';
fragment E_ :	'e' | 'E';
fragment F_ :	'f' | 'F';
fragment G_ :	'g' | 'G';
fragment H_ :	'h' | 'H';
fragment I_ :	'i' | 'I';
fragment J_ :	'j' | 'J';
fragment K_ :	'k' | 'K';
fragment L_ :	'l' | 'L';
fragment M_ :	'm' | 'M';
fragment N_ :	'n' | 'N';
fragment O_ :	'o' | 'O';
fragment P_ :	'p' | 'P';
fragment Q_ :	'q' | 'Q';
fragment R_ :	'r' | 'R';
fragment S_ :	's' | 'S';
fragment T_ :	't' | 'T';
fragment U_ :	'u' | 'U';
fragment V_ :	'v' | 'V';
fragment W_ :	'w' | 'W';
fragment X_ :	'x' | 'X';
fragment Y_ :	'y' | 'Y';
fragment Z_ :	'z' | 'Z';

// *** Reserved words ***
CREATE : C_ R_ E_ A_ T_ E_ ;
DATABASE : D_ A_ T_ A_ B_ A_ S_ E_ ;
DATABASES : D_ A_ T_ A_ B_ A_ S_ E_ S_;
ALTER : A_ L_ T_ E_ R_ ;
DROP : D_ R_ O_ P_ ;
SHOW : S_ H_ O_ W_ ;
USE : U_ S_ E_ ;
CONSTRAINT : C_ O_ N_ S_ T_ R_ A_ I_ N_ T_ ;
TABLE : T_ A_ B_ L_ E_ ;
TABLES : T_ A_ B_ L_ E_ S_ ;
PRIMARY : P_ R_ I_ M_ A_ R_ Y_ ;
FOREIGN : F_ O_ R_ E_ I_ G_ N_ ;
KEY : K_ E_ Y_ ;
REFERENCES : R_ E_ F_ E_ R_ E_ N_ C_ E_ S_ ;
CHECK : C_ H_ E_ C_ K_ ;

AND : A_ N_ D_ ;
OR : O_ R_ ;
NOT : N_ O_ T_ ;
RENAME : R_ E_ N_ A_ M_ E_ ;
TO : T_ O_ ;
ADD : A_ D_ D_ ;
COLUMN : C_ O_ L_ U_ M_ N_ ;
COLUMNS : C_ O_ L_ U_ M_ N_ S_ ;

FROM : F_ R_ O_ M_ ;
INSERT : I_ N_ S_ E_ R_ T_ ;
INTO : I_ N_ T_ O_ ;
VALUES : V_ A_ L_ U_ E_ S_ ;
UPDATE : U_ P_ D_ A_ T_ E_ ;
SET : S_ E_ T_ ;
WHERE : W_ H_ E_ R_ E_ ;
DELETE : D_ E_ L_ E_ T_ E_ ;
SELECT : S_ E_ L_ E_ C_ T_ ;
ORDER : O_ R_ D_ E_ R_ ;
BY : B_ Y_ ;
ASC : A_ S_ C_ ;
DESC : D_ E_ S_ C_ ;
NULL : N_ U_ L_ L_ ;

ECHO : E_ C_ H_ O_;
ENABLED : E_ N_ A_ B_ L_ E_ D_;
DISABLED : D_ I_ S_ A_ B_ L_ E_ D_;


//Tokens
fragment LETTER : ([a-z] | [A-Z]);
fragment DIGIT : [0-9];
fragment SYMBOL : ('/'|'-'|' '|'.'|'+'|'*'|'_');

// *** Symbols /Operators ***
END_LINE : ';';
DOT_OP : '.';
SUBTRACT_OP : '-';
SUM_OP : '+';
TIMES_OP : '*';
DIV_OP : '/';
MOD_OP : '%';
RIGHT_PAR : ')';
LEFT_PAR : '(';

REL_OP : '<' | '>' | '<=' | '>=' ;
EQUALITY_OP : '=';
INEQUALITY_OP : '<>';

// TYPES
INT : I_ N_ T_;
FLOAT : F_ L_ O_ A_ T_;
DATE : D_ A_ T_ E_;
CHAR : C_ H_ A_ R_;

// VALUES
ID : LETTER(LETTER|DIGIT|'_')*;
NUM : DIGIT(DIGIT)*;
STRING : '\'' (LETTER|DIGIT|SYMBOL)* '\'';
REAL : (DIGIT)+ '.' (DIGIT)+;


// *** PARSER ***


// ----------------DDL----------------


program : statement (END_LINE statement)* END_LINE?;


statement : createDatabase
          | alterDatabase
          | dropDatabase
          | showDatabases
          | useDatabase
          | createTable
          | alterTable
          | dropTable
          | showTables
          | showColumns
          | insertInto
          | update
          | deleteFrom
          | select
          | echo
          ;
            
 
echo : ECHO (ENABLED | DISABLED);

createDatabase : CREATE DATABASE ID;

alterDatabase : ALTER DATABASE ID RENAME TO ID;

dropDatabase : DROP DATABASE ID;

showDatabases : SHOW DATABASES;

useDatabase : USE DATABASE ID;

columnType : INT
            | FLOAT
            | DATE
            | CHAR LEFT_PAR int_literal RIGHT_PAR
            ;

idList : (ID)(',' ID)*;

constraintType : ID PRIMARY KEY LEFT_PAR idList RIGHT_PAR
               | ID FOREIGN KEY LEFT_PAR idList RIGHT_PAR REFERENCES ID LEFT_PAR idList RIGHT_PAR
               | ID CHECK ( LEFT_PAR expression RIGHT_PAR )
               ;

constraintList : (CONSTRAINT constraintType)(',' CONSTRAINT constraintType)*;

// Could be a disaster (TODO)
createTable : CREATE TABLE ID ( LEFT_PAR
            ( (ID columnType constraintList?)(',' ID columnType constraintList?)* )
            RIGHT_PAR )?;

action : ADD COLUMN ID columnType constraintList?
       | ADD CONSTRAINT constraintType
       | DROP COLUMN ID
       | DROP CONSTRAINT ID
       ;

alterTable : ALTER TABLE ID RENAME TO ID
           | ALTER TABLE ID (action)(',' action)*
           ;

dropTable : DROP TABLE ID;

showTables : SHOW TABLES;

showColumns : SHOW COLUMNS FROM ID;


//----------------DML----------------



valueList : (expression)(',' expression)*;

idValue : ID EQUALITY_OP expression;

idValueList : (idValue)(',' idValue)*;

insertInto : INSERT INTO ID (LEFT_PAR idList RIGHT_PAR)? VALUES LEFT_PAR valueList RIGHT_PAR;

update : UPDATE ID SET idValueList (WHERE expression)?;

deleteFrom : DELETE FROM ID (WHERE expression)?;

orderType : ASC | DESC;
orderExp : expression orderType?;
orderList : (orderExp)(',' orderExp)*;

select : SELECT ('*' | idList) FROM idList (WHERE expression)? (ORDER BY orderList)?;
//tabla : select | idList;

// *** EXPRESSIONS ***
location : (ID)(DOT_OP ID)?;

expression : location                               #locationExpr
           | literal                                #literalExpr
// Arithmetic
           | LEFT_PAR expression RIGHT_PAR             #parenthesisExpr
           | SUBTRACT_OP expression                       #negExpr
           | expression (TIMES_OP|DIV_OP|MOD_OP) expression   #multdivExpr
           | expression (SUM_OP|SUBTRACT_OP) expression     #addsubExpr
// Logic
           | NOT expression                         #notExpr
           | expression REL_OP expression           #relExpr
           | expression eq_op expression            #eqExpr
           | expression AND expression              #andExpr
           | expression OR expression               #orExpr
           ;

eq_op : EQUALITY_OP | INEQUALITY_OP;
literal : int_literal | string_literal | real_literal | null_literal ;
int_literal : NUM;
string_literal : STRING;
real_literal : REAL;
null_literal : NULL;

/* Comments and white chars */
WS : ( ' ' | '\t' | '\r' | '\n' )+ -> skip;
COMMENT : '--' ~('\r' | '\n')* -> skip;

