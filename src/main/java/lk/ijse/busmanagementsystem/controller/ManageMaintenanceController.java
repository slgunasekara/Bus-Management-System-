package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.busmanagementsystem.Main;
import lk.ijse.busmanagementsystem.dto.MaintenanceDTO;
import lk.ijse.busmanagementsystem.enums.MaintenanceType;
import lk.ijse.busmanagementsystem.bo.BOFactory;
import lk.ijse.busmanagementsystem.bo.custom.MaintenanceBO;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageMaintenanceController implements Initializable {

    @FXML private AnchorPane maintenanceContent;

    @FXML private TextField maintenanceId;
    @FXML private ComboBox<Integer> comboBusId;
    @FXML private Label lblBusDetails;
    @FXML private ComboBox<MaintenanceType> maintenanceType;
    @FXML private DatePicker date;
    @FXML private TextField mileage;
    @FXML private TextField cost;
    @FXML private TextField maintainedBy;
    @FXML private TextField description;
    @FXML private TextField txtSearch;

    @FXML private TableView<MaintenanceDTO> tableMaintenance;
    @FXML private TableColumn<MaintenanceDTO, Integer> colMaintenanceId;
    @FXML private TableColumn<MaintenanceDTO, Integer> colBusId;
    @FXML private TableColumn<MaintenanceDTO, MaintenanceType> colMaintenanceType;
    @FXML private TableColumn<MaintenanceDTO, LocalDate> colServiceDate;
    @FXML private TableColumn<MaintenanceDTO, Double> colMileage;
    @FXML private TableColumn<MaintenanceDTO, Double> colCost;
    @FXML private TableColumn<MaintenanceDTO, String> colTechnician;
    @FXML private TableColumn<MaintenanceDTO, String> colDescription;
    @FXML private TableColumn<MaintenanceDTO, Integer> colCreatedBy;

    private final MaintenanceBO maintenanceBO = (MaintenanceBO) BOFactory.getInstance().getBO(BOFactory.BOType.MAINTENANCE);
    private int currentUserId = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(" ManageMaintenanceController initialized");

        setupTableColumns();
        loadComboData();
        loadMaintenanceTable();
        setupEventListeners();

        maintenanceId.setEditable(false);
        lblBusDetails.setText("-");
    }

    private void setupTableColumns() {
        colMaintenanceId.setCellValueFactory(new PropertyValueFactory<>("maintId"));
        colBusId.setCellValueFactory(new PropertyValueFactory<>("busId"));
        colMaintenanceType.setCellValueFactory(new PropertyValueFactory<>("maintenanceType"));
        colServiceDate.setCellValueFactory(new PropertyValueFactory<>("serviceDate"));
        colMileage.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colTechnician.setCellValueFactory(new PropertyValueFactory<>("technician"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    }

    private void loadComboData() {
        maintenanceType.getItems().addAll(MaintenanceType.values());
        maintenanceType.setValue(MaintenanceType.FULL_SERVICE);

        try {
            List<Integer> busIds = maintenanceBO.getAllBusIds();
            ObservableList<Integer> busIdList = FXCollections.observableArrayList(busIds);
            comboBusId.setItems(busIdList);
            System.out.println(" Loaded " + busIds.size() + " bus IDs");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load bus IDs: " + e.getMessage());
        }
    }

    private void setupEventListeners() {
        tableMaintenance.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelected(newSelection);
            }
        });

        comboBusId.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadBusDetails(newValue);
            }
        });
    }

    @FXML
    private void handleSelectBus(ActionEvent event) {
        Integer selectedBusId = comboBusId.getValue();
        if (selectedBusId != null) {
            loadBusDetails(selectedBusId);
        }
    }

    private void loadBusDetails(int busId) {
        try {
            String details = maintenanceBO.getBusDetails(busId);
            lblBusDetails.setText(details);
        } catch (Exception e) {
            lblBusDetails.setText("Error loading bus details");
            e.printStackTrace();
        }
    }

    private void loadMaintenanceTable() {
        try {
            List<MaintenanceDTO> maintenanceList = maintenanceBO.getAllMaintenance();
            ObservableList<MaintenanceDTO> obList = FXCollections.observableArrayList(maintenanceList);
            tableMaintenance.setItems(obList);
            System.out.println(" Loaded " + maintenanceList.size() + " maintenance records");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error",
                    "Failed to load maintenance records: " + e.getMessage());
        }
    }

    private void fillFieldsFromSelected(MaintenanceDTO maintenance) {
        maintenanceId.setText(String.valueOf(maintenance.getMaintId()));
        comboBusId.setValue(maintenance.getBusId());
        maintenanceType.setValue(maintenance.getMaintenanceType());
        date.setValue(maintenance.getServiceDate());
        mileage.setText(String.valueOf(maintenance.getMileage()));
        cost.setText(String.valueOf(maintenance.getCost()));
        maintainedBy.setText(maintenance.getTechnician());
        description.setText(maintenance.getDescription());

        loadBusDetails(maintenance.getBusId());
    }

    @FXML
    private void saveMaintenance(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            int busIdValue = comboBusId.getValue();

            if (!maintenanceBO.isBusExists(busIdValue)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Bus",
                        "Bus ID does not exist!");
                return;
            }

            MaintenanceDTO dto = new MaintenanceDTO(
                    0,
                    busIdValue,
                    maintenanceType.getValue(),
                    date.getValue(),
                    Double.parseDouble(mileage.getText().trim()),
                    Double.parseDouble(cost.getText().trim()),
                    maintainedBy.getText().trim(),
                    description.getText().trim(),
                    currentUserId
            );

            boolean isSaved = maintenanceBO.saveMaintenance(dto);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Maintenance record saved successfully!");
                cleanFields();
                loadMaintenanceTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Save Failed",
                        "Failed to save maintenance record!");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Please enter valid numeric values!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error saving maintenance: " + e.getMessage());
        }
    }

    @FXML
    private void handleMaintenanceUpdate(ActionEvent event) {
        if (maintenanceId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a maintenance record to update!");
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            int busIdValue = comboBusId.getValue();

            if (!maintenanceBO.isBusExists(busIdValue)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Bus",
                        "Bus ID does not exist!");
                return;
            }

            MaintenanceDTO dto = new MaintenanceDTO(
                    Integer.parseInt(maintenanceId.getText().trim()),
                    busIdValue,
                    maintenanceType.getValue(),
                    date.getValue(),
                    Double.parseDouble(mileage.getText().trim()),
                    Double.parseDouble(cost.getText().trim()),
                    maintainedBy.getText().trim(),
                    description.getText().trim(),
                    currentUserId
            );

            boolean isUpdated = maintenanceBO.updateMaintenance(dto);

            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Maintenance record updated successfully!");
                cleanFields();
                loadMaintenanceTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed",
                        "Failed to update maintenance record!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error updating maintenance: " + e.getMessage());
        }
    }

    @FXML
    private void handleMaintenanceDelete(ActionEvent event) {
        if (maintenanceId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a maintenance record to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Maintenance Record");
        confirmAlert.setContentText("Are you sure you want to delete this maintenance record?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = maintenanceId.getText();

            try {
                boolean isDeleted = maintenanceBO.deleteMaintenance(id);

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Maintenance record deleted successfully!");
                    cleanFields();
                    loadMaintenanceTable();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Delete Failed",
                            "Failed to delete maintenance record!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Error deleting maintenance: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleMaintenanceReset(ActionEvent event) {
        cleanFields();
    }

    @FXML
    private void handleSearchMaintenance(KeyEvent event) {
        String id = maintenanceId.getText();

        if (id.isEmpty()) {
            return;
        }

        try {
            List<MaintenanceDTO> searchResults = maintenanceBO.searchMaintenance(id);

            if (!searchResults.isEmpty()) {
                fillFieldsFromSelected(searchResults.get(0));
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found",
                        "No maintenance record found with ID: " + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Search Error",
                    "Error searching maintenance: " + e.getMessage());
        }
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadMaintenanceTable();
            return;
        }

        try {
            List<MaintenanceDTO> searchResults = maintenanceBO.searchMaintenance(keyword);
            ObservableList<MaintenanceDTO> obList = FXCollections.observableArrayList(searchResults);
            tableMaintenance.setItems(obList);

            if (searchResults.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results",
                        "No maintenance records found matching: " + keyword);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Search Error",
                    "Error searching maintenance: " + e.getMessage());
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        txtSearch.clear();
        loadMaintenanceTable();
        loadComboData();
        showAlert(Alert.AlertType.INFORMATION, "Refreshed",
                "Maintenance list refreshed successfully!");
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
        if (comboBusId.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select a Bus ID!");
            comboBusId.requestFocus();
            return false;
        }

        if (maintenanceType.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select Maintenance Type!");
            maintenanceType.requestFocus();
            return false;
        }

        if (date.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select maintenance date!");
            date.requestFocus();
            return false;
        }

        if (date.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Maintenance date cannot be in the future!");
            date.requestFocus();
            return false;
        }

        if (mileage.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter mileage!");
            mileage.requestFocus();
            return false;
        }

        try {
            double mileageValue = Double.parseDouble(mileage.getText().trim());
            if (mileageValue < 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error",
                        "Mileage cannot be negative!");
                mileage.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Mileage must be a valid number!");
            mileage.requestFocus();
            return false;
        }

        if (cost.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter cost!");
            cost.requestFocus();
            return false;
        }

        try {
            double costValue = Double.parseDouble(cost.getText().trim());
            if (costValue <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error",
                        "Cost must be greater than 0!");
                cost.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Cost must be a valid number!");
            cost.requestFocus();
            return false;
        }

        if (maintainedBy.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter who maintained the bus!");
            maintainedBy.requestFocus();
            return false;
        }

        return true;
    }

    private void cleanFields() {
        maintenanceId.setText("");
        comboBusId.setValue(null);
        lblBusDetails.setText("-");
        maintenanceType.setValue(MaintenanceType.FULL_SERVICE);
        date.setValue(null);
        mileage.setText("");
        cost.setText("");
        maintainedBy.setText("");
        description.setText("");
        tableMaintenance.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }
}