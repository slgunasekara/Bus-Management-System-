package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lk.ijse.busmanagementsystem.dto.DailyProfitDTO;
import lk.ijse.busmanagementsystem.dto.DailyProfitTM;
import lk.ijse.busmanagementsystem.model.DailyProfitModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class DailyProfitController implements Initializable {

    @FXML
    private AnchorPane dailyProfitContent;

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private Button generateBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button printBtn;

    @FXML
    private Text totalIncomeText;

    @FXML
    private Text totalExpensesText;

    @FXML
    private Text netProfitText;

    @FXML
    private Text avgDailyProfitText;

    @FXML
    private TableView<DailyProfitTM> dailyProfitTableView;

    @FXML
    private TableColumn<DailyProfitTM, LocalDate> dateCol;

    @FXML
    private TableColumn<DailyProfitTM, String> totalIncomeCol;

    @FXML
    private TableColumn<DailyProfitTM, String> tripExpensesCol;

    @FXML
    private TableColumn<DailyProfitTM, String> salariesCol;

    @FXML
    private TableColumn<DailyProfitTM, String> maintenanceCol;

    @FXML
    private TableColumn<DailyProfitTM, String> partPurchasesCol;

    @FXML
    private TableColumn<DailyProfitTM, String> otherServicesCol;

    @FXML
    private TableColumn<DailyProfitTM, String> totalExpensesCol;

    @FXML
    private TableColumn<DailyProfitTM, String> netProfitCol;

    private final DailyProfitModel dailyProfitModel = new DailyProfitModel();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("DailyProfit window is loaded");

        // Setup table columns
        setupTableColumns();

        // Setup button actions
        setupButtonActions();

        // Load initial data (all data)
        loadAllDailyProfitData();
    }

    private void setupTableColumns() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalIncomeCol.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));
        tripExpensesCol.setCellValueFactory(new PropertyValueFactory<>("tripExpenses"));
        salariesCol.setCellValueFactory(new PropertyValueFactory<>("salaries"));
        maintenanceCol.setCellValueFactory(new PropertyValueFactory<>("maintenance"));
        partPurchasesCol.setCellValueFactory(new PropertyValueFactory<>("partPurchases"));
        otherServicesCol.setCellValueFactory(new PropertyValueFactory<>("otherServices"));
        totalExpensesCol.setCellValueFactory(new PropertyValueFactory<>("totalExpenses"));
        netProfitCol.setCellValueFactory(new PropertyValueFactory<>("netProfit"));
    }

    private void setupButtonActions() {
        generateBtn.setOnAction(event -> generateReport());
        resetBtn.setOnAction(event -> resetFilters());
        refreshBtn.setOnAction(event -> refreshData());
        printBtn.setOnAction(event -> printReport());
    }

    private void generateReport() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if (fromDate == null || toDate == null) {
            showAlert(Alert.AlertType.WARNING, "Date Selection Required",
                    "Please select both From Date and To Date");
            return;
        }

        if (fromDate.isAfter(toDate)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Date Range",
                    "From Date cannot be after To Date");
            return;
        }

        try {
            // Load data for selected date range
            List<DailyProfitDTO> dailyProfitList = dailyProfitModel.getDailyProfitByDateRange(fromDate, toDate);

            if (dailyProfitList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data",
                        "No data found for the selected date range");
                resetSummaryCards();
                dailyProfitTableView.getItems().clear();
                return;
            }

            // Update table
            loadTableData(dailyProfitList);

            // Update summary cards
            updateSummaryCards(dailyProfitList, fromDate, toDate);

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Report generated successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading data: " + e.getMessage());
        }
    }

    private void resetFilters() {
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        resetSummaryCards();
        dailyProfitTableView.getItems().clear();
        loadAllDailyProfitData();
        System.out.println("Filters reset");
    }

    private void refreshData() {
        System.out.println("Refreshing data...");
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if (fromDate != null && toDate != null) {
            generateReport();
        } else {
            loadAllDailyProfitData();
        }
    }

    private void loadAllDailyProfitData() {
        try {
            List<DailyProfitDTO> dailyProfitList = dailyProfitModel.getAllDailyProfit();
            loadTableData(dailyProfitList);

            if (!dailyProfitList.isEmpty()) {
                LocalDate minDate = dailyProfitList.stream()
                        .map(DailyProfitDTO::getDate)
                        .min(LocalDate::compareTo)
                        .orElse(LocalDate.now());

                LocalDate maxDate = dailyProfitList.stream()
                        .map(DailyProfitDTO::getDate)
                        .max(LocalDate::compareTo)
                        .orElse(LocalDate.now());

                updateSummaryCards(dailyProfitList, minDate, maxDate);
            } else {
                resetSummaryCards();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading data: " + e.getMessage());
        }
    }

    private void loadTableData(List<DailyProfitDTO> dailyProfitList) {
        ObservableList<DailyProfitTM> tmList = FXCollections.observableArrayList();

        for (DailyProfitDTO dto : dailyProfitList) {
            DailyProfitTM tm = new DailyProfitTM(
                    dto.getDate(),
                    "Rs. " + df.format(dto.getTotalIncome()),
                    "Rs. " + df.format(dto.getTripExpenses()),
                    "Rs. " + df.format(dto.getSalaries()),
                    "Rs. " + df.format(dto.getMaintenance()),
                    "Rs. " + df.format(dto.getPartPurchases()),
                    "Rs. " + df.format(dto.getOtherServices()),
                    "Rs. " + df.format(dto.getTotalExpenses()),
                    "Rs. " + df.format(dto.getNetProfit())
            );
            tmList.add(tm);
        }

        dailyProfitTableView.setItems(tmList);
    }

    private void updateSummaryCards(List<DailyProfitDTO> dailyProfitList,
                                    LocalDate fromDate, LocalDate toDate) {
        double totalIncome = 0;
        double totalExpenses = 0;

        for (DailyProfitDTO dto : dailyProfitList) {
            totalIncome += dto.getTotalIncome();
            totalExpenses += dto.getTotalExpenses();
        }

        double netProfit = totalIncome - totalExpenses;

        // Calculate number of days
        long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate) + 1;
        double avgDailyProfit = daysBetween > 0 ? netProfit / daysBetween : 0;

        totalIncomeText.setText("Rs. " + df.format(totalIncome));
        totalExpensesText.setText("Rs. " + df.format(totalExpenses));
        netProfitText.setText("Rs. " + df.format(netProfit));
        avgDailyProfitText.setText("Rs. " + df.format(avgDailyProfit));
    }

    private void resetSummaryCards() {
        totalIncomeText.setText("Rs. 0.00");
        totalExpensesText.setText("Rs. 0.00");
        netProfitText.setText("Rs. 0.00");
        avgDailyProfitText.setText("Rs. 0.00");
    }

    private void printReport() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if (fromDate == null || toDate == null) {
            showAlert(Alert.AlertType.WARNING, "Date Selection Required",
                    "Please select date range before printing");
            return;
        }

        try {
            List<DailyProfitDTO> dailyProfitList = dailyProfitModel.getDailyProfitByDateRange(fromDate, toDate);

            if (dailyProfitList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Data",
                        "No data available to print");
                return;
            }

            // Calculate summary
            double totalIncome = dailyProfitList.stream()
                    .mapToDouble(DailyProfitDTO::getTotalIncome).sum();
            double totalExpenses = dailyProfitList.stream()
                    .mapToDouble(DailyProfitDTO::getTotalExpenses).sum();
            double netProfit = totalIncome - totalExpenses;

            // Prepare parameters for report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("FromDate", fromDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            parameters.put("ToDate", toDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            parameters.put("TotalIncome", "Rs. " + df.format(totalIncome));
            parameters.put("TotalExpenses", "Rs. " + df.format(totalExpenses));
            parameters.put("NetProfit", "Rs. " + df.format(netProfit));

            // FIX: Use LocalDateTime instead of LocalDate for timestamp
            parameters.put("GeneratedDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // Load and fill report
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/lk/ijse/busmanagementsystem/reports/DailyProfitReport.jrxml")
            );

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    new JRBeanCollectionDataSource(dailyProfitList)
            );

            JasperViewer.viewReport(jasperPrint, false);
            System.out.println("Report printed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Print Error",
                    "Error printing report: " + e.getMessage());
        }
    }

    @FXML
    private void back(ActionEvent event) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
        System.out.println("Closing Daily Profit window");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}