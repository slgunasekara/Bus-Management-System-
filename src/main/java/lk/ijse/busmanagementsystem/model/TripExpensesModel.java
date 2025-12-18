package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.dto.TripExpensesDTO;
import lk.ijse.busmanagementsystem.enums.TripExpType;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TripExpensesModel {

    /**
     * Get all trip expenses
     */
    public List<TripExpensesDTO> getAllTripExpenses() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Trip_Expenses ORDER BY trip_exp_id DESC";

        ResultSet rst = CrudUtil.execute(sql);
        List<TripExpensesDTO> expensesList = new ArrayList<>();

        while (rst.next()) {
            try {
                String typeString = rst.getString("trip_exp_type");
                TripExpType type;

                try {
                    type = TripExpType.valueOf(typeString);
                } catch (IllegalArgumentException e) {
                    System.err.println("⚠️ Unknown expense type: '" + typeString +
                            "' for Expense ID: " + rst.getInt("trip_exp_id") +
                            ". Defaulting to OTHERS.");
                    type = TripExpType.OTHERS;
                }

                TripExpensesDTO dto = new TripExpensesDTO(
                        rst.getInt("trip_exp_id"),
                        rst.getInt("trip_id"),
                        type,
                        null,
                        rst.getDouble("amount"),
                        rst.getString("description"),
                        rst.getDate("date").toLocalDate(),
                        rst.getInt("created_by")
                );
                expensesList.add(dto);
            } catch (Exception e) {
                System.err.println("❌ Error loading expense record ID: " +
                        rst.getInt("trip_exp_id") + " - " + e.getMessage());
                e.printStackTrace();
            }
        }
        return expensesList;
    }

    /**
     * Save trip expense
     */
    public boolean saveTripExpense(TripExpensesDTO dto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Trip_Expenses(trip_id, trip_exp_type, " +
                "amount, description, date, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        return CrudUtil.execute(sql,
                dto.getTripId(),
                dto.getTripExpType().name(),
                dto.getAmount(),
                dto.getDescription(),
                java.sql.Date.valueOf(dto.getDate()),
                dto.getCreatedBy()
        );
    }

    /**
     * Update trip expense
     */
    public boolean updateTripExpense(TripExpensesDTO dto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Trip_Expenses SET trip_id=?, trip_exp_type=?, " +
                "amount=?, description=?, date=? WHERE trip_exp_id=?";

        return CrudUtil.execute(sql,
                dto.getTripId(),
                dto.getTripExpType().name(),
                dto.getAmount(),
                dto.getDescription(),
                java.sql.Date.valueOf(dto.getDate()),
                dto.getTripExpId()
        );
    }

    /**
     * Delete trip expense
     */
    public boolean deleteTripExpense(String expenseId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Trip_Expenses WHERE trip_exp_id=?", expenseId);
    }

    /**
     * Check if trip exists
     */
    public boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip WHERE trip_id=?", tripId);
        return rst.next();
    }

    /**
     * Get all available trip IDs
     */
    public List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip ORDER BY trip_id DESC");
        List<Integer> tripIds = new ArrayList<>();

        while (rst.next()) {
            tripIds.add(rst.getInt("trip_id"));
        }
        return tripIds;
    }

    /**
     * Search trip expenses
     */
    public List<TripExpensesDTO> searchTripExpenses(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Trip_Expenses " +
                "WHERE trip_exp_id LIKE ? OR " +
                "trip_id LIKE ? OR " +
                "trip_exp_type LIKE ? OR " +
                "description LIKE ? OR " +
                "created_by LIKE ? " +
                "ORDER BY trip_exp_id DESC";

        String searchPattern = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(sql, searchPattern, searchPattern,
                searchPattern, searchPattern, searchPattern);

        List<TripExpensesDTO> expensesList = new ArrayList<>();

        while (rst.next()) {
            try {
                String typeString = rst.getString("trip_exp_type");
                TripExpType type;

                try {
                    type = TripExpType.valueOf(typeString);
                } catch (IllegalArgumentException e) {
                    System.err.println("⚠️ Unknown expense type during search: '" + typeString + "'");
                    type = TripExpType.OTHERS;
                }

                TripExpensesDTO dto = new TripExpensesDTO(
                        rst.getInt("trip_exp_id"),
                        rst.getInt("trip_id"),
                        type,
                        null,
                        rst.getDouble("amount"),
                        rst.getString("description"),
                        rst.getDate("date").toLocalDate(),
                        rst.getInt("created_by")
                );
                expensesList.add(dto);
            } catch (Exception e) {
                System.err.println("❌ Error during search: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return expensesList;
    }

    /**
     * Get trip details by ID
     */
    public String getTripDetails(int tripId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT start_location, end_location, trip_date FROM Trip WHERE trip_id=?";
        ResultSet rst = CrudUtil.execute(sql, tripId);

        if (rst.next()) {
            return rst.getString("start_location") + " → " +
                    rst.getString("end_location") + " (" +
                    rst.getDate("trip_date") + ")";
        }
        return "Trip not found";
    }
}