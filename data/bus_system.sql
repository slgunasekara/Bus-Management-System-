CREATE DATABASE bus_system;

DROP DATABASE bus_system;

SHOW DATABASES;



-- Bus Management System Database Schema (Clean Rescheduled Version)

CREATE TABLE User (
                      user_id INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(50) NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      name VARCHAR(100) NOT NULL,
                      role VARCHAR(50), -- owner , admin
                      contact VARCHAR(20),
                      NIC VARCHAR(20),
                      email VARCHAR(100) NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO User (username, password, name, role, contact, NIC, email)
VALUES ('amg', 'amg123', 'Praveen Gunasekara', 'Owner', '0771194695', '200318900123', 'praveengunasekara7@gmail.com');

INSERT INTO User (username, password, name, role, contact, NIC, email)
VALUES ('sampath', 'sampath123', 'Sampath Kumara', 'Manager', '0771234567', '200184529425', 'mendisdanushka886@gmail.com');



CREATE TABLE Update_Prices (
                               update_prices_id INT AUTO_INCREMENT PRIMARY KEY,
                               update_type VARCHAR(50) NOT NULL,          -- FUEL / TICKET
                               change_type VARCHAR(20) NOT NULL,        -- INCREMENT / DECREMENT
                               previous_value DECIMAL(10,2),            -- old value
                               new_value DECIMAL(10,2) NOT NULL,        -- new value
                               change_amount DECIMAL(10,2),             -- increment/decrement amount
                               percentage_change DECIMAL(10,2),         -- % change
                               change_date DATE NOT NULL,               -- date ekath NOT NULL
                               description TEXT,
                               created_by INT NOT NULL,                 -- only user FK
                               FOREIGN KEY (created_by) REFERENCES User(user_id)
);

INSERT INTO Update_Prices (update_type, change_type, previous_value, new_value, change_amount, percentage_change, change_date, description, created_by) VALUES
( 'FUEL','INCREMENT',420.00, 450.00,30.00,7.14,'2025-01-10','Fuel price increased by government',1),
('TICKET','DECREMENT',100.00,90.00,10.00,10.00,'2025-01-10','Discount for holiday season',1);



# CREATE TABLE Employee_Category (
#                                    emp_cat_id INT AUTO_INCREMENT PRIMARY KEY,
#                                    emp_cat_name VARCHAR(100) NOT NULL,
#                                    description TEXT
# );

CREATE TABLE Employee (
                          emp_id INT AUTO_INCREMENT PRIMARY KEY,
#                           emp_cat_id INT,
                          emp_category VARCHAR(100),
                          emp_name VARCHAR(100) NOT NULL,
                          address VARCHAR(200) NOT NULL,
                          contact_no VARCHAR(20) NOT NULL,
                          nic_no VARCHAR(20) NOT NULL,
                          ntc_no VARCHAR(20),
                          driving_licence_no VARCHAR(20),
                          join_date DATE,
                          exit_date DATE,
                          emp_status VARCHAR(20),
                          created_by INT,
#                           FOREIGN KEY (emp_cat_id) REFERENCES Employee_Category(emp_cat_id),
                          FOREIGN KEY (created_by) REFERENCES User(user_id)
);

INSERT INTO Employee (emp_category, emp_name, address, contact_no, nic_no, ntc_no, driving_licence_no, join_date, exit_date, emp_status, created_by) VALUES
('DRIVER','Sunil Perera','Colombo','0771234567','901234567V','NTC12345','B1234567','2022-05-10',NULL,'ACTIVE',1),
('DRIVER','Nimal Fernando','Kandy','0723456789','901987654V','NTC23456','B2345678','2021-09-15',NULL,'ACTIVE',1),
('DRIVER','Mahesh Silva','Kurunegala','0713456789','925678901V','NTC45678','B3456789','2021-07-30',NULL,'ACTIVE',1),
('DRIVER','Chamara Senanayake','Anuradhapura','0714567890','927890123V','NTC56789','B4567890','2023-02-14',NULL,'ACTIVE',1);

# CREATE TABLE Trip_Category (
#                                trip_cat_id INT AUTO_INCREMENT PRIMARY KEY,
#                                trip_cat_name VARCHAR(100),
#                                description TEXT
# );

# CREATE TABLE Bus (
#                      bus_id INT AUTO_INCREMENT PRIMARY KEY,
#                      bus_brand_name VARCHAR(100),
#                      bus_number VARCHAR(20),
#                      bus_type VARCHAR(50),
#                      no_of_seats INT,
#                      bus_status VARCHAR(20),
#                      manufacture_date DATE,
#                      insurance_Expiry_Date DATE,
#                      license_Renewal_Date DATE,
#                      current_Mileage INT,
#                      created_by INT,
#                      FOREIGN KEY (created_by) REFERENCES User(user_id)
# );

CREATE TABLE Bus (
                     bus_id INT AUTO_INCREMENT PRIMARY KEY,
                     bus_brand_name VARCHAR(100),
                     bus_number VARCHAR(20) UNIQUE,
                     bus_type VARCHAR(50),
                     no_of_seats INT,
                     bus_status VARCHAR(20) DEFAULT 'Active',
                     manufacture_date DATE,
                     insurance_Expiry_Date DATE,
                     license_Renewal_Date DATE,
                     current_Mileage INT DEFAULT 0,
                     created_by INT,
                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                     FOREIGN KEY (created_by) REFERENCES User(user_id)
);



CREATE TABLE Trip (
                      trip_id INT AUTO_INCREMENT PRIMARY KEY,
#                       trip_cat_id INT,
                      trip_category VARCHAR(100),
                      bus_id INT,
                      start_location VARCHAR(100) NOT NULL,
                      end_location VARCHAR(100) NOT NULL,
                      distance DECIMAL(10,2),
                      total_income DECIMAL(10,2) NOT NULL,
                      trip_date DATE NOT NULL,
                      description TEXT,
                      created_by INT,
                      FOREIGN KEY (bus_id) REFERENCES Bus(bus_id),
#                       FOREIGN KEY (trip_cat_id) REFERENCES Trip_Category(trip_cat_id),
                      FOREIGN KEY (created_by) REFERENCES User(user_id)
);



# CREATE TABLE Fuel_Expenses (
#                                fuel_exp_id INT AUTO_INCREMENT PRIMARY KEY,
#                                bus_id INT,
#                                fuel_type VARCHAR(50),
#                                quantity DECIMAL(10,2),
#                                unit_price DECIMAL(10,2),
#                                total_cost DECIMAL(10,2) NOT NULL,
#                                created_by INT,
#                                FOREIGN KEY (created_by) REFERENCES User(user_id)
# );
# -- FOREIGN KEY (bus_id) REFERENCES Bus(bus_id), -- CASE

CREATE TABLE Employee_Salary (
                                 salary_id INT AUTO_INCREMENT PRIMARY KEY,
                                 emp_id INT,
                                 trip_id INT,
                                 amount DECIMAL(10,2),
                                 description TEXT,
                                 date DATE,
                                 created_by INT,
                                 FOREIGN KEY (emp_id) REFERENCES Employee(emp_id),
                                 FOREIGN KEY (trip_id) REFERENCES Trip(trip_id),
                                 FOREIGN KEY (created_by) REFERENCES User(user_id)
);



CREATE TABLE Trip_Expenses (
                               trip_exp_id INT AUTO_INCREMENT PRIMARY KEY,
                               trip_id INT,
                               trip_exp_type VARCHAR(100),
                               amount DECIMAL(10,2),
                               description TEXT,
                               date DATE,
                               created_by INT,
#                                fuel_exp_id INT, -- case fuel  (fuel expences type yatathema danna puluwan neda)
#                                Foreign Key (fuel_exp_id) REFERENCES Fuel_Expenses(fuel_exp_id),
                               FOREIGN KEY (trip_id) REFERENCES Trip(trip_id),
                               FOREIGN KEY (created_by) REFERENCES User(user_id)
);




# CREATE TABLE Maintenance_Category (
#                                       maint_cat_id INT AUTO_INCREMENT PRIMARY KEY,
#                                       maint_cat_name VARCHAR(100),
#                                       description TEXT
# );

CREATE TABLE Maintenance (
                             maint_id INT AUTO_INCREMENT PRIMARY KEY,
#                              maint_cat_id INT,
                             bus_id INT,
                             maintenance_type VARCHAR(100),
                             date DATE,
                             Mileage INT,
                             cost DECIMAL(10,2),
                             Maintained_by VARCHAR(100),
                             description TEXT,
                             created_by INT,
                             FOREIGN KEY (bus_id) REFERENCES Bus(bus_id),
#                              FOREIGN KEY (maint_cat_id) REFERENCES Maintenance_Category(maint_cat_id),
                             FOREIGN KEY (created_by) REFERENCES User(user_id)
);



# INSERT INTO Maintenance (bus_id, maintenance_type, date, Mileage, cost, Maintained_by, description, created_by)
# VALUES (1, 'Oil Change', '2021-01-01', 200000, 20000, 'yamuna', 'Oil change', 1);


CREATE TABLE Part_Purchases (
                                purchase_id INT AUTO_INCREMENT PRIMARY KEY,
                                bus_id INT NOT NULL,                      -- Bus ID is now required
                                maint_id INT NULL,                        -- Maintenance ID is optional
                                part_name VARCHAR(100) NOT NULL,
                                quantity INT NOT NULL,
                                unit_price DECIMAL(10,2) NOT NULL,
                                total_cost DECIMAL(10,2) NOT NULL,
                                supplier_name VARCHAR(100) NOT NULL,
                                part_description TEXT,
                                date DATE NOT NULL,
                                created_by INT NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (bus_id) REFERENCES Bus(bus_id) ON DELETE RESTRICT,
                                FOREIGN KEY (maint_id) REFERENCES Maintenance(maint_id) ON DELETE SET NULL,
                                FOREIGN KEY (created_by) REFERENCES User(user_id)
);



# -- උදාහරණ දත්ත:
#
# -- 1. Maintenance එකක් සමඟ part purchase එකක්
# INSERT INTO Part_Purchases (maint_id, part_name, quantity, unit_price, total_cost, supplier_name, part_description, created_by)
# VALUES (1, 'Engine Oil Filter', 2, 500.00, 1000.00, 'ABC Motors', 'Oil change සඳහා filter', 1);
#
# -- 2. Maintenance නැතිව වෙනම part purchase එකක් (stock එකට)
# INSERT INTO Part_Purchases (maint_id, part_name, quantity, unit_price, total_cost, supplier_name, part_description, created_by)
# VALUES (NULL, 'Brake Pads', 10, 2500.00, 25000.00, 'XYZ Spares', 'Stock එකට brake pads', 1);




CREATE TABLE Other_Services (
                                service_id INT AUTO_INCREMENT PRIMARY KEY,
                                bus_id INT NULL,
                                trip_id INT NULL,
                                service_name VARCHAR(100) NOT NULL,
                                cost DECIMAL(10,2) NOT NULL,
                                date DATE NOT NULL,
                                description TEXT,
                                created_by INT NOT NULL,
                                FOREIGN KEY (bus_id) REFERENCES Bus(bus_id) ON DELETE SET NULL,
                                FOREIGN KEY (trip_id) REFERENCES Trip(trip_id) ON DELETE SET NULL,
                                FOREIGN KEY (created_by) REFERENCES User(user_id)
);



CREATE TABLE Trip_Employee (
                               trip_emp_id INT AUTO_INCREMENT PRIMARY KEY,
                               trip_id INT NOT NULL,
                               emp_id INT NOT NULL,
                               role_in_trip VARCHAR(50), -- DRIVER, CONDUCTOR, HELPER
                               assigned_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               created_by INT,
                               FOREIGN KEY (trip_id) REFERENCES Trip(trip_id) ON DELETE CASCADE,
                               FOREIGN KEY (emp_id) REFERENCES Employee(emp_id) ON DELETE CASCADE,
                               FOREIGN KEY (created_by) REFERENCES User(user_id),
                               UNIQUE KEY unique_trip_emp_role (trip_id, emp_id, role_in_trip)  -- For one trip, one employee can be assigned to only one role.
);




SELECT * FROM Bus;



SHOW TABLES;

SELECT * FROM Bus;
SELECT * FROM User;

