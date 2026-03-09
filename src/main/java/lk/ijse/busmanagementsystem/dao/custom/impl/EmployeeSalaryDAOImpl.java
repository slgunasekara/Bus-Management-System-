package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.EmployeeSalaryDAO;
import lk.ijse.busmanagementsystem.entity.EmployeeSalary;
import lk.ijse.busmanagementsystem.tm.EmployeeSalaryTM;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSalaryDAOImpl implements EmployeeSalaryDAO {

    private static final String TM_SQL =
            "SELECT es.salary_id, es.emp_id, e.emp_name, es.trip_id, " +
                    "es.amount, es.description, es.date, u.username " +
                    "FROM Employee_Salary es " +
                    "INNER JOIN Employee e ON es.emp_id = e.emp_id " +
                    "LEFT JOIN User u ON es.created_by = u.user_id ";

    @Override
    public List<EmployeeSalary> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Employee_Salary ORDER BY salary_id DESC");
        List<EmployeeSalary> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public List<EmployeeSalaryTM> getAllTM() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(TM_SQL + "ORDER BY es.salary_id DESC");
        return mapTMResultSet(rst);
    }

    @Override
    public boolean save(EmployeeSalary es) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Employee_Salary(emp_id, trip_id, amount, description, date, created_by) VALUES (?, ?, ?, ?, ?, ?)",
                es.getEmpId(), es.getTripId(), es.getAmount(),
                es.getDescription(), java.sql.Date.valueOf(es.getDate()), es.getCreatedBy()
        );
    }

    @Override
    public boolean update(EmployeeSalary es) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Employee_Salary SET emp_id=?, trip_id=?, amount=?, description=?, date=? WHERE salary_id=?",
                es.getEmpId(), es.getTripId(), es.getAmount(),
                es.getDescription(), java.sql.Date.valueOf(es.getDate()), es.getSalaryId()
        );
    }

    @Override
    public boolean delete(String salaryId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Employee_Salary WHERE salary_id=?", salaryId);
    }

    @Override
    public boolean delete(int salaryId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Employee_Salary WHERE salary_id=?", salaryId);
    }

    @Override
    public boolean exists(String salaryId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT salary_id FROM Employee_Salary WHERE salary_id=?", salaryId);
        return rst.next();
    }

    @Override
    public boolean exists(int salaryId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT salary_id FROM Employee_Salary WHERE salary_id=?", salaryId);
        return rst.next();
    }

    @Override
    public EmployeeSalary search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Employee_Salary WHERE salary_id=?", id);
        if (rst.next()) return mapResultSet(rst);
        return null;
    }

    @Override
    public List<EmployeeSalaryTM> searchEmployeeSalaries(String keyword) throws SQLException, ClassNotFoundException {
        String p = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(
                TM_SQL + "WHERE es.salary_id LIKE ? OR es.emp_id LIKE ? OR e.emp_name LIKE ? OR " +
                        "es.trip_id LIKE ? OR es.description LIKE ? OR u.username LIKE ? ORDER BY es.salary_id DESC",
                p, p, p, p, p, p);
        return mapTMResultSet(rst);
    }

    @Override
    public boolean isEmployeeExists(int empId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT emp_id FROM Employee WHERE emp_id=?", empId);
        return rst.next();
    }

    @Override
    public boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip WHERE trip_id=?", tripId);
        return rst.next();
    }

    @Override
    public List<Integer> getAllActiveEmployeeIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT emp_id FROM Employee WHERE emp_status='ACTIVE' ORDER BY emp_id DESC");
        List<Integer> ids = new ArrayList<>();
        while (rst.next()) ids.add(rst.getInt("emp_id"));
        return ids;
    }

    @Override
    public List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip ORDER BY trip_id DESC");
        List<Integer> ids = new ArrayList<>();
        while (rst.next()) ids.add(rst.getInt("trip_id"));
        return ids;
    }

    @Override
    public String getEmployeeDetails(int empId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT emp_name, emp_category, contact_no FROM Employee WHERE emp_id=?", empId);
        if (rst.next()) return rst.getString("emp_name") + " | " + rst.getString("emp_category") + " | " + rst.getString("contact_no");
        return "Employee not found";
    }

    @Override
    public String getTripDetails(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT start_location, end_location, trip_date FROM Trip WHERE trip_id=?", tripId);
        if (rst.next()) return rst.getString("start_location") + " → " + rst.getString("end_location") + " (" + rst.getDate("trip_date") + ")";
        return "Trip not found";
    }

    private EmployeeSalary mapResultSet(ResultSet rst) throws SQLException {
        EmployeeSalary es = new EmployeeSalary();
        es.setSalaryId(rst.getInt("salary_id"));
        es.setEmpId(rst.getInt("emp_id"));
        es.setTripId(rst.getObject("trip_id") != null ? rst.getInt("trip_id") : null);
        es.setAmount(rst.getDouble("amount"));
        es.setDescription(rst.getString("description"));
        es.setDate(rst.getDate("date").toLocalDate());
        es.setCreatedBy(rst.getInt("created_by"));
        return es;
    }

    private List<EmployeeSalaryTM> mapTMResultSet(ResultSet rst) throws SQLException {
        List<EmployeeSalaryTM> list = new ArrayList<>();
        while (rst.next()) {
            try {
                Integer tripId = rst.getObject("trip_id") != null ? rst.getInt("trip_id") : null;
                list.add(new EmployeeSalaryTM(
                        rst.getInt("salary_id"), rst.getInt("emp_id"),
                        rst.getString("emp_name"), tripId, rst.getDouble("amount"),
                        rst.getString("description"), rst.getDate("date").toLocalDate(),
                        rst.getString("username")
                ));
            } catch (Exception e) {
                System.err.println("❌ Error loading salary record: " + e.getMessage());
            }
        }
        return list;
    }
}
