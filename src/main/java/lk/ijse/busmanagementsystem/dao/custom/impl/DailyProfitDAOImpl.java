package lk.ijse.busmanagementsystem.dao.impl;

import lk.ijse.busmanagementsystem.dto.DailyProfitDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyProfitDAOImpl {

    //Get daily profit data for a specific date range
    public List<DailyProfitDTO> getDailyProfitByDateRange(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {

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
                "ORDER BY DATE(t.trip_date) DESC";

        ResultSet rst = CrudUtil.execute(sql, fromDate, toDate);
        List<DailyProfitDTO> dailyProfitList = new ArrayList<>();

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

            dailyProfitList.add(dto);
        }

        return dailyProfitList;
    }

    //Get summary statistics for date range
    public DailyProfitDTO getSummaryStats(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {

        List<DailyProfitDTO> dailyData = getDailyProfitByDateRange(fromDate, toDate);

        double totalIncome = 0;
        double totalExpenses = 0;
        double tripExpenses = 0;
        double salaries = 0;
        double maintenance = 0;
        double partPurchases = 0;
        double otherServices = 0;
        int totalTrips = 0;

        for (DailyProfitDTO dto : dailyData) {
            totalIncome += dto.getTotalIncome();
            totalExpenses += dto.getTotalExpenses();
            tripExpenses += dto.getTripExpenses();
            salaries += dto.getSalaries();
            maintenance += dto.getMaintenance();
            partPurchases += dto.getPartPurchases();
            otherServices += dto.getOtherServices();
            totalTrips += dto.getTotalTrips();
        }

        double netProfit = totalIncome - totalExpenses;

        return new DailyProfitDTO(
                null, totalIncome, tripExpenses, salaries, maintenance,
                partPurchases, otherServices, totalExpenses, netProfit, totalTrips
        );
    }

    //Get all daily profit data
    public List<DailyProfitDTO> getAllDailyProfit()
            throws SQLException, ClassNotFoundException {

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
                "GROUP BY DATE(t.trip_date) " +
                "ORDER BY DATE(t.trip_date) DESC";

        ResultSet rst = CrudUtil.execute(sql);
        List<DailyProfitDTO> dailyProfitList = new ArrayList<>();

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

            dailyProfitList.add(dto);
        }

        return dailyProfitList;
    }
}