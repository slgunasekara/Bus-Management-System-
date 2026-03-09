package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.Employee;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO extends CrudDAO<Employee> {

    List<Employee> searchEmployees(String keyword)
            throws SQLException, ClassNotFoundException;

    boolean isNicExistsForOther(String nic, int empId)
            throws SQLException, ClassNotFoundException;
}
