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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.busmanagementsystem.Main;



import lk.ijse.busmanagementsystem.bo.BOFactory;
import lk.ijse.busmanagementsystem.bo.custom.BusBO;
import lk.ijse.busmanagementsystem.bo.custom.EmployeeBO;
import lk.ijse.busmanagementsystem.bo.custom.TripBO;
import lk.ijse.busmanagementsystem.bo.custom.TripEmployeeBO;
import lk.ijse.busmanagementsystem.dto.*;
import lk.ijse.busmanagementsystem.enums.TripCategory;
import lk.ijse.busmanagementsystem.tm.TripEmployeeTM;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageTripController implements Initializable {

    @FXML private AnchorPane tripContent;

    @FXML private TextField tripId;
    @FXML private ComboBox<TripCategory> tripCategory;
    @FXML private ComboBox<Integer> comboBusId;
    @FXML private Label lblBusNumber;
    @FXML private TextField startLocation;
    @FXML private TextField endLocation;
    @FXML private TextField distance;
    @FXML private TextField totalIncome;
    @FXML private DatePicker tripDate;
    @FXML private TextField description;
    @FXML private TextField txtSearch;

    @FXML private ComboBox<Number> comboEmployee;
    @FXML private Label lblEmployeeName;
    @FXML private Label lblEmployeeCategory;
    @FXML private ComboBox<String> comboRoleInTrip;

    @FXML private TableView<TripDTO> tableTrip;
    @FXML private TableColumn<TripDTO, Integer> colTripId;
    @FXML private TableColumn<TripDTO, TripCategory> colTripCategory;
    @FXML private TableColumn<TripDTO, Integer> colBusId;
    @FXML private TableColumn<TripDTO, String> colStartLocation;
    @FXML private TableColumn<TripDTO, String> colEndLocation;
    @FXML private TableColumn<TripDTO, Double> colDistance;
    @FXML private TableColumn<TripDTO, Double> colTotalIncome;
    @FXML private TableColumn<TripDTO, LocalDate> colTripDate;
    @FXML private TableColumn<TripDTO, String> colDescription;
    @FXML private TableColumn<TripDTO, String> colCreatedBy;

    @FXML private TableView<TripEmployeeTM> tblTripEmployees;
    @FXML private TableColumn<TripEmployeeTM, Integer> colEmpId;
    @FXML private TableColumn<TripEmployeeTM, String> colEmpName;
    @FXML private TableColumn<TripEmployeeTM, String> colEmpCategory;
    @FXML private TableColumn<TripEmployeeTM, String> colRoleInTrip;
    @FXML private TableColumn<TripEmployeeTM, String> colEmpContact;
    @FXML private TableColumn<TripEmployeeTM, Void> colAction;

    private final TripBO tripBO = (TripBO) BOFactory.getInstance().getBO(BOFactory.BOType.TRIP);
    private final EmployeeBO employeeBO = (EmployeeBO) BOFactory.getInstance().getBO(BOFactory.BOType.EMPLOYEE);
    private final TripEmployeeBO tripEmployeeBO = (TripEmployeeBO) BOFactory.getInstance().getBO(BOFactory.BOType.TRIP_EMPLOYEE);
    private final BusBO busBO = (BusBO) BOFactory.getInstance().getBO(BOFactory.BOType.BUS);

    private int currentSelectedTripId = 0;
    private final ObservableList<TripEmployeeTM> employeeObList = FXCollections.observableArrayList();
    private int currentUserId = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(" ManageTripController initialized");

        setupTripTable();
        setupEmployeeTable();
        loadInitialData();
        setupEventListeners();
    }


    private void setupTripTable() {
        colTripId.setCellValueFactory(new PropertyValueFactory<>("tripId"));
        colTripCategory.setCellValueFactory(new PropertyValueFactory<>("tripCategory"));
        colBusId.setCellValueFactory(new PropertyValueFactory<>("busId"));
        colStartLocation.setCellValueFactory(new PropertyValueFactory<>("startLocation"));
        colEndLocation.setCellValueFactory(new PropertyValueFactory<>("endLocation"));
        colDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        colTotalIncome.setCellValueFactory(new PropertyValueFactory<>("totalIncome"));
        colTripDate.setCellValueFactory(new PropertyValueFactory<>("tripDate"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdByUsername"));
    }


    private void setupEmployeeTable() {
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        colEmpCategory.setCellValueFactory(new PropertyValueFactory<>("empCategory"));
        colRoleInTrip.setCellValueFactory(new PropertyValueFactory<>("roleInTrip"));
        colEmpContact.setCellValueFactory(new PropertyValueFactory<>("contactNo"));

        colAction.setCellFactory(cell -> new TableCell<TripEmployeeTM, Void>() {
            private final Button removeBtn = new Button("🗑️ Remove");

            {
                removeBtn.setStyle(
                        "-fx-background-color: linear-gradient(to right, #e74c3c, #c0392b); " +
                                "-fx-text-fill: white; " +
                                "-fx-cursor: hand; " +
                                "-fx-background-radius: 5; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 11px;"
                );

                removeBtn.setOnAction(event -> {
                    TripEmployeeTM employee = getTableView().getItems().get(getIndex());
                    handleRemoveEmployee(employee);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeBtn);
            }
        });
    }


    private void loadInitialData() {
        tripCategory.getItems().addAll(TripCategory.values());
        tripCategory.setValue(TripCategory.ROUTE);
        comboRoleInTrip.getItems().addAll("DRIVER", "CONDUCTOR", "HELPER", "ASSISTANT");
        loadComboBus();
        loadComboEmployee();
        loadTripTable();
        tripId.setEditable(false);
        lblBusNumber.setText("-");
    }


    private void loadComboBus() {
        try {
            List<BusDTO> busList = busBO.getAllBuses();
            ObservableList<Integer> busIdObList = FXCollections.observableArrayList();

            for (BusDTO bus : busList) {
                busIdObList.add(bus.getBusId());
            }

            comboBusId.setItems(busIdObList);
            System.out.println(" Loaded " + busIdObList.size() + " buses");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load buses: " + e.getMessage());
        }
    }


    @FXML
    private void handleSelectBus(ActionEvent event) {
        try {
            Integer selectedBusId = comboBusId.getSelectionModel().getSelectedItem();
            if (selectedBusId == null) {
                lblBusNumber.setText("-");
                return;
            }

            BusDTO bus = busBO.getBusById(selectedBusId);

            if (bus != null) {
                lblBusNumber.setText(bus.getBusNumber() + " (" + bus.getBusBrandName() + ")");
                lblBusNumber.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
            } else {
                lblBusNumber.setText("Bus not found");
                lblBusNumber.setStyle("-fx-text-fill: #e74c3c;");
            }

        } catch (Exception e) {
            e.printStackTrace();
            lblBusNumber.setText("Error loading bus");
            lblBusNumber.setStyle("-fx-text-fill: #e74c3c;");
        }
    }


    private void setupEventListeners() {
        tableTrip.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelectedTrip(newSelection);
                currentSelectedTripId = newSelection.getTripId();
                loadEmployeesForSelectedTrip(currentSelectedTripId);
                System.out.println("📌 Selected Trip ID: " + currentSelectedTripId);
            }
        });
    }


    private void loadComboEmployee() {
        try {
            List<EmployeeDTO> employeeList = employeeBO.getAllEmployees();
            ObservableList<Number> empIdObList = FXCollections.observableArrayList();

            for (EmployeeDTO emp : employeeList) {
                if ("ACTIVE".equals(emp.getEmpStatus().name())) {
                    empIdObList.add(emp.getEmpId());
                }
            }

            comboEmployee.setItems(empIdObList);
            System.out.println(" Loaded " + empIdObList.size() + " active employees");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load employees: " + e.getMessage());
        }
    }


    private void loadEmployeesForSelectedTrip(int tripId) {
        try {
            List<TripEmployeeTM> employees = tripEmployeeBO.getEmployeesByTrip(tripId);

            employeeObList.clear();
            employeeObList.addAll(employees);
            tblTripEmployees.setItems(employeeObList);

            System.out.println(" Loaded " + employees.size() + " employees for Trip ID: " + tripId);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load employees: " + e.getMessage());
        }
    }


    @FXML
    private void handleSelectEmployee(ActionEvent event) {
        try {
            Number selectedId = comboEmployee.getSelectionModel().getSelectedItem();
            if (selectedId == null) return;

            int empId = selectedId.intValue();
            EmployeeDTO emp = employeeBO.searchEmployee(String.valueOf(empId));

            if (emp != null) {
                lblEmployeeName.setText(emp.getEmpName());
                lblEmployeeCategory.setText(emp.getEmpCategory().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load employee details");
        }
    }


    @FXML
    private void handleAddEmployee(ActionEvent event) {
        if (currentSelectedTripId == 0) {
            showAlert(Alert.AlertType.WARNING, "No Trip Selected",
                    "Please save a trip first or select an existing trip!");
            return;
        }

        if (comboEmployee.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select an employee!");
            return;
        }

        if (comboRoleInTrip.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a role!");
            return;
        }

        try {
            int empId = comboEmployee.getValue().intValue();
            String role = comboRoleInTrip.getValue();

            boolean alreadyAssigned = tripEmployeeBO.isEmployeeAssigned(
                    currentSelectedTripId, empId, role);

            if (alreadyAssigned) {
                showAlert(Alert.AlertType.WARNING, "Already Assigned",
                        "This employee is already assigned with this role to this trip!");
                return;
            }

            TripEmployeeDTO dto = new TripEmployeeDTO(
                    currentSelectedTripId,
                    empId,
                    role,
                    currentUserId
            );

            boolean assigned = tripEmployeeBO.assignEmployeeToTrip(dto);

            if (assigned) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Employee assigned successfully!");
                loadEmployeesForSelectedTrip(currentSelectedTripId);
                clearEmployeeSelection();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Failed to assign employee!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
        }
    }


    private void handleRemoveEmployee(TripEmployeeTM employee) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Removal");
        confirmAlert.setHeaderText("Remove Employee from Trip?");
        confirmAlert.setContentText(
                "Employee: " + employee.getEmpName() + "\n" +
                        "Role: " + employee.getRoleInTrip() + "\n\n" +
                        "Are you sure you want to remove this employee from the trip?"
        );

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean removed = tripEmployeeBO.removeEmployeeFromTrip(employee.getTripEmpId());

                if (removed) {
                    employeeObList.remove(employee);
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Employee removed successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed", "Failed to remove employee!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
            }
        }
    }


    @FXML
    private void handleSaveTripWithEmployees(ActionEvent event) {
        if (!validateTripFields()) {
            return;
        }

        try {
            int busIdValue = comboBusId.getValue();  // Get from ComboBox

            if (!tripBO.isBusExists(busIdValue)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Bus",
                        "Bus ID " + busIdValue + " does not exist!");
                return;
            }

            TripDTO tripDTO = new TripDTO(
                    0,
                    busIdValue,
                    tripCategory.getValue(),
                    startLocation.getText().trim(),
                    endLocation.getText().trim(),
                    Double.parseDouble(distance.getText().trim()),
                    Double.parseDouble(totalIncome.getText().trim()),
                    tripDate.getValue(),
                    description.getText().trim(),
                    currentUserId
            );

            boolean tripSaved = tripBO.saveTrip(tripDTO);

            if (tripSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Trip saved successfully!\n\nYou can now:\n" +
                                "1. Select the trip from the table\n" +
                                "2. Assign employees to it");

                loadTripTable();

                List<TripDTO> allTrips = tripBO.getAllTrips();
                if (!allTrips.isEmpty()) {
                    TripDTO lastTrip = allTrips.get(allTrips.size() - 1);
                    tableTrip.getSelectionModel().select(lastTrip);
                    fillFieldsFromSelectedTrip(lastTrip);
                    currentSelectedTripId = lastTrip.getTripId();
                    loadEmployeesForSelectedTrip(currentSelectedTripId);
                }

            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Failed to save trip!");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Please enter valid numeric values for Distance and Income!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
        }
    }


    @FXML
    private void handleUpdateTrip(ActionEvent event) {
        if (tripId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Trip Selected",
                    "Please select a trip to update!");
            return;
        }

        if (!validateTripFields()) {
            return;
        }

        try {
            int selectedTripId = Integer.parseInt(tripId.getText().trim());
            int busIdValue = comboBusId.getValue();  // Get from ComboBox

            if (!tripBO.isBusExists(busIdValue)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Bus",
                        "Bus ID does not exist!");
                return;
            }

            TripDTO tripDTO = new TripDTO(
                    selectedTripId,
                    busIdValue,
                    tripCategory.getValue(),
                    startLocation.getText().trim(),
                    endLocation.getText().trim(),
                    Double.parseDouble(distance.getText().trim()),
                    Double.parseDouble(totalIncome.getText().trim()),
                    tripDate.getValue(),
                    description.getText().trim(),
                    currentUserId
            );

            boolean updated = tripBO.updateTrip(tripDTO);

            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Trip updated successfully!");
                loadTripTable();
                loadEmployeesForSelectedTrip(selectedTripId);
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Failed to update trip!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
        }
    }


    @FXML
    private void handleDeleteTrip(ActionEvent event) {
        if (tripId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Trip Selected",
                    "Please select a trip to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Trip");
        confirmAlert.setContentText(
                "⚠️ This will delete the trip and ALL employee assignments!\n\n" +
                        "Trip ID: " + tripId.getText() + "\n" +
                        "Route: " + startLocation.getText() + " → " + endLocation.getText() + "\n\n" +
                        "Are you sure you want to continue?"
        );

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String id = tripId.getText();
                boolean deleted = tripBO.deleteTrip(id);

                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Trip deleted successfully!");
                    cleanFields();
                    loadTripTable();
                    employeeObList.clear();
                    currentSelectedTripId = 0;
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed", "Failed to delete trip!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
            }
        }
    }


    @FXML
    private void handleReset(ActionEvent event) {
        cleanFields();
        showAlert(Alert.AlertType.INFORMATION, "Reset", "All fields cleared!");
    }


    @FXML
    private void handleRefresh(ActionEvent event) {
        loadTripTable();
        loadComboEmployee();
        loadComboBus();  // Refresh bus list

        if (currentSelectedTripId != 0) {
            loadEmployeesForSelectedTrip(currentSelectedTripId);
        }

        showAlert(Alert.AlertType.INFORMATION, "Refreshed",
                "All data refreshed successfully!");
    }


    @FXML
    private void handleSearchTrip(ActionEvent event) {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadTripTable();
            return;
        }

        try {
            List<TripDTO> searchResults = tripBO.searchTrips(keyword);
            ObservableList<TripDTO> obList = FXCollections.observableArrayList(searchResults);
            tableTrip.setItems(obList);

            if (searchResults.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results",
                        "No trips found matching: " + keyword);
            } else {
                System.out.println(" Found " + searchResults.size() + " trips");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Search failed: " + e.getMessage());
        }
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


    private void loadTripTable() {
        try {
            List<TripDTO> tripList = tripBO.getAllTrips();
            ObservableList<TripDTO> obList = FXCollections.observableArrayList(tripList);
            tableTrip.setItems(obList);
            System.out.println(" Loaded " + tripList.size() + " trips");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load trips!");
        }
    }

    private void fillFieldsFromSelectedTrip(TripDTO trip) {
        tripId.setText(String.valueOf(trip.getTripId()));
        comboBusId.setValue(trip.getBusId());  // Set ComboBox value

        //Load bus number when trip is selected
        try {
            BusDTO bus = busBO.getBusById(trip.getBusId());
            if (bus != null) {
                lblBusNumber.setText(bus.getBusNumber() + " (" + bus.getBusBrandName() + ")");
                lblBusNumber.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
            }
        } catch (Exception e) {
            lblBusNumber.setText("-");
        }

        tripCategory.setValue(trip.getTripCategory());
        startLocation.setText(trip.getStartLocation());
        endLocation.setText(trip.getEndLocation());
        distance.setText(String.valueOf(trip.getDistance()));
        totalIncome.setText(String.valueOf(trip.getTotalIncome()));
        tripDate.setValue(trip.getTripDate());
        description.setText(trip.getDescription());
    }

    private boolean validateTripFields() {
        if (tripCategory.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select Trip Category!");
            return false;
        }

        if (comboBusId.getValue() == null) {  //  Changed validation
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select Bus ID!");
            return false;
        }

        if (startLocation.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter start location!");
            return false;
        }

        if (endLocation.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter end location!");
            return false;
        }

        if (distance.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter distance!");
            return false;
        }

        if (totalIncome.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter total income!");
            return false;
        }

        if (tripDate.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select trip date!");
            return false;
        }

        if (tripDate.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Invalid Date",
                    "Trip date cannot be in the future!");
            return false;
        }

        try {
            double dist = Double.parseDouble(distance.getText().trim());
            double income = Double.parseDouble(totalIncome.getText().trim());

            if (dist <= 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Distance",
                        "Distance must be greater than 0!");
                return false;
            }

            if (income < 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Income",
                        "Income cannot be negative!");
                return false;
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input",
                    "Distance and Income must be valid numbers!");
            return false;
        }

        return true;
    }

    private void cleanFields() {
        tripId.setText("");
        comboBusId.setValue(null);
        lblBusNumber.setText("-");
        tripCategory.setValue(null);
        startLocation.setText("");
        endLocation.setText("");
        distance.setText("");
        totalIncome.setText("");
        tripDate.setValue(null);
        description.setText("");

        clearEmployeeSelection();

        employeeObList.clear();
        currentSelectedTripId = 0;

        tableTrip.getSelectionModel().clearSelection();
    }

    private void clearEmployeeSelection() {
        comboEmployee.setValue(null);
        comboRoleInTrip.setValue(null);
        lblEmployeeName.setText("-");
        lblEmployeeCategory.setText("-");
    }


    @FXML
    private void tripEvent(ActionEvent event) throws Exception {
        openWindow("EventManage", "Event Manage - Gunasekara Travels");
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



    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }
}