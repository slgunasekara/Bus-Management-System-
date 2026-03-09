package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.MonthlyProfitDAO;
import lk.ijse.busmanagementsystem.entity.DailyProfit;
import lk.ijse.busmanagementsystem.entity.MonthlyProfit;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class MonthlyProfitDAOImpl implements MonthlyProfitDAO {

    private static final String PROFIT_JOINS =
            "FROM Trip t " +
                    "LEFT JOIN (SELECT trip_id, SUM(amount) as trip_expenses FROM Trip_Expenses GROUP BY trip_id) te ON t.trip_id = te.trip_id " +
                    "LEFT JOIN (SELECT DATE(date) as date, SUM(amount) as salaries FROM Employee_Salary GROUP BY DATE(date)) es ON DATE(t.trip_date) = es.date " +
                    "LEFT JOIN (SELECT DATE(date) as date, SUM(cost) as maintenance_cost FROM Maintenance GROUP BY DATE(date)) m ON DATE(t.trip_date) = m.date " +
                    "LEFT JOIN (SELECT DATE(date) as date, SUM(total_cost) as part_purchases FROM Part_Purchases GROUP BY DATE(date)) pp ON DATE(t.trip_date) = pp.date " +
                    "LEFT JOIN (SELECT DATE(date) as date, SUM(cost) as other_services FROM Other_Services GROUP BY DATE(date)) os ON DATE(t.trip_date) = os.date ";

    @Override
    public List<MonthlyProfit> getAll() throws SQLException, ClassNotFoundException {
        String sql = "SELECT YEAR(t.trip_date) as year, MONTH(t.trip_date) as month, " +
                "COALESCE(SUM(t.total_income), 0) as total_income, " +
                "COALESCE(SUM(te.trip_expenses), 0) as trip_expenses, " +
                "COALESCE(SUM(es.salaries), 0) as salaries, " +
                "COALESCE(SUM(m.maintenance_cost), 0) as maintenance, " +
                "COALESCE(SUM(pp.part_purchases), 0) as part_purchases, " +
                "COALESCE(SUM(os.other_services), 0) as other_services, " +
                "COUNT(DISTINCT t.trip_id) as total_trips " +
                PROFIT_JOINS +
                "GROUP BY YEAR(t.trip_date), MONTH(t.trip_date) ORDER BY YEAR(t.trip_date) DESC, MONTH(t.trip_date) DESC";
        ResultSet rst = CrudUtil.execute(sql);
        List<MonthlyProfit> list = new ArrayList<>();
        while (rst.next()) list.add(buildMonthly(rst, YearMonth.of(rst.getInt("year"), rst.getInt("month"))));
        return list;
    }

    @Override
    public MonthlyProfit getMonthlyProfit(int year, int month) throws SQLException, ClassNotFoundException {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        String sql = "SELECT COALESCE(SUM(t.total_income), 0) as total_income, " +
                "COALESCE(SUM(te.trip_expenses), 0) as trip_expenses, " +
                "COALESCE(SUM(es.salaries), 0) as salaries, " +
                "COALESCE(SUM(m.maintenance_cost), 0) as maintenance, " +
                "COALESCE(SUM(pp.part_purchases), 0) as part_purchases, " +
                "COALESCE(SUM(os.other_services), 0) as other_services, " +
                "COUNT(DISTINCT t.trip_id) as total_trips " +
                PROFIT_JOINS + "WHERE t.trip_date BETWEEN ? AND ?";
        ResultSet rst = CrudUtil.execute(sql, firstDay, lastDay);
        if (rst.next()) return buildMonthly(rst, YearMonth.of(year, month));
        return null;
    }

    @Override
    public List<DailyProfit> getDailyBreakdownForMonth(int year, int month) throws SQLException, ClassNotFoundException {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        String sql = "SELECT DATE(t.trip_date) as date, " +
                "COALESCE(SUM(t.total_income), 0) as total_income, " +
                "COALESCE(SUM(te.trip_expenses), 0) as trip_expenses, " +
                "COALESCE(SUM(es.salaries), 0) as salaries, " +
                "COALESCE(SUM(m.maintenance_cost), 0) as maintenance, " +
                "COALESCE(SUM(pp.part_purchases), 0) as part_purchases, " +
                "COALESCE(SUM(os.other_services), 0) as other_services, " +
                "COUNT(DISTINCT t.trip_id) as total_trips " +
                PROFIT_JOINS + "WHERE t.trip_date BETWEEN ? AND ? " +
                "GROUP BY DATE(t.trip_date) ORDER BY DATE(t.trip_date) ASC";
        ResultSet rst = CrudUtil.execute(sql, firstDay, lastDay);
        List<DailyProfit> list = new ArrayList<>();
        while (rst.next()) {
            double ti = rst.getDouble("total_income"), te = rst.getDouble("trip_expenses"),
                    s = rst.getDouble("salaries"), m = rst.getDouble("maintenance"),
                    pp = rst.getDouble("part_purchases"), os = rst.getDouble("other_services");
            double totalExp = te + s + m + pp + os;
            DailyProfit d = new DailyProfit();
            d.setDate(rst.getDate("date").toLocalDate());
            d.setTotalIncome(ti); d.setTripExpenses(te); d.setSalaries(s);
            d.setMaintenance(m); d.setPartPurchases(pp); d.setOtherServices(os);
            d.setTotalExpenses(totalExp); d.setNetProfit(ti - totalExp);
            d.setTotalTrips(rst.getInt("total_trips"));
            list.add(d);
        }
        return list;
    }

    // Not used — MonthlyProfit is read-only aggregate data
    @Override public boolean save(MonthlyProfit t) { return false; }
    @Override public boolean update(MonthlyProfit t) { return false; }
    @Override public boolean delete(String id) { return false; }
    @Override public boolean delete(int id) { return false; }
    @Override public boolean exists(String id) { return false; }
    @Override public boolean exists(int id) { return false; }
    @Override public MonthlyProfit search(String id) { return null; }

    private MonthlyProfit buildMonthly(ResultSet rst, YearMonth ym) throws SQLException {
        double ti = rst.getDouble("total_income"), te = rst.getDouble("trip_expenses"),
                s = rst.getDouble("salaries"), m = rst.getDouble("maintenance"),
                pp = rst.getDouble("part_purchases"), os = rst.getDouble("other_services");
        double totalExp = te + s + m + pp + os;
        MonthlyProfit mp = new MonthlyProfit();
        mp.setMonth(ym);
        mp.setTotalIncome(ti); mp.setTripExpenses(te); mp.setSalaries(s);
        mp.setMaintenance(m); mp.setPartPurchases(pp); mp.setOtherServices(os);
        mp.setTotalExpenses(totalExp); mp.setNetProfit(ti - totalExp);
        mp.setTotalTrips(rst.getInt("total_trips"));
        return mp;
    }
}
