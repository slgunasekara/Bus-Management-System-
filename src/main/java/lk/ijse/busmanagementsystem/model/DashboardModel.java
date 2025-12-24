package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.dto.DashboardDTO;
import lk.ijse.busmanagementsystem.dto.DailyProfitDTO;
import lk.ijse.busmanagementsystem.dto.ProfitChartDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardModel {

    private final DailyProfitModel dailyProfitModel = new DailyProfitModel();

    /**
     * Get dashboard summary data (Total buses, trips, employees, net profit)
     * Uses DailyProfitModel for consistent profit calculation
     */
    public DashboardDTO getDashboardSummary() throws SQLException, ClassNotFoundException {
        DashboardDTO dto = new DashboardDTO();

        // Get Total Active Buses
        String busQuery = "SELECT COUNT(*) as total FROM Bus WHERE bus_status = 'Active'";
        ResultSet busRs = CrudUtil.execute(busQuery);
        if (busRs.next()) {
            dto.setTotalBuses(busRs.getInt("total"));
        }

        // Get Total Trips
        String tripQuery = "SELECT COUNT(*) as total FROM Trip";
        ResultSet tripRs = CrudUtil.execute(tripQuery);
        if (tripRs.next()) {
            dto.setTotalTrips(tripRs.getInt("total"));
        }

        // Get Total Active Employees
        String empQuery = "SELECT COUNT(*) as total FROM Employee WHERE emp_status = 'ACTIVE'";
        ResultSet empRs = CrudUtil.execute(empQuery);
        if (empRs.next()) {
            dto.setTotalEmployees(empRs.getInt("total"));
        }

        // Get Net Profit from DailyProfitModel (reusing existing logic)
        calculateNetProfitFromDailyData(dto);

        return dto;
    }

    /**
     * Calculate net profit using DailyProfitModel for consistency
     * This ensures dashboard shows the same values as the DailyProfit report
     */
    private void calculateNetProfitFromDailyData(DashboardDTO dto)
            throws SQLException, ClassNotFoundException {

        // Get all daily profit data
        List<DailyProfitDTO> allDailyData = dailyProfitModel.getAllDailyProfit();

        double totalIncome = 0;
        double totalExpenses = 0;

        // Sum up all daily data
        for (DailyProfitDTO dailyDto : allDailyData) {
            totalIncome += dailyDto.getTotalIncome();
            totalExpenses += dailyDto.getTotalExpenses();
        }

        double netProfit = totalIncome - totalExpenses;

        dto.setTotalIncome(totalIncome);
        dto.setTotalExpenses(totalExpenses);
        dto.setNetProfit(netProfit);
    }

    /**
     * Get profit chart data for the last N days
     * Uses DailyProfitModel for consistent data
     */
    public List<ProfitChartDTO> getProfitChartData(int days)
            throws SQLException, ClassNotFoundException {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        // Get daily profit data from DailyProfitModel
        List<DailyProfitDTO> dailyProfitList = dailyProfitModel.getDailyProfitByDateRange(startDate, endDate);

        // Convert to chart data
        List<ProfitChartDTO> chartData = new ArrayList<>();

        for (DailyProfitDTO dailyDto : dailyProfitList) {
            ProfitChartDTO chartDto = new ProfitChartDTO(
                    dailyDto.getDate(),
                    dailyDto.getTotalIncome(),
                    dailyDto.getTotalExpenses(),
                    dailyDto.getNetProfit()
            );
            chartData.add(chartDto);
        }

        return chartData;
    }

    /**
     * Get simplified profit chart data (last 30 days)
     * Creates entries for all 30 days, even if no data exists
     */
    public List<ProfitChartDTO> getSimplifiedProfitData()
            throws SQLException, ClassNotFoundException {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(29); // Last 30 days including today

        // Get actual data from DailyProfitModel
        List<DailyProfitDTO> actualData = dailyProfitModel.getDailyProfitByDateRange(startDate, today);

        // Create a list with entries for all 30 days
        List<ProfitChartDTO> chartData = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            LocalDate currentDate = startDate.plusDays(i);

            // Find matching data for this date
            DailyProfitDTO matchingData = actualData.stream()
                    .filter(dto -> dto.getDate().equals(currentDate))
                    .findFirst()
                    .orElse(null);

            if (matchingData != null) {
                // Use actual data
                chartData.add(new ProfitChartDTO(
                        matchingData.getDate(),
                        matchingData.getTotalIncome(),
                        matchingData.getTotalExpenses(),
                        matchingData.getNetProfit()
                ));
            } else {
                // Create empty entry for date with no data
                chartData.add(new ProfitChartDTO(currentDate, 0, 0, 0));
            }
        }

        return chartData;
    }

    /**
     * Alternative method: Get profit data with all days filled
     * This ensures the chart has continuous data points
     */
    public List<ProfitChartDTO> getContinuousProfitData(int days)
            throws SQLException, ClassNotFoundException {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        // Get actual data
        List<DailyProfitDTO> actualData = dailyProfitModel.getDailyProfitByDateRange(startDate, endDate);

        // Create continuous list
        List<ProfitChartDTO> chartData = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate currentDate = date;

            // Find data for this date
            DailyProfitDTO dayData = actualData.stream()
                    .filter(dto -> dto.getDate().equals(currentDate))
                    .findFirst()
                    .orElse(null);

            if (dayData != null) {
                chartData.add(new ProfitChartDTO(
                        dayData.getDate(),
                        dayData.getTotalIncome(),
                        dayData.getTotalExpenses(),
                        dayData.getNetProfit()
                ));
            } else {
                chartData.add(new ProfitChartDTO(currentDate, 0, 0, 0));
            }
        }

        return chartData;
    }
}