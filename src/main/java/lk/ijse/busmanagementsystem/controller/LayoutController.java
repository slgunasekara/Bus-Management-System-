package lk.ijse.busmanagementsystem.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lk.ijse.busmanagementsystem.Main;
import lk.ijse.busmanagementsystem.dto.DashboardDTO;
import lk.ijse.busmanagementsystem.dto.ProfitChartDTO;
import lk.ijse.busmanagementsystem.bo.BOFactory;
import lk.ijse.busmanagementsystem.bo.custom.DashboardBO;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {

    @FXML
    private AnchorPane mainContent;

    @FXML
    private Text totalBusesText;

    @FXML
    private Text totalTripsText;

    @FXML
    private Text totalEmployeesText;

    @FXML
    private Text netProfitText;

    @FXML
    private LineChart<String, Number> profitChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private final DashboardBO dashboardBO = (DashboardBO) BOFactory.getInstance().getBO(BOFactory.BOType.DASHBOARD);
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Layout is loaded");

        Platform.runLater(() -> {
            setupHoverEffects();
            loadDashboardData();
            loadProfitChart();
        });
    }


    private void loadDashboardData() {
        try {
            DashboardDTO dashboardData = dashboardBO.getDashboardSummary();

            totalBusesText.setText(String.valueOf(dashboardData.getTotalBuses()));
            totalTripsText.setText(String.valueOf(dashboardData.getTotalTrips()));
            totalEmployeesText.setText(String.valueOf(dashboardData.getTotalEmployees()));
            netProfitText.setText("Rs " + df.format(dashboardData.getNetProfit()));

            System.out.println("Dashboard data loaded successfully");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Failed to load dashboard data: " + e.getMessage());
        }
    }

    private void loadProfitChart() {
        try {
            List<ProfitChartDTO> chartData = dashboardBO.getSimplifiedProfitData();

            // Configure chart axes
            xAxis.setLabel("Date");
            yAxis.setLabel("Amount (Rs)");

            // Create data series
            XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
            incomeSeries.setName("Income");

            XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
            expenseSeries.setName("Expenses");

            XYChart.Series<String, Number> profitSeries = new XYChart.Series<>();
            profitSeries.setName("Profit");

            // Populate series with data
            for (ProfitChartDTO data : chartData) {
                String dateLabel = data.getFormattedDate();

                incomeSeries.getData().add(new XYChart.Data<>(dateLabel, data.getIncome()));
                expenseSeries.getData().add(new XYChart.Data<>(dateLabel, data.getExpense()));
                profitSeries.getData().add(new XYChart.Data<>(dateLabel, data.getProfit()));
            }

            // Clear existing data and add new series
            profitChart.getData().clear();
            profitChart.getData().addAll(incomeSeries, expenseSeries, profitSeries);

            // Style the chart
            profitChart.setTitle("Last 30 Days Financial Overview");
            profitChart.setLegendVisible(true);
            profitChart.setAnimated(true);

            // Apply custom colors to lines
            Platform.runLater(() -> {
                // Income - Green
                incomeSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #2ecc71; -fx-stroke-width: 3px;");
                // Expenses - Red
                expenseSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #e74c3c; -fx-stroke-width: 3px;");
                // Profit - Blue
                profitSeries.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: #3498db; -fx-stroke-width: 3px;");
            });

            System.out.println("Profit chart loaded with " + chartData.size() + " days of data");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Chart Error",
                    "Failed to load profit chart: " + e.getMessage());
        }
    }

    /**
     * Refresh dashboard data
     */
    @FXML
    private void refreshDashboard() {
        loadDashboardData();
        loadProfitChart();
        showAlert(Alert.AlertType.INFORMATION, "Refreshed", "Dashboard data refreshed successfully!");
    }

    private void setupHoverEffects() {
        if (mainContent.getScene() != null) {
            mainContent.getScene().getRoot().lookupAll(".button").forEach(node -> {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    addHoverEffect(button);
                }
            });
        }
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> {
            button.setScaleX(1.05);
            button.setScaleY(1.05);

            if (button.getGraphic() instanceof HBox) {
                HBox graphic = (HBox) button.getGraphic();
                graphic.getChildren().forEach(child -> {
                    if (child instanceof Text) {
                        Text text = (Text) child;
                        if (text.getUserData() == null) {
                            text.setUserData(text.getFont().getSize());
                        }
                        double originalSize = (double) text.getUserData();
                        text.setStyle("-fx-font-size: " + (originalSize + 2) + "px; -fx-font-weight: bold;");
                    }
                });
            }

            String currentStyle = button.getStyle();
            if (!currentStyle.contains("-fx-effect: dropshadow(gaussian, rgba(255,255,255")) {
                button.setStyle(currentStyle + "; -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.6), 20, 0, 0, 0);");
            }
        });

        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);

            if (button.getGraphic() instanceof HBox) {
                HBox graphic = (HBox) button.getGraphic();
                graphic.getChildren().forEach(child -> {
                    if (child instanceof Text) {
                        Text text = (Text) child;
                        if (text.getUserData() != null) {
                            double originalSize = (double) text.getUserData();
                            text.setStyle("-fx-font-size: " + originalSize + "px; -fx-font-weight: bold;");
                        }
                    }
                });
            }

            String originalStyle = button.getStyle();
            if (originalStyle.contains("; -fx-effect: dropshadow(gaussian, rgba(255,255,255")) {
                int effectIndex = originalStyle.indexOf("; -fx-effect: dropshadow(gaussian, rgba(255,255,255");
                button.setStyle(originalStyle.substring(0, effectIndex));
            }
        });
    }

    @FXML
    private void clickBusNav() throws IOException {
        Parent BusFXML = Main.loadFXML("ManageBus");
        mainContent.getChildren().setAll(BusFXML);
    }

    @FXML
    private void clickTripNav() throws IOException {
        Parent TripFXML = Main.loadFXML("ManageTrip");
        mainContent.getChildren().setAll(TripFXML);
    }

    @FXML
    private void clickOtherServicesNav() throws IOException {
        Parent OtherServicesFXML = Main.loadFXML("ManageOtherServices");
        mainContent.getChildren().setAll(OtherServicesFXML);
    }

    @FXML
    private void clickTripExpensesNav() throws IOException {
        Parent TripExpensesFXML = Main.loadFXML("ManageTripExpenses");
        mainContent.getChildren().setAll(TripExpensesFXML);
    }

    @FXML
    private void clickEmployeeNav() throws IOException {
        Parent EmployeeFXML = Main.loadFXML("ManageEmployee");
        mainContent.getChildren().setAll(EmployeeFXML);
    }

    @FXML
    private void clickEmployeeSalaryNav() throws IOException {
        Parent EmployeeSalaryFXML = Main.loadFXML("ManageEmployeeSalary");
        mainContent.getChildren().setAll(EmployeeSalaryFXML);
    }

    @FXML
    private void clickMaintenanceNav() throws IOException {
        Parent MaintenanceFXML = Main.loadFXML("ManageMaintenance");
        mainContent.getChildren().setAll(MaintenanceFXML);
    }

    @FXML
    private void clickPartPurchaseNav() throws IOException {
        Parent PartPurchaseFXML = Main.loadFXML("ManagePartPurchase");
        mainContent.getChildren().setAll(PartPurchaseFXML);
    }

    @FXML
    private void clickBusManagementToolsNav() throws IOException {
        Parent ManagementToolFXML = Main.loadFXML("BusManagementTools");
        mainContent.getChildren().setAll(ManagementToolFXML);
    }

    @FXML
    private void clickUpdatePricesNav() throws IOException {
        Parent CheckStatusFXML = Main.loadFXML("UpdatePrices");
        mainContent.getChildren().setAll(CheckStatusFXML);
    }

    @FXML
    private void clickManageReportNav() throws IOException {
        Parent ReportFXML = Main.loadFXML("ManageReport");
        mainContent.getChildren().setAll(ReportFXML);
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {
        Main.setRoot("login");
        System.out.println("Logout");
    }

    @FXML
    private void desktop(ActionEvent event) throws Exception {
        Main.setRoot("Layout");
    }

    @FXML
    private void clickManageUserNav() throws IOException {
        Parent ManageUserFXML = Main.loadFXML("ManageUser");
        mainContent.getChildren().setAll(ManageUserFXML);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}