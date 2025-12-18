
-- User ---------------------------------------------------------------------------------------------------------

-- Insert
INSERT INTO User (username, password, name, role, contact, NIC)
VALUES ('amg', 'amg123', 'Praveen Gunasekara', 'Owner', '0771194695', '200318900123');

INSERT INTO User (username, password, name, role, contact, NIC)
VALUES ('sampath', 'sampath123', 'Sampath Kumara', 'Manager', '0771234567', '200184529425');


-- Update
UPDATE User SET name='Praveen G.', contact='0779876543' WHERE user_id=1;

-- Delete
DELETE FROM User WHERE user_id=1;

-- Select All
SELECT * FROM User;

-- Select By ID
SELECT * FROM User WHERE user_id=1;



-- Employee ---------------------------------------------------------------------------------------------------------

-- Insert
INSERT INTO Employee
(emp_category, emp_name, address, contact_no, nic_no, ntc_no, driving_licence_no, join_date, emp_status, created_by)
VALUES
    ('DRIVER', 'Driver 1', 'Colombo', '0771111111', '200000100001', 'NTC101', 'DL101', '2025-01-01', 'Active', 1),
    ('DRIVER', 'Driver 2', 'Gampaha', '0771111112', '200000100002', 'NTC102', 'DL102', '2025-01-01', 'Active', 1),
    ('DRIVER', 'Driver 3', 'Galle', '0771111113', '200000100003', 'NTC103', 'DL103', '2025-01-01', 'Active', 1),
    ('DRIVER', 'Driver 4', 'Kandy', '0771111114', '200000100004', 'NTC104', 'DL104', '2025-01-01', 'Active', 1),
    ('DRIVER', 'Driver 5', 'Matara', '0771111115', '200000100005', 'NTC105', 'DL105', '2025-01-01', 'Active', 1),
    ('DRIVER', 'Driver 6', 'Kurunegala', '0771111116', '200000100006', 'NTC106', 'DL106', '2025-01-01', 'Active', 1),
    ('DRIVER', 'Driver 7', 'Negombo', '0771111117', '200000100007', 'NTC107', 'DL107', '2025-01-01', 'Active', 1),
    ('DRIVER', 'Driver 8', 'Kalutara', '0771111118', '200000100008', 'NTC108', 'DL108', '2025-01-01', 'Active', 1),
    ('DRIVER', 'Driver 9', 'Rathnapura', '0771111119', '200000100009', 'NTC109', 'DL109', '2025-01-01', 'Active', 1),
    ('DRIVER', 'Driver 10', 'Badulla', '0771111120', '200000100010', 'NTC110', 'DL110', '2025-01-01', 'Active', 1);
-- Update
UPDATE Employee SET address='Colombo 05', contact_no='0779988776' WHERE emp_id=1;

-- Delete
DELETE FROM Employee WHERE emp_id=1;

-- Select All
SELECT * FROM Employee;

-- Select By ID
SELECT * FROM Employee WHERE emp_id=1;




-- Bus ---------------------------------------------------------------------------------------------------------

-- Insert

INSERT INTO Bus (bus_brand_name, bus_number, bus_type, no_of_seats, bus_status, manufacture_date, created_by) VALUES
                                                                                                                  ('Ashok Leyland', 'NB-1001', 'Express', 45, 'Active', '2015-03-15', 1),
                                                                                                                  ('Tata', 'WP-2002', 'Semi-Luxury', 40, 'Active', '2016-07-22', 1),
                                                                                                                  ('Volvo', 'CP-3003', 'Luxury', 35, 'Active', '2018-11-10', 1),
                                                                                                                  ('Mercedes-Benz', 'SP-4004', 'AC', 50, 'Active', '2017-05-18', 1),
                                                                                                                  ('Scania', 'NP-5005', 'Express', 48, 'Maintenance', '2019-02-28', 1),
                                                                                                                  ('Isuzu', 'EP-6006', 'Non-AC', 52, 'Active', '2014-09-12', 1),
                                                                                                                  ('Hino', 'SB-7007', 'Seater', 44, 'Active', '2020-01-25', 1),
                                                                                                                  ('MAN', 'UP-8008', 'Semi-Luxury', 38, 'Inactive', '2013-06-30', 1),
                                                                                                                  ('Yutong', 'NC-9009', 'Luxury', 42, 'Active', '2021-04-17', 1);
