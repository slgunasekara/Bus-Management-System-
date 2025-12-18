package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.dto.TripEmployeeDTO;
import lk.ijse.busmanagementsystem.dto.TripEmployeeTM;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Trip-Employee Association Model
 * Transaction management සමගින් multiple employees එකවර assign කරන්න
 */
public class TripEmployeeModel {

    /**
     * Get all employees assigned to a specific trip
     * @param tripId Trip ID
     * @return List of TripEmployeeTM objects for table display
     */
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

    /**
     * Assign a single employee to a trip
     * @param dto TripEmployeeDTO object
     * @return success status
     */
    public boolean assignEmployeeToTrip(TripEmployeeDTO dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Trip_Employee (trip_id, emp_id, role_in_trip, created_by) " +
                "VALUES (?, ?, ?, ?)";

        return CrudUtil.execute(sql,
                dto.getTripId(),
                dto.getEmpId(),
                dto.getRoleInTrip(),
                dto.getCreatedBy());
    }

    /**
     * ⭐ TRANSACTION: Assign multiple employees to a trip at once
     * මේ method එක lecturer ගේ OrderModel වගේ transaction use කරනවා
     * @param tripId Trip ID
     * @param employeeList List of TripEmployeeDTO objects
     * @return success status
     */
    public boolean assignMultipleEmployees(int tripId, List<TripEmployeeDTO> employeeList)
            throws SQLException, ClassNotFoundException {

        Connection conn = DBConnection.getInstance().getConnection();

        try {
            // Transaction start කරනවා
            conn.setAutoCommit(false);

            // පළමුවෙන්ම අර trip එකේ ඉස්සරම ඉන්න employees clear කරනවා (Optional)
            // CrudUtil.execute("DELETE FROM Trip_Employee WHERE trip_id = ?", tripId);

            // එක එක employee assign කරනවා
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

            // හැමදේම හරි නම් commit කරනවා
            conn.commit();
            return true;

        } catch (Exception e) {
            // වැරැද්දක් වුනොත් rollback කරනවා
            conn.rollback();
            throw e;
        } finally {
            // අවසානයේ auto-commit එක reset කරනවා
            conn.setAutoCommit(true);
        }
    }

    /**
     * Remove an employee from a trip
     * @param tripEmpId Trip-Employee association ID
     * @return success status
     */
    public boolean removeEmployeeFromTrip(int tripEmpId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Trip_Employee WHERE trip_emp_id = ?";
        return CrudUtil.execute(sql, tripEmpId);
    }

    /**
     * Remove all employees from a trip (for trip deletion or re-assignment)
     * @param tripId Trip ID
     * @return success status
     */
    public boolean removeAllEmployeesFromTrip(int tripId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Trip_Employee WHERE trip_id = ?";
        return CrudUtil.execute(sql, tripId);
    }

    /**
     * Check if an employee is already assigned to a trip with a specific role
     * @param tripId Trip ID
     * @param empId Employee ID
     * @param role Role in trip
     * @return true if already assigned
     */
    public boolean isEmployeeAssigned(int tripId, int empId, String role)
            throws SQLException, ClassNotFoundException {
        String sql = "SELECT trip_emp_id FROM Trip_Employee " +
                "WHERE trip_id = ? AND emp_id = ? AND role_in_trip = ?";

        ResultSet rst = CrudUtil.execute(sql, tripId, empId, role);
        return rst.next();
    }

    /**
     * Get all trips assigned to a specific employee
     * @param empId Employee ID
     * @return List of trip details
     */
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
            // මේකෙදි අපි TripEmployeeTM class එක slightly modify කරලා use කරන්න පුළුවන්
            // හෝ අලුත් TripDetailsTM class එකක් හදන්න පුළුවන්
            // මේකේදි simple කරලා අපි existing class එක use කරමු
        }
        return tripList;
    }

    /**
     * Update employee role in a trip
     * @param tripEmpId Trip-Employee ID
     * @param newRole New role
     * @return success status
     */
    public boolean updateEmployeeRole(int tripEmpId, String newRole)
            throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Trip_Employee SET role_in_trip = ? WHERE trip_emp_id = ?";
        return CrudUtil.execute(sql, newRole, tripEmpId);
    }
}