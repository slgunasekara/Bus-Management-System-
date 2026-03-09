package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.DailyProfitDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface DailyProfitBO extends SuperBO {
    List<DailyProfitDTO> getDailyProfitByDateRange(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException;
    DailyProfitDTO getSummaryStats(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException;
    List<DailyProfitDTO> getAllDailyProfit()
            throws SQLException, ClassNotFoundException;
}