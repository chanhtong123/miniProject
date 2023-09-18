CREATE DATABASE SuperDataWorker;

USE SuperDataWorker;

CREATE TABLE Customer (
    CustomerId VARCHAR(255) PRIMARY KEY,
    FirstName VARCHAR(255),
    LastName VARCHAR(255),
    Address VARCHAR(255),
    Age INT,
    Status VARCHAR(255)
);


CREATE TABLE Apartment (
	ApartmentId VARCHAR(255) PRIMARY KEY,
    Address VARCHAR(255),
    RentalPrice VARCHAR(255),
    NumberOfRoom INT    
);

CREATE TABLE Contract (
    ContractId VARCHAR(255) PRIMARY KEY,
    CustomerId VARCHAR(255),
    ApartmentId VARCHAR(255),
    StartDate DATE,
    EndDate DATE,
    FOREIGN KEY (CustomerId) REFERENCES Customer(CustomerId),
    FOREIGN KEY (ApartmentId) REFERENCES Apartment(ApartmentId)
);

INSERT INTO Customer (CustomerId, FirstName, LastName, Address, Age, Status) VALUES
('1', 'John', 'Doe', '123 Main St', 35, 'Active'),
('2', 'Alice', 'Smith', '456 Elm St', 28, 'Active'),
('3', 'Bob', 'Johnson', '789 Oak St', 40, 'Inactive');
INSERT INTO Apartment (Address, ApartmentId, RentalPrice, NumberOfRoom) VALUES
('123 Main St', 'SE1','1500.00', 2),
('456 Elm St', 'SE2', '1800.00', 3),
('789 Oak St', 'SE3' ,'1300.00', 1);

-- Hợp đồng số 1: John thuê căn hộ tại địa chỉ 123 Main St
INSERT INTO Contract (ContractId, CustomerId, ApartmentId, StartDate, EndDate) VALUES
('1', '1', 'SE1', '2023-01-01', '2023-12-31');

-- Hợp đồng số 2: Alice thuê căn hộ tại địa chỉ 456 Elm St
INSERT INTO Contract (ContractId, CustomerId, ApartmentId, StartDate, EndDate) VALUES
('2', '2', 'SE2', '2023-02-15', '2023-12-31');

-- Hợp đồng số 3: Bob thuê căn hộ tại địa chỉ 789 Oak St
INSERT INTO Contract (ContractId, CustomerId, ApartmentId, StartDate, EndDate) VALUES
('3', '3', 'SE3', '2023-03-01', '2023-06-30');


