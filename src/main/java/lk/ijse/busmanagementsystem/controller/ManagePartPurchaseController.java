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
import lk.ijse.busmanagementsystem.dto.PartPurchaseDTO;
import lk.ijse.busmanagementsystem.dto.PartPurchaseTM;
import lk.ijse.busmanagementsystem.model.PartPurchaseModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManagePartPurchaseController implements Initializable {

    @FXML private AnchorPane partContent;
    @FXML private TextField purchaseId;
    @FXML private ComboBox<Integer> comboBusId;
    @FXML private Label lblBusDetails;
    @FXML private ComboBox<Integer> comboMaintId;
    @FXML private Label lblMaintDetails;
    @FXML private TextField partName;
    @FXML private TextField quantity;
    @FXML private TextField unitPrice;
    @FXML private TextField totalCost;
    @FXML private TextField supplierName;
    @FXML private TextField partDescription;
    @FXML private DatePicker purchaseDate;
    @FXML private TextField txtSearch;

    @FXML private TableView<PartPurchaseTM> tablePartPurchases;
    @FXML private TableColumn<PartPurchaseTM, Integer> colPurchaseId;
    @FXML private TableColumn<PartPurchaseTM, Integer> colBusId;
    @FXML private TableColumn<PartPurchaseTM, String> colBusNumber;
    @FXML private TableColumn<PartPurchaseTM, Integer> colMaintId;
    @FXML private TableColumn<PartPurchaseTM, String> colPartName;
    @FXML private TableColumn<PartPurchaseTM, Integer> colQuantity;
    @FXML private TableColumn<PartPurchaseTM, Double> colUnitPrice;
    @FXML private TableColumn<PartPurchaseTM, Double> colTotalCost;
    @FXML private TableColumn<PartPurchaseTM, String> colSupplierName;
    @FXML private TableColumn<PartPurchaseTM, String> colDescription;
    @FXML private TableColumn<PartPurchaseTM, LocalDate> colDate;
    @FXML private TableColumn<PartPurchaseTM, String> colCreatedBy;

    private final PartPurchaseModel partPurchaseModel = new PartPurchaseModel();
    private int currentUserId = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("ManagePartPurchases is loaded");

        setupTableColumns();
        loadComboData();
        loadPartPurchaseTable();
        setupEventListeners();

        purchaseId.setEditable(false);
        lblBusDetails.setText("-");
        lblMaintDetails.setText("-");
    }

    private void setupTableColumns() {
        colPurchaseId.setCellValueFactory(new PropertyValueFactory<>("purchaseId"));
        colBusId.setCellValueFactory(new PropertyValueFactory<>("busId"));
        colBusNumber.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        colMaintId.setCellValueFactory(new PropertyValueFactory<>("maintId"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("partDescription"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdByUsername"));
    }

    private void loadComboData() {
        try {
            // Load Bus IDs
            List<Integer> busIds = partPurchaseModel.getAllActiveBusIds();
            ObservableList<Integer> busIdList = FXCollections.observableArrayList(busIds);
            comboBusId.setItems(busIdList);
            System.out.println("✓ Loaded " + busIds.size() + " bus IDs");

            // Load Maintenance IDs
            List<Integer> maintIds = partPurchaseModel.getAllMaintenanceIds();
            ObservableList<Integer> maintIdList = FXCollections.observableArrayList(maintIds);
            comboMaintId.setItems(maintIdList);
            System.out.println("✓ Loaded " + maintIds.size() + " maintenance IDs");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load combo data: " + e.getMessage());
        }
    }

    private void setupEventListeners() {
        // Auto-calculate total cost
        quantity.textProperty().addListener((obs, oldVal, newVal) -> calculateTotalCost());
        unitPrice.textProperty().addListener((obs, oldVal, newVal) -> calculateTotalCost());

        // Table row selection
        tablePartPurchases.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelected(newSelection);
            }
        });

        // ComboBox selection listeners
        comboBusId.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadBusDetails(newValue);
            }
        });

        comboMaintId.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                loadMaintenanceDetails(newValue);
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
    private void handleSelectMaintenance(ActionEvent event) {
        Integer selectedMaintId = comboMaintId.getValue();
        if (selectedMaintId != null) {
            loadMaintenanceDetails(selectedMaintId);
        }
    }

    private void loadBusDetails(int busId) {
        try {
            String details = partPurchaseModel.getBusDetails(busId);
            lblBusDetails.setText(details);
        } catch (Exception e) {
            lblBusDetails.setText("Error loading bus details");
            e.printStackTrace();
        }
    }

    private void loadMaintenanceDetails(int maintId) {
        try {
            String details = partPurchaseModel.getMaintenanceDetails(maintId);
            lblMaintDetails.setText(details);
        } catch (Exception e) {
            lblMaintDetails.setText("Error loading maintenance details");
            e.printStackTrace();
        }
    }

    private void calculateTotalCost() {
        try {
            if (!quantity.getText().trim().isEmpty() && !unitPrice.getText().trim().isEmpty()) {
                int qty = Integer.parseInt(quantity.getText().trim());
                double price = Double.parseDouble(unitPrice.getText().trim());
                double total = qty * price;
                totalCost.setText(String.format("%.2f", total));
            }
        } catch (NumberFormatException e) {
            totalCost.setText("");
        }
    }

    private void loadPartPurchaseTable() {
        try {
            List<PartPurchaseTM> partPurchaseList = partPurchaseModel.getAllPartPurchases();
            ObservableList<PartPurchaseTM> obList = FXCollections.observableArrayList(partPurchaseList);
            tablePartPurchases.setItems(obList);
            System.out.println("✓ Loaded " + partPurchaseList.size() + " part purchase records");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load part purchases: " + e.getMessage());
        }
    }

    private void fillFieldsFromSelected(PartPurchaseTM tm) {
        purchaseId.setText(String.valueOf(tm.getPurchaseId()));
        comboBusId.setValue(tm.getBusId());
        comboMaintId.setValue(tm.getMaintId());
        partName.setText(tm.getPartName());
        quantity.setText(String.valueOf(tm.getQuantity()));
        unitPrice.setText(String.format("%.2f", tm.getUnitPrice()));
        totalCost.setText(String.format("%.2f", tm.getTotalCost()));
        supplierName.setText(tm.getSupplierName());
        partDescription.setText(tm.getPartDescription());
        purchaseDate.setValue(tm.getDate());

        if (tm.getBusId() != null) {
            loadBusDetails(tm.getBusId());
        } else {
            lblBusDetails.setText("No bus associated");
        }

        if (tm.getMaintId() != null) {
            loadMaintenanceDetails(tm.getMaintId());
        } else {
            lblMaintDetails.setText("No maintenance associated");
        }
    }

    @FXML
    private void savePartPurchase(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            Integer busId = comboBusId.getValue();
            if (busId != null && !partPurchaseModel.isBusExists(busId)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Bus", "Bus ID does not exist!");
                return;
            }

            Integer maintId = comboMaintId.getValue();
            if (maintId != null && !partPurchaseModel.isMaintenanceExists(maintId)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Maintenance", "Maintenance ID does not exist!");
                return;
            }

            PartPurchaseDTO dto = new PartPurchaseDTO(
                    0,
                    busId,
                    maintId,
                    partName.getText().trim(),
                    Integer.parseInt(quantity.getText().trim()),
                    Double.parseDouble(unitPrice.getText().trim()),
                    Double.parseDouble(totalCost.getText().trim()),
                    supplierName.getText().trim(),
                    partDescription.getText().trim(),
                    purchaseDate.getValue(),
                    currentUserId
            );

            boolean isSaved = partPurchaseModel.savePartPurchase(dto);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Part purchase saved successfully!");
                cleanFields();
                loadPartPurchaseTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Save Failed", "Failed to save part purchase!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving part purchase: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchPartPurchase(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String id = purchaseId.getText();

            if (id.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Empty Field", "Please enter Purchase ID!");
                return;
            }

            try {
                PartPurchaseDTO dto = partPurchaseModel.searchPartPurchase(id);

                if (dto != null) {
                    // Create TM from DTO for display
                    comboBusId.setValue(dto.getBusId());
                    comboMaintId.setValue(dto.getMaintId());
                    partName.setText(dto.getPartName());
                    quantity.setText(String.valueOf(dto.getQuantity()));
                    unitPrice.setText(String.format("%.2f", dto.getUnitPrice()));
                    totalCost.setText(String.format("%.2f", dto.getTotalCost()));
                    supplierName.setText(dto.getSupplierName());
                    partDescription.setText(dto.getPartDescription());
                    purchaseDate.setValue(dto.getDate());

                    if (dto.getBusId() != null) loadBusDetails(dto.getBusId());
                    if (dto.getMaintId() != null) loadMaintenanceDetails(dto.getMaintId());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Not Found", "Part purchase not found!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error searching part purchase: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handlePartPurchaseUpdate(ActionEvent event) {
        if (purchaseId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a part purchase to update!");
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            Integer busId = comboBusId.getValue();
            if (busId != null && !partPurchaseModel.isBusExists(busId)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Bus", "Bus ID does not exist!");
                return;
            }

            Integer maintId = comboMaintId.getValue();
            if (maintId != null && !partPurchaseModel.isMaintenanceExists(maintId)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Maintenance", "Maintenance ID does not exist!");
                return;
            }

            PartPurchaseDTO dto = new PartPurchaseDTO(
                    Integer.parseInt(purchaseId.getText().trim()),
                    busId,
                    maintId,
                    partName.getText().trim(),
                    Integer.parseInt(quantity.getText().trim()),
                    Double.parseDouble(unitPrice.getText().trim()),
                    Double.parseDouble(totalCost.getText().trim()),
                    supplierName.getText().trim(),
                    partDescription.getText().trim(),
                    purchaseDate.getValue(),
                    currentUserId
            );

            boolean isUpdated = partPurchaseModel.updatePartPurchase(dto);

            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Part purchase updated successfully!");
                cleanFields();
                loadPartPurchaseTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update part purchase!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating part purchase: " + e.getMessage());
        }
    }

    @FXML
    private void handlePartPurchaseDelete(ActionEvent event) {
        if (purchaseId.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a part purchase to delete!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Part Purchase");
        confirmAlert.setContentText("Are you sure you want to delete this part purchase?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = purchaseId.getText();

            try {
                boolean isDeleted = partPurchaseModel.deletePartPurchase(id);

                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Part purchase deleted successfully!");
                    cleanFields();
                    loadPartPurchaseTable();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Delete Failed", "Failed to delete part purchase!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting part purchase: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handlePartPurchaseReset(ActionEvent event) {
        cleanFields();
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadPartPurchaseTable();
            return;
        }

        try {
            List<PartPurchaseTM> searchResults = partPurchaseModel.searchPartPurchases(keyword);
            ObservableList<PartPurchaseTM> obList = FXCollections.observableArrayList(searchResults);
            tablePartPurchases.setItems(obList);

            if (searchResults.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results", "No part purchases found matching: " + keyword);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error searching part purchases: " + e.getMessage());
        }
    }

    @FXML
    public void handleRefresh(ActionEvent event) {
        txtSearch.clear();
        loadPartPurchaseTable();
        loadComboData();
        showAlert(Alert.AlertType.INFORMATION, "Refreshed", "Part purchases list refreshed successfully!");
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
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a Bus ID!");
            comboBusId.requestFocus();
            return false;
        }

        if (partName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter part name!");
            partName.requestFocus();
            return false;
        }

        if (quantity.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter quantity!");
            quantity.requestFocus();
            return false;
        }

        try {
            int qty = Integer.parseInt(quantity.getText().trim());
            if (qty <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Quantity must be positive!");
                quantity.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Quantity must be a valid number!");
            quantity.requestFocus();
            return false;
        }

        if (unitPrice.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter unit price!");
            unitPrice.requestFocus();
            return false;
        }

        try {
            double price = Double.parseDouble(unitPrice.getText().trim());
            if (price <= 0) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Unit price must be positive!");
                unitPrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Unit price must be a valid number!");
            unitPrice.requestFocus();
            return false;
        }

        if (supplierName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter supplier name!");
            supplierName.requestFocus();
            return false;
        }

        if (purchaseDate.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select purchase date!");
            purchaseDate.requestFocus();
            return false;
        }

        if (purchaseDate.getValue().isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Purchase date cannot be in the future!");
            purchaseDate.requestFocus();
            return false;
        }

        return true;
    }

    private void cleanFields() {
        purchaseId.setText("");
        comboBusId.setValue(null);
        comboMaintId.setValue(null);
        lblBusDetails.setText("-");
        lblMaintDetails.setText("-");
        partName.setText("");
        quantity.setText("");
        unitPrice.setText("");
        totalCost.setText("");
        supplierName.setText("");
        partDescription.setText("");
        purchaseDate.setValue(null);
        tablePartPurchases.getSelectionModel().clearSelection();
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