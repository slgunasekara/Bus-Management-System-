package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Modality;
import lk.ijse.busmanagementsystem.Main;
import lk.ijse.busmanagementsystem.dto.*;
import lk.ijse.busmanagementsystem.model.ReportModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.net.URL;


public class ManageReportController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb){
        System.out.println("ManageReport is loaded");
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {
        Main.setRoot("login");
        System.out.println("Logout");
    }

    @FXML
    private void dailyProfit(ActionEvent event) throws Exception {
        try {
            // Load the DailyProfit FXML using Main class method
            Parent root = Main.loadFXML("DailyProfit");

            // Create a new stage (window)
            Stage dailyProfitStage = new Stage();
            dailyProfitStage.setTitle("Daily Profit Report - Gunasekara Bus Management");

            // Set modality to block interaction with parent window
            dailyProfitStage.initModality(Modality.APPLICATION_MODAL);

            // Create and set the scene
            Scene scene = new Scene(root);
            dailyProfitStage.setScene(scene);

            // Make window full screen
            dailyProfitStage.setMaximized(true);

            // Set minimum window size
            dailyProfitStage.setMinWidth(1280);
            dailyProfitStage.setMinHeight(700);

            dailyProfitStage.getIcons().add(new Image(getClass().getResourceAsStream("/lk/ijse/busmanagementsystem/assets/Gunasekara.jpg"))); // add icon

            // Allow window resizing
            dailyProfitStage.setResizable(true);

            // Show the window and wait for it to close
            dailyProfitStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Daily Profit window: " + e.getMessage());
        }
    }

    @FXML
    private void monthlyProfit(ActionEvent event) throws Exception {
        try {
            // Load the DailyProfit FXML using Main class method
            Parent root = Main.loadFXML("MonthlyProfit");

            // Create a new stage (window)
            Stage monthlyProfitStage = new Stage();
            monthlyProfitStage.setTitle("Monthly Profit Report - Gunasekara Bus Management");

            // Set modality to block interaction with parent window
            monthlyProfitStage.initModality(Modality.APPLICATION_MODAL);

            // Create and set the scene
            Scene scene = new Scene(root);
            monthlyProfitStage.setScene(scene);

            // Make window full screen
            monthlyProfitStage.setMaximized(true);

            // Set minimum window size
            monthlyProfitStage.setMinWidth(1280);
            monthlyProfitStage.setMinHeight(700);

            monthlyProfitStage.getIcons().add(new Image(getClass().getResourceAsStream("/lk/ijse/busmanagementsystem/assets/Gunasekara.jpg"))); // add icon

            // Allow window resizing
            monthlyProfitStage.setResizable(true);

            // Show the window and wait for it to close
            monthlyProfitStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Monthly Profit window: " + e.getMessage());
        }
    }
}

//--------------------------

