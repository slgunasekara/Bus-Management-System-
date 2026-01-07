package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.busmanagementsystem.Main;
import lk.ijse.busmanagementsystem.dto.BusDTO;
import lk.ijse.busmanagementsystem.model.BusModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class ManageBusController implements Initializable {

    @FXML
    private AnchorPane BusContent;

    @FXML
    private TextField busId;

    @FXML
    private TextField brandName;

    @FXML
    private TextField busNumber;

    @FXML
    private ComboBox<String> busType;

    @FXML
    private TextField noOfSeats;

    @FXML
    private ComboBox<String> busStatus;

    @FXML
    private DatePicker manufactureDate;

    @FXML
    private DatePicker insuranceExpiryDate;

    @FXML
    private DatePicker licenseRenewalDate;

    @FXML
    private TextField currentMileage;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<BusDTO> tableCustomer;

    @FXML
    private TableColumn<BusDTO, Integer> colId;

    @FXML
    private TableColumn<BusDTO, String> colBrandName;

    @FXML
    private TableColumn<BusDTO, String> colBusNumber;

    @FXML
    private TableColumn<BusDTO, String> colBusType;

    @FXML
    private TableColumn<BusDTO, Integer> colNoOfSeats;

    @FXML
    private TableColumn<BusDTO, String> colBusStatus;

    @FXML
    private TableColumn<BusDTO, LocalDate> colManufactureDate;

    @FXML
    private TableColumn<BusDTO, LocalDate> colInsuranceExpiry;

    @FXML
    private TableColumn<BusDTO, LocalDate> colLicenseRenewal;

    @FXML
    private TableColumn<BusDTO, Integer> colMileage;

    @FXML
    private TableColumn<BusDTO, Integer> colCreatedBy;

    @FXML
    private TableColumn<BusDTO, LocalDateTime> colCreatedAt;

    @FXML
    private TableColumn<BusDTO, LocalDateTime> colUpdatedAt;

    private final BusModel busModel = new BusModel();
    private int currentUserId = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ManageBus is loaded");


        setupComboBoxes();


        setupTableColumns();


        loadBusTable();


        busId.setEditable(false);


        tableCustomer.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelectedBus(newSelection);
            }
        });
    }

    private void setupComboBoxes() {
        // Bus Type ComboBox
        ObservableList<String> busTypes = FXCollections.observableArrayList(
                "Non-AC", "AC", "Semi Luxury", "Luxury"
        );
        busType.setItems(busTypes);
        busType.setPromptText("Select Bus Type");

        // Bus Status ComboBox
        ObservableList<String> busStatuses = FXCollections.observableArrayList(
                "Active", "Maintenance", "Inactive", "Sold"
        );
        busStatus.setItems(busStatuses);
        busStatus.setPromptText("Select Bus Status");
        busStatus.setValue("Active"); // Default value
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("busId"));
        colBrandName.setCellValueFactory(new PropertyValueFactory<>("busBrandName"));
        colBusNumber.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        colBusType.setCellValueFactory(new PropertyValueFactory<>("busType"));
        colNoOfSeats.setCellValueFactory(new PropertyValueFactory<>("noOfSeats"));
        colBusStatus.setCellValueFactory(new PropertyValueFactory<>("busStatus"));
        colManufactureDate.setCellValueFactory(new PropertyValueFactory<>("manufactureDate"));
        colInsuranceExpiry.setCellValueFactory(new PropertyValueFactory<>("insuranceExpiryDate"));
        colLicenseRenewal.setCellValueFactory(new PropertyValueFactory<>("licenseRenewalDate"));
        colMileage.setCellValueFactory(new PropertyValueFactory<>("currentMileage"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    }

    private void loadBusTable() {
        try {
            List<BusDTO> busList = busModel.getAllBuses();
            ObservableList<BusDTO> obList = FXCollections.observableArrayList(busList);
            tableCustomer.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load buses: " + e.getMessage()).show();
        }
    }

    private void fillFieldsFromSelectedBus(BusDTO bus) {
        busId.setText(String.valueOf(bus.getBusId()));
        brandName.setText(bus.getBusBrandName());
        busNumber.setText(bus.getBusNumber());
        busType.setValue(bus.getBusType());
        noOfSeats.setText(String.valueOf(bus.getNoOfSeats()));
        busStatus.setValue(bus.getBusStatus());
        manufactureDate.setValue(bus.getManufactureDate());
        insuranceExpiryDate.setValue(bus.getInsuranceExpiryDate());
        licenseRenewalDate.setValue(bus.getLicenseRenewalDate());
        currentMileage.setText(String.valueOf(bus.getCurrentMileage()));
    }

    @FXML
    private void saveCustomer(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            // Check if bus number already exists
            if (busModel.isBusNumberExists(busNumber.getText().trim())) {
                new Alert(Alert.AlertType.WARNING, "Bus number already exists!").show();
                return;
            }

            BusDTO busDTO = new BusDTO(
                    0, // ID will be auto-generated
                    brandName.getText().trim(),
                    busNumber.getText().trim(),
                    busType.getValue(),
                    Integer.parseInt(noOfSeats.getText().trim()),
                    busStatus.getValue(),
                    manufactureDate.getValue(),
                    insuranceExpiryDate.getValue(),
                    licenseRenewalDate.getValue(),
                    currentMileage.getText().trim().isEmpty() ? 0 : Integer.parseInt(currentMileage.getText().trim()),
                    currentUserId
            );

            boolean isSaved = busModel.saveBus(busDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Bus saved successfully!").show();
                cleanFields();
                loadBusTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save bus!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error saving bus: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleSearchCustomer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String id = busId.getText();

            if (id.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Bus ID!").show();
                return;
            }

            try {
                BusDTO busDTO = busModel.searchBus(id);

                if (busDTO != null) {
                    fillFieldsFromSelectedBus(busDTO);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Bus not found!").show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error searching bus: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleCustomerUpdate(ActionEvent event) {
        if (busId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a bus to update!").show();
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            int currentBusId = Integer.parseInt(busId.getText().trim());

            // Check if bus number exists for another bus
            if (busModel.isBusNumberExistsForUpdate(busNumber.getText().trim(), currentBusId)) {
                new Alert(Alert.AlertType.WARNING, "Bus number already exists for another bus!").show();
                return;
            }

            BusDTO busDTO = new BusDTO(
                    currentBusId,
                    brandName.getText().trim(),
                    busNumber.getText().trim(),
                    busType.getValue(),
                    Integer.parseInt(noOfSeats.getText().trim()),
                    busStatus.getValue(),
                    manufactureDate.getValue(),
                    insuranceExpiryDate.getValue(),
                    licenseRenewalDate.getValue(),
                    currentMileage.getText().trim().isEmpty() ? 0 : Integer.parseInt(currentMileage.getText().trim()),
                    currentUserId
            );

            boolean isUpdated = busModel.updateBus(busDTO);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Bus updated successfully!").show();
                cleanFields();
                loadBusTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update bus!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error updating bus: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleCustomerDelete(ActionEvent event) {
        if (busId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a bus to delete!").show();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Bus");
        confirmAlert.setContentText("Are you sure you want to delete this bus?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = busId.getText();

            try {
                boolean isDeleted = busModel.deleteBus(id);

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Bus deleted successfully!").show();
                    cleanFields();
                    loadBusTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete bus!").show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error deleting bus: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleCustomerReset(ActionEvent event) {
        cleanFields();
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadBusTable();
            return;
        }

        try {
            List<BusDTO> searchResults = busModel.searchBuses(keyword);
            ObservableList<BusDTO> obList = FXCollections.observableArrayList(searchResults);
            tableCustomer.setItems(obList);

            if (searchResults.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No buses found matching: " + keyword).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error searching buses: " + e.getMessage()).show();
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        txtSearch.clear();
        loadBusTable();
        new Alert(Alert.AlertType.INFORMATION, "Bus list refreshed successfully!").show();
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Logout");
        confirmAlert.setHeaderText("Confirm Logout");
        confirmAlert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Main.setRoot("login");
            System.out.println("Logging out...");
        }
    }

    private boolean validateFields() {
        if (brandName.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter brand name!").show();
            return false;
        }

        if (busNumber.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter bus number!").show();
            return false;
        }

        if (busType.getValue() == null || busType.getValue().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select bus type!").show();
            return false;
        }

        if (noOfSeats.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter number of seats!").show();
            return false;
        }

        try {
            int seats = Integer.parseInt(noOfSeats.getText().trim());
            if (seats <= 0) {
                new Alert(Alert.AlertType.WARNING, "Number of seats must be positive!").show();
                return false;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Number of seats must be a valid number!").show();
            return false;
        }

        if (busStatus.getValue() == null || busStatus.getValue().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select bus status!").show();
            return false;
        }

        if (manufactureDate.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select manufacture date!").show();
            return false;
        }

        if (manufactureDate.getValue().isAfter(LocalDate.now())) {
            new Alert(Alert.AlertType.WARNING, "Manufacture date cannot be in the future!").show();
            return false;
        }

        // Validate insurance expiry date if provided
        if (insuranceExpiryDate.getValue() != null && insuranceExpiryDate.getValue().isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Insurance Warning");
            alert.setHeaderText("Insurance Expired");
            alert.setContentText("The insurance expiry date is in the past. Please renew the insurance.");
            alert.showAndWait();
        }

        // Validate license renewal date if provided
        if (licenseRenewalDate.getValue() != null && licenseRenewalDate.getValue().isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("License Warning");
            alert.setHeaderText("License Renewal Overdue");
            alert.setContentText("The license renewal date is in the past. Please renew the license.");
            alert.showAndWait();
        }

        // Validate mileage if provided
        if (!currentMileage.getText().trim().isEmpty()) {
            try {
                int mileage = Integer.parseInt(currentMileage.getText().trim());
                if (mileage < 0) {
                    new Alert(Alert.AlertType.WARNING, "Mileage cannot be negative!").show();
                    return false;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Mileage must be a valid number!").show();
                return false;
            }
        }

        return true;
    }

    private void cleanFields() {
        busId.setText("");
        brandName.setText("");
        busNumber.setText("");
        busType.setValue(null);
        noOfSeats.setText("");
        busStatus.setValue("Active");
        manufactureDate.setValue(null);
        insuranceExpiryDate.setValue(null);
        licenseRenewalDate.setValue(null);
        currentMileage.setText("");
    }
    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }


    //Jasper Report
    public void handlePrint(ActionEvent actionEvent) {
        try {
            // Get the current table data
            ObservableList<BusDTO> busList = tableCustomer.getItems();

            if (busList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Data", "No buses to generate report!");
                return;
            }

            // Load the JRXML file
            InputStream reportStream = getClass().getResourceAsStream("/lk/ijse/busmanagementsystem/reports/BusManagementReport.jrxml");

            if (reportStream == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Report template not found!");
                return;
            }

            // Compile the report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Calculate statistics
            int totalBuses = busList.size();
            long activeBuses = busList.stream()
                    .filter(bus -> "Active".equalsIgnoreCase(bus.getBusStatus()))
                    .count();
            long maintenanceBuses = busList.stream()
                    .filter(bus -> bus.getBusStatus() != null &&
                            (bus.getBusStatus().toLowerCase().contains("maintenance") ||
                                    bus.getBusStatus().toLowerCase().contains("repair")))
                    .count();

            // Create parameters map
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("GeneratedDate", LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")));
            parameters.put("TotalBuses", String.valueOf(totalBuses));
            parameters.put("ActiveBuses", String.valueOf(activeBuses));
            parameters.put("MaintenanceBuses", String.valueOf(maintenanceBuses));

            // Convert ObservableList to JRBeanCollectionDataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(busList);

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String outputPath = "BusFleetReport_" + timestamp + ".pdf";

            // Export to PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Report generated successfully!\nSaved as: " + outputPath);

            // Optional: Open the PDF automatically
            Desktop.getDesktop().open(new File(outputPath));

        } catch (JRException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error generating report: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error opening PDF: " + e.getMessage());
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}