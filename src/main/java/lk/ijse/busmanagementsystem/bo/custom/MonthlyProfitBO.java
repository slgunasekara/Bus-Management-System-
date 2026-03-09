package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.DailyProfitDTO;
import lk.ijse.busmanagementsystem.dto.MonthlyProfitDTO;

import java.sql.SQLException;
import java.util.List;

public interface MonthlyProfitBO extends SuperBO {
    MonthlyProfitDTO getMonthlyProfit(int year, int month)
            throws SQLException, ClassNotFoundException;
    List<DailyProfitDTO> getDailyBreakdownForMonth(int year, int month)
            throws SQLException, ClassNotFoundException;
    List<MonthlyProfitDTO> getAllMonthlyProfit()
            throws SQLException, ClassNotFoundException;
}