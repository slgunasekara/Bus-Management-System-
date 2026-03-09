package lk.ijse.busmanagementsystem.dao.impl;

import lk.ijse.busmanagementsystem.dto.EmployeeDTO;
import lk.ijse.busmanagementsystem.enums.EmployeeCategory;
import lk.ijse.busmanagementsystem.enums.EmployeeStatus;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl {

    public List<EmployeeDTO> getAllEmployees() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Employee");
        List<EmployeeDTO> employeeList = new ArrayList<>();

        while (rst.next()) {
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    rst.getInt("emp_id"),
                    EmployeeCategory.valueOf(rst.getString("emp_category")),
                    rst.getString("emp_name"),
                    rst.getString("address"),
                    rst.getString("contact_no"),
                    rst.getString("nic_no"),
                    rst.getString("ntc_no"),
                    rst.getString("driving_licence_no"),
                    rst.getDate("join_date") != null ? rst.getDate("join_date").toLocalDate() : null,
                    rst.getDate("exit_date") != null ? rst.getDate("exit_date").toLocalDate() : null,
                    EmployeeStatus.valueOf(rst.getString("emp_status")),
                    rst.getInt("created_by")
            );
            employeeList.add(employeeDTO);
        }
        return employeeList;
    }

    public boolean saveEmployee(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Employee(emp_category, emp_name, address, contact_no, " +
                "nic_no, ntc_no, driving_licence_no, join_date, exit_date, emp_status, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                employeeDTO.getEmpCategory().name(),
                employeeDTO.getEmpName(),
                employeeDTO.getAddress(),
                employeeDTO.getContactNo(),
                employeeDTO.getNicNo(),
                employeeDTO.getNtcNo(),
                employeeDTO.getDrivingLicenceNo(),
                employeeDTO.getJoinDate() != null ? java.sql.Date.valueOf(employeeDTO.getJoinDate()) : null,
                employeeDTO.getExitDate() != null ? java.sql.Date.valueOf(employeeDTO.getExitDate()) : null,
                employeeDTO.getEmpStatus().name(),
                employeeDTO.getCreatedBy()
        );
    }

    public boolean updateEmployee(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Employee SET emp_category=?, emp_name=?, address=?, " +
                "contact_no=?, nic_no=?, ntc_no=?, driving_licence_no=?, join_date=?, " +
                "exit_date=?, emp_status=? WHERE emp_id=?";
        return CrudUtil.execute(sql,
                employeeDTO.getEmpCategory().name(),
                employeeDTO.getEmpName(),
                employeeDTO.getAddress(),
                employeeDTO.getContactNo(),
                employeeDTO.getNicNo(),
                employeeDTO.getNtcNo(),
                employeeDTO.getDrivingLicenceNo(),
                employeeDTO.getJoinDate() != null ? java.sql.Date.valueOf(employeeDTO.getJoinDate()) : null,
                employeeDTO.getExitDate() != null ? java.sql.Date.valueOf(employeeDTO.getExitDate()) : null,
                employeeDTO.getEmpStatus().name(),
                employeeDTO.getEmpId()
        );
    }

    public boolean deleteEmployee(String empId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Employee WHERE emp_id=?", empId);
    }

    public EmployeeDTO searchEmployee(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Employee WHERE emp_id=?", id);

        if (rst.next()) {
            return new EmployeeDTO(
                    rst.getInt("emp_id"),
                    EmployeeCategory.valueOf(rst.getString("emp_category")),
                    rst.getString("emp_name"),
                    rst.getString("address"),
                    rst.getString("contact_no"),
                    rst.getString("nic_no"),
                    rst.getString("ntc_no"),
                    rst.getString("driving_licence_no"),
                    rst.getDate("join_date") != null ? rst.getDate("join_date").toLocalDate() : null,
                    rst.getDate("exit_date") != null ? rst.getDate("exit_date").toLocalDate() : null,
                    EmployeeStatus.valueOf(rst.getString("emp_status")),
                    rst.getInt("created_by")
            );
        }
        return null;
    }

    public List<EmployeeDTO> searchEmployees(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Employee WHERE " +
                "emp_id LIKE ? OR " +
                "emp_category LIKE ? OR " +
                "emp_name LIKE ? OR " +
                "address LIKE ? OR " +
                "contact_no LIKE ? OR " +
                "nic_no LIKE ? OR " +
                "ntc_no LIKE ? OR " +
                "driving_licence_no LIKE ? OR " +
                "emp_status LIKE ?";

        String searchPattern = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(sql, searchPattern, searchPattern, searchPattern,
                searchPattern, searchPattern, searchPattern, searchPattern, searchPattern, searchPattern);

        List<EmployeeDTO> employeeList = new ArrayList<>();

        while (rst.next()) {
            EmployeeDTO employeeDTO = new EmployeeDTO(
                    rst.getInt("emp_id"),
                    EmployeeCategory.valueOf(rst.getString("emp_category")),
                    rst.getString("emp_name"),
                    rst.getString("address"),
                    rst.getString("contact_no"),
                    rst.getString("nic_no"),
                    rst.getString("ntc_no"),
                    rst.getString("driving_licence_no"),
                    rst.getDate("join_date") != null ? rst.getDate("join_date").toLocalDate() : null,
                    rst.getDate("exit_date") != null ? rst.getDate("exit_date").toLocalDate() : null,
                    EmployeeStatus.valueOf(rst.getString("emp_status")),
                    rst.getInt("created_by")
            );
            employeeList.add(employeeDTO);
        }
        return employeeList;
    }

    // Validate if NIC already exists (for duplicate check)
    public boolean isNicExists(String nic) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT nic_no FROM Employee WHERE nic_no=?", nic);
        return rst.next();
    }

    // Validate if NIC exists but for different employee (for update)
    public boolean isNicExistsForOther(String nic, int empId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT nic_no FROM Employee WHERE nic_no=? AND emp_id!=?", nic, empId);
        return rst.next();
    }
}