package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.DailyProfitBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.DailyProfitDAO;
import lk.ijse.busmanagementsystem.dto.DailyProfitDTO;
import lk.ijse.busmanagementsystem.entity.DailyProfit;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DailyProfitBOImpl implements DailyProfitBO {

    private final DailyProfitDAO dailyProfitDAO =
            (DailyProfitDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.DAILY_PROFIT);

    @Override
    public List<DailyProfitDTO> getAllDailyProfit() throws SQLException, ClassNotFoundException {
        return dailyProfitDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<DailyProfitDTO> getDailyProfitByDateRange(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date cannot be after To date!");
        }
        return dailyProfitDAO.getDailyProfitByDateRange(fromDate, toDate)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public DailyProfitDTO getSummaryStats(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date cannot be after To date!");
        }
        DailyProfit summary = dailyProfitDAO.getSummaryStats(fromDate, toDate);
        return summary != null ? toDTO(summary) : null;
    }


    private DailyProfitDTO toDTO(DailyProfit e) {
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
