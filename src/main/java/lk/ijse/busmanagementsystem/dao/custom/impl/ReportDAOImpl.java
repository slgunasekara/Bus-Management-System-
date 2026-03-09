package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.SuperDAO;
import lk.ijse.busmanagementsystem.dao.custom.ReportDAO;
import lk.ijse.busmanagementsystem.entity.*;
import lk.ijse.busmanagementsystem.enums.TripCategory;
import lk.ijse.busmanagementsystem.enums.TripExpType;
import lk.ijse.busmanagementsystem.tm.EmployeeSalaryTM;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl implements SuperDAO {

    public Report getReportSummary(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        double totalIncome = getTotalIncome(fromDate, toDate);
        double totalExpenses = getTotalExpenses(fromDate, toDate);
        double totalSalary = getTotalSalary(fromDate, toDate);
        int totalTrips = getTotalTrips(fromDate, toDate);
        double netProfit = totalIncome - (totalExpenses + totalSalary);

        Report report = new Report();
        report.setTotalIncome(totalIncome);
        report.setTotalExpenses(totalExpenses);
        report.setTotalSalary(totalSalary);
        report.setNetProfit(netProfit);
        report.setTotalTrips(totalTrips);
        return report;
    }

    private double getTotalIncome(LocalDate from, LocalDate to) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT COALESCE(SUM(total_income), 0) as total FROM Trip WHERE trip_date BETWEEN ? AND ?", from, to);
        return rst.next() ? rst.getDouble("total") : 0.0;
    }

    private double getTotalExpenses(LocalDate from, LocalDate to) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT COALESCE(SUM(amount), 0) as total FROM Trip_Expenses WHERE date BETWEEN ? AND ?", from, to);
        return rst.next() ? rst.getDouble("total") : 0.0;
    }

    private double getTotalSalary(LocalDate from, LocalDate to) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT COALESCE(SUM(amount), 0) as total FROM Employee_Salary WHERE date BETWEEN ? AND ?", from, to);
        return rst.next() ? rst.getDouble("total") : 0.0;
    }

    private int getTotalTrips(LocalDate from, LocalDate to) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT COUNT(*) as total FROM Trip WHERE trip_date BETWEEN ? AND ?", from, to);
        return rst.next() ? rst.getInt("total") : 0;
    }

    public List<Trip> getIncomeReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT t.*, u.username FROM Trip t LEFT JOIN User u ON t.created_by=u.user_id " +
                        "WHERE t.trip_date BETWEEN ? AND ? ORDER BY t.trip_date DESC",
                fromDate, toDate);
        List<Trip> list = new ArrayList<>();
        while (rst.next()) {
            Trip trip = new Trip();
            trip.setTripId(rst.getInt("trip_id"));
            trip.setBusId(rst.getInt("bus_id"));
            trip.setTripCategory(TripCategory.valueOf(rst.getString("trip_category")));
            trip.setStartLocation(rst.getString("start_location"));
            trip.setEndLocation(rst.getString("end_location"));
            trip.setDistance(rst.getDouble("distance"));
            trip.setTotalIncome(rst.getDouble("total_income"));
            trip.setTripDate(rst.getDate("trip_date").toLocalDate());
            trip.setDescription(rst.getString("description"));
            trip.setCreatedBy(rst.getInt("created_by"));
            trip.setCreatedByUsername(rst.getString("username"));
            list.add(trip);
        }
        return list;
    }

    public List<TripExpenses> getExpenseReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT te.*, u.username FROM Trip_Expenses te LEFT JOIN User u ON te.created_by=u.user_id " +
                        "WHERE te.date BETWEEN ? AND ? ORDER BY te.date DESC",
                fromDate, toDate);
        List<TripExpenses> list = new ArrayList<>();
        while (rst.next()) {
            TripExpenses te = new TripExpenses();
            te.setTripExpId(rst.getInt("trip_exp_id"));
            te.setTripId(rst.getInt("trip_id"));
            te.setTripExpType(TripExpType.valueOf(rst.getString("trip_exp_type")));
            te.setAmount(rst.getDouble("amount"));
            te.setDescription(rst.getString("description"));
            te.setDate(rst.getDate("date").toLocalDate());
            te.setCreatedBy(rst.getInt("created_by"));
            te.setCreatedByUsername(rst.getString("username"));
            list.add(te);
        }
        return list;
    }

    public List<EmployeeSalaryTM> getSalaryReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT es.salary_id, es.emp_id, e.emp_name, es.trip_id, es.amount, es.description, es.date, u.username " +
                        "FROM Employee_Salary es LEFT JOIN Employee e ON es.emp_id=e.emp_id " +
                        "LEFT JOIN User u ON es.created_by=u.user_id WHERE es.date BETWEEN ? AND ? ORDER BY es.date DESC",
                fromDate, toDate);
        List<EmployeeSalaryTM> list = new ArrayList<>();
        while (rst.next()) {
            list.add(new EmployeeSalaryTM(
                    rst.getInt("salary_id"), rst.getInt("emp_id"), rst.getString("emp_name"),
                    rst.getObject("trip_id") != null ? rst.getInt("trip_id") : null,
                    rst.getDouble("amount"), rst.getString("description"),
                    rst.getDate("date").toLocalDate(), rst.getString("username")
            ));
        }
        return list;
    }

    public List<Trip> getTripReport(LocalDate fromDate, LocalDate toDate)
            throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT t.*, b.bus_number, u.username FROM Trip t " +
                        "LEFT JOIN Bus b ON t.bus_id=b.bus_id " +
                        "LEFT JOIN User u ON t.created_by=u.user_id " +
                        "WHERE t.trip_date BETWEEN ? AND ? ORDER BY t.trip_date DESC",
                fromDate, toDate);
        List<Trip> list = new ArrayList<>();
        while (rst.next()) {
            Trip trip = new Trip();
            trip.setTripId(rst.getInt("trip_id"));
            trip.setBusId(rst.getInt("bus_id"));
            trip.setBusNumber(rst.getString("bus_number"));
            trip.setTripCategory(TripCategory.valueOf(rst.getString("trip_category")));
            trip.setStartLocation(rst.getString("start_location"));
            trip.setEndLocation(rst.getString("end_location"));
            trip.setDistance(rst.getDouble("distance"));
            trip.setTotalIncome(rst.getDouble("total_income"));
            trip.setTripDate(rst.getDate("trip_date").toLocalDate());
            trip.setDescription(rst.getString("description"));
            trip.setCreatedBy(rst.getInt("created_by"));
            trip.setCreatedByUsername(rst.getString("username"));
            list.add(trip);
        }
        return list;
    }

    public String getBusNumberById(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_number FROM Bus WHERE bus_id=?", busId);
        return rst.next() ? rst.getString("bus_number") : "N/A";
    }
}
