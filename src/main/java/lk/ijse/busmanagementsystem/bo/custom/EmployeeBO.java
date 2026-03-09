package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.EmployeeDTO;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeBO extends SuperBO {
    List<EmployeeDTO> getAllEmployees() throws SQLException, ClassNotFoundException;
    boolean saveEmployee(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException;
    boolean updateEmployee(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException;
    boolean deleteEmployee(String empId) throws SQLException, ClassNotFoundException;
    EmployeeDTO searchEmployee(String id) throws SQLException, ClassNotFoundException;
    List<EmployeeDTO> searchEmployees(String keyword) throws SQLException, ClassNotFoundException;
    boolean isNicExists(String nic) throws SQLException, ClassNotFoundException;
    boolean isNicExistsForOther(String nic, int empId) throws SQLException, ClassNotFoundException;
}