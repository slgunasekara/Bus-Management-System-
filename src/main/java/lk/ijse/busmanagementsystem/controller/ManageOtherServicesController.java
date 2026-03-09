package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.busmanagementsystem.Main;
import lk.ijse.busmanagementsystem.dto.OtherServicesDTO;
import lk.ijse.busmanagementsystem.bo.BOFactory;
import lk.ijse.busmanagementsystem.bo.custom.OtherServicesBO;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageOtherServicesController implements Initializable {

    @FXML private AnchorPane otherServicesContent;
    @FXML private TextField serviceId;
    @FXML private ComboBox<Integer> comboBusId;
    @FXML private Label lblBusDetails;
    @FXML private ComboBox<Integer> comboTripId;
    @FXML private Label lblTripDetails;
    @FXML private TextField serviceName;
    @FXML private TextField cost;
    @FXML private DatePicker datePicker;
    @FXML private TextField description;
    @FXML private TextField txtSearch;

    @FXML private TableView<OtherServicesDTO> tableServices;
    @FXML private TableColumn<OtherServicesDTO, Integer> colServiceId;
    @FXML private TableColumn<OtherServicesDTO, Integer> colBusId;
    @FXML private TableColumn<OtherServicesDTO, Integer> colTripId;
    @FXML private TableColumn<OtherServicesDTO, String> colServiceName;
    @FXML private TableColumn<OtherServicesDTO, Double> colCost;
    @FXML private TableColumn<OtherServicesDTO, LocalDate> colDate;
    @FXML private TableColumn<OtherServicesDTO, String> colDescription;
    @FXML private TableColumn<OtherServicesDTO, Integer> colCreatedBy;

    private final OtherServicesBO servicesBO = (OtherServicesBO) BOFactory.getInstance().getBO(BOFactory.BOType.OTHER_SERVICES);
    private int currentUserId = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ManageOtherServices is loaded");

        setupTableColumns();
        loadComboData();
        loadServicesTable();
        setupEventListeners();

        serviceId.setEditable(false);
        lblBusDetails.setText("-");
        lblTripDetails.setText("-");
    }

    private void setupTableColumns() {
        colServiceId.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        colBusId.setCellValueFactory(new PropertyValueFactory<>("busId"));
        colTripId.setCellValueFactory(new PropertyValueFactory<>("tripId"));
        colServiceName.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    }

    private void loadComboData() {
        try {
            List<Integer> busIds = servicesBO.getAllBusIds();
            ObservableList<Integer> busIdList = FXCollections.observableArrayList(busIds);
            comboBusId.setItems(busIdList);
            System.out.println("✓ Loaded " + busIds.size() + " bus IDs");

            List<Integer> tripIds = servicesBO.getAllTripIds();
            ObservableList<Integer> tripIdList = FXCollections.observableArrayList(tripIds);
            comboTripId.setItems(tripIdList);
            System.out.println("✓ Loaded " + tripIds.size() + " trip IDs");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load combo data: " + e.getMessage());
        }
    }

    private void setupEventListeners() {
        tableServices.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelectedService(newSelection);
            }
        });

        comboBusId.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadBusDetails(newValue);
            }
        });

        comboTripId.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadTripDetails(newValue);
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

    @FXML
    private void handleSelectTrip(ActionEvent event) {
        Integer selectedTripId = comboTripId.getValue();
        if (selectedTripId != null) {
            loadTripDetails(selectedTripId);
        }
    }

    private void loadBusDetails(int busId) {
        try {
            String details = servicesBO.getBusDetails(busId);
            lblBusDetails.setText(details);
        } catch (Exception e) {
            lblBusDetails.setText("Error loading bus details");
            e.printStackTrace();
        }
    }

    private void loadTripDetails(int tripId) {
        try {
            String details = servicesBO.getTripDetails(tripId);
            lblTripDetails.setText(details);
        } catch (Exception e) {
            lblTripDetails.setText("Error loading trip details");
            e.printStackTrace();
        }
    }

    private void loadServicesTable() {
        try {
            List<OtherServicesDTO> servicesList = servicesBO.getAllServices();
            ObservableList<OtherServicesDTO> obList = FXCollections.observableArrayList(servicesList);
            tableServices.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load services: " + e.getMessage());
        }
    }

    private void fillFieldsFromSelectedService(OtherServicesDTO service) {
        serviceId.setText(String.valueOf(service.getServiceId()));

        comboBusId.setValue(service.getBusId());
        if (service.getBusId() != null) {
            loadBusDetails(service.getBusId());
        } else {
            lblBusDetails.setText("No bus associated");
        }

        comboTripId.setValue(service.getTripId());
        if (service.getTripId() != null) {
            loadTripDetails(service.getTripId());
        } else {
            lblTripDetails.setText("No trip associated");
        }

        serviceName.setText(service.getServiceName());
        cost.setText(String.valueOf(service.getCost()));
        datePicker.setValue(service.getDate());
        description.setText(service.getDescription());
    }

    @FXML
    private void saveService(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            Integer busIdValue = comboBusId.getValue();
            Integer tripIdValue = comboTripId.getValue();

            if (busIdValue != null && !servicesBO.isBusExists(busIdValue)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Bus", "Bus ID does not exist!");
                return;
            }

            if (tripIdValue != null && !servicesBO.isTripExists(tripIdValue)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Trip", "Trip ID does not exist!");
                return;
            }

            OtherServicesDTO serviceDTO = new OtherServicesDTO(
                    0,
                    busIdValue,
                    tripIdValue,
                    serviceName.getText().trim(),
                    Double.parseDouble(cost.getText().trim()),
                    datePicker.getValue(),
                    description.getText().trim(),
                    currentUserId
            );

            boolean isSaved = servicesBO.saveService(serviceDTO);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Service saved successfully!");
                cleanFields();
                loadServicesTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save service!");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving service: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchService(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String id = serviceId.getText();

            if (id.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please enter Service ID!");
                return;
            }

            try {
                OtherServicesDTO serviceDTO = servicesBO.searchService(id);

                if (serviceDTO != null) {
                    fillFieldsFromSelectedService(serviceDTO);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Not Found", "Service not found!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error searching service: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleServiceUpdate(ActionEvent event) {
        if (serviceId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a service to update!");
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            Integer busIdValue = comboBusId.getValue();
            Integer tripIdValue = comboTripId.getValue();

            if (busIdValue != null && !servicesBO.isBusExists(busIdValue)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Bus", "Bus ID does not exist!");
                return;
            }

            if (tripIdValue != null && !servicesBO.isTripExists(tripIdValue)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Trip", "Trip ID does not exist!");
                return;
            }

            OtherServicesDTO serviceDTO = new OtherServicesDTO(
                    Integer.parseInt(serviceId.getText().trim()),
                    busIdValue,
                    tripIdValue,
                    serviceName.getText().trim(),
                    Double.parseDouble(cost.getText().trim()),
                    datePicker.getValue(),
                    description.getText().trim(),
                    currentUserId
            );

            boolean isUpdated = servicesBO.updateService(serviceDTO);

            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Service updated successfully!");
                cleanFields();
                loadServicesTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update service!");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating service: " + e.getMessage());
        }
    }

    @FXML
    private void handleServiceDelete(ActionEvent event) {
        if (serviceId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a service to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Service");
        confirmAlert.setContentText("Are you sure you want to delete this service?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = serviceId.getText();

            try {
                boolean isDeleted = servicesBO.deleteService(id);

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Service deleted successfully!");
                    cleanFields();
                    loadServicesTable();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete service!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting service: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleServiceReset(ActionEvent event) {
        cleanFields();
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadServicesTable();
            return;
        }

        try {
            List<OtherServicesDTO> searchResults = servicesBO.searchServices(keyword);
            ObservableList<OtherServicesDTO> obList = FXCollections.observableArrayList(searchResults);
            tableServices.setItems(obList);

            if (searchResults.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No services found matching: " + keyword);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error searching services: " + e.getMessage());
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        txtSearch.clear();
        loadServicesTable();
        loadComboData();
        showAlert(Alert.AlertType.INFORMATION, "Refreshed", "Service list refreshed successfully!");
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
        if (serviceName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter service name!");
            return false;
        }

        if (cost.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter cost!");
            return false;
        }

        try {
            double costValue = Double.parseDouble(cost.getText().trim());
            if (costValue <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Cost must be positive!");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Cost must be a valid number!");
            return false;
        }

        if (datePicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a date!");
            return false;
        }

        if (datePicker.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Date cannot be in the future!");
            return false;
        }

        return true;
    }

    private void cleanFields() {
        serviceId.setText("");
        comboBusId.setValue(null);
        comboTripId.setValue(null);
        lblBusDetails.setText("-");
        lblTripDetails.setText("-");
        serviceName.setText("");
        cost.setText("");
        datePicker.setValue(null);
        description.setText("");
        tableServices.getSelectionModel().clearSelection();
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