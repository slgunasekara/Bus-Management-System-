package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.MonthlyProfitBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.MonthlyProfitDAO;
import lk.ijse.busmanagementsystem.dto.DailyProfitDTO;
import lk.ijse.busmanagementsystem.dto.MonthlyProfitDTO;
import lk.ijse.busmanagementsystem.entity.DailyProfit;
import lk.ijse.busmanagementsystem.entity.MonthlyProfit;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class MonthlyProfitBOImpl implements MonthlyProfitBO {

    private final MonthlyProfitDAO monthlyProfitDAO =
            (MonthlyProfitDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.MONTHLY_PROFIT);

    @Override
    public List<MonthlyProfitDTO> getAllMonthlyProfit() throws SQLException, ClassNotFoundException {
        return monthlyProfitDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public MonthlyProfitDTO getMonthlyProfit(int year, int month) throws SQLException, ClassNotFoundException {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12!");
        }
        MonthlyProfit e = monthlyProfitDAO.getMonthlyProfit(year, month);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public List<DailyProfitDTO> getDailyBreakdownForMonth(int year, int month)
            throws SQLException, ClassNotFoundException {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12!");
        }
        return monthlyProfitDAO.getDailyBreakdownForMonth(year, month)
                .stream().map(this::toDailyDTO).collect(Collectors.toList());
    }


    private MonthlyProfitDTO toDTO(MonthlyProfit e) {
        MonthlyProfitDTO dto = new MonthlyProfitDTO();
        dto.setMonth(e.getMonth());
        dto.setTotalIncome(e.getTotalIncome());
        dto.setTripExpenses(e.getTripExpenses());
        dto.setSalaries(e.getSalaries());
        dto.setMaintenance(e.getMaintenance());
        dto.setPartPurchases(e.getPartPurchases());
        dto.setOtherServices(e.getOtherServices());
        dto.setTotalExpenses(e.getTotalExpenses());
        dto.setNetProfit(e.getNetProfit());
        dto.setTotalTrips(e.getTotalTrips());
        return dto;
    }

    private DailyProfitDTO toDailyDTO(DailyProfit e) {
        double totalExp = e.getTripExpenses() + e.getSalaries() + e.getMaintenance()
                + e.getPartPurchases() + e.getOtherServices();
        DailyProfitDTO dto = new DailyProfitDTO();
        dto.setDate(e.getDate());
        dto.setTotalIncome(e.getTotalIncome());
        dto.setTripExpenses(e.getTripExpenses());
        dto.setSalaries(e.getSalaries());
        dto.setMaintenance(e.getMaintenance());
        dto.setPartPurchases(e.getPartPurchases());
        dto.setOtherServices(e.getOtherServices());
        dto.setTotalExpenses(totalExp);
        dto.setNetProfit(e.getTotalIncome() - totalExp);
        dto.setTotalTrips(e.getTotalTrips());
        return dto;
    }
}
