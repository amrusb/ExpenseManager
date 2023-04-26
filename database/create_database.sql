DROP DATABASE IF EXISTS expense_manager_database;
CREATE DATABASE expense_manager_database;

USE expense_manager_database;

CREATE TABLE users (
	user_id INT NOT NULL AUTO_INCREMENT ,
    name varchar(30) NOT NULL UNIQUE,
    password varchar(30) NOT NULL,
    email VARCHAR(30) NOT NULL UNIQUE,
    PRIMARY KEY(user_id)
);

CREATE TABLE expense_categories(
	category_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    PRIMARY KEY(category_id)
);

INSERT INTO expense_categories(name) VALUES('Jedzenie');
INSERT INTO expense_categories(name) VALUES('Mieszkanie');
INSERT INTO expense_categories(name) VALUES('Inne opłaty');
INSERT INTO expense_categories(name) VALUES('Zdrowie');
INSERT INTO expense_categories(name) VALUES('Ubrania');
INSERT INTO expense_categories(name) VALUES('Relaks');
INSERT INTO expense_categories(name) VALUES('Transport');
INSERT INTO expense_categories(name) VALUES('Inne');

CREATE TABLE expenses(
	expense_id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(30),
    category_id INT NOT NULL,
    amount DECIMAL(5,2) NOT NULL,
    expense_date DATE DEFAULT (CURRENT_DATE), 
    user_id INT NOT NULL,
    PRIMARY KEY(expense_id),
    FOREIGN KEY(category_id) REFERENCES expense_categories(category_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id)
);

DROP USER IF EXISTS 'databaseUser'@'localhost';
CREATE USER 'databaseUser'@'localhost' IDENTIFIED BY '!HuTh%4DtYfTraA';
GRANT ALL PRIVILEGES ON expense_manager_database.users TO 'databaseUser'@'localhost';
REVOKE DROP ON expense_manager_database.users FROM 'databaseUser'@'localhost';
GRANT ALL PRIVILEGES ON expense_manager_database.expenses TO 'databaseUser'@'localhost';
REVOKE DROP ON expense_manager_database.expenses FROM 'databaseUser'@'localhost';
GRANT SELECT ON expense_manager_database.expense_categories TO 'databaseUser'@'localhost';

ALTER TABLE expenses CHANGE COLUMN `amount` `amount` DECIMAL(10,2) NOT NULL ;