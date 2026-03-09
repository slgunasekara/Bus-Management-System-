package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.DashboardDTO;
import lk.ijse.busmanagementsystem.dto.ProfitChartDTO;

import java.sql.SQLException;
import java.util.List;

public interface DashboardBO extends SuperBO {
    DashboardDTO getDashboardSummary() throws SQLException, ClassNotFoundException;
    List<ProfitChartDTO> getSimplifiedProfitData() throws SQLException, ClassNotFoundException;
    List<ProfitChartDTO> getProfitChartData(int days) throws SQLException, ClassNotFoundException;
}
