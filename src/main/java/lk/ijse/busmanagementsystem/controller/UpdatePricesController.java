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
import lk.ijse.busmanagementsystem.dto.UpdatePricesDTO;
import lk.ijse.busmanagementsystem.enums.updateprices.ChangeType;
import lk.ijse.busmanagementsystem.enums.updateprices.UpdateType;
import lk.ijse.busmanagementsystem.model.UpdatePricesModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdatePricesController implements Initializable {

    @FXML
    private AnchorPane statusContent;

    @FXML
    private TextField statusId;

    @FXML
    private ComboBox<String> updateTypeCombo;

    @FXML
    private ComboBox<String> changeTypeCombo;

    @FXML
    private TextField previousValue;

    @FXML
    private TextField newValue;

    @FXML
    private TextField changeAmount;

    @FXML
    private TextField percentageChange;

    @FXML
    private DatePicker changeDate;

    @FXML
    private TextField description;

    @FXML
    private TextField txtSearch;

    @FXML
    private TableView<UpdatePricesDTO> tablePrices;

    @FXML
    private TableColumn<UpdatePricesDTO, Integer> colId;

    @FXML
    private TableColumn<UpdatePricesDTO, String> colUpdateType;

    @FXML
    private TableColumn<UpdatePricesDTO, String> colChangeType;

    @FXML
    private TableColumn<UpdatePricesDTO, Double> colPreviousValue;

    @FXML
    private TableColumn<UpdatePricesDTO, Double> colNewValue;

    @FXML
    private TableColumn<UpdatePricesDTO, Double> colChangeAmount;

    @FXML
    private TableColumn<UpdatePricesDTO, Double> colPercentageChange;

    @FXML
    private TableColumn<UpdatePricesDTO, LocalDate> colChangeDate;

    @FXML
    private TableColumn<UpdatePricesDTO, String> colDescription;

    @FXML
    private TableColumn<UpdatePricesDTO, Integer> colCreatedBy;

    private final UpdatePricesModel pricesModel = new UpdatePricesModel();
    private int currentUserId = 1; // This should come from your login session

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("UpdatePrices is loaded");

        // Setup table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("updatePricesId"));
        colUpdateType.setCellValueFactory(new PropertyValueFactory<>("updateType"));
        colChangeType.setCellValueFactory(new PropertyValueFactory<>("changeType"));
        colPreviousValue.setCellValueFactory(new PropertyValueFactory<>("previousValue"));
        colNewValue.setCellValueFactory(new PropertyValueFactory<>("newValue"));
        colChangeAmount.setCellValueFactory(new PropertyValueFactory<>("changeAmount"));
        colPercentageChange.setCellValueFactory(new PropertyValueFactory<>("percentageChange"));
        colChangeDate.setCellValueFactory(new PropertyValueFactory<>("changeDate"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));

        // Initialize ComboBoxes
        initializeComboBoxes();

        // Load all prices
        loadPricesTable();

        // Make statusId field non-editable
        statusId.setEditable(false);

        // Make calculated fields non-editable
        changeAmount.setEditable(false);
        percentageChange.setEditable(false);

        // Add listeners for automatic calculation
        setupCalculationListeners();

        // Add table row selection listener
        tablePrices.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelectedPrice(newSelection);
            }
        });

        // Add listener to load latest price when update type changes
        updateTypeCombo.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty()) {
                loadLatestPrice(newValue);
            }
        });
    }

    private void initializeComboBoxes() {
        // Populate Update Type ComboBox
        ObservableList<String> updateTypes = FXCollections.observableArrayList();
        for (UpdateType type : UpdateType.values()) {
            updateTypes.add(type.getValue());
        }
        updateTypeCombo.setItems(updateTypes);

        // Populate Change Type ComboBox
        ObservableList<String> changeTypes = FXCollections.observableArrayList();
        for (ChangeType type : ChangeType.values()) {
            changeTypes.add(type.getValue());
        }
        changeTypeCombo.setItems(changeTypes);
    }

    private void setupCalculationListeners() {
        // Calculate change amount and percentage when values change
        previousValue.textProperty().addListener((obs, oldVal, newVal) -> calculateChanges());
        newValue.textProperty().addListener((obs, oldVal, newVal) -> calculateChanges());
    }

    private void calculateChanges() {
        try {
            if (!previousValue.getText().trim().isEmpty() && !newValue.getText().trim().isEmpty()) {
                double prevValue = Double.parseDouble(previousValue.getText().trim());
                double newVal = Double.parseDouble(newValue.getText().trim());

                // Calculate change amount
                double changeAmt = pricesModel.calculateChangeAmount(prevValue, newVal);
                changeAmount.setText(String.format("%.2f", changeAmt));

                // Calculate percentage change
                double percentChange = pricesModel.calculatePercentageChange(prevValue, newVal);
                percentageChange.setText(String.format("%.2f", percentChange));
            } else {
                changeAmount.setText("");
                percentageChange.setText("");
            }
        } catch (NumberFormatException e) {
            changeAmount.setText("");
            percentageChange.setText("");
        }
    }

    private void loadLatestPrice(String updateType) {
        try {
            Double latestPrice = pricesModel.getLatestPrice(updateType);
            if (latestPrice != null) {
                previousValue.setText(String.format("%.2f", latestPrice));
            } else {
                previousValue.setText("0.00");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPricesTable() {
        try {
            List<UpdatePricesDTO> pricesList = pricesModel.getAllUpdatePrices();
            ObservableList<UpdatePricesDTO> obList = FXCollections.observableArrayList(pricesList);
            tablePrices.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load prices: " + e.getMessage()).show();
        }
    }

    private void fillFieldsFromSelectedPrice(UpdatePricesDTO price) {
        statusId.setText(String.valueOf(price.getUpdatePricesId()));
        updateTypeCombo.setValue(price.getUpdateType());
        changeTypeCombo.setValue(price.getChangeType());
        previousValue.setText(String.format("%.2f", price.getPreviousValue()));
        newValue.setText(String.format("%.2f", price.getNewValue()));
        changeAmount.setText(String.format("%.2f", price.getChangeAmount()));
        percentageChange.setText(String.format("%.2f", price.getPercentageChange()));
        changeDate.setValue(price.getChangeDate());
        description.setText(price.getDescription());
    }

    @FXML
    private void savePrice(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            UpdatePricesDTO priceDTO = new UpdatePricesDTO(
                    0, // ID will be auto-generated
                    updateTypeCombo.getValue(),
                    changeTypeCombo.getValue(),
                    Double.parseDouble(previousValue.getText().trim()),
                    Double.parseDouble(newValue.getText().trim()),
                    Double.parseDouble(changeAmount.getText().trim()),
                    Double.parseDouble(percentageChange.getText().trim()),
                    changeDate.getValue(),
                    description.getText().trim(),
                    currentUserId
            );

            boolean isSaved = pricesModel.saveUpdatePrice(priceDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Price update saved successfully!").show();
                cleanFields();
                loadPricesTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save price update!").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format!").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error saving price update: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleSearchPrice(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String id = statusId.getText();

            if (id.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Status ID!").show();
                return;
            }

            try {
                UpdatePricesDTO priceDTO = pricesModel.searchUpdatePrice(id);

                if (priceDTO != null) {
                    fillFieldsFromSelectedPrice(priceDTO);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Price update not found!").show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error searching price update: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handlePriceDelete(ActionEvent event) {
        if (statusId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a price update to delete!").show();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Price Update");
        confirmAlert.setContentText("Are you sure you want to delete this price update?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = statusId.getText();

            try {
                boolean isDeleted = pricesModel.deleteUpdatePrice(id);

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Price update deleted successfully!").show();
                    cleanFields();
                    loadPricesTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete price update!").show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error deleting price update: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handlePriceReset(ActionEvent event) {
        cleanFields();
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadPricesTable();
            return;
        }

        try {
            List<UpdatePricesDTO> searchResults = pricesModel.searchUpdatePrices(keyword);
            ObservableList<UpdatePricesDTO> obList = FXCollections.observableArrayList(searchResults);
            tablePrices.setItems(obList);

            if (searchResults.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No price updates found matching: " + keyword).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error searching price updates: " + e.getMessage()).show();
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        txtSearch.clear();
        loadPricesTable();
        new Alert(Alert.AlertType.INFORMATION, "Price list refreshed successfully!").show();
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
        if (updateTypeCombo.getValue() == null || updateTypeCombo.getValue().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select update type!").show();
            return false;
        }

        if (changeTypeCombo.getValue() == null || changeTypeCombo.getValue().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select change type!").show();
            return false;
        }

        if (previousValue.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter previous value!").show();
            return false;
        }

        if (newValue.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter new value!").show();
            return false;
        }

        try {
            double prevValue = Double.parseDouble(previousValue.getText().trim());
            if (prevValue < 0) {
                new Alert(Alert.AlertType.WARNING, "Previous value cannot be negative!").show();
                return false;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Previous value must be a valid number!").show();
            return false;
        }

        try {
            double newVal = Double.parseDouble(newValue.getText().trim());
            if (newVal < 0) {
                new Alert(Alert.AlertType.WARNING, "New value cannot be negative!").show();
                return false;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "New value must be a valid number!").show();
            return false;
        }

        if (changeDate.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select change date!").show();
            return false;
        }

        if (changeDate.getValue().isAfter(LocalDate.now())) {
            new Alert(Alert.AlertType.WARNING, "Change date cannot be in the future!").show();
            return false;
        }

        return true;
    }

    private void cleanFields() {
        statusId.setText("");
        updateTypeCombo.setValue(null);
        changeTypeCombo.setValue(null);
        previousValue.setText("");
        newValue.setText("");
        changeAmount.setText("");
        percentageChange.setText("");
        changeDate.setValue(null);
        description.setText("");
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }
}

