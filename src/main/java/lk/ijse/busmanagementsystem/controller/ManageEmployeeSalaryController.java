package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.busmanagementsystem.Main;
import lk.ijse.busmanagementsystem.dto.EmployeeSalaryDTO;
import lk.ijse.busmanagementsystem.dto.EmployeeSalaryTM;
import lk.ijse.busmanagementsystem.model.EmployeeSalaryModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageEmployeeSalaryController implements Initializable {

    @FXML private AnchorPane salaryContent;

    // Form Fields
    @FXML private TextField salaryId;
    @FXML private ComboBox<Integer> comboEmployeeId;
    @FXML private Label lblEmployeeDetails;
    @FXML private ComboBox<Integer> comboTripId;
    @FXML private Label lblTripDetails;
    @FXML private TextField txtAmount;
    @FXML private TextField txtDescription;
    @FXML private DatePicker dateSalary;
    @FXML private TextField txtSearch;

    // Table
    @FXML private TableView<EmployeeSalaryTM> tableSalary;
    @FXML private TableColumn<EmployeeSalaryTM, Integer> colSalaryId;
    @FXML private TableColumn<EmployeeSalaryTM, Integer> colEmployeeId;
    @FXML private TableColumn<EmployeeSalaryTM, String> colEmployeeName;
    @FXML private TableColumn<EmployeeSalaryTM, Integer> colTripId;
    @FXML private TableColumn<EmployeeSalaryTM, Double> colAmount;
    @FXML private TableColumn<EmployeeSalaryTM, String> colDescription;
    @FXML private TableColumn<EmployeeSalaryTM, LocalDate> colDate;
    @FXML private TableColumn<EmployeeSalaryTM, String> colCreatedBy;

    private final EmployeeSalaryModel salaryModel = new EmployeeSalaryModel();
    private int currentUserId = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(" ManageEmployeeSalary is loaded");

        setupTableColumns();
        loadComboData();
        loadSalaryTable();
        setupEventListeners();

        salaryId.setEditable(false);
        lblEmployeeDetails.setText("-");
        lblTripDetails.setText("-");
    }

    private void setupTableColumns() {
        colSalaryId.setCellValueFactory(new PropertyValueFactory<>("salaryId"));
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colEmployeeName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        colTripId.setCellValueFactory(new PropertyValueFactory<>("tripId"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdByUsername"));
    }

    private void loadComboData() {
        try {
            // Load Employee IDs
            List<Integer> empIds = salaryModel.getAllActiveEmployeeIds();
            ObservableList<Integer> empIdList = FXCollections.observableArrayList(empIds);
            comboEmployeeId.setItems(empIdList);
            System.out.println(" Loaded " + empIds.size() + " employee IDs");

            // Load Trip IDs
            List<Integer> tripIds = salaryModel.getAllTripIds();
            ObservableList<Integer> tripIdList = FXCollections.observableArrayList(tripIds);
            comboTripId.setItems(tripIdList);
            System.out.println(" Loaded " + tripIds.size() + " trip IDs");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load combo data: " + e.getMessage());
        }
    }

    private void setupEventListeners() {
        tableSalary.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelected(newSelection);
            }
        });

        comboEmployeeId.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadEmployeeDetails(newValue);
            }
        });

        comboTripId.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadTripDetails(newValue);
            }
        });
    }

    @FXML
    private void handleSelectEmployee(ActionEvent event) {
        Integer selectedEmpId = comboEmployeeId.getValue();
        if (selectedEmpId != null) {
            loadEmployeeDetails(selectedEmpId);
        }
    }

    @FXML
    private void handleSelectTrip(ActionEvent event) {
        Integer selectedTripId = comboTripId.getValue();
        if (selectedTripId != null) {
            loadTripDetails(selectedTripId);
        }
    }

    private void loadEmployeeDetails(int empId) {
        try {
            String details = salaryModel.getEmployeeDetails(empId);
            lblEmployeeDetails.setText(details);
        } catch (Exception e) {
            lblEmployeeDetails.setText("Error loading employee details");
            e.printStackTrace();
        }
    }

    private void loadTripDetails(int tripId) {
        try {
            String details = salaryModel.getTripDetails(tripId);
            lblTripDetails.setText(details);
        } catch (Exception e) {
            lblTripDetails.setText("Error loading trip details");
            e.printStackTrace();
        }
    }

    private void loadSalaryTable() {
        try {
            List<EmployeeSalaryTM> salaryList = salaryModel.getAllEmployeeSalaries();
            ObservableList<EmployeeSalaryTM> obList = FXCollections.observableArrayList(salaryList);
            tableSalary.setItems(obList);
            System.out.println(" Loaded " + salaryList.size() + " salary records");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error",
                    "Failed to load salary records: " + e.getMessage());
        }
    }

    private void fillFieldsFromSelected(EmployeeSalaryTM salary) {
        salaryId.setText(String.valueOf(salary.getSalaryId()));
        comboEmployeeId.setValue(salary.getEmpId());
        comboTripId.setValue(salary.getTripId());
        txtAmount.setText(String.valueOf(salary.getAmount()));
        txtDescription.setText(salary.getDescription());
        dateSalary.setValue(salary.getDate());

        loadEmployeeDetails(salary.getEmpId());
        if (salary.getTripId() != null) {
            loadTripDetails(salary.getTripId());
        } else {
            lblTripDetails.setText("No trip associated");
        }
    }

    @FXML
    private void handleSaveSalary(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            int empId = comboEmployeeId.getValue();

            if (!salaryModel.isEmployeeExists(empId)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Employee",
                        "Employee ID does not exist!");
                return;
            }

            Integer tripId = comboTripId.getValue();
            if (tripId != null && !salaryModel.isTripExists(tripId)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Trip",
                        "Trip ID does not exist!");
                return;
            }

            EmployeeSalaryDTO dto = new EmployeeSalaryDTO(
                    0,
                    empId,
                    tripId,
                    Double.parseDouble(txtAmount.getText().trim()),
                    txtDescription.getText().trim(),
                    dateSalary.getValue(),
                    currentUserId
            );

            boolean isSaved = salaryModel.saveEmployeeSalary(dto);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Employee salary saved successfully!");
                cleanFields();
                loadSalaryTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Save Failed",
                        "Failed to save employee salary!");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Please enter valid numeric values!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error saving salary: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateSalary(ActionEvent event) {
        if (salaryId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a salary record to update!");
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            int empId = comboEmployeeId.getValue();

            if (!salaryModel.isEmployeeExists(empId)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Employee",
                        "Employee ID does not exist!");
                return;
            }

            Integer tripId = comboTripId.getValue();
            if (tripId != null && !salaryModel.isTripExists(tripId)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Trip",
                        "Trip ID does not exist!");
                return;
            }

            EmployeeSalaryDTO dto = new EmployeeSalaryDTO(
                    Integer.parseInt(salaryId.getText().trim()),
                    empId,
                    tripId,
                    Double.parseDouble(txtAmount.getText().trim()),
                    txtDescription.getText().trim(),
                    dateSalary.getValue(),
                    currentUserId
            );

            boolean isUpdated = salaryModel.updateEmployeeSalary(dto);

            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Employee salary updated successfully!");
                cleanFields();
                loadSalaryTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed",
                        "Failed to update employee salary!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error updating salary: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteSalary(ActionEvent event) {
        if (salaryId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a salary record to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Salary Record");
        confirmAlert.setContentText("Are you sure you want to delete this salary record?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = salaryId.getText();

            try {
                boolean isDeleted = salaryModel.deleteEmployeeSalary(id);

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Salary record deleted successfully!");
                    cleanFields();
                    loadSalaryTable();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Delete Failed",
                            "Failed to delete salary record!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Error deleting salary: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        cleanFields();
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadSalaryTable();
            return;
        }

        try {
            List<EmployeeSalaryTM> searchResults = salaryModel.searchEmployeeSalaries(keyword);
            ObservableList<EmployeeSalaryTM> obList = FXCollections.observableArrayList(searchResults);
            tableSalary.setItems(obList);

            if (searchResults.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results",
                        "No salary records found matching: " + keyword);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Search Error",
                    "Error searching salaries: " + e.getMessage());
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        txtSearch.clear();
        loadSalaryTable();
        loadComboData();
        showAlert(Alert.AlertType.INFORMATION, "Refreshed",
                "Salary records refreshed successfully!");
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
        if (comboEmployeeId.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select an Employee ID!");
            comboEmployeeId.requestFocus();
            return false;
        }

        if (txtAmount.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter salary amount!");
            txtAmount.requestFocus();
            return false;
        }

        try {
            double amount = Double.parseDouble(txtAmount.getText().trim());
            if (amount <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error",
                        "Amount must be greater than 0!");
                txtAmount.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Amount must be a valid number!");
            txtAmount.requestFocus();
            return false;
        }

        if (dateSalary.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select salary date!");
            dateSalary.requestFocus();
            return false;
        }

        if (dateSalary.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Salary date cannot be in the future!");
            dateSalary.requestFocus();
            return false;
        }

        return true;
    }

    private void cleanFields() {
        salaryId.setText("");
        comboEmployeeId.setValue(null);
        comboTripId.setValue(null);
        lblEmployeeDetails.setText("-");
        lblTripDetails.setText("-");
        txtAmount.setText("");
        txtDescription.setText("");
        dateSalary.setValue(null);
        tableSalary.getSelectionModel().clearSelection();
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