INSERT INTO Bus (bus_brand_name, bus_number, bus_type, no_of_seats, bus_status, manufacture_date, created_by) VALUES
('Layland', 'NE6754', 'Non AC', 54, 'Available', '2015-10-02', null);

-- Update
UPDATE Bus SET bus_status='Unavailable' WHERE bus_id=1;

-- Delete
DELETE FROM Bus WHERE bus_id=1;

-- Select All
SELECT * FROM Bus;

-- Select By ID
SELECT * FROM Bus WHERE bus_id=1;



-- Trip ---------------------------------------------------------------------------------------------------------

-- Insert
INSERT INTO Trip
(trip_category, bus_id, start_location, end_location, distance, total_income, trip_date, description, created_by)
VALUES
    ('ROUTE', 1, 'Colombo', 'Galle', 116.0, 45000.00, '2025-01-01', 'Daily express trip', 1),
    ('ROUTE', 2, 'Kandy', 'Colombo', 133.0, 52000.00, '2025-01-02', 'Passenger trip', 1),
    ('ROUTE', 3, 'Matara', 'Colombo', 160.0, 60000.00, '2025-01-03', 'Evening trip', 1);

-- Update
UPDATE Trip SET total_income=4000.00 WHERE trip_id=1;

-- Delete
DELETE FROM Trip WHERE trip_id=3;

-- Select All
SELECT * FROM Trip;

-- Select By ID
SELECT * FROM Trip WHERE trip_id=1;



-- Trip Expenses---------------------------------------------------------------------------------------------------------

-- Insert
INSERT INTO Trip_Expenses (trip_id, trip_exp_type, amount, description, date, created_by)
VALUES (1, 'Food', 500.00, 'Driver meals', '2025-11-26', 1);

-- Update
UPDATE Trip_Expenses SET amount=550.00 WHERE trip_exp_id=1;

-- Delete
DELETE FROM Trip_Expenses WHERE trip_exp_id=1;

-- Select All
SELECT * FROM Trip_Expenses;

-- Select By ID
SELECT * FROM Trip_Expenses WHERE trip_exp_id=1;



-- Maintenance ---------------------------------------------------------------------------------------------------------

-- Insert
INSERT INTO Maintenance (bus_id, Maintained_by, description, total_cost, date, created_by)
VALUES (1, 'Sampath Perera', 'Engine repair', 12000.00, '2025-11-26', 1);

-- Update
UPDATE Maintenance SET total_cost=12500.00 WHERE maint_id=1;

-- Delete
DELETE FROM Maintenance WHERE maint_id=1;

-- Select All
SELECT * FROM Maintenance;

-- Select By ID
SELECT * FROM Maintenance WHERE maint_id=1;


-- Part Purchases ---------------------------------------------------------------------------------------------------------

-- Insert
INSERT INTO Part_Purchases (maint_id, part_name, quantity, unit_price, total_cost, supplier_name, part_description, created_by)
VALUES (1, 'Engine Oil', 5, 2500.00, 12500.00, 'Auto Supplier', 'Premium oil', 1);

-- Update
UPDATE Part_Purchases SET quantity=6, total_cost=15000.00 WHERE purchase_id=1;

-- Delete
DELETE FROM Part_Purchases WHERE purchase_id=1;

-- Select All
SELECT * FROM Part_Purchases;

-- Select By ID
SELECT * FROM Part_Purchases WHERE purchase_id=1;



-- Other Services ---------------------------------------------------------------------------------------------------------

-- Insert
INSERT INTO Other_Services (bus_id, service_name, cost, date, description, created_by)
VALUES (1, 'Cleaning', 500.00, '2025-11-26', 'Full bus cleaning', 1);

-- Update
UPDATE Other_Services SET cost=600.00 WHERE service_id=1;

-- Delete
DELETE FROM Other_Services WHERE service_id=1;

-- Select All
SELECT * FROM Other_Services;

-- Select By ID
SELECT * FROM Other_Services WHERE service_id=1;


