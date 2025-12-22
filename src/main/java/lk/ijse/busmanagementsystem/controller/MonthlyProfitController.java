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
import lk.ijse.busmanagementsystem.dto.MonthlyProfitDTO;
import lk.ijse.busmanagementsystem.dto.MonthlyProfitTM;
import lk.ijse.busmanagementsystem.model.MonthlyProfitModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class MonthlyProfitController implements Initializable {

    @FXML
    private AnchorPane monthlyProfitContent;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private ComboBox<Integer> yearComboBox;

    @FXML
    private Button generateBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button refreshBtn1;

    @FXML
    private Button printBtn;

    @FXML
    private Text totalIncomeText;

    @FXML
    private Text totalExpensesText;

    @FXML
    private Text netProfitText;

    @FXML
    private Text totalTripsText;

    // Bottom Table - Monthly Total
    @FXML
    private TableView<MonthlyProfitTM> monthlyProfitTableView;

    @FXML
    private TableColumn<MonthlyProfitTM, String> monthCol;

    @FXML
    private TableColumn<MonthlyProfitTM, String> totalIncomeCol;

    @FXML
    private TableColumn<MonthlyProfitTM, String> tripExpensesCol;

    @FXML
    private TableColumn<MonthlyProfitTM, String> salariesCol;

    @FXML
    private TableColumn<MonthlyProfitTM, String> maintenanceCol;

    @FXML
    private TableColumn<MonthlyProfitTM, String> partPurchasesCol;

    @FXML
    private TableColumn<MonthlyProfitTM, String> otherServicesCol;

    @FXML
    private TableColumn<MonthlyProfitTM, String> totalExpensesCol;

    @FXML
    private TableColumn<MonthlyProfitTM, String> netProfitCol;

    // Top Table - Daily Breakdown
    @FXML
    private TableView<DailyProfitTM> monthlyProfitTableView1;

    @FXML
    private TableColumn<DailyProfitTM, LocalDate> monthCol1;

    @FXML
    private TableColumn<DailyProfitTM, String> totalIncomeCol1;

    @FXML
    private TableColumn<DailyProfitTM, String> tripExpensesCol1;

    @FXML
    private TableColumn<DailyProfitTM, String> salariesCol1;

    @FXML
    private TableColumn<DailyProfitTM, String> maintenanceCol1;

    @FXML
    private TableColumn<DailyProfitTM, String> partPurchasesCol1;

    @FXML
    private TableColumn<DailyProfitTM, String> otherServicesCol1;

    @FXML
    private TableColumn<DailyProfitTM, String> totalExpensesCol1;

    @FXML
    private TableColumn<DailyProfitTM, String> netProfitCol1;

    private final MonthlyProfitModel monthlyProfitModel = new MonthlyProfitModel();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    private static final String[] MONTHS = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("MonthlyProfit window is loaded");

        setupTableColumns();
        setupComboBoxes();
        setupButtonActions();
        loadAllMonthlyProfitData();
    }

    private void setupTableColumns() {
        // Bottom Table - Monthly Total
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        totalIncomeCol.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));
        tripExpensesCol.setCellValueFactory(new PropertyValueFactory<>("tripExpenses"));
        salariesCol.setCellValueFactory(new PropertyValueFactory<>("salaries"));
        maintenanceCol.setCellValueFactory(new PropertyValueFactory<>("maintenance"));
        partPurchasesCol.setCellValueFactory(new PropertyValueFactory<>("partPurchases"));
        otherServicesCol.setCellValueFactory(new PropertyValueFactory<>("otherServices"));
        totalExpensesCol.setCellValueFactory(new PropertyValueFactory<>("totalExpenses"));
        netProfitCol.setCellValueFactory(new PropertyValueFactory<>("netProfit"));

        // Top Table - Daily Breakdown
        monthCol1.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalIncomeCol1.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));
        tripExpensesCol1.setCellValueFactory(new PropertyValueFactory<>("tripExpenses"));
        salariesCol1.setCellValueFactory(new PropertyValueFactory<>("salaries"));
        maintenanceCol1.setCellValueFactory(new PropertyValueFactory<>("maintenance"));
        partPurchasesCol1.setCellValueFactory(new PropertyValueFactory<>("partPurchases"));
        otherServicesCol1.setCellValueFactory(new PropertyValueFactory<>("otherServices"));
        totalExpensesCol1.setCellValueFactory(new PropertyValueFactory<>("totalExpenses"));
        netProfitCol1.setCellValueFactory(new PropertyValueFactory<>("netProfit"));
    }

    private void setupComboBoxes() {
        // Setup Month ComboBox
        monthComboBox.setItems(FXCollections.observableArrayList(MONTHS));

        // Setup Year ComboBox
        int currentYear = YearMonth.now().getYear();
        List<Integer> years = new ArrayList<>();
        for (int i = currentYear; i >= currentYear - 10; i--) {
            years.add(i);
        }
        yearComboBox.setItems(FXCollections.observableArrayList(years));

        // Set current month and year
        YearMonth currentMonth = YearMonth.now();
        monthComboBox.setValue(MONTHS[currentMonth.getMonthValue() - 1]);
        yearComboBox.setValue(currentMonth.getYear());
    }

    private void setupButtonActions() {
        generateBtn.setOnAction(event -> generateReport());
        resetBtn.setOnAction(event -> resetFilters());
        refreshBtn.setOnAction(event -> refreshData());
        refreshBtn1.setOnAction(event -> refreshData());
        printBtn.setOnAction(event -> printReport());
    }

    private void generateReport() {
        String selectedMonth = monthComboBox.getValue();
        Integer selectedYear = yearComboBox.getValue();

        if (selectedMonth == null || selectedYear == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Required",
                    "Please select both Month and Year");
            return;
        }

        int monthIndex = Arrays.asList(MONTHS).indexOf(selectedMonth) + 1;

        try {
            // Load monthly summary
            MonthlyProfitDTO monthlyData = monthlyProfitModel.getMonthlyProfit(selectedYear, monthIndex);

            if (monthlyData == null) {
                showAlert(Alert.AlertType.INFORMATION, "No Data",
                        "No data found for the selected month");
                resetSummaryCards();
                monthlyProfitTableView1.getItems().clear();
                return;
            }

            // Update summary cards
            updateSummaryCards(monthlyData);

            // Load daily breakdown
            List<DailyProfitDTO> dailyList = monthlyProfitModel.getDailyBreakdownForMonth(selectedYear, monthIndex);
            loadDailyBreakdownTable(dailyList);

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Report generated successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading data: " + e.getMessage());
        }
    }

    private void resetFilters() {
        YearMonth currentMonth = YearMonth.now();
        monthComboBox.setValue(MONTHS[currentMonth.getMonthValue() - 1]);
        yearComboBox.setValue(currentMonth.getYear());
        resetSummaryCards();
        monthlyProfitTableView1.getItems().clear();
        loadAllMonthlyProfitData();
        System.out.println("Filters reset");
    }

    private void refreshData() {
        System.out.println("Refreshing data...");
        String selectedMonth = monthComboBox.getValue();
        Integer selectedYear = yearComboBox.getValue();

        if (selectedMonth != null && selectedYear != null) {
            generateReport();
        } else {
            loadAllMonthlyProfitData();
        }
    }

    private void loadAllMonthlyProfitData() {
        try {
            List<MonthlyProfitDTO> monthlyList = monthlyProfitModel.getAllMonthlyProfit();
            loadMonthlyTableData(monthlyList);

            if (!monthlyList.isEmpty()) {
                MonthlyProfitDTO firstMonth = monthlyList.get(0);
                updateSummaryCards(firstMonth);

                // Load daily breakdown for the most recent month
                int year = firstMonth.getMonth().getYear();
                int month = firstMonth.getMonth().getMonthValue();
                List<DailyProfitDTO> dailyList = monthlyProfitModel.getDailyBreakdownForMonth(year, month);
                loadDailyBreakdownTable(dailyList);
            } else {
                resetSummaryCards();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading data: " + e.getMessage());
        }
    }

    private void loadMonthlyTableData(List<MonthlyProfitDTO> monthlyList) {
        ObservableList<MonthlyProfitTM> tmList = FXCollections.observableArrayList();

        for (MonthlyProfitDTO dto : monthlyList) {
            String monthDisplay = dto.getMonth().format(DateTimeFormatter.ofPattern("MMMM yyyy"));

            MonthlyProfitTM tm = new MonthlyProfitTM(
                    monthDisplay,
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

        monthlyProfitTableView.setItems(tmList);
    }

    private void loadDailyBreakdownTable(List<DailyProfitDTO> dailyList) {
        ObservableList<DailyProfitTM> tmList = FXCollections.observableArrayList();

        for (DailyProfitDTO dto : dailyList) {
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

        monthlyProfitTableView1.setItems(tmList);
    }

    private void updateSummaryCards(MonthlyProfitDTO monthlyData) {
        totalIncomeText.setText("Rs. " + df.format(monthlyData.getTotalIncome()));
        totalExpensesText.setText("Rs. " + df.format(monthlyData.getTotalExpenses()));
        netProfitText.setText("Rs. " + df.format(monthlyData.getNetProfit()));
        totalTripsText.setText(String.valueOf(monthlyData.getTotalTrips()));
    }

    private void resetSummaryCards() {
        totalIncomeText.setText("Rs. 0.00");
        totalExpensesText.setText("Rs. 0.00");
        netProfitText.setText("Rs. 0.00");
        totalTripsText.setText("0");
    }

    private void printReport() {
        String selectedMonth = monthComboBox.getValue();
        Integer selectedYear = yearComboBox.getValue();

        if (selectedMonth == null || selectedYear == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Required",
                    "Please select month and year before printing");
            return;
        }

        int monthIndex = Arrays.asList(MONTHS).indexOf(selectedMonth) + 1;

        try {
            MonthlyProfitDTO monthlyData = monthlyProfitModel.getMonthlyProfit(selectedYear, monthIndex);

            if (monthlyData == null) {
                showAlert(Alert.AlertType.WARNING, "No Data",
                        "No data available to print");
                return;
            }

            List<DailyProfitDTO> dailyList = monthlyProfitModel.getDailyBreakdownForMonth(selectedYear, monthIndex);

            // Prepare parameters for report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("Month", selectedMonth);
            parameters.put("Year", String.valueOf(selectedYear));
            parameters.put("TotalIncome", "Rs. " + df.format(monthlyData.getTotalIncome()));
            parameters.put("TotalExpenses", "Rs. " + df.format(monthlyData.getTotalExpenses()));
            parameters.put("NetProfit", "Rs. " + df.format(monthlyData.getNetProfit()));
            parameters.put("TotalTrips", String.valueOf(monthlyData.getTotalTrips()));
            parameters.put("GeneratedDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // Load and fill report
            JasperReport jasperReport = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/lk/ijse/busmanagementsystem/reports/MonthlyProfitReport.jrxml")
            );

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    new JRBeanCollectionDataSource(dailyList)
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
        System.out.println("Closing Monthly Profit window");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}