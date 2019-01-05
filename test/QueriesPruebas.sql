-- createDatabase : CREATE DATABASE ID;
CREATE DATABASE Jorge;

-- alterDatabase : ALTER DATABASE ID RENAME TO ID;
ALTER DATABASE Jorge RENAME TO Tabla;

-- dropDatabase : DROP DATABASE ID;
DROP DATABASE Tabla;

-- showDatabses : SHOW DATABASES;
SHOW DATABASES;

-- useDatabase : USE DATABASE ID;
USE DATABASE Tabla;


-- createTable : CREATE TABLE ID '(' ( (ID tipoColumna)(',' ID tipoColumna)* (CONSTRAINT tipoConstraint)(',' (CONSTRAINT tipoConstraint)*) ')' );

CREATE TABLE Tabla;
CREATE TABLE Tabla( id INT );
CrEaTe TABLE Tabla( id INT, dinero FLOAT, fecha DATE, andres CHAR(5) );
CrEaTe TABLE Tabla( id INT, dinero FLOAT, fecha DATE, andres CHAR(5)
CONSTRAINT PK_idTabla PRIMARY KEY( id ), CONSTRAINT CH_rev CHECK( id < 100 AND fecha > hoy OR NOT( dinero > 100 ) ),
CONSTRAINT FK_foranea FOREIGN KEY( idUsuario, nombre ) REFERENCES Tabla( idUsuario, nombreUsuario ),
CONSTRAINT FK_foranea FOREIGN KEY( idUsuario ) REFERENCES Tabla( idUsuario ) );

ALTER TABLE Tabla RENAME TO NuevaTabla;
ALTER TABLE Tabla
	ADD COLUMN columna FLOAT CONSTRAINT CH_columna CHECK ( columna = columna ),
	ADD COLUMN sinConstraint CHAR(10),
	ADD CONSTRAINT PK_columna PRIMARY KEY( columna ),
	DROP COLUMN dinero,
	DROP CONSTRAINT FK_foranea;
	
DROP TABLE NuevaTabla;

SHOW TABLES;

SHOW COLUMNS FROM Tabla;




-- DML
INSERT INTO Tabla VALUES ( 10, 'Hola', '14/abr/2014' );
INSERT INTO Tabla (id, andres, fecha) VALUES ( 10, 'Hola', '14/abr/2014' );

UPDATE Tabla SET id=10, dinero=1000 WHERE id=10;
UPDATE Tabla SET dinero=1000 WHERE id=10;
UPDATE Tabla SET fecha='14/abr/2000';

DELETE FROM Tabla WHERE id<>10;
DELETE FROM Tabla;


SELECT * FROM Tabla WHERE 1=1;
SELECT * FROM Tabla WHERE 1=1 AND Tabla.id = 33;
SELECT * FROM Tabla;
SELECT id, fecha, dinero FROM Tabla ORDER BY id;
SELECT id, fecha, dinero FROM Tabla ORDER BY id ASC, dinero DESC;
SELECT id, fecha, dinero FROM Tabla WHERE 1=1 ORDER BY id ASC, dinero DESC;