-- Employee Salary ---------------------------------------------------------------------------------------------------------

-- Insert
INSERT INTO Employee_Salary (emp_id, amount, description, date, created_by)
VALUES (1, 50000.00, 'Monthly salary', '2025-11-26', 1);

-- Update
UPDATE Employee_Salary SET amount=55000.00 WHERE salary_id=1;

-- Delete
DELETE FROM Employee_Salary WHERE salary_id=1;

-- Select All
SELECT * FROM Employee_Salary;

-- Select By ID
SELECT * FROM Employee_Salary WHERE salary_id=1;


-- Update_Prices ---------------------------------------------------------------------------------------------------------

-- Insert


-- Delete
DELETE FROM Update_Prices WHERE update_prices_id = 1;

-- Select All
SELECT * FROM Update_Prices;

-- Select By ID
SELECT * FROM Update_Prices WHERE update_prices_id= 1;

-- Select By Fuel history
SELECT * FROM Update_Prices WHERE update_type = 'FUEL';

-- Select By Ticket history
SELECT * FROM Update_Prices WHERE update_type = 'TICKET';

-- /////////////////////////////////////////////////////////////////


-- ////////////////////////////////////////


-- Sample Data Insert කරමු
-- පළමුව Employee table එකට data තිබේද බලමු (already ඇති නම් skip කරන්න)
INSERT INTO Employee (emp_category, emp_name, address, contact_no, nic_no, join_date, emp_status, created_by)
VALUES
    ('DRIVER', 'Nimal Perera', 'Colombo', '0771234567', '199012345678', '2020-01-15', 'ACTIVE', 1),
    ('CONDUCTOR', 'Kamal Silva', 'Galle', '0712345678', '198523456789', '2019-05-20', 'ACTIVE', 1),
    ('HELPER', 'Sunil Fernando', 'Kandy', '0723456789', '199134567890', 'B1234567', '2021-03-10', 'ACTIVE', 1);

-- Trip table එකට sample data (already ඇති නම් skip කරන්න)
INSERT INTO Trip (trip_category, bus_id, start_location, end_location, distance, total_income, trip_date, description, created_by)
VALUES
    ('ROUTE', 1, 'Colombo', 'Kandy', 115.5, 25000.00, '2024-12-08', 'Daily route service', 1),
    ('SCHOOL_SERVICE', 1, 'Galle', 'Matara', 45.2, 15000.00, '2024-12-09', 'School transport', 1);

-- Trip-Employee associations එක්කරමු
-- Trip 1 (Colombo-Kandy) - Driver + Conductor
INSERT INTO Trip_Employee (trip_id, emp_id, role_in_trip, created_by)
VALUES
    (1, 1, 'DRIVER', 1),      -- Nimal as Driver
    (1, 2, 'CONDUCTOR', 1);   -- Kamal as Conductor

-- Trip 2 (Galle-Matara) - Driver + Conductor + Helper
INSERT INTO Trip_Employee (trip_id, emp_id, role_in_trip, created_by)
VALUES
    (2, 1, 'DRIVER', 1),      -- Nimal as Driver
    (2, 2, 'CONDUCTOR', 1),   -- Kamal as Conductor
    (2, 3, 'HELPER', 1);      -- Sunil as Helper

-- Queries to verify
SELECT * FROM Trip_Employee;

-- Trip එකක් සමග employees බලන්න
SELECT
    te.trip_emp_id,
    t.trip_id,
    t.start_location,
    t.end_location,
    e.emp_name,
    e.emp_category,
    te.role_in_trip,
    te.assigned_date
FROM Trip_Employee te
         JOIN Trip t ON te.trip_id = t.trip_id
         JOIN Employee e ON te.emp_id = e.emp_id
WHERE t.trip_id = 1;

-- Employee එකක් assign වෙච්ච trips බලන්න
SELECT
    e.emp_name,
    e.emp_category,
    t.start_location,
    t.end_location,
    t.trip_date,
    te.role_in_trip
FROM Trip_Employee te
         JOIN Trip t ON te.trip_id = t.trip_id
         JOIN Employee e ON te.emp_id = e.emp_id
WHERE e.emp_id = 1
ORDER BY t.trip_date DESC;