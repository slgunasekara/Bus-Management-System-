package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.TripEmployee;
import lk.ijse.busmanagementsystem.tm.TripEmployeeTM;

import java.sql.SQLException;
import java.util.List;

public interface TripEmployeeDAO extends CrudDAO<TripEmployee> {


    List<TripEmployeeTM> getEmployeesByTrip(int tripId)
            throws SQLException, ClassNotFoundException;

    boolean assignEmployeeToTrip(TripEmployee tripEmployee)
            throws SQLException, ClassNotFoundException;

    boolean assignMultipleEmployees(int tripId, List<TripEmployee> employeeList)
            throws SQLException, ClassNotFoundException;

    boolean removeAllEmployeesFromTrip(int tripId)
            throws SQLException, ClassNotFoundException;

    boolean isEmployeeAssigned(int tripId, int empId, String role)
            throws SQLException, ClassNotFoundException;

    List<TripEmployeeTM> getTripsByEmployee(int empId)
            throws SQLException, ClassNotFoundException;

    boolean updateEmployeeRole(int tripEmpId, String newRole)
            throws SQLException, ClassNotFoundException;
}
