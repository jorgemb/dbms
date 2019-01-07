--1: Check database creation
CREATE DATABASE project;
CREATE DATABASE project_tst;
CREATE DATABASE project_delete;
SHOW DATABASES;

--2: Database rename
ALTER DATABASE project_tst RENAME TO project_test;
SHOW DATABASES;

--3: Database drop
DROP DATABASE project_delete;
SHOW DATABASES

--4: Table creation
USE DATABASE project;
CREATE TABLE invoice (id INT, name CHAR(20), nit CHAR(10), total FLOAT, bought_on DATE);
CREATE TABLE user (id INT, name CHAR(20), comission FLOAT CONSTRAINT ch_com CHECK (comission <= 10.0));
CREATE TABLE office (id INT, address CHAR(40), opened_on DATE CONSTRAINT pk_office PRIMARY KEY (id));
CREATE TABLE country (id INT, country CHAR(20));
SHOW TABLES;
-- TODO: Add char restriction

--5: Table rename
USE DATABASE project;
ALTER TABLE user RENAME TO employee;
SHOW TABLES;
-- TODO: Message shows EMPLOYEE instead of employee

--6: Add constraints
USE DATABASE project;
ALTER TABLE office ADD COLUMN name CHAR(10);
ALTER TABLE invoice ADD CONSTRAINT pk_invoice PRIMARY KEY (id);
ALTER TABLE employee ADD CONSTRAINT pk_employee PRIMARY KEY (id);
ALTER TABLE employee ADD COLUMN code INT;
ALTER TABLE employee ADD CONSTRAINT ch_code CHECK (0 < code AND code < 1000);
SHOW TABLES;
-- TODO: Add foreign key constraints

--7: Drop constraints
USE DATABASE project;
ALTER TABLE employee DROP CONSTRAINT ch_code;
ALTER TABLE employee DROP COLUMN code;
SHOW TABLES;

--8: Table drop
USE DATABASE project;
DROP TABLE country;
SHOW TABLES;

--9: Complex creation
--USE DATABASE project;
--CREATE TABLE empleado_sucursal (empleado_id INT, sucursal_id INT, asignado_en DATE CONSTRAINT FK_empleado FOREIGN KEY ( empleado_id ) REFERENCES employee (id), FK_sucursal FOREIGN KEY ( sucursal_id ) REFERENCES office ( id ), PK_us PRIMARY KEY (empleado_id, sucursal_id))
--SHOW TABLES
--SHOW COLUMNS FROM empleado_sucursal
--TODO: Not working foreign keys

--10 (Database and table errors)
USE DATABASE project;
ALTER DATABASE project_tst RENAME TO new_database; -- Databse project_tst does not exist
DROP DATABASE project_success; --Database project_success does not exist
DROP DATABASE project_delete; --Database project_delete does not exist
ALTER TABLE unexistent_table ADD COLUMN name CHAR(10); -- Table does not exist
ALTER TABLE country ADD COLUMN name CHAR(10); -- Table does not exist
DROP TABLE unexistent_table; -- Table does not exist
DROP TABLE country; -- Table does not exist

--11 (table errors)
DROP TABLE office; --Foreign key violation (TODO: Foreign keys not working)
CREATE TABLE user (id INT, name CHAR(20), comission xxx); -- xxx type does not exist
CREATE TABLE country (id INT, country CHAR(20), CONSTRAINT pk_office PRIMARY KEY (id_country)); --Column id_country does not exist
ALTER TABLE employee ADD CONSTRAINT ch_code CHECK (0 < code AND code < 1000); --Column code does not exist

--12 (Table errors with foreign keys)
-- ALTER TABLE empleado_sucursal ADD CONSTRAINT fk_empleado FOREIGN KEY (empleado_id) REFERENCES employee (id); --la restricción ya existe
-- ALTER TABLE empleado_sucursal ADD CONSTRAINT fk_empleado2 FOREIGN KEY (asignado_en) REFERENCES employee (id); --la restricción no puede ser implementada por tipos incompatibles (DATE e INT)
-- ALTER TABLE empleado_sucursal ADD CONSTRAINT fk_empleado3 FOREIGN KEY (empleado_id) REFERENCES invoice (comission); --no existe la columna comisión referida
-- ALTER TABLE empleado_sucursal ADD CONSTRAINT fk_empleado4 FOREIGN KEY (empleado_id) REFERENCES employee (comission); --no hay restriccion única que coincida con las columnas
-- ALTER TABLE employee ADD CONSTRAINT pk_empleado2 PRIMARY KEY (id); --no se permiten múltiples llaves primarias para la tabla employee

--13: Inserts
USE DATABASE project;
INSERT INTO employee VALUES (1,'Juan',5.0);
INSERT INTO employee VALUES (2,'Pedro',NULL);
INSERT INTO employee (id, name) VALUES (3, 'Julio');
INSERT INTO employee (name, id) VALUES ('Jose',4);
INSERT INTO employee (comission, id, name) VALUES (6.0,5,'Tomas');