//package lk.ijse.busmanagementsystem.controller;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//        import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.image.Image;
//import javafx.scene.text.Text;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//import lk.ijse.busmanagementsystem.Main;
//import lk.ijse.busmanagementsystem.model.ReportModel;
//import lk.ijse.busmanagementsystem.dto.*;
//
//        import java.io.IOException;
//import java.net.URL;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.ResourceBundle;
//
//public class ManageReportController implements Initializable {
//
//    // ========== FXML Components - Date Pickers ==========
//    @FXML
//    private DatePicker fromDatePicker;
//
//    @FXML
//    private DatePicker toDatePicker;
//
//    // ========== FXML Components - Buttons ==========
//    @FXML
//    private Button generateBtn;
//
//    @FXML
//    private Button resetBtn;
//
//    @FXML
//    private Button printIncomeBtn;
//
//    @FXML
//    private Button printExpensesBtn;
//
//    @FXML
//    private Button printSalaryBtn;
//
//    @FXML
//    private Button printTripBtn;
//
//    @FXML
//    private Button dailyProfitBtn;
//
//    @FXML
//    private Button monthlyProfitBtn;
//
//    // ========== FXML Components - Summary Cards ==========
//    @FXML
//    private Text totalIncomeText;
//
//    @FXML
//    private Text totalExpensesText;
//
//    @FXML
//    private Text totalSalaryText;
//
//    @FXML
//    private Text netProfitText;
//
//    @FXML
//    private Text totalTripsText;
//
//    // ========== FXML Components - Income Table ==========
//    @FXML
//    private TableView<IncomeTM> incomeTableView;
//
//    @FXML
//    private TableColumn<IncomeTM, Integer> incomeIdCol;
//
//    @FXML
//    private TableColumn<IncomeTM, String> incomeSourceCol;
//
//    @FXML
//    private TableColumn<IncomeTM, LocalDate> incomeDateCol;
//
//    @FXML
//    private TableColumn<IncomeTM, Double> incomeAmountCol;
//
//    // ========== FXML Components - Expenses Table ==========
//    @FXML
//    private TableView<ExpenseTM> expensesTableView;
//
//    @FXML
//    private TableColumn<ExpenseTM, LocalDate> expenseDateCol;
//
//    @FXML
//    private TableColumn<ExpenseTM, Double> expenseAmountCol;
//
//    @FXML
//    private TableColumn<ExpenseTM, String> expenseCategoryCol;
//
//    // ========== FXML Components - Salary Table ==========
//    @FXML
//    private TableView<SalaryTM> salaryTableView;
//
//    @FXML
//    private TableColumn<SalaryTM, Integer> salaryIdCol;
//
//    @FXML
//    private TableColumn<SalaryTM, String> salaryNameCol;
//
//    @FXML
//    private TableColumn<SalaryTM, LocalDate> salaryDateCol;
//
//    @FXML
//    private TableColumn<SalaryTM, Double> salaryAmountCol;
//
//    // ========== FXML Components - Trip Table ==========
//    @FXML
//    private TableView<TripReportTM> tripTableView;
//
//    @FXML
//    private TableColumn<TripReportTM, Integer> tripIdCol;
//
//    @FXML
//    private TableColumn<TripReportTM, LocalDate> tripDateCol;
//
//    @FXML
//    private TableColumn<TripReportTM, Integer> tripBusIdCol;
//
//    @FXML
//    private TableColumn<TripReportTM, String> tripRouteCol;
//
//    @FXML
//    private TableColumn<TripReportTM, String> tripStatusCol;
//
//    // ========== Model Instance ==========
//    private final ReportModel reportModel = new ReportModel();
//
//    // ========== Initialize Method ==========
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        setupTableColumns();
//        setupButtonActions();
//        setDefaultDates();
//    }
//
//    // ========== Setup Table Columns ==========
//    private void setupTableColumns() {
//        // Income Table
//        incomeIdCol.setCellValueFactory(new PropertyValueFactory<>("tripId"));
//        incomeSourceCol.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
//        incomeDateCol.setCellValueFactory(new PropertyValueFactory<>("tripDate"));
//        incomeAmountCol.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));
//
//        // Expenses Table
//        expenseDateCol.setCellValueFactory(new PropertyValueFactory<>("expenseDate"));
//        expenseAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
//        expenseCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
//
//        // Salary Table
//        salaryIdCol.setCellValueFactory(new PropertyValueFactory<>("empId"));
//        salaryNameCol.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
//        salaryDateCol.setCellValueFactory(new PropertyValueFactory<>("salaryDate"));
//        salaryAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
//
//        // Trip Table
//        tripIdCol.setCellValueFactory(new PropertyValueFactory<>("tripId"));
//        tripDateCol.setCellValueFactory(new PropertyValueFactory<>("tripDate"));
//        tripBusIdCol.setCellValueFactory(new PropertyValueFactory<>("busId"));
//        tripRouteCol.setCellValueFactory(new PropertyValueFactory<>("route"));
//        tripStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
//    }
//
//    // ========== Setup Button Actions ==========
//    private void setupButtonActions() {
//        generateBtn.setOnAction(event -> generateReport());
//        resetBtn.setOnAction(event -> resetReport());
//    }
//
//    // ========== Set Default Dates ==========
//    private void setDefaultDates() {
//        LocalDate today = LocalDate.now();
//        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
//
//        fromDatePicker.setValue(firstDayOfMonth);
//        toDatePicker.setValue(today);
//    }
//
//    // ========== Generate Report Method ==========
//    private void generateReport() {
//        LocalDate fromDate = fromDatePicker.getValue();
//        LocalDate toDate = toDatePicker.getValue();
//
//        // Validate dates
//        if (fromDate == null || toDate == null) {
//            showAlert(Alert.AlertType.WARNING, "Validation Error",
//                    "Please select both From Date and To Date");
//            return;
//        }
//
//        if (fromDate.isAfter(toDate)) {
//            showAlert(Alert.AlertType.WARNING, "Validation Error",
//                    "From Date cannot be after To Date");
//            return;
//        }
//
//        try {
//            // Load all report data
//            loadReportSummary(fromDate, toDate);
//            loadIncomeReport(fromDate, toDate);
//            loadExpenseReport(fromDate, toDate);
//            loadSalaryReport(fromDate, toDate);
//            loadTripReport(fromDate, toDate);
//
//            showAlert(Alert.AlertType.INFORMATION, "Success",
//                    "Report generated successfully!");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Database Error",
//                    "Failed to generate report: " + e.getMessage());
//        }
//    }
//
//    // ========== Load Report Summary ==========
//    private void loadReportSummary(LocalDate fromDate, LocalDate toDate) throws SQLException {
//        ReportDTO reportDTO = reportModel.getReportSummary(fromDate, toDate);
//
//        // Update summary cards
//        totalIncomeText.setText(String.format("Rs. %.2f", reportDTO.getTotalIncome()));
//        totalExpensesText.setText(String.format("Rs. %.2f", reportDTO.getTotalExpenses()));
//        totalSalaryText.setText(String.format("Rs. %.2f", reportDTO.getTotalSalary()));
//        netProfitText.setText(String.format("Rs. %.2f", reportDTO.getNetProfit()));
//        totalTripsText.setText(String.valueOf(reportDTO.getTotalTrips()));
//    }
//
//    // ========== Load Income Report ==========
//    private void loadIncomeReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
//        List<TripDTO> tripList = reportModel.getIncomeReport(fromDate, toDate);
//
//        ObservableList<IncomeTM> incomeTMList = FXCollections.observableArrayList();
//
//        for (TripDTO dto : tripList) {
//            // Bus number එක fetch කරන්න ඕන (ඔයාගේ Bus model එකෙන්)
//            String busNumber = getBusNumber(dto.getBusId());
//
//            IncomeTM tm = new IncomeTM(
//                    dto.getTripId(),
//                    busNumber,
//                    dto.getTripDate(),
//                    dto.getTotalIncome()
//            );
//            incomeTMList.add(tm);
//        }
//
//        incomeTableView.setItems(incomeTMList);
//    }
//
//    // ========== Load Expense Report ==========
//    private void loadExpenseReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
//        List<TripExpensesDTO> expenseList = reportModel.getExpenseReport(fromDate, toDate);
//
//        ObservableList<ExpenseTM> expenseTMList = FXCollections.observableArrayList();
//
//        for (TripExpensesDTO dto : expenseList) {
//            ExpenseTM tm = new ExpenseTM(
//                    dto.getDate(),
//                    dto.getAmount(),
//                    dto.getTripExpType().toString()
//            );
//            expenseTMList.add(tm);
//        }
//
//        expensesTableView.setItems(expenseTMList);
//    }
//
//    // ========== Load Salary Report ==========
//    private void loadSalaryReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
//        List<EmployeeSalaryTM> salaryList = reportModel.getSalaryReport(fromDate, toDate);
//
//        ObservableList<SalaryTM> salaryTMList = FXCollections.observableArrayList();
//
//        for (EmployeeSalaryTM dto : salaryList) {
//            SalaryTM tm = new SalaryTM(
//                    dto.getEmpId(),
//                    dto.getEmpName(),
//                    dto.getDate(),
//                    dto.getAmount()
//            );
//            salaryTMList.add(tm);
//        }
//
//        salaryTableView.setItems(salaryTMList);
//    }
//
//    // ========== Load Trip Report ==========
//    private void loadTripReport(LocalDate fromDate, LocalDate toDate) throws SQLException {
//        List<TripDTO> tripList = reportModel.getTripReport(fromDate, toDate);
//
//        ObservableList<TripReportTM> tripTMList = FXCollections.observableArrayList();
//
//        for (TripDTO dto : tripList) {
//            String route = dto.getStartLocation() + " → " + dto.getEndLocation();
//
//            TripReportTM tm = new TripReportTM(
//                    dto.getTripId(),
//                    dto.getTripDate(),
//                    dto.getBusId(),
//                    route,
//                    "COMPLETED"
//            );
//            tripTMList.add(tm);
//        }
//
//        tripTableView.setItems(tripTMList);
//    }
//
//    // ========== Helper Method to Get Bus Number ==========
//    private String getBusNumber(int busId) {
//        try {
//            // ඔයාගේ BusModel එකෙන් bus number එක fetch කරන්න
//            // මේ method එක ඔයාගේ BusModel එකේ තියෙන්න ඕන
//            // return busModel.getBusNumberById(busId);
//
//            // Temporary: Return bus ID as string
//            return "BUS-" + busId;
//        } catch (Exception e) {
//            return "N/A";
//        }
//    }
//
//    // ========== Reset Report ==========
//    private void resetReport() {
//        // Reset date pickers
//        setDefaultDates();
//
//        // Clear summary cards
//        totalIncomeText.setText("Rs. 0.00");
//        totalExpensesText.setText("Rs. 0.00");
//        totalSalaryText.setText("Rs. 0.00");
//        netProfitText.setText("Rs. 0.00");
//        totalTripsText.setText("0");
//
//        // Clear all tables
//        incomeTableView.getItems().clear();
//        expensesTableView.getItems().clear();
//        salaryTableView.getItems().clear();
//        tripTableView.getItems().clear();
//    }
//
//    // ========== Alert Helper Method ==========
//    private void showAlert(Alert.AlertType type, String title, String message) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    // ========== FXML Action Methods ==========
//    @FXML
//    private void dailyProfit(ActionEvent event) throws Exception {
//        try {
//            // Load the DailyProfit FXML using Main class method
//            Parent root = Main.loadFXML("DailyProfit");
//
//            // Create a new stage (window)
//            Stage dailyProfitStage = new Stage();
//            dailyProfitStage.setTitle("Daily Profit Report - Gunasekara Bus Management");
//
//            // Set modality to block interaction with parent window
//            dailyProfitStage.initModality(Modality.APPLICATION_MODAL);
//
//            // Create and set the scene
//            Scene scene = new Scene(root);
//            dailyProfitStage.setScene(scene);
//
//            // Make window full screen
//            dailyProfitStage.setMaximized(true);
//
//            // Set minimum window size
//            dailyProfitStage.setMinWidth(1280);
//            dailyProfitStage.setMinHeight(700);
//
//            dailyProfitStage.getIcons().add(new Image(getClass().getResourceAsStream("/lk/ijse/busmanagementsystem/assets/Gunasekara.jpg"))); // add icon
//
//            // Allow window resizing
//            dailyProfitStage.setResizable(true);
//
//            // Show the window and wait for it to close
//            dailyProfitStage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Error loading Daily Profit window: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    private void monthlyProfit(ActionEvent event) throws Exception {
//        try {
//            // Load the DailyProfit FXML using Main class method
//            Parent root = Main.loadFXML("MonthlyProfit");
//
//            // Create a new stage (window)
//            Stage monthlyProfitStage = new Stage();
//            monthlyProfitStage.setTitle("Monthly Profit Report - Gunasekara Bus Management");
//
//            // Set modality to block interaction with parent window
//            monthlyProfitStage.initModality(Modality.APPLICATION_MODAL);
//
//            // Create and set the scene
//            Scene scene = new Scene(root);
//            monthlyProfitStage.setScene(scene);
//
//            // Make window full screen
//            monthlyProfitStage.setMaximized(true);
//
//            // Set minimum window size
//            monthlyProfitStage.setMinWidth(1280);
//            monthlyProfitStage.setMinHeight(700);
//
//            monthlyProfitStage.getIcons().add(new Image(getClass().getResourceAsStream("/lk/ijse/busmanagementsystem/assets/Gunasekara.jpg"))); // add icon
//
//            // Allow window resizing
//            monthlyProfitStage.setResizable(true);
//
//            // Show the window and wait for it to close
//            monthlyProfitStage.showAndWait();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Error loading Monthly Profit window: " + e.getMessage());
//        }
//    }
//
//    @FXML
//    private void logout(ActionEvent event) {
//        System.out.println("Logout clicked");
//    }
//}