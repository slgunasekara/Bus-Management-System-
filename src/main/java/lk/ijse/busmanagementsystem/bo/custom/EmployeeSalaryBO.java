package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.EmployeeSalaryDTO;
import lk.ijse.busmanagementsystem.tm.EmployeeSalaryTM;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeSalaryBO extends SuperBO {
    List<EmployeeSalaryTM> getAllEmployeeSalaries() throws SQLException, ClassNotFoundException;
    boolean saveEmployeeSalary(EmployeeSalaryDTO dto) throws SQLException, ClassNotFoundException;
    boolean updateEmployeeSalary(EmployeeSalaryDTO dto) throws SQLException, ClassNotFoundException;
    boolean deleteEmployeeSalary(String salaryId) throws SQLException, ClassNotFoundException;
    boolean isEmployeeExists(int empId) throws SQLException, ClassNotFoundException;
    boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException;
    List<Integer> getAllActiveEmployeeIds() throws SQLException, ClassNotFoundException;
    List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException;
    List<EmployeeSalaryTM> searchEmployeeSalaries(String keyword) throws SQLException, ClassNotFoundException;
    String getEmployeeDetails(int empId) throws SQLException, ClassNotFoundException;
    String getTripDetails(int tripId) throws SQLException, ClassNotFoundException;
}