--14: Value errors
USE DATABASE project;
INSERT INTO employee (name, id, comission) VALUES ('Luis',6); --INSERT has more columns than excepted
INSERT INTO employee VALUES (1, 'Roberto', 5.0); -- Duplicated keys
INSERT INTO employee VALUES (6, 'Ruben', 10.1); -- Check constraint violation
INSERT INTO employee VALUES (6, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 7.0); -- Char constraint violation

--15: CAST
USE DATABASE project;
INSERT INTO employee VALUES ('6','Ruben', 9.0); --cast
INSERT INTO employee VALUES (7, 10001, 8.0); --cast
INSERT INTO employee VALUES (8.0, 'Jesus', 6.5); --cast
INSERT INTO employee VALUES (9, 'Esteban', 5); --cast
INSERT INTO employee VALUES (10.8, 'Jorge', 6.4); --cast

-- TODO: Casts not working properly

--16: Inserts
USE DATABASE project;
INSERT INTO office VALUES (1, 'z.15', '01-01-2014', 'VH');
INSERT INTO office VALUES (2, 'z.10', '10-01-2013', 'GEMINIS');
INSERT INTO office (address, id, name) VALUES ('z.11',3,'MAJADAS');
INSERT INTO office (id, name, address, opened_on) VALUES (4,'SAN CRIS','z.7 mixco','08-07-2012');

--17: Insert errors
USE DATABASE project;
INSERT INTO office (id, address, opened_on, name) VALUES (5, 'z.7 mixco', '08-07-2012' ,'SAN CRIS'); -- Date error
INSERT INTO office (id, address, opened_on, name) VALUES (6, 'z.7 mixco', '45-46-2012' ,'SAN CRIS'); -- Date error
INSERT INTO office (id, address, last_date, name) VALUES (6, 'z.7 mixco', '2012-08-07' ,'SAN CRIS'); --No column last_date
INSERT INTO office (id, name, address, opened_on) VALUES (6,'SAN CRIS','z.7 mixco',2012-08-07); --Wrong value

--18: Update 
USE DATABASE project;
UPDATE employee SET comission = 11.0; -- New register violates constraint
UPDATE employee SET comission = 5.0;
UPDATE employee SET name = 'Sofia' WHERE name = '10001';
UPDATE employee SET name = 'Juana' WHERE id = 1;
UPDATE employee SET lastname = 'Suarez'; -- No column lastname
UPDATE employee SET name = 'Sin name' WHERE name_id = 1; -- No column name_id
UPDATE employee SET name = 'Maria', comission = 10.0 WHERE id = 6;
UPDATE employee SET name = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'; -- Value too long for char
UPDATE office SET opened_on = '21-05-2014' WHERE id = 3;
UPDATE employee SET comission = NULL WHERE id = 1;

--19: Delete
DELETE FROM office WHERE name = 'SAN CRIS';
DELETE FROM sucursal2 WHERE name = 'SAN CRIS'; -- No table sucursal2
DELETE FROM office WHERE nombre2 = 'SAN CRIS'; -- No field nombre2

--20: Select
SELECT id,name FROM office; --3
SELECT id,name FROM employee; --10
SELECT * FROM office, employee; --3X10 = 30
SELECT * FROM employee WHERE comission > 5.0; --Maria 6
SELECT * FROM employee WHERE comission = NULL; --Juana 1
UPDATE employee SET name = 'Tomas' WHERE id = 6;
SELECT * FROM employee WHERE comission < 10 AND name = 'Tomas'; --Tomas 5
SELECT * FROM employee WHERE comission = 5 OR name = 'Tomas' ORDER BY name; --9
SELECT id,name FROM employee ORDER BY name DESC;
SELECT id,name FROM office ORDER BY id ASC;
SELECT name,comission FROM employee ORDER BY comission;
SELECT id,comission FROM employee ORDER BY comission DESC; 

--21: Insert errors
INSERT INTO empleado_sucursal VALUES (1,1, '2014-01-01');
INSERT INTO empleado_sucursal VALUES (1,1, '2014-01-01'); --PK violation
INSERT INTO empleado_sucursal VALUES (100,1,'2014-01-01'); --FK violation
INSERT INTO empleado_sucursal VALUES (1,100,'2014-01-01'); --FK violation
UPDATE empleado_sucursal SET sucursal_id = 100 WHERE empleado_id = 1; --FK violation
UPDATE empleado_sucursal SET empleado_id = 100 WHERE sucursal_id = 1; --FK violation
DELETE FROM office WHERE id = 1; -- FK violation
UPDATE employee SET id=100 WHERE id=1; --FK violation
-- TODO: No foreign keys
