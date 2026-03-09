package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.EmployeeSalary;
import lk.ijse.busmanagementsystem.tm.EmployeeSalaryTM;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeSalaryDAO extends CrudDAO<EmployeeSalary> {

    List<EmployeeSalaryTM> getAllTM()
            throws SQLException, ClassNotFoundException;

    List<EmployeeSalaryTM> searchEmployeeSalaries(String keyword)
            throws SQLException, ClassNotFoundException;

    boolean isEmployeeExists(int empId) throws SQLException, ClassNotFoundException;
    boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException;

    List<Integer> getAllActiveEmployeeIds() throws SQLException, ClassNotFoundException;
    List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException;

    String getEmployeeDetails(int empId) throws SQLException, ClassNotFoundException;
    String getTripDetails(int tripId) throws SQLException, ClassNotFoundException;
}
