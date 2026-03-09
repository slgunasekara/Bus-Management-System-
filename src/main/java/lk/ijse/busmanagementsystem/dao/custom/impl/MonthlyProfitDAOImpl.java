package lk.ijse.busmanagementsystem.dao.impl;

import lk.ijse.busmanagementsystem.dto.DailyProfitDTO;
import lk.ijse.busmanagementsystem.dto.MonthlyProfitDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class MonthlyProfitDAOImpl {

    //Get monthly profit data for a specific year and month
    public MonthlyProfitDTO getMonthlyProfit(int year, int month)
            throws SQLException, ClassNotFoundException {

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        String sql = "SELECT " +
                "    COALESCE(SUM(t.total_income), 0) as total_income, " +
                "    COALESCE(SUM(te.trip_expenses), 0) as trip_expenses, " +
                "    COALESCE(SUM(es.salaries), 0) as salaries, " +
                "    COALESCE(SUM(m.maintenance_cost), 0) as maintenance, " +
                "    COALESCE(SUM(pp.part_purchases), 0) as part_purchases, " +
                "    COALESCE(SUM(os.other_services), 0) as other_services, " +
                "    COUNT(DISTINCT t.trip_id) as total_trips " +
                "FROM Trip t " +
                "LEFT JOIN ( " +
                "    SELECT trip_id, SUM(amount) as trip_expenses " +
                "    FROM Trip_Expenses " +
                "    GROUP BY trip_id " +
                ") te ON t.trip_id = te.trip_id " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(amount) as salaries " +
                "    FROM Employee_Salary " +
                "    GROUP BY DATE(date) " +
                ") es ON DATE(t.trip_date) = es.date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(cost) as maintenance_cost " +
                "    FROM Maintenance " +
                "    GROUP BY DATE(date) " +
                ") m ON DATE(t.trip_date) = m.date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(total_cost) as part_purchases " +
                "    FROM Part_Purchases " +
                "    GROUP BY DATE(date) " +
                ") pp ON DATE(t.trip_date) = pp.date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(cost) as other_services " +
                "    FROM Other_Services " +
                "    GROUP BY DATE(date) " +
                ") os ON DATE(t.trip_date) = os.date " +
                "WHERE t.trip_date BETWEEN ? AND ?";

        ResultSet rst = CrudUtil.execute(sql, firstDay, lastDay);

        if (rst.next()) {
            double totalIncome = rst.getDouble("total_income");
            double tripExpenses = rst.getDouble("trip_expenses");
            double salaries = rst.getDouble("salaries");
            double maintenance = rst.getDouble("maintenance");
            double partPurchases = rst.getDouble("part_purchases");
            double otherServices = rst.getDouble("other_services");
            int totalTrips = rst.getInt("total_trips");

            double totalExpenses = tripExpenses + salaries + maintenance + partPurchases + otherServices;
            double netProfit = totalIncome - totalExpenses;

            return new MonthlyProfitDTO(
                    YearMonth.of(year, month), totalIncome, tripExpenses, salaries,
                    maintenance, partPurchases, otherServices, totalExpenses, netProfit, totalTrips
            );
        }

        return null;
    }

    //Get daily breakdown for a specific month
    public List<DailyProfitDTO> getDailyBreakdownForMonth(int year, int month)
            throws SQLException, ClassNotFoundException {

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        String sql = "SELECT " +
                "    DATE(t.trip_date) as date, " +
                "    COALESCE(SUM(t.total_income), 0) as total_income, " +
                "    COALESCE(SUM(te.trip_expenses), 0) as trip_expenses, " +
                "    COALESCE(SUM(es.salaries), 0) as salaries, " +
                "    COALESCE(SUM(m.maintenance_cost), 0) as maintenance, " +
                "    COALESCE(SUM(pp.part_purchases), 0) as part_purchases, " +
                "    COALESCE(SUM(os.other_services), 0) as other_services, " +
                "    COUNT(DISTINCT t.trip_id) as total_trips " +
                "FROM Trip t " +
                "LEFT JOIN ( " +
                "    SELECT trip_id, SUM(amount) as trip_expenses " +
                "    FROM Trip_Expenses " +
                "    GROUP BY trip_id " +
                ") te ON t.trip_id = te.trip_id " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(amount) as salaries " +
                "    FROM Employee_Salary " +
                "    GROUP BY DATE(date) " +
                ") es ON DATE(t.trip_date) = es.date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(cost) as maintenance_cost " +
                "    FROM Maintenance " +
                "    GROUP BY DATE(date) " +
                ") m ON DATE(t.trip_date) = m.date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(total_cost) as part_purchases " +
                "    FROM Part_Purchases " +
                "    GROUP BY DATE(date) " +
                ") pp ON DATE(t.trip_date) = pp.date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(cost) as other_services " +
                "    FROM Other_Services " +
                "    GROUP BY DATE(date) " +
                ") os ON DATE(t.trip_date) = os.date " +
                "WHERE t.trip_date BETWEEN ? AND ? " +
                "GROUP BY DATE(t.trip_date) " +
                "ORDER BY DATE(t.trip_date) ASC";

        ResultSet rst = CrudUtil.execute(sql, firstDay, lastDay);
        List<DailyProfitDTO> dailyList = new ArrayList<>();

        while (rst.next()) {
            LocalDate date = rst.getDate("date").toLocalDate();
            double totalIncome = rst.getDouble("total_income");
            double tripExpenses = rst.getDouble("trip_expenses");
            double salaries = rst.getDouble("salaries");
            double maintenance = rst.getDouble("maintenance");
            double partPurchases = rst.getDouble("part_purchases");
            double otherServices = rst.getDouble("other_services");
            int totalTrips = rst.getInt("total_trips");

            double totalExpenses = tripExpenses + salaries + maintenance + partPurchases + otherServices;
            double netProfit = totalIncome - totalExpenses;

            DailyProfitDTO dto = new DailyProfitDTO(
                    date, totalIncome, tripExpenses, salaries, maintenance,
                    partPurchases, otherServices, totalExpenses, netProfit, totalTrips
            );

            dailyList.add(dto);
        }

        return dailyList;
    }

    //Get all monthly profit data
    public List<MonthlyProfitDTO> getAllMonthlyProfit()
            throws SQLException, ClassNotFoundException {

        String sql = "SELECT " +
                "    YEAR(t.trip_date) as year, " +
                "    MONTH(t.trip_date) as month, " +
                "    COALESCE(SUM(t.total_income), 0) as total_income, " +
                "    COALESCE(SUM(te.trip_expenses), 0) as trip_expenses, " +
                "    COALESCE(SUM(es.salaries), 0) as salaries, " +
                "    COALESCE(SUM(m.maintenance_cost), 0) as maintenance, " +
                "    COALESCE(SUM(pp.part_purchases), 0) as part_purchases, " +
                "    COALESCE(SUM(os.other_services), 0) as other_services, " +
                "    COUNT(DISTINCT t.trip_id) as total_trips " +
                "FROM Trip t " +
                "LEFT JOIN ( " +
                "    SELECT trip_id, SUM(amount) as trip_expenses " +
                "    FROM Trip_Expenses " +
                "    GROUP BY trip_id " +
                ") te ON t.trip_id = te.trip_id " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(amount) as salaries " +
                "    FROM Employee_Salary " +
                "    GROUP BY DATE(date) " +
                ") es ON DATE(t.trip_date) = es.date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(cost) as maintenance_cost " +
                "    FROM Maintenance " +
                "    GROUP BY DATE(date) " +
                ") m ON DATE(t.trip_date) = m.date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(total_cost) as part_purchases " +
                "    FROM Part_Purchases " +
                "    GROUP BY DATE(date) " +
                ") pp ON DATE(t.trip_date) = pp.date " +
                "LEFT JOIN ( " +
                "    SELECT DATE(date) as date, SUM(cost) as other_services " +
                "    FROM Other_Services " +
                "    GROUP BY DATE(date) " +
                ") os ON DATE(t.trip_date) = os.date " +
                "GROUP BY YEAR(t.trip_date), MONTH(t.trip_date) " +
                "ORDER BY YEAR(t.trip_date) DESC, MONTH(t.trip_date) DESC";

        ResultSet rst = CrudUtil.execute(sql);
        List<MonthlyProfitDTO> monthlyList = new ArrayList<>();

        while (rst.next()) {
            int year = rst.getInt("year");
            int month = rst.getInt("month");
            double totalIncome = rst.getDouble("total_income");
            double tripExpenses = rst.getDouble("trip_expenses");
            double salaries = rst.getDouble("salaries");
            double maintenance = rst.getDouble("maintenance");
            double partPurchases = rst.getDouble("part_purchases");
            double otherServices = rst.getDouble("other_services");
            int totalTrips = rst.getInt("total_trips");

            double totalExpenses = tripExpenses + salaries + maintenance + partPurchases + otherServices;
            double netProfit = totalIncome - totalExpenses;

            MonthlyProfitDTO dto = new MonthlyProfitDTO(
                    YearMonth.of(year, month), totalIncome, tripExpenses, salaries,
                    maintenance, partPurchases, otherServices, totalExpenses, netProfit, totalTrips
            );

            monthlyList.add(dto);
        }

        return monthlyList;
    }
}