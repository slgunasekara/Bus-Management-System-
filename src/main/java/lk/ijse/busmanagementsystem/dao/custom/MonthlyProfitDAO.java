package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.DailyProfit;
import lk.ijse.busmanagementsystem.entity.MonthlyProfit;

import java.sql.SQLException;
import java.util.List;

public interface MonthlyProfitDAO extends CrudDAO<MonthlyProfit> {

    MonthlyProfit getMonthlyProfit(int year, int month)
            throws SQLException, ClassNotFoundException;

    List<DailyProfit> getDailyBreakdownForMonth(int year, int month)
            throws SQLException, ClassNotFoundException;
}
