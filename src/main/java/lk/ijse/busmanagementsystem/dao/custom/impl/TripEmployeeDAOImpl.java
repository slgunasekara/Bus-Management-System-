package lk.ijse.busmanagementsystem.dao.impl;

import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.dto.TripEmployeeDTO;
import lk.ijse.busmanagementsystem.dto.TripEmployeeTM;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Trip-Employee Association Model
//Transaction management සමගින් multiple employees එකවර assign කරන්න
public class TripEmployeeDAOImpl {


    public List<TripEmployeeTM> getEmployeesByTrip(int tripId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT te.trip_emp_id, te.emp_id, e.emp_name, e.emp_category, " +
                "te.role_in_trip, e.contact_no, e.nic_no, " +
                "DATE_FORMAT(te.assigned_date, '%Y-%m-%d %H:%i') as assigned_date " +
                "FROM Trip_Employee te " +
                "JOIN Employee e ON te.emp_id = e.emp_id " +
                "WHERE te.trip_id = ? " +
                "ORDER BY te.assigned_date DESC";

        ResultSet rst = CrudUtil.execute(sql, tripId);
        List<TripEmployeeTM> employeeList = new ArrayList<>();

        while (rst.next()) {
            TripEmployeeTM tm = new TripEmployeeTM(
                    rst.getInt("trip_emp_id"),
                    rst.getInt("emp_id"),
                    rst.getString("emp_name"),
                    rst.getString("emp_category"),
                    rst.getString("role_in_trip"),
                    rst.getString("contact_no"),
                    rst.getString("assigned_date")
            );
            employeeList.add(tm);
        }
        return employeeList;
    }


    public boolean assignEmployeeToTrip(TripEmployeeDTO dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Trip_Employee (trip_id, emp_id, role_in_trip, created_by) " +
                "VALUES (?, ?, ?, ?)";

        return CrudUtil.execute(sql,
                dto.getTripId(),
                dto.getEmpId(),
                dto.getRoleInTrip(),
                dto.getCreatedBy());
    }


    public boolean assignMultipleEmployees(int tripId, List<TripEmployeeDTO> employeeList)
            throws SQLException, ClassNotFoundException {

        Connection conn = DBConnection.getInstance().getConnection();

        
        try {
            conn.setAutoCommit(false);


            for (TripEmployeeDTO dto : employeeList) {
                String sql = "INSERT INTO Trip_Employee (trip_id, emp_id, role_in_trip, created_by) " +
                        "VALUES (?, ?, ?, ?)";

                boolean result = CrudUtil.execute(sql,
                        tripId,
                        dto.getEmpId(),
                        dto.getRoleInTrip(),
                        dto.getCreatedBy());

                if (!result) {
                    throw new SQLException("Failed to assign employee: " + dto.getEmpId());
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {

            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }


    public boolean removeEmployeeFromTrip(int tripEmpId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Trip_Employee WHERE trip_emp_id = ?";
        return CrudUtil.execute(sql, tripEmpId);
    }


    public boolean removeAllEmployeesFromTrip(int tripId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Trip_Employee WHERE trip_id = ?";
        return CrudUtil.execute(sql, tripId);
    }


    public boolean isEmployeeAssigned(int tripId, int empId, String role)
            throws SQLException, ClassNotFoundException {
        String sql = "SELECT trip_emp_id FROM Trip_Employee " +
                "WHERE trip_id = ? AND emp_id = ? AND role_in_trip = ?";

        ResultSet rst = CrudUtil.execute(sql, tripId, empId, role);
        return rst.next();
    }


    public List<TripEmployeeTM> getTripsByEmployee(int empId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT te.trip_emp_id, t.trip_id, t.start_location, t.end_location, " +
                "t.trip_date, te.role_in_trip " +
                "FROM Trip_Employee te " +
                "JOIN Trip t ON te.trip_id = t.trip_id " +
                "WHERE te.emp_id = ? " +
                "ORDER BY t.trip_date DESC";

        ResultSet rst = CrudUtil.execute(sql, empId);
        List<TripEmployeeTM> tripList = new ArrayList<>();

        while (rst.next()) {

        }
        return tripList;
    }


    public boolean updateEmployeeRole(int tripEmpId, String newRole)
            throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Trip_Employee SET role_in_trip = ? WHERE trip_emp_id = ?";
        return CrudUtil.execute(sql, newRole, tripEmpId);
    }
}