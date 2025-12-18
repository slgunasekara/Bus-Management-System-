// ========================================
// Updated ReportModel.java (Using Your Existing DTOs)
// ========================================
package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.dto.*;
import lk.ijse.busmanagementsystem.enums.TripCategory;
import lk.ijse.busmanagementsystem.enums.TripExpType;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportModel {

    // ========== Report Summary Methods ==========

    public ReportDTO getReportSummary(LocalDate fromDate, LocalDate toDate) throws SQLException {
        BigDecimal totalIncome = getTotalIncome(fromDate, toDate);
        BigDecimal totalExpenses = getTotalExpenses(fromDate, toDate);
        BigDecimal totalSalary = getTotalSalary(fromDate, toDate);
        int totalTrips = getTotalTrips(fromDate, toDate);

        BigDecimal netProfit = totalIncome.subtract(totalExpenses).subtract(totalSalary);

        return new ReportDTO(totalIncome, totalExpenses, totalSalary, netProfit, totalTrips, fromDate, toDate);
    }

    private BigDecimal getTotalIncome(LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_income), 0) as total FROM Trip WHERE trip_date BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setDate(1, Date.valueOf(fromDate));
            pstm.setDate(2, Date.valueOf(toDate));

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return BigDecimal.valueOf(rs.getDouble("total"));
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getTotalExpenses(LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM Trip_Expenses WHERE date BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setDate(1, Date.valueOf(fromDate));
            pstm.setDate(2, Date.valueOf(toDate));

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return BigDecimal.valueOf(rs.getDouble("total"));
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal getTotalSalary(LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total FROM Employee_Salary WHERE date BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setDate(1, Date.valueOf(fromDate));
            pstm.setDate(2, Date.valueOf(toDate));

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return BigDecimal.valueOf(rs.getDouble("total"));
            }
        }
        return BigDecimal.ZERO;
    }

    private int getTotalTrips(LocalDate fromDate, LocalDate toDate) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM Trip WHERE trip_date BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setDate(1, Date.valueOf(fromDate));
            pstm.setDate(2, Date.valueOf(toDate));

            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // ========== Income Report Methods (Using TripDTO) ==========

    public List<TripDTO> getIncomeReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<TripDTO> tripList = new ArrayList<>();

        String sql = "SELECT t.trip_id, t.bus_id, t.trip_category, t.start_location, " +
                "t.end_location, t.distance, t.total_income, t.trip_date, " +
                "t.description, t.created_by, u.username as created_by_username " +
                "FROM Trip t " +
                "LEFT JOIN User u ON t.created_by = u.user_id " +
                "WHERE t.trip_date BETWEEN ? AND ? " +
                "ORDER BY t.trip_date DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setDate(1, Date.valueOf(fromDate));
            pstm.setDate(2, Date.valueOf(toDate));

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                TripDTO trip = new TripDTO(
                        rs.getInt("trip_id"),
                        rs.getInt("bus_id"),
                        TripCategory.valueOf(rs.getString("trip_category")),
                        rs.getString("start_location"),
                        rs.getString("end_location"),
                        rs.getDouble("distance"),
                        rs.getDouble("total_income"),
                        rs.getDate("trip_date").toLocalDate(),
                        rs.getString("description"),
                        rs.getInt("created_by"),
                        rs.getString("created_by_username")
                );
                tripList.add(trip);
            }
        }
        return tripList;
    }

    // ========== Expense Report Methods (Using TripExpensesDTO) ==========

    public List<TripExpensesDTO> getExpenseReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<TripExpensesDTO> expenseList = new ArrayList<>();

        String sql = "SELECT te.trip_exp_id, te.trip_id, te.trip_exp_type, te.amount, " +
                "te.description, te.date, te.created_by, u.username as created_by_username " +
                "FROM Trip_Expenses te " +
                "LEFT JOIN User u ON te.created_by = u.user_id " +
                "WHERE te.date BETWEEN ? AND ? " +
                "ORDER BY te.date DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setDate(1, Date.valueOf(fromDate));
            pstm.setDate(2, Date.valueOf(toDate));

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                TripExpensesDTO expense = new TripExpensesDTO(
                        rs.getInt("trip_exp_id"),
                        rs.getInt("trip_id"),
                        TripExpType.valueOf(rs.getString("trip_exp_type")),
                        null, // salaryId
                        rs.getDouble("amount"),
                        rs.getString("description"),
                        rs.getDate("date").toLocalDate(),
                        rs.getInt("created_by"),
                        rs.getString("created_by_username")
                );
                expenseList.add(expense);
            }
        }
        return expenseList;
    }

    // ========== Salary Report Methods (Using EmployeeSalaryTM) ==========

    public List<EmployeeSalaryTM> getSalaryReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<EmployeeSalaryTM> salaryList = new ArrayList<>();

        String sql = "SELECT es.salary_id, es.emp_id, e.emp_name, es.trip_id, " +
                "es.amount, es.description, es.date, u.username as created_by_username " +
                "FROM Employee_Salary es " +
                "JOIN Employee e ON es.emp_id = e.emp_id " +
                "LEFT JOIN User u ON es.created_by = u.user_id " +
                "WHERE es.date BETWEEN ? AND ? " +
                "ORDER BY es.date DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setDate(1, Date.valueOf(fromDate));
            pstm.setDate(2, Date.valueOf(toDate));

            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                EmployeeSalaryTM salary = new EmployeeSalaryTM(
                        rs.getInt("salary_id"),
                        rs.getInt("emp_id"),
                        rs.getString("emp_name"),
                        rs.getObject("trip_id") != null ? rs.getInt("trip_id") : null,
                        rs.getDouble("amount"),
                        rs.getString("description"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("created_by_username")
                );
                salaryList.add(salary);
            }
        }
        return salaryList;
    }

    // ========== Trip Report Methods (Using TripDTO) ==========

    public List<TripDTO> getTripReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
        return getIncomeReport(fromDate, toDate); // Same as income report
    }
}