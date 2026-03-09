package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.ReportDTO;
import lk.ijse.busmanagementsystem.dto.TripDTO;
import lk.ijse.busmanagementsystem.dto.TripExpensesDTO;
import lk.ijse.busmanagementsystem.tm.EmployeeSalaryTM;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ReportBO extends SuperBO {
    ReportDTO getReportSummary(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException;

    List<TripDTO> getIncomeReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException;

    List<TripExpensesDTO> getExpenseReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException;

    List<EmployeeSalaryTM> getSalaryReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException;

    List<TripDTO> getTripReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException;

    String getBusNumberById(int busId) throws SQLException, ClassNotFoundException;
}
