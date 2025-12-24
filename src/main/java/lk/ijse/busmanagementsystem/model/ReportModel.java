package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.dto.*;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportModel {

    /**
     * Get report summary for date range
     */
    public ReportDTO getReportSummary(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {

        double totalIncome = getTotalIncome(fromDate, toDate);
        double totalExpenses = getTotalExpenses(fromDate, toDate);
        double totalSalary = getTotalSalary(fromDate, toDate);
        int totalTrips = getTotalTrips(fromDate, toDate);
        double netProfit = totalIncome - (totalExpenses + totalSalary);

        return new ReportDTO(totalIncome, totalExpenses, totalSalary, netProfit, totalTrips);
    }

    /**
     * Get total income for date range
     */
    private double getTotalIncome(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(total_income), 0) as total " +
                "FROM Trip WHERE trip_date BETWEEN ? AND ?";
        ResultSet rst = CrudUtil.execute(sql, fromDate, toDate);
        if (rst.next()) {
            return rst.getDouble("total");
        }
        return 0.0;
    }

    /**
     * Get total expenses for date range
     */
    private double getTotalExpenses(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total " +
                "FROM Trip_Expenses WHERE date BETWEEN ? AND ?";
        ResultSet rst = CrudUtil.execute(sql, fromDate, toDate);
        if (rst.next()) {
            return rst.getDouble("total");
        }
        return 0.0;
    }

    /**
     * Get total salary for date range
     */
    private double getTotalSalary(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(amount), 0) as total " +
                "FROM Employee_Salary WHERE date BETWEEN ? AND ?";
        ResultSet rst = CrudUtil.execute(sql, fromDate, toDate);
        if (rst.next()) {
            return rst.getDouble("total");
        }
        return 0.0;
    }

    /**
     * Get total trips for date range
     */
    private int getTotalTrips(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) as total FROM Trip WHERE trip_date BETWEEN ? AND ?";
        ResultSet rst = CrudUtil.execute(sql, fromDate, toDate);
        if (rst.next()) {
            return rst.getInt("total");
        }
        return 0;
    }

    /**
     * Get income report (Trip Income)
     */
    public List<TripDTO> getIncomeReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {

        String sql = "SELECT t.trip_id, t.bus_id, t.trip_category, t.start_location, " +
                "t.end_location, t.distance, t.total_income, t.trip_date, " +
                "t.description, t.created_by, u.username " +
                "FROM Trip t " +
                "LEFT JOIN User u ON t.created_by = u.user_id " +
                "WHERE t.trip_date BETWEEN ? AND ? " +
                "ORDER BY t.trip_date DESC";

        ResultSet rst = CrudUtil.execute(sql, fromDate, toDate);
        List<TripDTO> tripList = new ArrayList<>();

        while (rst.next()) {
            TripDTO dto = new TripDTO(
                    rst.getInt("trip_id"),
                    rst.getInt("bus_id"),
                    lk.ijse.busmanagementsystem.enums.TripCategory.valueOf(rst.getString("trip_category")),
                    rst.getString("start_location"),
                    rst.getString("end_location"),
                    rst.getDouble("distance"),
                    rst.getDouble("total_income"),
                    rst.getDate("trip_date").toLocalDate(),
                    rst.getString("description"),
                    rst.getInt("created_by"),
                    rst.getString("username")
            );
            tripList.add(dto);
        }
        return tripList;
    }

    /**
     * Get expense report (Trip Expenses)
     */
    public List<TripExpensesDTO> getExpenseReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {

        String sql = "SELECT te.trip_exp_id, te.trip_id, te.trip_exp_type, " +
                "te.amount, te.description, te.date, te.created_by, u.username " +
                "FROM Trip_Expenses te " +
                "LEFT JOIN User u ON te.created_by = u.user_id " +
                "WHERE te.date BETWEEN ? AND ? " +
                "ORDER BY te.date DESC";

        ResultSet rst = CrudUtil.execute(sql, fromDate, toDate);
        List<TripExpensesDTO> expenseList = new ArrayList<>();

        while (rst.next()) {
            TripExpensesDTO dto = new TripExpensesDTO(
                    rst.getInt("trip_exp_id"),
                    rst.getInt("trip_id"),
                    lk.ijse.busmanagementsystem.enums.TripExpType.valueOf(rst.getString("trip_exp_type")),
                    null,
                    rst.getDouble("amount"),
                    rst.getString("description"),
                    rst.getDate("date").toLocalDate(),
                    rst.getInt("created_by"),
                    rst.getString("username")
            );
            expenseList.add(dto);
        }
        return expenseList;
    }

    /**
     * Get salary report
     */
    public List<EmployeeSalaryTM> getSalaryReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {

        String sql = "SELECT es.salary_id, es.emp_id, e.emp_name, es.trip_id, " +
                "es.amount, es.description, es.date, es.created_by, u.username " +
                "FROM Employee_Salary es " +
                "LEFT JOIN Employee e ON es.emp_id = e.emp_id " +
                "LEFT JOIN User u ON es.created_by = u.user_id " +
                "WHERE es.date BETWEEN ? AND ? " +
                "ORDER BY es.date DESC";

        ResultSet rst = CrudUtil.execute(sql, fromDate, toDate);
        List<EmployeeSalaryTM> salaryList = new ArrayList<>();

        while (rst.next()) {
            EmployeeSalaryTM tm = new EmployeeSalaryTM(
                    rst.getInt("salary_id"),
                    rst.getInt("emp_id"),
                    rst.getString("emp_name"),
                    rst.getObject("trip_id") != null ? rst.getInt("trip_id") : null,
                    rst.getDouble("amount"),
                    rst.getString("description"),
                    rst.getDate("date").toLocalDate(),
                    rst.getString("username")
            );
            salaryList.add(tm);
        }
        return salaryList;
    }

    /**
     * Get trip report
     */
    public List<TripDTO> getTripReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        return getIncomeReport(fromDate, toDate);
    }

    /**
     * Get bus number by bus ID
     */
    public String getBusNumberById(int busId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT bus_number FROM Bus WHERE bus_id = ?";
        ResultSet rst = CrudUtil.execute(sql, busId);
        if (rst.next()) {
            return rst.getString("bus_number");
        }
        return "N/A";
    }
}