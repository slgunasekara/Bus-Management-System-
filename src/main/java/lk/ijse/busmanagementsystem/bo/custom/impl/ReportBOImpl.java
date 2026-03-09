package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.ReportBO;
import lk.ijse.busmanagementsystem.dao.custom.impl.ReportDAOImpl;
import lk.ijse.busmanagementsystem.dto.ReportDTO;
import lk.ijse.busmanagementsystem.dto.TripDTO;
import lk.ijse.busmanagementsystem.dto.TripExpensesDTO;
import lk.ijse.busmanagementsystem.entity.Report;
import lk.ijse.busmanagementsystem.entity.Trip;
import lk.ijse.busmanagementsystem.entity.TripExpenses;
import lk.ijse.busmanagementsystem.tm.EmployeeSalaryTM;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReportBOImpl implements ReportBO {

    private final ReportDAOImpl reportDAO = new ReportDAOImpl();

    @Override
    public ReportDTO getReportSummary(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date cannot be after To date!");
        }
        Report e = reportDAO.getReportSummary(fromDate, toDate);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public List<TripDTO> getIncomeReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date cannot be after To date!");
        }
        return reportDAO.getIncomeReport(fromDate, toDate)
                .stream().map(this::tripToDTO).collect(Collectors.toList());
    }

    @Override
    public List<TripExpensesDTO> getExpenseReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date cannot be after To date!");
        }
        return reportDAO.getExpenseReport(fromDate, toDate)
                .stream().map(this::expToDTO).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeSalaryTM> getSalaryReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date cannot be after To date!");
        }
        // ReportDAOImpl returns EmployeeSalaryTM directly (join result — no entity needed)
        return reportDAO.getSalaryReport(fromDate, toDate);
    }

    @Override
    public List<TripDTO> getTripReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date cannot be after To date!");
        }
        return reportDAO.getTripReport(fromDate, toDate)
                .stream().map(this::tripToDTO).collect(Collectors.toList());
    }

    @Override
    public String getBusNumberById(int busId) throws SQLException, ClassNotFoundException {
        return reportDAO.getBusNumberById(busId);
    }

    private ReportDTO toDTO(Report e) {
        ReportDTO dto = new ReportDTO();
        dto.setTotalIncome(e.getTotalIncome());
        dto.setTotalExpenses(e.getTotalExpenses());
        dto.setTotalSalary(e.getTotalSalary());
        dto.setNetProfit(e.getNetProfit());
        dto.setTotalTrips(e.getTotalTrips());
        return dto;
    }

    private TripDTO tripToDTO(Trip e) {
        TripDTO dto = new TripDTO();
        dto.setTripId(e.getTripId());
        dto.setBusId(e.getBusId());
        dto.setTripCategory(e.getTripCategory());
        dto.setStartLocation(e.getStartLocation());
        dto.setEndLocation(e.getEndLocation());
        dto.setDistance(e.getDistance());
        dto.setTotalIncome(e.getTotalIncome());
        dto.setTripDate(e.getTripDate());
        dto.setDescription(e.getDescription());
        dto.setCreatedBy(e.getCreatedBy());
        dto.setCreatedByUsername(e.getCreatedByUsername());
        return dto;
    }

    private TripExpensesDTO expToDTO(TripExpenses e) {
        TripExpensesDTO dto = new TripExpensesDTO();
        dto.setTripExpId(e.getTripExpId());
        dto.setTripId(e.getTripId());
        dto.setTripExpType(e.getTripExpType());
        dto.setAmount(e.getAmount());
        dto.setDescription(e.getDescription());
        dto.setDate(e.getDate());
        dto.setCreatedBy(e.getCreatedBy());
        dto.setCreatedByUsername(e.getCreatedByUsername());
        return dto;
    }
}