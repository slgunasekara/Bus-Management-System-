package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.DashboardBO;
import lk.ijse.busmanagementsystem.dao.custom.impl.DashboardDAOImpl;
import lk.ijse.busmanagementsystem.dto.DashboardDTO;
import lk.ijse.busmanagementsystem.dto.ProfitChartDTO;
import lk.ijse.busmanagementsystem.entity.Dashboard;
import lk.ijse.busmanagementsystem.entity.ProfitChart;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardBOImpl implements DashboardBO {

    private final DashboardDAOImpl dashboardDAO = new DashboardDAOImpl();

    @Override
    public DashboardDTO getDashboardSummary() throws SQLException, ClassNotFoundException {
        Dashboard e = dashboardDAO.getDashboardSummary();
        return toDTO(e);
    }

    @Override
    public List<ProfitChartDTO> getSimplifiedProfitData() throws SQLException, ClassNotFoundException {
        return dashboardDAO.getSimplifiedProfitData()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProfitChartDTO> getProfitChartData(int days) throws SQLException, ClassNotFoundException {
        if (days <= 0) days = 30;
        return dashboardDAO.getProfitChartData(days)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }


    private DashboardDTO toDTO(Dashboard e) {
        DashboardDTO dto = new DashboardDTO();
        dto.setTotalBuses(e.getTotalBuses());
        dto.setTotalTrips(e.getTotalTrips());
        dto.setTotalEmployees(e.getTotalEmployees());
        dto.setTotalIncome(e.getTotalIncome());
        dto.setTotalExpenses(e.getTotalExpenses());
        dto.setNetProfit(e.getNetProfit());
        return dto;
    }

    private ProfitChartDTO toDTO(ProfitChart e) {
        return new ProfitChartDTO(e.getDate(), e.getIncome(), e.getExpense(), e.getProfit());
    }
}
