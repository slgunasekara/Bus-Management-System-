package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.TripEmployeeDAO;
import lk.ijse.busmanagementsystem.entity.TripEmployee;
import lk.ijse.busmanagementsystem.tm.TripEmployeeTM;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TripEmployeeDAOImpl implements TripEmployeeDAO {

    @Override
    public List<TripEmployee> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Trip_Employee ORDER BY trip_emp_id DESC");
        List<TripEmployee> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public boolean save(TripEmployee te) throws SQLException, ClassNotFoundException {
        return assignEmployeeToTrip(te);
    }

    @Override
    public boolean update(TripEmployee te) throws SQLException, ClassNotFoundException {
        return updateEmployeeRole(te.getTripEmpId(), te.getRoleInTrip());
    }

    @Override
    public boolean delete(String tripEmpId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Trip_Employee WHERE trip_emp_id=?", tripEmpId);
    }

    @Override
    public boolean delete(int tripEmpId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Trip_Employee WHERE trip_emp_id=?", tripEmpId);
    }

    @Override
    public boolean exists(String tripEmpId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_emp_id FROM Trip_Employee WHERE trip_emp_id=?", tripEmpId);
        return rst.next();
    }

    @Override
    public boolean exists(int tripEmpId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_emp_id FROM Trip_Employee WHERE trip_emp_id=?", tripEmpId);
        return rst.next();
    }

    @Override
    public TripEmployee search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Trip_Employee WHERE trip_emp_id=?", id);
        if (rst.next()) return mapResultSet(rst);
        return null;
    }

    @Override
    public List<TripEmployeeTM> getEmployeesByTrip(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT te.trip_emp_id, te.emp_id, e.emp_name, e.emp_category, " +
                        "te.role_in_trip, e.contact_no, e.nic_no, DATE_FORMAT(te.assigned_date, '%Y-%m-%d %H:%i') as assigned_date " +
                        "FROM Trip_Employee te JOIN Employee e ON te.emp_id=e.emp_id " +
                        "WHERE te.trip_id=? ORDER BY te.assigned_date DESC",
                tripId);
        List<TripEmployeeTM> list = new ArrayList<>();
        while (rst.next()) {
            list.add(new TripEmployeeTM(
                    rst.getInt("trip_emp_id"), rst.getInt("emp_id"),
                    rst.getString("emp_name"), rst.getString("emp_category"),
                    rst.getString("role_in_trip"), rst.getString("contact_no"),
                    rst.getString("assigned_date")
            ));
        }
        return list;
    }

    @Override
    public boolean assignEmployeeToTrip(TripEmployee te) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Trip_Employee(trip_id, emp_id, role_in_trip, created_by) VALUES (?, ?, ?, ?)",
                te.getTripId(), te.getEmpId(), te.getRoleInTrip(), te.getCreatedBy()
        );
    }

    /**
     * Simple delegation — loops through list calling assignEmployeeToTrip for each.
     * Transaction management (commit/rollback) is handled by TripEmployeeBOImpl.
     */
    @Override
    public boolean assignMultipleEmployees(int tripId, List<TripEmployee> employeeList)
            throws SQLException, ClassNotFoundException {
        for (TripEmployee te : employeeList) {
            te.setTripId(tripId);
            if (!assignEmployeeToTrip(te)) return false;
        }
        return true;
    }

    @Override
    public boolean removeAllEmployeesFromTrip(int tripId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Trip_Employee WHERE trip_id=?", tripId);
    }

    @Override
    public boolean isEmployeeAssigned(int tripId, int empId, String role)
            throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT trip_emp_id FROM Trip_Employee WHERE trip_id=? AND emp_id=? AND role_in_trip=?",
                tripId, empId, role);
        return rst.next();
    }


    @Override
    public List<TripEmployeeTM> getTripsByEmployee(int empId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT te.trip_emp_id, t.trip_id, t.start_location, t.end_location, t.trip_date, te.role_in_trip " +
                        "FROM Trip_Employee te JOIN Trip t ON te.trip_id=t.trip_id WHERE te.emp_id=? ORDER BY t.trip_date DESC",
                empId);
        List<TripEmployeeTM> list = new ArrayList<>();
        while (rst.next()) {
            list.add(new TripEmployeeTM(
                    rst.getInt("trip_emp_id"), rst.getInt("trip_id"),
                    rst.getString("start_location") + " → " + rst.getString("end_location"),
                    "", rst.getString("role_in_trip"), "",
                    rst.getDate("trip_date").toString()
            ));
        }
        return list;
    }

    @Override
    public boolean updateEmployeeRole(int tripEmpId, String newRole) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Trip_Employee SET role_in_trip=? WHERE trip_emp_id=?", newRole, tripEmpId);
    }

    private TripEmployee mapResultSet(ResultSet rst) throws SQLException {
        TripEmployee te = new TripEmployee();
        te.setTripEmpId(rst.getInt("trip_emp_id"));
        te.setTripId(rst.getInt("trip_id"));
        te.setEmpId(rst.getInt("emp_id"));
        te.setRoleInTrip(rst.getString("role_in_trip"));
        te.setCreatedBy(rst.getInt("created_by"));
        return te;
    }
}
