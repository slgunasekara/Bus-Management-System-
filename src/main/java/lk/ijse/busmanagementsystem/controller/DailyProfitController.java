package lk.ijse.busmanagementsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
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
    private TableView<?> dailyProfitTableView;

    @FXML
    private TableColumn<?, ?> dateCol;

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
        System.out.println("DailyProfit window is loaded");

        // Setup button actions
        setupButtonActions();

        // Load initial data
        loadDailyProfitData();
    }

    private void setupButtonActions() {
        generateBtn.setOnAction(event -> generateReport());
        resetBtn.setOnAction(event -> resetFilters());
        refreshBtn.setOnAction(event -> refreshData());
        printBtn.setOnAction(event -> printReport());
    }

    private void generateReport() {
        // Implement report generation logic based on date range
        System.out.println("Generating report from " + fromDatePicker.getValue() +
                " to " + toDatePicker.getValue());

        //  Add your database query logic
        // Calculate totals and update the summary cards
        updateSummaryCards();
    }

    private void resetFilters() {
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);

        // Reset summary cards
        totalIncomeText.setText("Rs. 0.00");
        totalExpensesText.setText("Rs. 0.00");
        netProfitText.setText("Rs. 0.00");
        avgDailyProfitText.setText("Rs. 0.00");

        // Clear table
        dailyProfitTableView.getItems().clear();

        System.out.println("Filters reset");
    }

    private void refreshData() {
        System.out.println("Refreshing data...");
        loadDailyProfitData();
    }

    private void printReport() {
        System.out.println("Printing report...");
    }

    private void loadDailyProfitData() {
        System.out.println("Loading daily profit data...");
    }

    private void updateSummaryCards() {
    }

    @FXML
    private void back(ActionEvent event) {
        // Close the current window
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
        System.out.println("Closing Daily Profit window");
    }
}