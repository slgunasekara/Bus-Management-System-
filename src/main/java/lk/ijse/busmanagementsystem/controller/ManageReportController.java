package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.busmanagementsystem.Main;
import lk.ijse.busmanagementsystem.dto.*;
import lk.ijse.busmanagementsystem.bo.BOFactory;
import lk.ijse.busmanagementsystem.bo.custom.ReportBO;
import lk.ijse.busmanagementsystem.tm.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ManageReportController implements Initializable {

    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private Button generateBtn;
    @FXML private Button resetBtn;
    @FXML private Button printIncomeBtn;
    @FXML private Button printExpensesBtn;
    @FXML private Button printSalaryBtn;
    @FXML private Button printTripBtn;
    @FXML private Button dailyProfitBtn;
    @FXML private Button monthlyProfitBtn;
    @FXML private Text totalIncomeText;
    @FXML private Text totalExpensesText;
    @FXML private Text totalSalaryText;
    @FXML private Text totalTripsText;
    @FXML private TableView<IncomeTM> incomeTableView;
    @FXML private TableColumn<IncomeTM, Integer> incomeIdCol;
    @FXML private TableColumn<IncomeTM, String> incomeSourceCol;
    @FXML private TableColumn<IncomeTM, LocalDate> incomeDateCol;
    @FXML private TableColumn<IncomeTM, String> incomeAmountCol;
    @FXML private TableView<ExpenseTM> expensesTableView;
    @FXML private TableColumn<ExpenseTM, LocalDate> expenseDateCol;
    @FXML private TableColumn<ExpenseTM, String> expenseAmountCol;
    @FXML private TableColumn<ExpenseTM, String> expenseCategoryCol;
    @FXML private TableView<SalaryTM> salaryTableView;
    @FXML private TableColumn<SalaryTM, Integer> salaryIdCol;
    @FXML private TableColumn<SalaryTM, String> salaryNameCol;
    @FXML private TableColumn<SalaryTM, LocalDate> salaryDateCol;
    @FXML private TableColumn<SalaryTM, String> salaryAmountCol;
    @FXML private TableView<TripReportTM> tripTableView;
    @FXML private TableColumn<TripReportTM, Integer> tripIdCol;
    @FXML private TableColumn<TripReportTM, LocalDate> tripDateCol;
    @FXML private TableColumn<TripReportTM, Integer> tripBusIdCol;
    @FXML private TableColumn<TripReportTM, String> tripRouteCol;
    @FXML private TableColumn<TripReportTM, String> tripStatusCol;

    private final ReportBO reportBO = (ReportBO) BOFactory.getInstance().getBO(BOFactory.BOType.REPORT);
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ManageReport is loaded");
        setupTableColumns();
        setupButtonActions();
        setDefaultDates();
        loadInitialData();
    }

    private void setupTableColumns() {
        incomeIdCol.setCellValueFactory(new PropertyValueFactory<>("tripId"));
        incomeSourceCol.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        incomeDateCol.setCellValueFactory(new PropertyValueFactory<>("tripDate"));
        incomeAmountCol.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));

        expenseDateCol.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
        expenseAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        expenseCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        salaryIdCol.setCellValueFactory(new PropertyValueFactory<>("empId"));
        salaryNameCol.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        salaryDateCol.setCellValueFactory(new PropertyValueFactory<>("salaryDate"));
        salaryAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tripIdCol.setCellValueFactory(new PropertyValueFactory<>("tripId"));
        tripDateCol.setCellValueFactory(new PropertyValueFactory<>("tripDate"));
        tripBusIdCol.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        tripRouteCol.setCellValueFactory(new PropertyValueFactory<>("route"));
        tripStatusCol.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    private void setupButtonActions() {
        generateBtn.setOnAction(event -> generateReport());
        resetBtn.setOnAction(event -> reset());
        printIncomeBtn.setOnAction(event -> printIncomeReport());
        printExpensesBtn.setOnAction(event -> printExpenseReport());
        printSalaryBtn.setOnAction(event -> printSalaryReport());
        printTripBtn.setOnAction(event -> printTripReport());
    }

    private void setDefaultDates() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        fromDatePicker.setValue(firstDayOfMonth);
        toDatePicker.setValue(today);
    }

    private void loadInitialData() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        try {
            loadReportSummary(firstDayOfMonth, today);
            loadIncomeReport(firstDayOfMonth, today);
            loadExpenseReport(firstDayOfMonth, today);
            loadSalaryReport(firstDayOfMonth, today);
            loadTripReport(firstDayOfMonth, today);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load initial data: " + e.getMessage());
        }
    }

    private void generateReport() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if (fromDate == null || toDate == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select both From Date and To Date");
            return;
        }

        if (fromDate.isAfter(toDate)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "From Date cannot be after To Date");
            return;
        }

        try {
            loadReportSummary(fromDate, toDate);
            loadIncomeReport(fromDate, toDate);
            loadExpenseReport(fromDate, toDate);
            loadSalaryReport(fromDate, toDate);
            loadTripReport(fromDate, toDate);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Report generated successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to generate report: " + e.getMessage());
        }
    }

    private void loadReportSummary(LocalDate fromDate, LocalDate toDate) throws SQLException, ClassNotFoundException {
        ReportDTO reportDTO = reportBO.getReportSummary(fromDate, toDate);
        totalIncomeText.setText("Rs. " + df.format(reportDTO.getTotalIncome()));
        totalExpensesText.setText("Rs. " + df.format(reportDTO.getTotalExpenses()));
        totalSalaryText.setText("Rs. " + df.format(reportDTO.getTotalSalary()));
        totalTripsText.setText(String.valueOf(reportDTO.getTotalTrips()));
    }

    private void loadIncomeReport(LocalDate fromDate, LocalDate toDate) throws SQLException, ClassNotFoundException {
        List<TripDTO> tripList = reportBO.getIncomeReport(fromDate, toDate);
        ObservableList<IncomeTM> incomeTMList = FXCollections.observableArrayList();

        for (TripDTO dto : tripList) {
            String busNumber = reportBO.getBusNumberById(dto.getBusId());
            IncomeTM tm = new IncomeTM(
                    dto.getTripId(),
                    busNumber,
                    dto.getTripDate(),
                    "Rs. " + df.format(dto.getTotalIncome())
            );
            incomeTMList.add(tm);
        }
        incomeTableView.setItems(incomeTMList);
    }

    private void loadExpenseReport(LocalDate fromDate, LocalDate toDate) throws SQLException, ClassNotFoundException {
        List<TripExpensesDTO> expenseList = reportBO.getExpenseReport(fromDate, toDate);
        ObservableList<ExpenseTM> expenseTMList = FXCollections.observableArrayList();

        for (TripExpensesDTO dto : expenseList) {
            ExpenseTM tm = new ExpenseTM(
                    dto.getDate(),
                    "Rs. " + df.format(dto.getAmount()),
                    dto.getTripExpType().toString()
            );
            expenseTMList.add(tm);
        }
        expensesTableView.setItems(expenseTMList);
    }

    private void loadSalaryReport(LocalDate fromDate, LocalDate toDate) throws SQLException, ClassNotFoundException {
        List<EmployeeSalaryTM> salaryList = reportBO.getSalaryReport(fromDate, toDate);
        ObservableList<SalaryTM> salaryTMList = FXCollections.observableArrayList();

        for (EmployeeSalaryTM dto : salaryList) {
            SalaryTM tm = new SalaryTM(
                    dto.getEmpId(),
                    dto.getEmpName(),
                    dto.getDate(),
                    "Rs. " + df.format(dto.getAmount())
            );
            salaryTMList.add(tm);
        }
        salaryTableView.setItems(salaryTMList);
    }

    private void loadTripReport(LocalDate fromDate, LocalDate toDate) throws SQLException, ClassNotFoundException {
        List<TripDTO> tripList = reportBO.getTripReport(fromDate, toDate);
        ObservableList<TripReportTM> tripTMList = FXCollections.observableArrayList();

        for (TripDTO dto : tripList) {
            String route = dto.getStartLocation() + " → " + dto.getEndLocation();
            String busNumber = reportBO.getBusNumberById(dto.getBusId());
            TripReportTM tm = new TripReportTM(
                    dto.getTripId(),
                    dto.getTripDate(),
                    busNumber,
                    route,
                    dto.getTripCategory().toString()
            );
            tripTMList.add(tm);
        }
        tripTableView.setItems(tripTMList);
    }

    private void reset() {
        setDefaultDates();
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        totalIncomeText.setText("Rs. 0.00");
        totalExpensesText.setText("Rs. 0.00");
        totalSalaryText.setText("Rs. 0.00");
        totalTripsText.setText("0");
        incomeTableView.getItems().clear();
        expensesTableView.getItems().clear();
        salaryTableView.getItems().clear();
        tripTableView.getItems().clear();
    }

    private void printIncomeReport() {
        printReport("Income", incomeTableView.getItems(), "/lk/ijse/busmanagementsystem/reports/IncomeReport.jrxml");
    }

    private void printExpenseReport() {
        printReport("Expense", expensesTableView.getItems(), "/lk/ijse/busmanagementsystem/reports/ExpenseReport.jrxml");
    }

    private void printSalaryReport() {
        printReport("Salary", salaryTableView.getItems(), "/lk/ijse/busmanagementsystem/reports/SalaryReport.jrxml");
    }

    private void printTripReport() {
        printReport("Trip", tripTableView.getItems(), "/lk/ijse/busmanagementsystem/reports/TripReport.jrxml");
    }

    private void printReport(String reportType, ObservableList<?> data, String jrxmlPath) {
        if (data.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "No data available to print");
            return;
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("FromDate", fromDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            parameters.put("ToDate", toDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            parameters.put("ReportType", reportType);
            parameters.put("GeneratedDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream(jrxmlPath));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(data));
            JasperViewer.viewReport(jasperPrint, false);
            System.out.println(reportType + " Report printed successfully");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Print Error", "Error printing report: " + e.getMessage());
        }
    }

    @FXML
    private void dailyProfit(ActionEvent event) throws Exception {
        openWindow("DailyProfit", "Daily Profit Report - Gunasekara Bus Management");
    }

    @FXML
    private void monthlyProfit(ActionEvent event) throws Exception {
        openWindow("MonthlyProfit", "Monthly Profit Report - Gunasekara Bus Management");
    }

    private void openWindow(String fxmlName, String title) {
        try {
            Parent root = Main.loadFXML(fxmlName);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setFullScreen(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/lk/ijse/busmanagementsystem/assets/Gunasekara.jpg")));
            stage.setResizable(true);
            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading " + fxmlName + " window: " + e.getMessage());
        }
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {
        Main.setRoot("login");
        System.out.println("Logout");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

