--1
CREATE DATABASE proyecto;
CREATE DATABASE proyecto_tst;
CREATE DATABASE proyecto_delete;
--SHOW DATABASES

-- Todos se realizan sin problema


--2
ALTER DATABASE proyecto_tst RENAME TO proyecto_test;
--SHOW DATABASES

-- Todos se realizan sin problemas

--3
DROP DATABASE proyecto_delete;
--SHOW DATABASES

-- Todos se realizan sin problemas


--4
USE DATABASE proyecto;
CREATE TABLE factura (id INT, nombre CHAR(20), nit CHAR(10), total FLOAT, comprado_en DATE);
CREATE TABLE usuario (id INT, nombre CHAR(20), comision FLOAT CONSTRAINT ch_com CHECK (comision <= 10.0));
CREATE TABLE sucursal (id INT, direccion CHAR(40), abierta_en DATE CONSTRAINT pk_sucursal PRIMARY KEY (id));
CREATE TABLE pais (id INT, pais CHAR(20));
--SHOW TABLES

-- Hace falta agregar check y restricción para longitud de char.


--5
ALTER TABLE usuario RENAME TO empleado;
--SHOW TABLES

-- Todos se realizan sin problemas
-- Nota: mensaje mostrado [Tabla usuario cambió de nombre a EMPLEADO.]


--6
ALTER TABLE sucursal ADD COLUMN nombre CHAR(10);
ALTER TABLE factura ADD CONSTRAINT pk_factura PRIMARY KEY (id);
ALTER TABLE empleado ADD CONSTRAINT pk_empleado PRIMARY KEY (id);
ALTER TABLE empleado ADD COLUMN codigo INT;
ALTER TABLE empleado ADD CONSTRAINT ch_codigo CHECK (0 < codigo AND codigo < 1000);
--SHOW TABLES


-- Hace falta agregar llave foránea.


--7
ALTER TABLE empleado DROP CONSTRAINT ch_codigo;
ALTER TABLE empleado DROP COLUMN codigo;
--SHOW TABLES

-- Todos se realizan sin problemas
-- Nota: verificar que realmente se elmine la restricción

--8
DROP TABLE pais;
--SHOW TABLES

-- Todos se realizan sin problemas


--9
--CREATE TABLE empleado_sucursal (empleado_id INT, sucursal_id INT, asignado_en DATE CONSTRAINT FK_empleado FOREIGN KEY ( empleado_id ) REFERENCES empleado (id), FK_sucursal FOREIGN KEY ( sucursal_id ) REFERENCES sucursal ( id ), PK_us PRIMARY KEY (empleado_id, sucursal_id))
--SHOW TABLES
--SHOW COLUMNS FROM empleado_sucursal

-- No corre, hcen falta bastantes cosas.


--10 (ERRORES)
ALTER DATABASE proyecto_tst RENAME TO nueva; --no existe la base de datos proyecto_tst
DROP DATABASE proyecto_ganado; --no existe la base de datos proyecto_ganado
DROP DATABASE proyecto_delete; --no existe la base de datos proyecto_delete
ALTER TABLE tabla_no_existe ADD COLUMN nombre CHAR(10); --no existe la relación tabla_no_existe
ALTER TABLE pais ADD COLUMN nombre CHAR(10); --no existe la relación pais
DROP TABLE tabla_no_existe; --no existe la tabla tabla_no_existe
DROP TABLE pais; --no existe la tabla pais


-- Todos se realizan sin problemas


--11 (ERRORES)
DROP TABLE sucursal; --no se puede eliminar tabla sucursal porque otros objetos dependen de el (fk_sucursal en empleado_sucursal)
-- Verificación de DROP DATABASE proyecto_tst
CREATE TABLE usuario (id INT, nombre CHAR(20), comision xxx); --no existe el tipo xxx
CREATE TABLE pais (id INT, pais CHAR(20), CONSTRAINT pk_sucursal PRIMARY KEY (id_pais)); --no existe la columna id_pais en la llave
ALTER TABLE empleado ADD CONSTRAINT ch_codigo CHECK (0 < codigo AND codigo < 1000); --no existe la columna codigo


-- Si deja eliminar la base de datos sucursal, en llave para id_pais realiza todo menos la llave, alter table no se realiza.


--12 (ERRORES)
ALTER TABLE empleado_sucursal ADD CONSTRAINT fk_empleado FOREIGN KEY (empleado_id) REFERENCES empleado (id); --la restricción ya existe
ALTER TABLE empleado_sucursal ADD CONSTRAINT fk_empleado2 FOREIGN KEY (asignado_en) REFERENCES empleado (id); --la restricción no puede ser implementada por tipos incompatibles (DATE e INT)
ALTER TABLE empleado_sucursal ADD CONSTRAINT fk_empleado3 FOREIGN KEY (empleado_id) REFERENCES factura (comision); --no existe la columna comisión referida
ALTER TABLE empleado_sucursal ADD CONSTRAINT fk_empleado4 FOREIGN KEY (empleado_id) REFERENCES empleado (comision); --no hay restriccion única que coincida con las columnas
ALTER TABLE empleado ADD CONSTRAINT pk_empleado2 PRIMARY KEY (id); --no se permiten múltiples llaves primarias para la tabla empleado

-- No existen llaves foráneas, no se muestra el nombre de la restricción en el error.


--13
INSERT INTO empleado VALUES (1,'Juan',5.0);
INSERT INTO empleado VALUES (2,'Pedro',NULL);
INSERT INTO empleado (id, nombre) VALUES (3, 'Julio');
INSERT INTO empleado (nombre, id) VALUES ('Jose',4);
INSERT INTO empleado (comision, id, nombre) VALUES (6.0,5,'Tomas');


