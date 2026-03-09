package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.DailyProfitDAO;
import lk.ijse.busmanagementsystem.entity.DailyProfit;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyProfitDAOImpl implements DailyProfitDAO {

    private static final String DAILY_SQL =
            "SELECT DATE(t.trip_date) as date, " +
                    "COALESCE(SUM(t.total_income), 0) as total_income, " +
                    "COALESCE(SUM(te.trip_expenses), 0) as trip_expenses, " +
                    "COALESCE(SUM(es.salaries), 0) as salaries, " +
                    "COALESCE(SUM(m.maintenance_cost), 0) as maintenance, " +
                    "COALESCE(SUM(pp.part_purchases), 0) as part_purchases, " +
                    "COALESCE(SUM(os.other_services), 0) as other_services, " +
                    "COUNT(DISTINCT t.trip_id) as total_trips " +
                    "FROM Trip t " +
                    "LEFT JOIN (SELECT trip_id, SUM(amount) as trip_expenses FROM Trip_Expenses GROUP BY trip_id) te ON t.trip_id = te.trip_id " +
                    "LEFT JOIN (SELECT DATE(date) as date, SUM(amount) as salaries FROM Employee_Salary GROUP BY DATE(date)) es ON DATE(t.trip_date) = es.date " +
                    "LEFT JOIN (SELECT DATE(date) as date, SUM(cost) as maintenance_cost FROM Maintenance GROUP BY DATE(date)) m ON DATE(t.trip_date) = m.date " +
                    "LEFT JOIN (SELECT DATE(date) as date, SUM(total_cost) as part_purchases FROM Part_Purchases GROUP BY DATE(date)) pp ON DATE(t.trip_date) = pp.date " +
                    "LEFT JOIN (SELECT DATE(date) as date, SUM(cost) as other_services FROM Other_Services GROUP BY DATE(date)) os ON DATE(t.trip_date) = os.date " +
                    "WHERE t.trip_date BETWEEN ? AND ? " +
                    "GROUP BY DATE(t.trip_date) ORDER BY DATE(t.trip_date) ASC";

    @Override
    public List<DailyProfit> getAll() throws SQLException, ClassNotFoundException {
        return getDailyProfitByDateRange(LocalDate.of(2000, 1, 1), LocalDate.now());
    }

    @Override
    public List<DailyProfit> getDailyProfitByDateRange(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(DAILY_SQL, fromDate, toDate);
        List<DailyProfit> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public DailyProfit getSummaryStats(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        List<DailyProfit> dailyList = getDailyProfitByDateRange(fromDate, toDate);
        double totalIncome = 0, tripExp = 0, salaries = 0, maintenance = 0, partPurch = 0, otherServ = 0;
        int totalTrips = 0;
        for (DailyProfit d : dailyList) {
            totalIncome += d.getTotalIncome();
            tripExp += d.getTripExpenses();
            salaries += d.getSalaries();
            maintenance += d.getMaintenance();
            partPurch += d.getPartPurchases();
            otherServ += d.getOtherServices();
            totalTrips += d.getTotalTrips();
        }
        double totalExp = tripExp + salaries + maintenance + partPurch + otherServ;
        DailyProfit summary = new DailyProfit();
        summary.setTotalIncome(totalIncome);
        summary.setTripExpenses(tripExp);
        summary.setSalaries(salaries);
        summary.setMaintenance(maintenance);
        summary.setPartPurchases(partPurch);
        summary.setOtherServices(otherServ);
        summary.setTotalTrips(totalTrips);
        // totalExpenses + netProfit computed via entity getters
        return summary;
    }

    // Not used — DailyProfit is read-only aggregate data
    @Override public boolean save(DailyProfit t) { return false; }
    @Override public boolean update(DailyProfit t) { return false; }
    @Override public boolean delete(String id) { return false; }
    @Override public boolean delete(int id) { return false; }
    @Override public boolean exists(String id) { return false; }
    @Override public boolean exists(int id) { return false; }
    @Override public DailyProfit search(String id) { return null; }

    private DailyProfit mapResultSet(ResultSet rst) throws SQLException {
        double ti = rst.getDouble("total_income"), te = rst.getDouble("trip_expenses"),
                s = rst.getDouble("salaries"), m = rst.getDouble("maintenance"),
                pp = rst.getDouble("part_purchases"), os = rst.getDouble("other_services");
        DailyProfit d = new DailyProfit();
        d.setDate(rst.getDate("date").toLocalDate());
        d.setTotalIncome(ti);
        d.setTripExpenses(te);
        d.setSalaries(s);
        d.setMaintenance(m);
        d.setPartPurchases(pp);
        d.setOtherServices(os);
        d.setTotalTrips(rst.getInt("total_trips"));
        // NOTE: totalExpenses and netProfit are COMPUTED in entity:
        //   getTotalExpenses() = tripExpenses + salaries + maintenance + partPurchases + otherServices
        //   getNetProfit()     = totalIncome - getTotalExpenses()
        return d;
    }
}
