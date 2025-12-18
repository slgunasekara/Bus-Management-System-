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
import lk.ijse.busmanagementsystem.dto.TripExpensesDTO;
import lk.ijse.busmanagementsystem.enums.TripExpType;
import lk.ijse.busmanagementsystem.model.TripExpensesModel;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageTripExpensesController implements Initializable {

    @FXML private AnchorPane tripExpensesContent;

    // Form Fields
    @FXML private TextField tripExpenseId;
    @FXML private ComboBox<Integer> comboTripId;
    @FXML private Label lblTripDetails;
    @FXML private ComboBox<TripExpType> comboExpenseType;
    @FXML private TextField txtAmount;
    @FXML private TextField txtDescription;
    @FXML private DatePicker dateExpense;
    @FXML private TextField txtSearch;

    // Table
    @FXML private TableView<TripExpensesDTO> tableExpenses;
    @FXML private TableColumn<TripExpensesDTO, Integer> colExpenseId;
    @FXML private TableColumn<TripExpensesDTO, Integer> colTripId;
    @FXML private TableColumn<TripExpensesDTO, TripExpType> colExpenseType;
    @FXML private TableColumn<TripExpensesDTO, Double> colAmount;
    @FXML private TableColumn<TripExpensesDTO, String> colDescription;
    @FXML private TableColumn<TripExpensesDTO, LocalDate> colDate;
    @FXML private TableColumn<TripExpensesDTO, Integer> colCreatedBy;

    private final TripExpensesModel expensesModel = new TripExpensesModel();
    private int currentUserId = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(" ManageTripExpensesController initialized");

        setupTableColumns();
        loadComboData();
        loadExpensesTable();
        setupEventListeners();

        tripExpenseId.setEditable(false);
        lblTripDetails.setText("-");
    }

    private void setupTableColumns() {
        colExpenseId.setCellValueFactory(new PropertyValueFactory<>("tripExpId"));
        colTripId.setCellValueFactory(new PropertyValueFactory<>("tripId"));
        colExpenseType.setCellValueFactory(new PropertyValueFactory<>("tripExpType"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    }

    private void loadComboData() {
        // Load Expense Types
        comboExpenseType.getItems().addAll(TripExpType.values());
        comboExpenseType.setValue(TripExpType.FUEL);

        // Load Trip IDs
        try {
            List<Integer> tripIds = expensesModel.getAllTripIds();
            ObservableList<Integer> tripIdList = FXCollections.observableArrayList(tripIds);
            comboTripId.setItems(tripIdList);
            System.out.println(" Loaded " + tripIds.size() + " trip IDs");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load trip IDs: " + e.getMessage());
        }
    }

    private void setupEventListeners() {
        tableExpenses.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelected(newSelection);
            }
        });

        comboTripId.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadTripDetails(newValue);
            }
        });
    }

    @FXML
    private void handleSelectTrip(ActionEvent event) {
        Integer selectedTripId = comboTripId.getValue();
        if (selectedTripId != null) {
            loadTripDetails(selectedTripId);
        }
    }

    private void loadTripDetails(int tripId) {
        try {
            String details = expensesModel.getTripDetails(tripId);
            lblTripDetails.setText(details);
        } catch (Exception e) {
            lblTripDetails.setText("Error loading trip details");
            e.printStackTrace();
        }
    }

    private void loadExpensesTable() {
        try {
            List<TripExpensesDTO> expensesList = expensesModel.getAllTripExpenses();
            ObservableList<TripExpensesDTO> obList = FXCollections.observableArrayList(expensesList);
            tableExpenses.setItems(obList);
            System.out.println(" Loaded " + expensesList.size() + " expenses");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error",
                    "Failed to load expenses: " + e.getMessage());
        }
    }

    private void fillFieldsFromSelected(TripExpensesDTO expense) {
        tripExpenseId.setText(String.valueOf(expense.getTripExpId()));
        comboTripId.setValue(expense.getTripId());
        comboExpenseType.setValue(expense.getTripExpType());
        txtAmount.setText(String.valueOf(expense.getAmount()));
        txtDescription.setText(expense.getDescription());
        dateExpense.setValue(expense.getDate());

        loadTripDetails(expense.getTripId());
    }

    @FXML
    private void handleSaveExpense(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            int tripId = comboTripId.getValue();

            if (!expensesModel.isTripExists(tripId)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Trip",
                        "Trip ID does not exist!");
                return;
            }

            TripExpensesDTO dto = new TripExpensesDTO(
                    0,
                    tripId,
                    comboExpenseType.getValue(),
                    null,
                    Double.parseDouble(txtAmount.getText().trim()),
                    txtDescription.getText().trim(),
                    dateExpense.getValue(),
                    currentUserId
            );

            boolean isSaved = expensesModel.saveTripExpense(dto);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Trip expense saved successfully!");
                cleanFields();
                loadExpensesTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Save Failed",
                        "Failed to save trip expense!");
            }

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input",
                    "Please enter valid numeric values!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error saving expense: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateExpense(ActionEvent event) {
        if (tripExpenseId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select an expense to update!");
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            int tripId = comboTripId.getValue();

            if (!expensesModel.isTripExists(tripId)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Trip",
                        "Trip ID does not exist!");
                return;
            }

            TripExpensesDTO dto = new TripExpensesDTO(
                    Integer.parseInt(tripExpenseId.getText().trim()),
                    tripId,
                    comboExpenseType.getValue(),
                    null,
                    Double.parseDouble(txtAmount.getText().trim()),
                    txtDescription.getText().trim(),
                    dateExpense.getValue(),
                    currentUserId
            );

            boolean isUpdated = expensesModel.updateTripExpense(dto);

            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Trip expense updated successfully!");
                cleanFields();
                loadExpensesTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed",
                        "Failed to update trip expense!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error updating expense: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteExpense(ActionEvent event) {
        if (tripExpenseId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select an expense to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Trip Expense");
        confirmAlert.setContentText("Are you sure you want to delete this expense?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = tripExpenseId.getText();

            try {
                boolean isDeleted = expensesModel.deleteTripExpense(id);

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Trip expense deleted successfully!");
                    cleanFields();
                    loadExpensesTable();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Delete Failed",
                            "Failed to delete trip expense!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Error deleting expense: " + e.getMessage());
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
            loadExpensesTable();
            return;
        }

        try {
            List<TripExpensesDTO> searchResults = expensesModel.searchTripExpenses(keyword);
            ObservableList<TripExpensesDTO> obList = FXCollections.observableArrayList(searchResults);
            tableExpenses.setItems(obList);

            if (searchResults.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results",
                        "No expenses found matching: " + keyword);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Search Error",
                    "Error searching expenses: " + e.getMessage());
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        txtSearch.clear();
        loadExpensesTable();
        loadComboData();
        showAlert(Alert.AlertType.INFORMATION, "Refreshed",
                "Expenses list refreshed successfully!");
    }

    @FXML
    public void logout(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Logout");
        confirmAlert.setHeaderText("Confirm Logout");
        confirmAlert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Logging out...");
        }
    }

    private boolean validateFields() {
        if (comboTripId.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select a Trip ID!");
            comboTripId.requestFocus();
            return false;
        }

        if (comboExpenseType.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select Expense Type!");
            comboExpenseType.requestFocus();
            return false;
        }

        if (txtAmount.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter amount!");
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

        if (dateExpense.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select expense date!");
            dateExpense.requestFocus();
            return false;
        }

        if (dateExpense.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Expense date cannot be in the future!");
            dateExpense.requestFocus();
            return false;
        }

        return true;
    }

    private void cleanFields() {
        tripExpenseId.setText("");
        comboTripId.setValue(null);
        lblTripDetails.setText("-");
        comboExpenseType.setValue(TripExpType.FUEL);
        txtAmount.setText("");
        txtDescription.setText("");
        dateExpense.setValue(null);
        tableExpenses.getSelectionModel().clearSelection();
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