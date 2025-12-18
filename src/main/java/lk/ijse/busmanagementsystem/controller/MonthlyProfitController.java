package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;

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
    private Button printBtn;

    @FXML
    private Text totalIncomeText;

    @FXML
    private Text totalExpensesText;

    @FXML
    private Text netProfitText;

    @FXML
    private Text totalTripsText;

    @FXML
    private TableView<?> monthlyProfitTableView;

    @FXML
    private TableColumn<?, ?> monthCol;

    @FXML
    private TableColumn<?, ?> totalIncomeCol;

    @FXML
    private TableColumn<?, ?> tripExpensesCol;

    @FXML
    private TableColumn<?, ?> salariesCol;

    @FXML
    private TableColumn<?, ?> maintenanceCol;

    @FXML
    private TableColumn<?, ?> partPurchasesCol;

    @FXML
    private TableColumn<?, ?> otherServicesCol;

    @FXML
    private TableColumn<?, ?> totalExpensesCol;

    @FXML
    private TableColumn<?, ?> netProfitCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("MonthlyProfit window is loaded");

        // Initialize combo boxes
        initializeComboBoxes();

        // Setup button actions
        setupButtonActions();

        // Load initial data
        loadMonthlyProfitData();
    }

    private void initializeComboBoxes() {
        // Populate month combo box
        ObservableList<String> months = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
        monthComboBox.setItems(months);

        // Populate year combo box with last 5 years
        ObservableList<Integer> years = FXCollections.observableArrayList();
        int currentYear = Year.now().getValue();
        for (int i = currentYear; i >= currentYear - 5; i--) {
            years.add(i);
        }
        yearComboBox.setItems(years);

        // Set current month and year as default
        int currentMonth = java.time.LocalDate.now().getMonthValue() - 1;
        monthComboBox.getSelectionModel().select(currentMonth);
        yearComboBox.getSelectionModel().select(Integer.valueOf(currentYear));
    }

    private void setupButtonActions() {
        generateBtn.setOnAction(event -> generateReport());
        resetBtn.setOnAction(event -> resetFilters());
        refreshBtn.setOnAction(event -> refreshData());
        printBtn.setOnAction(event -> printReport());
    }

    private void generateReport() {
        String selectedMonth = monthComboBox.getValue();
        Integer selectedYear = yearComboBox.getValue();

        if (selectedMonth == null || selectedYear == null) {
            System.out.println("Please select both month and year");
            return;
        }

        System.out.println("Generating report for " + selectedMonth + " " + selectedYear);

        // TODO: Add your database query logic here
        // Calculate totals and update the summary cards
        updateSummaryCards();
    }

    private void resetFilters() {
        // Reset to current month and year
        int currentMonth = java.time.LocalDate.now().getMonthValue() - 1;
        int currentYear = Year.now().getValue();

        monthComboBox.getSelectionModel().select(currentMonth);
        yearComboBox.getSelectionModel().select(Integer.valueOf(currentYear));

        // Reset summary cards
        totalIncomeText.setText("Rs. 0.00");
        totalExpensesText.setText("Rs. 0.00");
        netProfitText.setText("Rs. 0.00");
        totalTripsText.setText("0");

        // Clear table
        monthlyProfitTableView.getItems().clear();

        System.out.println("Filters reset");
    }

    private void refreshData() {
        System.out.println("Refreshing data...");
        loadMonthlyProfitData();
    }

    private void printReport() {
        System.out.println("Printing report...");
        // TODO: Implement print functionality
    }

    private void loadMonthlyProfitData() {
        // TODO: Load data from database
        System.out.println("Loading monthly profit data...");
    }

    private void updateSummaryCards() {
        // TODO: Calculate and update summary values
        // Example:
        // totalIncomeText.setText("Rs. 1,500,000.00");
        // totalExpensesText.setText("Rs. 800,000.00");
        // netProfitText.setText("Rs. 700,000.00");
        // totalTripsText.setText("120");
    }

    @FXML
    private void back(ActionEvent event) {
        // Close the current window
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
        System.out.println("Closing Monthly Profit window");
    }
}