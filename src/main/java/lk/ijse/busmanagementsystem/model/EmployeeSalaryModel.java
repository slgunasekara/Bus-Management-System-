package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.dto.EmployeeSalaryDTO;
import lk.ijse.busmanagementsystem.dto.EmployeeSalaryTM;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSalaryModel {

    //Get all employee salaries with employee names and creator usernames
    public List<EmployeeSalaryTM> getAllEmployeeSalaries() throws SQLException, ClassNotFoundException {
        String sql = "SELECT es.salary_id, es.emp_id, e.emp_name, es.trip_id, " +
                "es.amount, es.description, es.date, u.username " +
                "FROM Employee_Salary es " +
                "INNER JOIN Employee e ON es.emp_id = e.emp_id " +
                "LEFT JOIN User u ON es.created_by = u.user_id " +
                "ORDER BY es.salary_id DESC";

        ResultSet rst = CrudUtil.execute(sql);
        List<EmployeeSalaryTM> salaryList = new ArrayList<>();

        while (rst.next()) {
            try {
                // Handle nullable trip_id
                Integer tripId = rst.getObject("trip_id") != null ?
                        rst.getInt("trip_id") : null;

                EmployeeSalaryTM tm = new EmployeeSalaryTM(
                        rst.getInt("salary_id"),
                        rst.getInt("emp_id"),
                        rst.getString("emp_name"),
                        tripId,
                        rst.getDouble("amount"),
                        rst.getString("description"),
                        rst.getDate("date").toLocalDate(),
                        rst.getString("username")
                );
                salaryList.add(tm);
            } catch (Exception e) {
                System.err.println("❌ Error loading salary record ID: " +
                        rst.getInt("salary_id") + " - " + e.getMessage());
                e.printStackTrace();
            }
        }
        return salaryList;
    }

    //Save employee salary
    public boolean saveEmployeeSalary(EmployeeSalaryDTO dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Employee_Salary(emp_id, trip_id, amount, " +
                "description, date, created_by) VALUES (?, ?, ?, ?, ?, ?)";

        return CrudUtil.execute(sql,
                dto.getEmpId(),
                dto.getTripId(),
                dto.getAmount(),
                dto.getDescription(),
                java.sql.Date.valueOf(dto.getDate()),
                dto.getCreatedBy()
        );
    }

    //Update employee salary
    public boolean updateEmployeeSalary(EmployeeSalaryDTO dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Employee_Salary SET emp_id=?, trip_id=?, amount=?, " +
                "description=?, date=? WHERE salary_id=?";

        return CrudUtil.execute(sql,
                dto.getEmpId(),
                dto.getTripId(),
                dto.getAmount(),
                dto.getDescription(),
                java.sql.Date.valueOf(dto.getDate()),
                dto.getSalaryId()
        );
    }

    //Delete employee salary
    public boolean deleteEmployeeSalary(String salaryId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Employee_Salary WHERE salary_id=?", salaryId);
    }

    //Check if employee exists
    public boolean isEmployeeExists(int empId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT emp_id FROM Employee WHERE emp_id=?", empId);
        return rst.next();
    }

    //Check if trip exists
    public boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip WHERE trip_id=?", tripId);
        return rst.next();
    }

    //Get all active employee IDs
    public List<Integer> getAllActiveEmployeeIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT emp_id FROM Employee WHERE emp_status='ACTIVE' ORDER BY emp_id DESC"
        );
        List<Integer> empIds = new ArrayList<>();

        while (rst.next()) {
            empIds.add(rst.getInt("emp_id"));
        }
        return empIds;
    }

    //Get all trip IDs
    public List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip ORDER BY trip_id DESC");
        List<Integer> tripIds = new ArrayList<>();

        while (rst.next()) {
            tripIds.add(rst.getInt("trip_id"));
        }
        return tripIds;
    }

    //Search employee salaries
    public List<EmployeeSalaryTM> searchEmployeeSalaries(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT es.salary_id, es.emp_id, e.emp_name, es.trip_id, " +
                "es.amount, es.description, es.date, u.username " +
                "FROM Employee_Salary es " +
                "INNER JOIN Employee e ON es.emp_id = e.emp_id " +
                "LEFT JOIN User u ON es.created_by = u.user_id " +
                "WHERE es.salary_id LIKE ? OR " +
                "es.emp_id LIKE ? OR " +
                "e.emp_name LIKE ? OR " +
                "es.trip_id LIKE ? OR " +
                "es.description LIKE ? OR " +
                "u.username LIKE ? " +
                "ORDER BY es.salary_id DESC";

        String searchPattern = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(sql, searchPattern, searchPattern,
                searchPattern, searchPattern, searchPattern, searchPattern);

        List<EmployeeSalaryTM> salaryList = new ArrayList<>();

        while (rst.next()) {
            try {
                Integer tripId = rst.getObject("trip_id") != null ?
                        rst.getInt("trip_id") : null;

                EmployeeSalaryTM tm = new EmployeeSalaryTM(
                        rst.getInt("salary_id"),
                        rst.getInt("emp_id"),
                        rst.getString("emp_name"),
                        tripId,
                        rst.getDouble("amount"),
                        rst.getString("description"),
                        rst.getDate("date").toLocalDate(),
                        rst.getString("username")
                );
                salaryList.add(tm);
            } catch (Exception e) {
                System.err.println("❌ Error during search: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return salaryList;
    }

    //Get employee details by ID
    public String getEmployeeDetails(int empId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT emp_name, emp_category, contact_no FROM Employee WHERE emp_id=?";
        ResultSet rst = CrudUtil.execute(sql, empId);

        if (rst.next()) {
            return rst.getString("emp_name") + " | " +
                    rst.getString("emp_category") + " | " +
                    rst.getString("contact_no");
        }
        return "Employee not found";
    }

    //Get trip details by ID
    public String getTripDetails(int tripId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT start_location, end_location, trip_date FROM Trip WHERE trip_id=?";
        ResultSet rst = CrudUtil.execute(sql, tripId);

        if (rst.next()) {
            return rst.getString("start_location") + " → " +
                    rst.getString("end_location") + " (" +
                    rst.getDate("trip_date") + ")";
        }
        return "Trip not found";
    }
}