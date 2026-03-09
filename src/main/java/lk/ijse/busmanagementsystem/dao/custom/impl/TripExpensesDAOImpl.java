package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.TripExpensesDAO;
import lk.ijse.busmanagementsystem.entity.TripExpenses;
import lk.ijse.busmanagementsystem.enums.TripExpType;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TripExpensesDAOImpl implements TripExpensesDAO {

    @Override
    public List<TripExpenses> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Trip_Expenses ORDER BY trip_exp_id DESC");
        List<TripExpenses> list = new ArrayList<>();
        while (rst.next()) {
            try { list.add(mapResultSet(rst)); }
            catch (Exception e) { System.err.println("❌ Error loading expense ID: " + rst.getInt("trip_exp_id") + " - " + e.getMessage()); }
        }
        return list;
    }

    @Override
    public boolean save(TripExpenses te) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Trip_Expenses(trip_id, trip_exp_type, amount, description, date, created_by) VALUES (?, ?, ?, ?, ?, ?)",
                te.getTripId(), te.getTripExpType().name(), te.getAmount(),
                te.getDescription(), java.sql.Date.valueOf(te.getDate()), te.getCreatedBy()
        );
    }

    @Override
    public boolean update(TripExpenses te) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Trip_Expenses SET trip_id=?, trip_exp_type=?, amount=?, description=?, date=? WHERE trip_exp_id=?",
                te.getTripId(), te.getTripExpType().name(), te.getAmount(),
                te.getDescription(), java.sql.Date.valueOf(te.getDate()), te.getTripExpId()
        );
    }

    @Override
    public boolean delete(String expenseId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Trip_Expenses WHERE trip_exp_id=?", expenseId);
    }

    @Override
    public boolean delete(int expenseId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Trip_Expenses WHERE trip_exp_id=?", expenseId);
    }

    @Override
    public boolean exists(String expenseId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_exp_id FROM Trip_Expenses WHERE trip_exp_id=?", expenseId);
        return rst.next();
    }

    @Override
    public boolean exists(int expenseId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_exp_id FROM Trip_Expenses WHERE trip_exp_id=?", expenseId);
        return rst.next();
    }

    @Override
    public TripExpenses search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Trip_Expenses WHERE trip_exp_id=?", id);
        if (rst.next()) return mapResultSet(rst);
        return null;
    }

    @Override
    public boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip WHERE trip_id=?", tripId);
        return rst.next();
    }

    @Override
    public List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip ORDER BY trip_id DESC");
        List<Integer> ids = new ArrayList<>();
        while (rst.next()) ids.add(rst.getInt("trip_id"));
        return ids;
    }

    @Override
    public List<TripExpenses> searchTripExpenses(String keyword) throws SQLException, ClassNotFoundException {
        String p = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(
                "SELECT * FROM Trip_Expenses WHERE trip_exp_id LIKE ? OR trip_id LIKE ? OR trip_exp_type LIKE ? OR description LIKE ? ORDER BY trip_exp_id DESC",
                p, p, p, p);
        List<TripExpenses> list = new ArrayList<>();
        while (rst.next()) {
            try { list.add(mapResultSet(rst)); }
            catch (Exception e) { System.err.println("❌ Search error: " + e.getMessage()); }
        }
        return list;
    }

    @Override
    public String getTripDetails(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT start_location, end_location, trip_date FROM Trip WHERE trip_id=?", tripId);
        if (rst.next()) return rst.getString("start_location") + " → " + rst.getString("end_location") + " (" + rst.getDate("trip_date") + ")";
        return "Trip not found";
    }

    private TripExpenses mapResultSet(ResultSet rst) throws SQLException {
        TripExpType type;
        try {
            type = TripExpType.valueOf(rst.getString("trip_exp_type"));
        } catch (IllegalArgumentException e) {
            type = TripExpType.OTHERS;
        }
        TripExpenses te = new TripExpenses();
        te.setTripExpId(rst.getInt("trip_exp_id"));
        te.setTripId(rst.getInt("trip_id"));
        te.setTripExpType(type);
        te.setAmount(rst.getDouble("amount"));
        te.setDescription(rst.getString("description"));
        te.setDate(rst.getDate("date").toLocalDate());
        te.setCreatedBy(rst.getInt("created_by"));
        return te;
    }
}
