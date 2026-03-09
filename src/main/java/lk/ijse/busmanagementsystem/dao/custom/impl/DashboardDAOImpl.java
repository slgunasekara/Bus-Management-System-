package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.SuperDAO;
import lk.ijse.busmanagementsystem.dao.custom.DashboardDAO;
import lk.ijse.busmanagementsystem.entity.Dashboard;
import lk.ijse.busmanagementsystem.entity.DailyProfit;
import lk.ijse.busmanagementsystem.entity.ProfitChart;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAOImpl implements SuperDAO {

    private final DailyProfitDAOImpl dailyProfitDAO = new DailyProfitDAOImpl();

    public Dashboard getDashboardSummary() throws SQLException, ClassNotFoundException {
        Dashboard dashboard = new Dashboard();

        ResultSet busRs = CrudUtil.execute("SELECT COUNT(*) as total FROM Bus WHERE bus_status = 'Active'");
        if (busRs.next()) dashboard.setTotalBuses(busRs.getInt("total"));

        ResultSet tripRs = CrudUtil.execute("SELECT COUNT(*) as total FROM Trip");
        if (tripRs.next()) dashboard.setTotalTrips(tripRs.getInt("total"));

        ResultSet empRs = CrudUtil.execute("SELECT COUNT(*) as total FROM Employee WHERE emp_status = 'ACTIVE'");
        if (empRs.next()) dashboard.setTotalEmployees(empRs.getInt("total"));

        List<DailyProfit> allData = dailyProfitDAO.getAll();
        double totalIncome = 0, totalExpenses = 0;
        for (DailyProfit d : allData) {
            totalIncome   += d.getTotalIncome();
            totalExpenses += d.getTripExpenses() + d.getSalaries() + d.getMaintenance()
                    + d.getPartPurchases() + d.getOtherServices();
        }
        dashboard.setTotalIncome(totalIncome);
        dashboard.setTotalExpenses(totalExpenses);
        dashboard.setNetProfit(totalIncome - totalExpenses);

        return dashboard;
    }

    public List<ProfitChart> getProfitChartData(int days) throws SQLException, ClassNotFoundException {
        LocalDate endDate   = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        List<DailyProfit> dailyList = dailyProfitDAO.getDailyProfitByDateRange(startDate, endDate);

        List<ProfitChart> chartData = new ArrayList<>();
        for (DailyProfit d : dailyList) {
            double expense = d.getTripExpenses() + d.getSalaries() + d.getMaintenance()
                    + d.getPartPurchases() + d.getOtherServices();
            double profit  = d.getTotalIncome() - expense;
            // ProfitChart(date, income, expense, profit) — same field order as ProfitChartDTO
            chartData.add(new ProfitChart(d.getDate(), d.getTotalIncome(), expense, profit));
        }
        return chartData;
    }

    public List<ProfitChart> getSimplifiedProfitData() throws SQLException, ClassNotFoundException {
        LocalDate today     = LocalDate.now();
        LocalDate startDate = today.minusDays(29);
        List<DailyProfit> actualData = dailyProfitDAO.getDailyProfitByDateRange(startDate, today);

        List<ProfitChart> chartData = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            DailyProfit match = actualData.stream()
                    .filter(d -> d.getDate().equals(currentDate))
                    .findFirst().orElse(null);

            if (match != null) {
                double expense = match.getTripExpenses() + match.getSalaries() + match.getMaintenance()
                        + match.getPartPurchases() + match.getOtherServices();
                double profit  = match.getTotalIncome() - expense;
                chartData.add(new ProfitChart(currentDate, match.getTotalIncome(), expense, profit));
            } else {
                chartData.add(new ProfitChart(currentDate, 0, 0, 0));
            }
        }
        return chartData;
    }

}
