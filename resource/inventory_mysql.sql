CREATE DATABASE inventory;

GRANT ALL PRIVILEGES ON inventory.* TO stock@localhost IDENTIFIED BY 'check';
GRANT ALL PRIVILEGES ON inventory.* TO stock@'%' IDENTIFIED BY 'check';

USE inventory;

DROP TABLE IF EXISTS Categories;
CREATE TABLE Categories(CategoryID int, CategoryName TINYTEXT);

INSERT INTO Categories VALUES(1,"Beverages");
INSERT INTO Categories VALUES(2,"Condiments");
INSERT INTO Categories VALUES(3,"Confections");
INSERT INTO Categories VALUES(4,"Dairy");
INSERT INTO Categories VALUES(5,"Grains/Cereals");
INSERT INTO Categories VALUES(6,"Meat/Poultry");
INSERT INTO Categories VALUES(7,"Produce");
INSERT INTO Categories VALUES(8,"Seafood");

DROP TABLE IF EXISTS Inventory;
CREATE TABLE Inventory(ProductID int, ProductName TINYTEXT, ProductDescription TINYTEXT, CategoryID int, Price float, ReorderLevel int, Discontinued tinyint, LeadTime TINYTEXT, Quantity int);

INSERT INTO Inventory VALUES (2,"Tibetan Barley Beer","Unique flavor",1,4.0,10,0,"30 Days",100);
INSERT INTO Inventory VALUES (5,"Chef Anton's Gumbo Mix","Gumbo mix",8,55.0,10,0,"10 Days",3);
INSERT INTO Inventory VALUES (15,"Blue potato chips","nice texture",5,12.0,5,0,"5 Days",7);
INSERT INTO Inventory VALUES (17,"Wax lips","tasty treat",3,2.0,10,1,"10 Days",0);
INSERT INTO Inventory VALUES (19,"Pita pieces","",5,15.0,20,0,"1 Day",4);
INSERT INTO Inventory VALUES (20,"Tomato Jelly","yummy",2,1.0,100,0," 2 Days",20);
INSERT INTO Inventory VALUES (21,"Fishheads","",8,12.0,50,0,"10 Days",0);
INSERT INTO Inventory VALUES (24,"Jam Juice","",1,3.0,10,0,"1 Day",5);

DROP TABLE IF EXISTS ProductSuppliers;
CREATE TABLE ProductSuppliers (SupplierID int, ProductID int, Price float);

INSERT INTO ProductSuppliers VALUES (1,2,5.75);
INSERT INTO ProductSuppliers VALUES (4,2,7.0);
INSERT INTO ProductSuppliers VALUES (3,5,2.25);
INSERT INTO ProductSuppliers VALUES (1,15,7.75);
INSERT INTO ProductSuppliers VALUES (2,21,25.0);
INSERT INTO ProductSuppliers VALUES (4,17,1.0);
INSERT INTO ProductSuppliers VALUES (3,15,5.0);
INSERT INTO ProductSuppliers VALUES (5,19,5.0);
INSERT INTO ProductSuppliers VALUES (4,21,12.0);
INSERT INTO ProductSuppliers VALUES (1,24,3.0);
INSERT INTO ProductSuppliers VALUES (2,24,4.0);

DROP TABLE IF EXISTS Suppliers;
CREATE TABLE Suppliers (SupplierID int, SupplierName TINYTEXT, ContactName TINYTEXT,ContactTitle TINYTEXT, Address TINYTEXT, City TINYTEXT, PostalCode TINYTEXT, StateOrProvince TINYTEXT, Country TINYTEXT, PhoneNumber TINYTEXT, FaxNumber TINYTEXT, PaymentTerms TINYTEXT, EmailAddress TINYTEXT, Notes TINYTEXT);

INSERT INTO Suppliers VALUES (1,"Exotic Liquids","Charlotte Cooper","Purchasing Manager","49 Gilbert St.","London","EC1 4SD","","UK","( 71) 555-2222","","Net 30 days","","");
INSERT INTO Suppliers VALUES (2,"New Orleans Cajun Delights","Shelley Burke","Order Administrator","P.O. Box 78934","New Orleans","70117-","","USA","(100) 555-4822","","Net 60 days","","");
INSERT INTO Suppliers VALUES (3,"Grandma Kelly's Homestead","Regina Murphy","Sales Representative","707 Oxford Rd.","Ann Arbor","48104-","","USA","(313) 555-5735","(313) 555-3349","Net 30 days","","");
INSERT INTO Suppliers VALUES (4,"Tokyo Traders","Yoshi Nagase","Marketing Manager","9-8 Sekimai Musashino-shi","Tokyo","100  -","","Japan","(03) 3555-5011","","Net 30 days","","");
INSERT INTO Suppliers VALUES (5,"Cooperativa de Quesos 'Las Cabras'","Antonio del Valle Saavedra ","Export Administrator","Calle del Rosal 4","Oviedo","33007-","","Spain","(98) 598 76 54","","Net 30 days","","");

DROP TABLE IF EXISTS Employee;
CREATE TABLE Employee (EmployeeNum int, EmployeeName varchar(25), UserName varchar(20), Pass varchar(20));

INSERT INTO Employee VALUES (0, 'Admin', 'admin', 'pass');

DROP TABLE IF EXISTS Timesheet;
CREATE TABLE Timesheet (EmployeeNum int, EndWeek DATE, WeekNumber int);

DROP TABLE IF EXISTS TimesheetRow;
CREATE TABLE TimesheetRow (EndWeek DATE, ProjectID int, WorkPackage varchar(10), SAT decimal, SUN decimal, MON decimal, TUE decimal, WED decimal, THU decimal, FRI decimal, Notes TINYTEXT, EmployeeNum int);