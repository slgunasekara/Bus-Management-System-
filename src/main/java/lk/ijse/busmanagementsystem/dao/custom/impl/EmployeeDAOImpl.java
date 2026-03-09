package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.EmployeeDAO;
import lk.ijse.busmanagementsystem.entity.Employee;
import lk.ijse.busmanagementsystem.enums.EmployeeCategory;
import lk.ijse.busmanagementsystem.enums.EmployeeStatus;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public List<Employee> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Employee ORDER BY emp_id DESC");
        List<Employee> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public boolean save(Employee emp) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Employee(emp_category, emp_name, address, contact_no, nic_no, ntc_no, " +
                        "driving_licence_no, join_date, exit_date, emp_status, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                emp.getEmpCategory().name(), emp.getEmpName(), emp.getAddress(),
                emp.getContactNo(), emp.getNicNo(), emp.getNtcNo(), emp.getDrivingLicenceNo(),
                emp.getJoinDate() != null ? java.sql.Date.valueOf(emp.getJoinDate()) : null,
                emp.getExitDate() != null ? java.sql.Date.valueOf(emp.getExitDate()) : null,
                emp.getEmpStatus().name(), emp.getCreatedBy()
        );
    }

    @Override
    public boolean update(Employee emp) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Employee SET emp_category=?, emp_name=?, address=?, contact_no=?, nic_no=?, " +
                        "ntc_no=?, driving_licence_no=?, join_date=?, exit_date=?, emp_status=? WHERE emp_id=?",
                emp.getEmpCategory().name(), emp.getEmpName(), emp.getAddress(),
                emp.getContactNo(), emp.getNicNo(), emp.getNtcNo(), emp.getDrivingLicenceNo(),
                emp.getJoinDate() != null ? java.sql.Date.valueOf(emp.getJoinDate()) : null,
                emp.getExitDate() != null ? java.sql.Date.valueOf(emp.getExitDate()) : null,
                emp.getEmpStatus().name(), emp.getEmpId()
        );
    }

    @Override
    public boolean delete(String empId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Employee WHERE emp_id=?", empId);
    }

    @Override
    public boolean delete(int empId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Employee WHERE emp_id=?", empId);
    }

    @Override
    public boolean exists(String nic) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT emp_id FROM Employee WHERE nic_no=?", nic);
        return rst.next();
    }

    @Override
    public boolean exists(int empId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT emp_id FROM Employee WHERE emp_id=?", empId);
        return rst.next();
    }

    @Override
    public Employee search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Employee WHERE emp_id=?", id);
        if (rst.next()) return mapResultSet(rst);
        return null;
    }

    @Override
    public List<Employee> searchEmployees(String keyword) throws SQLException, ClassNotFoundException {
        String p = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(
                "SELECT * FROM Employee WHERE emp_id LIKE ? OR emp_category LIKE ? OR emp_name LIKE ? OR " +
                        "address LIKE ? OR contact_no LIKE ? OR nic_no LIKE ? OR ntc_no LIKE ? OR " +
                        "driving_licence_no LIKE ? OR emp_status LIKE ?",
                p, p, p, p, p, p, p, p, p);
        List<Employee> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public boolean isNicExistsForOther(String nic, int empId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT emp_id FROM Employee WHERE nic_no=? AND emp_id!=?", nic, empId);
        return rst.next();
    }

    private Employee mapResultSet(ResultSet rst) throws SQLException {
        Employee emp = new Employee();
        emp.setEmpId(rst.getInt("emp_id"));
        emp.setEmpCategory(EmployeeCategory.valueOf(rst.getString("emp_category")));
        emp.setEmpName(rst.getString("emp_name"));
        emp.setAddress(rst.getString("address"));
        emp.setContactNo(rst.getString("contact_no"));
        emp.setNicNo(rst.getString("nic_no"));
        emp.setNtcNo(rst.getString("ntc_no"));
        emp.setDrivingLicenceNo(rst.getString("driving_licence_no"));
        emp.setJoinDate(rst.getDate("join_date") != null ? rst.getDate("join_date").toLocalDate() : null);
        emp.setExitDate(rst.getDate("exit_date") != null ? rst.getDate("exit_date").toLocalDate() : null);
        emp.setEmpStatus(EmployeeStatus.valueOf(rst.getString("emp_status")));
        emp.setCreatedBy(rst.getInt("created_by"));
        return emp;
    }
}