-- Todas funcionaron con éxito


--14 (ERRORES)
INSERT INTO empleado (nombre, id, comision) VALUES ('Luis',6); --INSERT tiene más columnas de destino que expresiones
INSERT INTO empleado VALUES (1, 'Roberto', 5.0); --llave duplicada viola la restriccion de unicidad
INSERT INTO empleado VALUES (6, 'Ruben', 10.1); --el nuevo registro para empleado viola la restriccion check
INSERT INTO empleado VALUES (6, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 7.0); --el valor es demasiado largo para el tipo CHAR(20)

-- No se comprueba longitud de char.


--15 CAST
INSERT INTO empleado VALUES ('6','Ruben', 9.0); --cast
INSERT INTO empleado VALUES (7, 10001, 8.0); --cast
INSERT INTO empleado VALUES (8.0, 'Jesus', 6.5); --cast
INSERT INTO empleado VALUES (9, 'Esteban', 5); --cast
INSERT INTO empleado VALUES (10.8, 'Jorge', 6.4); --cast

-- No se realiza ningún casteo


--16
INSERT INTO sucursal VALUES (1, 'z.15', '01-01-2014', 'VH');
INSERT INTO sucursal VALUES (2, 'z.10', '10-01-2013', 'GEMINIS');
INSERT INTO sucursal (direccion, id, nombre) VALUES ('z.11',3,'MAJADAS');
INSERT INTO sucursal (id, nombre, direccion, abierta_en) VALUES (4,'SAN CRIS','z.7 mixco','08-07-2012');

-- Todas se realizan sin problemas


--17
INSERT INTO sucursal (id, direccion, abierta_en, nombre) VALUES (5, 'z.7 mixco', '08-07-2012' ,'SAN CRIS'); --fecha
INSERT INTO sucursal (id, direccion, abierta_en, nombre) VALUES (6, 'z.7 mixco', '45-46-2012' ,'SAN CRIS'); --valor de hora/fecha fuera de rango
INSERT INTO sucursal (id, direccion, fecha, nombre) VALUES (6, 'z.7 mixco', '2012-08-07' ,'SAN CRIS'); --no existe la columna fecha en la relacion sucursal
INSERT INTO sucursal (id, nombre, direccion, abierta_en) VALUES (6,'SAN CRIS','z.7 mixco',2012-08-07); --la columna abierta_en es de tipo DATE pero la expresion es INT


-- Todas funcionan sin problemas


--18
UPDATE empleado SET comision = 11.0; --el nuevo registro para la relacion empleado viola la restriccion check
UPDATE empleado SET comision = 5.0;
UPDATE empleado SET nombre = 'Sofia' WHERE nombre = '10001';
UPDATE empleado SET nombre = 'Juana' WHERE id = 1;
UPDATE empleado SET apellido = 'Suarez'; -- no existe la columna apellido en la relación empleado
UPDATE empleado SET nombre = 'Sin nombre' WHERE nombre_id = 1; -- no existe la columna nombre id
UPDATE empleado SET nombre = 'Maria', comision = 10.0 WHERE id = 6;
UPDATE empleado SET nombre = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'; --el valor es demasiado largo para el tipo CHAR(20)
UPDATE sucursal SET abierta_en = '21-05-2014' WHERE id = 3;
UPDATE empleado SET comision = NULL WHERE id = 1;


-- No se verifica si un valor es demasiado largo para el char


--19
DELETE FROM sucursal WHERE nombre = 'SAN CRIS';
DELETE FROM sucursal2 WHERE nombre = 'SAN CRIS'; -- no existe la relacion sucursal2
DELETE FROM sucursal WHERE nombre2 = 'SAN CRIS'; -- no existe la columna nombre2


-- Todas se realizan sin problemas


--20
SELECT id,nombre FROM sucursal; --3
SELECT id,nombre FROM empleado; --10
SELECT * FROM sucursal, empleado; --producto cartesiano 3X10 = 30
SELECT * FROM empleado WHERE comision > 5.0; --Maria 6
SELECT * FROM empleado WHERE comision = NULL; --Juana 1
UPDATE empleado SET nombre = 'Tomas' WHERE id = 6;
SELECT * FROM empleado WHERE comision < 10 AND nombre = 'Tomas'; --Tomas 5
SELECT * FROM empleado WHERE comision = 5 OR nombre = 'Tomas' ORDER BY nombre; --9
SELECT id,nombre FROM empleado ORDER BY nombre DESC;
SELECT id,nombre FROM sucursal ORDER BY id ASC;
SELECT nombre,comision FROM empleado ORDER BY comision; --null al final
SELECT id,comision FROM empleado ORDER BY comision DESC; --null al principio

-- No hay buena precedencia de operadores con el OR. Se arregla añadiendo paréntesis.
-- El null se muestra al revés de como se pide.


--21
INSERT INTO empleado_sucursal VALUES (1,1, '2014-01-01');
INSERT INTO empleado_sucursal VALUES (1,1, '2014-01-01'); --viola PK
INSERT INTO empleado_sucursal VALUES (100,1,'2014-01-01'); --viola FK
INSERT INTO empleado_sucursal VALUES (1,100,'2014-01-01'); --viola FK
UPDATE empleado_sucursal SET sucursal_id = 100 WHERE empleado_id = 1; --viola FK
UPDATE empleado_sucursal SET empleado_id = 100 WHERE sucursal_id = 1; --viola FK
DELETE FROM sucursal WHERE id = 1; -- viola FK
UPDATE empleado SET id=100 WHERE id=1; --viola FK

-- No hay llaves foráneas


--22
--INSERT MASIVO
--UPDATE MASIVO
--DELETE MASIVO
