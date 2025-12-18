package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.busmanagementsystem.dto.EmployeeDTO;
import lk.ijse.busmanagementsystem.enums.EmployeeCategory;
import lk.ijse.busmanagementsystem.enums.EmployeeStatus;
import lk.ijse.busmanagementsystem.model.EmployeeModel;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageEmployeeController implements Initializable {

    @FXML
    private AnchorPane employeeContent;

    @FXML
    private TextField empId;

    @FXML
    private ComboBox<EmployeeCategory> empCategory;

    @FXML
    private TextField empName;

    @FXML
    private TextField empAddress;

    @FXML
    private TextField empContact;

    @FXML
    private TextField empNic;

    @FXML
    private TextField empNTC;

    @FXML
    private TextField empDriveLic;

    @FXML
    private DatePicker empJoinDate;

    @FXML
    private DatePicker empExitDate;

    @FXML
    private ComboBox<EmployeeStatus> empStatus;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<EmployeeDTO> tableEmployee;

    @FXML
    private TableColumn<EmployeeDTO, Integer> colId;

    @FXML
    private TableColumn<EmployeeDTO, EmployeeCategory> colEmpCategory;

    @FXML
    private TableColumn<EmployeeDTO, String> colEmpName;

    @FXML
    private TableColumn<EmployeeDTO, String> colEmpAddress;

    @FXML
    private TableColumn<EmployeeDTO, String> colEmpContact;

    @FXML
    private TableColumn<EmployeeDTO, String> colEmpNic;

    @FXML
    private TableColumn<EmployeeDTO, String> colEmpNtc;

    @FXML
    private TableColumn<EmployeeDTO, String> colEmpDriveLic;

    @FXML
    private TableColumn<EmployeeDTO, LocalDate> colEmpJoinDate;

    @FXML
    private TableColumn<EmployeeDTO, LocalDate> colEmpExitDate;

    @FXML
    private TableColumn<EmployeeDTO, EmployeeStatus> colEmpStatus;

    @FXML
    private TableColumn<EmployeeDTO, Integer> colCreatedBy;

    private final EmployeeModel employeeModel = new EmployeeModel();
    private int currentUserId = 1; // This should come from your login session

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ManageEmployee is loaded");

        // Setup table columns - Database column names use කරනවා
        colId.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colEmpCategory.setCellValueFactory(new PropertyValueFactory<>("empCategory"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("empName"));
        colEmpAddress.setCellValueFactory(new PropertyValueFactory<>("address")); // Database: address
        colEmpContact.setCellValueFactory(new PropertyValueFactory<>("contactNo")); // Database: contact_no
        colEmpNic.setCellValueFactory(new PropertyValueFactory<>("nicNo")); // Database: nic_no
        colEmpNtc.setCellValueFactory(new PropertyValueFactory<>("ntcNo")); // Database: ntc_no
        colEmpDriveLic.setCellValueFactory(new PropertyValueFactory<>("drivingLicenceNo")); // Database: driving_licence_no
        colEmpJoinDate.setCellValueFactory(new PropertyValueFactory<>("joinDate")); // Database: join_date
        colEmpExitDate.setCellValueFactory(new PropertyValueFactory<>("exitDate")); // Database: exit_date
        colEmpStatus.setCellValueFactory(new PropertyValueFactory<>("empStatus"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));

        // Load enum values to ComboBoxes
        empCategory.getItems().addAll(EmployeeCategory.values());
        empCategory.setValue(EmployeeCategory.DRIVER); // Default value

        empStatus.getItems().addAll(EmployeeStatus.values());
        empStatus.setValue(EmployeeStatus.ACTIVE); // Default value

        // Load all employees
        loadEmployeeTable();

        // Make empId field non-editable
        empId.setEditable(false);

        // Add table row selection listener
        tableEmployee.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelectedEmployee(newSelection);
            }
        });
    }

    private void loadEmployeeTable() {
        try {
            List<EmployeeDTO> employeeList = employeeModel.getAllEmployees();
            ObservableList<EmployeeDTO> obList = FXCollections.observableArrayList(employeeList);
            tableEmployee.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load employees: " + e.getMessage()).show();
        }
    }

    private void fillFieldsFromSelectedEmployee(EmployeeDTO employee) {
        empId.setText(String.valueOf(employee.getEmpId()));
        empCategory.setValue(employee.getEmpCategory());
        empName.setText(employee.getEmpName());
        empAddress.setText(employee.getAddress());
        empContact.setText(employee.getContactNo());
        empNic.setText(employee.getNicNo());
        empNTC.setText(employee.getNtcNo());
        empDriveLic.setText(employee.getDrivingLicenceNo());
        empJoinDate.setValue(employee.getJoinDate());
        empExitDate.setValue(employee.getExitDate());
        empStatus.setValue(employee.getEmpStatus());
    }

    @FXML
    private void saveEmployee(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            // Check if NIC already exists
            if (employeeModel.isNicExists(empNic.getText().trim())) {
                new Alert(Alert.AlertType.WARNING, "NIC Number already exists! Please enter a unique NIC.").show();
                return;
            }

            EmployeeDTO employeeDTO = new EmployeeDTO(
                    0,
                    empCategory.getValue(),
                    empName.getText().trim(),
                    empAddress.getText().trim(),
                    empContact.getText().trim(),
                    empNic.getText().trim(),
                    empNTC.getText().trim(),
                    empDriveLic.getText().trim(),
                    empJoinDate.getValue(),
                    empExitDate.getValue(),
                    empStatus.getValue(),
                    currentUserId
            );

            boolean isSaved = employeeModel.saveEmployee(employeeDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Employee saved successfully!").show();
                cleanFields();
                loadEmployeeTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save employee!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error saving employee: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleEmployeeUpdate(ActionEvent event) {
        if (empId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an employee to update!").show();
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            int employeeId = Integer.parseInt(empId.getText().trim());

            // Check if NIC exists for another employee
            if (employeeModel.isNicExistsForOther(empNic.getText().trim(), employeeId)) {
                new Alert(Alert.AlertType.WARNING, "NIC Number already exists for another employee!").show();
                return;
            }

            EmployeeDTO employeeDTO = new EmployeeDTO(
                    employeeId,
                    empCategory.getValue(),
                    empName.getText().trim(),
                    empAddress.getText().trim(),
                    empContact.getText().trim(),
                    empNic.getText().trim(),
                    empNTC.getText().trim(),
                    empDriveLic.getText().trim(),
                    empJoinDate.getValue(),
                    empExitDate.getValue(),
                    empStatus.getValue(),
                    currentUserId
            );

            boolean isUpdated = employeeModel.updateEmployee(employeeDTO);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully!").show();
                cleanFields();
                loadEmployeeTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update employee!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error updating employee: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleEmployeeDelete(ActionEvent event) {
        if (empId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an employee to delete!").show();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Employee");
        confirmAlert.setContentText("Are you sure you want to delete this employee?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = empId.getText();

            try {
                boolean isDeleted = employeeModel.deleteEmployee(id);

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Employee deleted successfully!").show();
                    cleanFields();
                    loadEmployeeTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete employee!").show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error deleting employee: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleEmployeeReset(ActionEvent event) {
        cleanFields();
    }

    @FXML
    public void handleSearchEmployee(ActionEvent event) {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadEmployeeTable();
            return;
        }

        try {
            List<EmployeeDTO> searchResults = employeeModel.searchEmployees(keyword);

            ObservableList<EmployeeDTO> obList = FXCollections.observableArrayList();

            for (EmployeeDTO employeeDTO : searchResults) {
                obList.add(employeeDTO);
            }

            tableEmployee.setItems(obList);

            if (searchResults.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No employees found matching: " + keyword).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error searching employees: " + e.getMessage()).show();
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        txtSearch.clear();
        loadEmployeeTable();
        new Alert(Alert.AlertType.INFORMATION, "Employee list refreshed successfully!").show();
    }

    @FXML
    public void logout(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Logout");
        confirmAlert.setHeaderText("Confirm Logout");
        confirmAlert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Navigate to login page
            System.out.println("Logging out...");
        }
    }

    private boolean validateFields() {
        // Employee Category validation
        if (empCategory.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select an Employee Category!").show();
            empCategory.requestFocus();
            return false;
        }

        // Name validation
        if (empName.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter Employee Name!").show();
            empName.requestFocus();
            return false;
        }

        // Address validation
        if (empAddress.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter Employee Address!").show();
            empAddress.requestFocus();
            return false;
        }

        // Contact validation
        if (empContact.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter Contact Number!").show();
            empContact.requestFocus();
            return false;
        }

        // Contact number format validation (10 digits)
        if (!empContact.getText().trim().matches("\\d{10}")) {
            new Alert(Alert.AlertType.WARNING, "Contact Number must be 10 digits!").show();
            empContact.requestFocus();
            return false;
        }

        // NIC validation
        if (empNic.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter NIC Number!").show();
            empNic.requestFocus();
            return false;
        }

        // NIC format validation (old: 9 digits + V, new: 12 digits)
        String nicPattern = "^([0-9]{9}[vVxX]|[0-9]{12})$";
        if (!empNic.getText().trim().matches(nicPattern)) {
            new Alert(Alert.AlertType.WARNING, "Invalid NIC format! Use 9 digits + V or 12 digits.").show();
            empNic.requestFocus();
            return false;
        }

        // Join Date validation
        if (empJoinDate.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select Join Date!").show();
            empJoinDate.requestFocus();
            return false;
        }

        // Join date cannot be in the future
        if (empJoinDate.getValue().isAfter(LocalDate.now())) {
            new Alert(Alert.AlertType.WARNING, "Join Date cannot be in the future!").show();
            empJoinDate.requestFocus();
            return false;
        }

        // Exit Date validation (if provided)
        if (empExitDate.getValue() != null) {
            // Exit date must be after join date
            if (empExitDate.getValue().isBefore(empJoinDate.getValue())) {
                new Alert(Alert.AlertType.WARNING, "Exit Date must be after Join Date!").show();
                empExitDate.requestFocus();
                return false;
            }
        }

        // Employee Status validation
        if (empStatus.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select Employee Status!").show();
            empStatus.requestFocus();
            return false;
        }

        return true;
    }

    private void cleanFields() {
        empId.setText("");
        empCategory.setValue(null);
        empName.setText("");
        empAddress.setText("");
        empContact.setText("");
        empNic.setText("");
        empNTC.setText("");
        empDriveLic.setText("");
        empJoinDate.setValue(null);
        empExitDate.setValue(null);
        empStatus.setValue(null);
        tableEmployee.getSelectionModel().clearSelection();
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }
}