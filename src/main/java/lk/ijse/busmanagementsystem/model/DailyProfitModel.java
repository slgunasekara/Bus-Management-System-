package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.dto.DailyProfitDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyProfitModel {

    /**
     * Get daily profit data for a date range by joining multiple tables
     */
    public List<DailyProfitDTO> getDailyProfitData(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        List<DailyProfitDTO> dailyProfitList = new ArrayList<>();

        String query = "SELECT " +
                "DATE(t.trip_date) AS date, " +
                "COALESCE(SUM(t.total_income), 0) AS total_income, " +
                "COALESCE(SUM(te.trip_exp_amount), 0) AS trip_expenses, " +
                "COALESCE(SUM(es.salary_amount), 0) AS salaries, " +
                "COALESCE(SUM(m.maintenance_cost), 0) AS maintenance, " +
                "COALESCE(SUM(pp.part_cost), 0) AS part_purchases, " +
                "COALESCE(SUM(os.service_cost), 0) AS other_services " +
                "FROM Trip t " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) AS exp_date, SUM(amount) AS trip_exp_amount " +
                "    FROM Trip_Expenses " +
                "    WHERE DATE(date) BETWEEN ? AND ? " +
                "    GROUP BY DATE(date) " +
                ") te ON DATE(t.trip_date) = te.exp_date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) AS sal_date, SUM(amount) AS salary_amount " +
                "    FROM Employee_Salary " +
                "    WHERE DATE(date) BETWEEN ? AND ? " +
                "    GROUP BY DATE(date) " +
                ") es ON DATE(t.trip_date) = es.sal_date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) AS maint_date, SUM(cost) AS maintenance_cost " +
                "    FROM Maintenance " +
                "    WHERE DATE(date) BETWEEN ? AND ? " +
                "    GROUP BY DATE(date) " +
                ") m ON DATE(t.trip_date) = m.maint_date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) AS part_date, SUM(total_cost) AS part_cost " +
                "    FROM Part_Purchases " +
                "    WHERE DATE(date) BETWEEN ? AND ? " +
                "    GROUP BY DATE(date) " +
                ") pp ON DATE(t.trip_date) = pp.part_date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) AS service_date, SUM(cost) AS service_cost " +
                "    FROM Other_Services " +
                "    WHERE DATE(date) BETWEEN ? AND ? " +
                "    GROUP BY DATE(date) " +
                ") os ON DATE(t.trip_date) = os.service_date " +
                "WHERE DATE(t.trip_date) BETWEEN ? AND ? " +
                "GROUP BY DATE(t.trip_date) " +
                "ORDER BY DATE(t.trip_date) ASC";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(query);

        // Set parameters (12 parameters - 6 pairs of from/to dates)
        String fromDateStr = fromDate.toString();
        String toDateStr = toDate.toString();

        pstm.setString(1, fromDateStr);
        pstm.setString(2, toDateStr);
        pstm.setString(3, fromDateStr);
        pstm.setString(4, toDateStr);
        pstm.setString(5, fromDateStr);
        pstm.setString(6, toDateStr);
        pstm.setString(7, fromDateStr);
        pstm.setString(8, toDateStr);
        pstm.setString(9, fromDateStr);
        pstm.setString(10, toDateStr);
        pstm.setString(11, fromDateStr);
        pstm.setString(12, toDateStr);

        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            String date = resultSet.getString("date");
            double totalIncome = resultSet.getDouble("total_income");
            double tripExpenses = resultSet.getDouble("trip_expenses");
            double salaries = resultSet.getDouble("salaries");
            double maintenance = resultSet.getDouble("maintenance");
            double partPurchases = resultSet.getDouble("part_purchases");
            double otherServices = resultSet.getDouble("other_services");

            // Calculate in Java
            double totalExpenses = tripExpenses + salaries + maintenance + partPurchases + otherServices;
            double netProfit = totalIncome - totalExpenses;

            DailyProfitDTO dto = new DailyProfitDTO(
                    date,
                    totalIncome,
                    tripExpenses,
                    salaries,
                    maintenance,
                    partPurchases,
                    otherServices,
                    totalExpenses,
                    netProfit
            );

            dailyProfitList.add(dto);
        }

        return dailyProfitList;
    }

    /**
     * Get daily profit data for a specific date
     */
    public DailyProfitDTO getDailyProfitDataForDate(LocalDate date)
            throws SQLException, ClassNotFoundException {
        List<DailyProfitDTO> result = getDailyProfitData(date, date);

        if (result.isEmpty()) {
            return new DailyProfitDTO(
                    date.toString(), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
            );
        }

        return result.get(0);
    }

    /**
     * Get total income for a specific date from Trip table
     */
    public double getDailyIncome(LocalDate date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(total_income), 0) AS total_income " +
                "FROM Trip " +
                "WHERE DATE(trip_date) = ?";

        ResultSet resultSet = CrudUtil.execute(sql, date.toString());

        if (resultSet.next()) {
            return resultSet.getDouble("total_income");
        }
        return 0.0;
    }

    /**
     * Get total expenses for a specific date (calculated from multiple tables)
     */
    public double getDailyExpenses(LocalDate date) throws SQLException, ClassNotFoundException {
        double tripExpenses = getDailyTripExpenses(date);
        double salaries = getDailySalaries(date);
        double maintenance = getDailyMaintenance(date);
        double partPurchases = getDailyPartPurchases(date);
        double otherServices = getDailyOtherServices(date);

        return tripExpenses + salaries + maintenance + partPurchases + otherServices;
    }

    private double getDailyTripExpenses(LocalDate date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total " +
                "FROM Trip_Expenses " +
                "WHERE DATE(date) = ?";
        ResultSet rs = CrudUtil.execute(sql, date.toString());
        return rs.next() ? rs.getDouble("total") : 0.0;
    }

    private double getDailySalaries(LocalDate date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total " +
                "FROM Employee_Salary " +
                "WHERE DATE(date) = ?";
        ResultSet rs = CrudUtil.execute(sql, date.toString());
        return rs.next() ? rs.getDouble("total") : 0.0;
    }

    private double getDailyMaintenance(LocalDate date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(cost), 0) AS total " +
                "FROM Maintenance " +
                "WHERE DATE(date) = ?";
        ResultSet rs = CrudUtil.execute(sql, date.toString());
        return rs.next() ? rs.getDouble("total") : 0.0;
    }

    private double getDailyPartPurchases(LocalDate date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(total_cost), 0) AS total " +
                "FROM Part_Purchases " +
                "WHERE DATE(date) = ?";
        ResultSet rs = CrudUtil.execute(sql, date.toString());
        return rs.next() ? rs.getDouble("total") : 0.0;
    }

    private double getDailyOtherServices(LocalDate date) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COALESCE(SUM(cost), 0) AS total " +
                "FROM Other_Services " +
                "WHERE DATE(date) = ?";
        ResultSet rs = CrudUtil.execute(sql, date.toString());
        return rs.next() ? rs.getDouble("total") : 0.0;
    }

    /**
     * Get net profit for a specific date (calculated)
     */
    public double getDailyNetProfit(LocalDate date) throws SQLException, ClassNotFoundException {
        double income = getDailyIncome(date);
        double expenses = getDailyExpenses(date);
        return income - expenses;
    }

    /**
     * Get average daily profit for a date range (calculated)
     */
    public double getAverageDailyProfit(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        List<DailyProfitDTO> dailyData = getDailyProfitData(fromDate, toDate);

        if (dailyData.isEmpty()) {
            return 0.0;
        }

        double totalProfit = 0;
        for (DailyProfitDTO data : dailyData) {
            totalProfit += data.getNetProfit();
        }

        return totalProfit / dailyData.size();
    }
}