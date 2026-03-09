package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.TripEmployeeDTO;
import lk.ijse.busmanagementsystem.tm.TripEmployeeTM;

import java.sql.SQLException;
import java.util.List;

public interface TripEmployeeBO extends SuperBO {
    List<TripEmployeeTM> getEmployeesByTrip(int tripId) throws SQLException, ClassNotFoundException;
    boolean assignEmployeeToTrip(TripEmployeeDTO dto) throws SQLException, ClassNotFoundException;
    boolean assignMultipleEmployees(int tripId, List<TripEmployeeDTO> employeeList)
            throws SQLException, ClassNotFoundException;
    boolean removeEmployeeFromTrip(int tripEmpId) throws SQLException, ClassNotFoundException;
    boolean removeAllEmployeesFromTrip(int tripId) throws SQLException, ClassNotFoundException;
    boolean isEmployeeAssigned(int tripId, int empId, String role)
            throws SQLException, ClassNotFoundException;
    List<TripEmployeeTM> getTripsByEmployee(int empId) throws SQLException, ClassNotFoundException;
    boolean updateEmployeeRole(int tripEmpId, String newRole)
            throws SQLException, ClassNotFoundException;
}