package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.DailyProfit;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface DailyProfitDAO extends CrudDAO<DailyProfit> {

    List<DailyProfit> getDailyProfitByDateRange(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException;

    DailyProfit getSummaryStats(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException;
}
