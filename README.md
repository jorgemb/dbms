# DBMS

## History
A simple Database management system written in Java for the Databases class in Universidad del Valle de Guatemala.
Written in collaboration with Eddy Omar Castro as final project.

The project supports most of the functions for creation, alteration and dropping of databases and tables.
Plus query operations.

Currently supports the following instructions:
- CREATE DATABASE name
- ALTER DATABASE name RENAME TO newName
- DROP DATABASE name
- SHOW DATABASES
- USE DATABASE name
- CREATE TABLE
- CREATE TABLE name ( columna Tipo [..,] CONSTRAINT C [..,])
- CONSTRAINTS of type PRIMARY KEY, CHECK (Exp)
- ALTER TABLE name RENAME TO newName
- ALTER TABLE name action
- \<action\> can be ADD COLUMN, ADD CONSTRAINT, DROP COLUMN, DROP CONSTRAINT 
- DROP TABLE name
- SHOW TABLES
- SHOW COLUMNS FROM name
- INSERT INTO name (column[..,]) VALUES (value [..,])
- UPDATE name SET column = value [..,] WHERE condition 
- DELETE FROM name WHERE condition
- SELECT * | column[..,] FROM table WHERE condition ORDER BY [field ASC | DESC [..,]]
- \<table\> can also be relation in the form of a comma delimited list of table names.

## Prerrequisites
In order to build all only Java 1.7/8 is required. All prerrequisites are included in the Lib folder.

```
ant build
ant run
```

## Graphical User Interface
The project incluse a (very) basic user interface where code can be written and tested.

![Basig GUI window][basic_gui]

Errors and other output is presented in the botton windows, using HTML for table presentation.

![Insert into table][gui_insert]

![Select from table][gui_select]


[basic_gui]: screenshots/basic_gui.png "Basic GUI"
[gui_insert]: screenshots/gui_insert.png "Basic insert"
[gui_select]: screenshots/gui_select.png "Basic select"


