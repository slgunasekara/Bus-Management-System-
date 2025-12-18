package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.dto.MonthlyProfitDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MonthlyProfitModel {

    /**
     * Get monthly profit data for a specific month and year by joining multiple tables
     */
    public List<MonthlyProfitDTO> getMonthlyProfitData(int month, int year)
            throws SQLException, ClassNotFoundException {
        List<MonthlyProfitDTO> monthlyProfitList = new ArrayList<>();

        String query = "SELECT " +
                "DATE_FORMAT(t.trip_date, '%Y-%m') AS month, " +
                "COALESCE(SUM(t.total_income), 0) AS total_income, " +
                "COALESCE(SUM(te.trip_exp_amount), 0) AS trip_expenses, " +
                "COALESCE(SUM(es.salary_amount), 0) AS salaries, " +
                "COALESCE(SUM(m.maintenance_cost), 0) AS maintenance, " +
                "COALESCE(SUM(pp.part_cost), 0) AS part_purchases, " +
                "COALESCE(SUM(os.service_cost), 0) AS other_services, " +
                "COUNT(DISTINCT t.trip_id) AS total_trips " +
                "FROM Trip t " +
                "LEFT JOIN ( " +
                "    SELECT SUM(amount) AS trip_exp_amount " +
                "    FROM Trip_Expenses " +
                "    WHERE MONTH(date) = ? AND YEAR(date) = ? " +
                ") te ON 1=1 " +
                "LEFT JOIN ( " +
                "    SELECT SUM(amount) AS salary_amount " +
                "    FROM Employee_Salary " +
                "    WHERE MONTH(date) = ? AND YEAR(date) = ? " +
                ") es ON 1=1 " +
                "LEFT JOIN ( " +
                "    SELECT SUM(cost) AS maintenance_cost " +
                "    FROM Maintenance " +
                "    WHERE MONTH(date) = ? AND YEAR(date) = ? " +
                ") m ON 1=1 " +
                "LEFT JOIN ( " +
                "    SELECT SUM(total_cost) AS part_cost " +
                "    FROM Part_Purchases " +
                "    WHERE MONTH(date) = ? AND YEAR(date) = ? " +
                ") pp ON 1=1 " +
                "LEFT JOIN ( " +
                "    SELECT SUM(cost) AS service_cost " +
                "    FROM Other_Services " +
                "    WHERE MONTH(date) = ? AND YEAR(date) = ? " +
                ") os ON 1=1 " +
                "WHERE MONTH(t.trip_date) = ? AND YEAR(t.trip_date) = ? " +
                "GROUP BY DATE_FORMAT(t.trip_date, '%Y-%m')";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);

        // Set parameters for all subqueries (12 parameters)
        pstm.setInt(1, month);
        pstm.setInt(2, year);
        pstm.setInt(3, month);
        pstm.setInt(4, year);
        pstm.setInt(5, month);
        pstm.setInt(6, year);
        pstm.setInt(7, month);
        pstm.setInt(8, year);
        pstm.setInt(9, month);
        pstm.setInt(10, year);
        pstm.setInt(11, month);
        pstm.setInt(12, year);

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String monthYear = resultSet.getString("month");
            double totalIncome = resultSet.getDouble("total_income");
            double tripExpenses = resultSet.getDouble("trip_expenses");
            double salaries = resultSet.getDouble("salaries");
            double maintenance = resultSet.getDouble("maintenance");
            double partPurchases = resultSet.getDouble("part_purchases");
            double otherServices = resultSet.getDouble("other_services");
            int totalTrips = resultSet.getInt("total_trips");

            // Calculate in Java
            double totalExpenses = tripExpenses + salaries + maintenance + partPurchases + otherServices;
            double netProfit = totalIncome - totalExpenses;

            MonthlyProfitDTO dto = new MonthlyProfitDTO(
                    monthYear,
                    totalIncome,
                    tripExpenses,
                    salaries,
                    maintenance,
                    partPurchases,
                    otherServices,
                    totalExpenses,
                    netProfit,
                    totalTrips
            );

            monthlyProfitList.add(dto);
        }

        return monthlyProfitList;
    }

    /**
     * Get monthly profit data for entire year (all 12 months)
     */
    public List<MonthlyProfitDTO> getYearlyMonthlyProfitData(int year)
            throws SQLException, ClassNotFoundException {
        List<MonthlyProfitDTO> monthlyProfitList = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            List<MonthlyProfitDTO> monthData = getMonthlyProfitData(month, year);

            if (monthData.isEmpty()) {
                String monthName = getMonthName(month) + " " + year;
                MonthlyProfitDTO emptyDto = new MonthlyProfitDTO(
                        monthName, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0
                );
                monthlyProfitList.add(emptyDto);
            } else {
                MonthlyProfitDTO dto = monthData.get(0);
                dto.setMonth(getMonthName(month) + " " + year);
                monthlyProfitList.add(dto);
            }
        }

        return monthlyProfitList;
    }

    /**
     * Get total income for a specific month from Trip table
     */
    public double getMonthlyIncome(int month, int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(total_income), 0) AS total_income " +
                "FROM Trip " +
                "WHERE MONTH(trip_date) = ? AND YEAR(trip_date) = ?";

        ResultSet resultSet = CrudUtil.execute(sql, month, year);

        if (resultSet.next()) {
            return resultSet.getDouble("total_income");
        }
        return 0.0;
    }

    /**
     * Get total expenses for a specific month (calculated from multiple tables)
     */
    public double getMonthlyExpenses(int month, int year) throws SQLException, ClassNotFoundException {
        double tripExpenses = getMonthlyTripExpenses(month, year);
        double salaries = getMonthlySalaries(month, year);
        double maintenance = getMonthlyMaintenance(month, year);
        double partPurchases = getMonthlyPartPurchases(month, year);
        double otherServices = getMonthlyOtherServices(month, year);

        return tripExpenses + salaries + maintenance + partPurchases + otherServices;
    }

    private double getMonthlyTripExpenses(int month, int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total " +
                "FROM Trip_Expenses " +
                "WHERE MONTH(date) = ? AND YEAR(date) = ?";
        ResultSet rs = CrudUtil.execute(sql, month, year);
        return rs.next() ? rs.getDouble("total") : 0.0;
    }

    private double getMonthlySalaries(int month, int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total " +
                "FROM Employee_Salary " +
                "WHERE MONTH(date) = ? AND YEAR(date) = ?";
        ResultSet rs = CrudUtil.execute(sql, month, year);
        return rs.next() ? rs.getDouble("total") : 0.0;
    }

    private double getMonthlyMaintenance(int month, int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(cost), 0) AS total " +
                "FROM Maintenance " +
                "WHERE MONTH(date) = ? AND YEAR(date) = ?";
        ResultSet rs = CrudUtil.execute(sql, month, year);
        return rs.next() ? rs.getDouble("total") : 0.0;
    }

    private double getMonthlyPartPurchases(int month, int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(total_cost), 0) AS total " +
                "FROM Part_Purchases " +
                "WHERE MONTH(date) = ? AND YEAR(date) = ?";
        ResultSet rs = CrudUtil.execute(sql, month, year);
        return rs.next() ? rs.getDouble("total") : 0.0;
    }

    private double getMonthlyOtherServices(int month, int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(cost), 0) AS total " +
                "FROM Other_Services " +
                "WHERE MONTH(date) = ? AND YEAR(date) = ?";
        ResultSet rs = CrudUtil.execute(sql, month, year);
        return rs.next() ? rs.getDouble("total") : 0.0;
    }

    /**
     * Get total trips for a specific month
     */
    public int getMonthlyTripCount(int month, int year) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) AS trip_count " +
                "FROM Trip " +
                "WHERE MONTH(trip_date) = ? AND YEAR(trip_date) = ?";

        ResultSet resultSet = CrudUtil.execute(sql, month, year);

        if (resultSet.next()) {
            return resultSet.getInt("trip_count");
        }
        return 0;
    }

    /**
     * Helper method to get month name
     */
    private String getMonthName(int month) {
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return months[month - 1];
    }
}