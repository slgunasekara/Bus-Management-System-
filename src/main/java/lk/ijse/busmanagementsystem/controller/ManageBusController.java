package lk.ijse.busmanagementsystem.controller;

//OK

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import lk.ijse.busmanagementsystem.bo.BOFactory;
import lk.ijse.busmanagementsystem.bo.custom.BusBO;
import lk.ijse.busmanagementsystem.dto.BusDTO;
import lk.ijse.busmanagementsystem.tm.BusTM;
import lk.ijse.busmanagementsystem.util.SessionManager;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class ManageBusController implements Initializable {

    @FXML private TextField txtBusId;
    @FXML private TextField txtBrandName;
    @FXML private TextField txtBusNumber;
    @FXML private ComboBox<String> cmbBusType;
    @FXML private TextField txtNoOfSeats;
    @FXML private ComboBox<String> cmbBusStatus;
    @FXML private DatePicker dateManufactureDate;
    @FXML private DatePicker dateInsuranceExpiry;
    @FXML private DatePicker dateLicenseRenewal;
    @FXML private TextField txtCurrentMileage;

    @FXML private TableView<BusTM> tblBus;
    @FXML private TableColumn<BusTM, Integer>   colId;
    @FXML private TableColumn<BusTM, String>    colBrand;
    @FXML private TableColumn<BusTM, String>    colBusNumber;
    @FXML private TableColumn<BusTM, String>    colType;
    @FXML private TableColumn<BusTM, Integer>   colSeats;
    @FXML private TableColumn<BusTM, String>    colStatus;
    @FXML private TableColumn<BusTM, LocalDate> colManufacture;
    @FXML private TableColumn<BusTM, LocalDate> colInsurance;
    @FXML private TableColumn<BusTM, LocalDate> colLicense;
    @FXML private TableColumn<BusTM, Integer>   colMileage;
    @FXML private TableColumn<BusTM, String>    colCreatedBy;
    @FXML private TableColumn<BusTM, LocalDateTime> colCreatedAt;
    @FXML private TableColumn<BusTM, LocalDateTime> colUpdatedAt;

    @FXML private TextField txtSearch;

    private final BusBO busBO =
            (BusBO) BOFactory.getInstance().getBO(BOFactory.BOType.BUS);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComboBoxes();
        initTableColumns();
        loadAllBuses();
        loadNextBusId();
        setupTableRowClick();

    }


    private void initComboBoxes() {
        cmbBusType.setItems(FXCollections.observableArrayList(
                "Luxury", "Semi-Luxury", "Normal", "Mini", "Tourist", "School"
        ));
        cmbBusStatus.setItems(FXCollections.observableArrayList(
                "Active", "Maintenance", "Inactive", "Sold"
        ));
    }

    private void initTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("busId"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("busBrandName"));
        colBusNumber.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        colType.setCellValueFactory(new PropertyValueFactory<>("busType"));
        colSeats.setCellValueFactory(new PropertyValueFactory<>("noOfSeats"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("busStatus"));
        colManufacture.setCellValueFactory(new PropertyValueFactory<>("manufactureDate"));
        colInsurance.setCellValueFactory(new PropertyValueFactory<>("insuranceExpiryDate"));
        colLicense.setCellValueFactory(new PropertyValueFactory<>("licenseRenewalDate"));
        colMileage.setCellValueFactory(new PropertyValueFactory<>("currentMileage"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Active"      -> setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                        case "Maintenance" -> setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
                        case "Inactive"    -> setStyle("-fx-text-fill: #95a5a6; -fx-font-weight: bold;");
                        case "Sold"        -> setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                        default            -> setStyle("");
                    }
                }
            }
        });
    }

    private void setupTableRowClick() {
        tblBus.setOnMouseClicked(event -> {
            BusTM selected = tblBus.getSelectionModel().getSelectedItem();
            if (selected != null) {
                populateForm(selected);
            }
        });
    }


    private void loadAllBuses() {
        try {
            List<BusDTO> buses = busBO.getAllBuses();
            populateTable(buses);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load buses: " + e.getMessage());
        }
    }

    private void loadNextBusId() {
        try {
            Integer nextId = busBO.getNextBusId();
            txtBusId.setText(String.valueOf(nextId));
        } catch (Exception e) {
            txtBusId.setText("1");
        }
    }

    private void populateTable(List<BusDTO> buses) {
        ObservableList<BusTM> tmList = FXCollections.observableArrayList();
        for (BusDTO dto : buses) {
            tmList.add(new BusTM(
                    dto.getBusId(),
                    dto.getBusBrandName(),
                    dto.getBusNumber(),
                    dto.getBusType(),
                    dto.getNoOfSeats() != null ? dto.getNoOfSeats() : 0,
                    dto.getBusStatus(),
                    dto.getManufactureDate(),
                    dto.getInsuranceExpiryDate(),
                    dto.getLicenseRenewalDate(),
                    dto.getCurrentMileage(),
                    dto.getCreatedBy() != null ? String.valueOf(dto.getCreatedBy()) : "-",
                    dto.getCreatedAt(),
                    dto.getUpdatedAt()
            ));
        }
        tblBus.setItems(tmList);
    }

    private void populateForm(BusTM tm) {
        txtBusId.setText(String.valueOf(tm.getBusId()));
        txtBrandName.setText(tm.getBusBrandName());
        txtBusNumber.setText(tm.getBusNumber());
        cmbBusType.setValue(tm.getBusType());
        txtNoOfSeats.setText(String.valueOf(tm.getNoOfSeats()));
        cmbBusStatus.setValue(tm.getBusStatus());
        dateManufactureDate.setValue(tm.getManufactureDate());
        dateInsuranceExpiry.setValue(tm.getInsuranceExpiryDate());
        dateLicenseRenewal.setValue(tm.getLicenseRenewalDate());
        txtCurrentMileage.setText(tm.getCurrentMileage() != null
                ? String.valueOf(tm.getCurrentMileage()) : "");
    }


    @FXML
    void handleSave(ActionEvent event) {
        if (!validateForm()) return;

        try {
            BusDTO dto = buildDTOFromForm();
            busBO.saveBus(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Bus '" + dto.getBusNumber() + "' saved successfully!");
            handleReset(null);
            loadAllBuses();
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save bus: " + e.getMessage());
        }
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        String idText = txtBusId.getText().trim();
        if (idText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning",
                    "Please select a bus from the table to update.");
            return;
        }
        if (!validateForm()) return;

        try {
            BusDTO dto = buildDTOFromForm();
            dto.setBusId(Integer.parseInt(idText));
            busBO.updateBus(dto);
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Bus '" + dto.getBusNumber() + "' updated successfully!");
            handleReset(null);
            loadAllBuses();
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update bus: " + e.getMessage());
        }
    }

    @FXML
    void handleDelete(ActionEvent event) {
        String idText = txtBusId.getText().trim();
        if (idText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning",
                    "Please select a bus from the table to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText("Delete bus #" + idText + "? This action cannot be undone.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    busBO.deleteBus(Integer.parseInt(idText));
                    showAlert(Alert.AlertType.INFORMATION, "Deleted",
                            "Bus deleted successfully.");
                    handleReset(null);
                    loadAllBuses();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "Failed to delete bus: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    void handleReset(ActionEvent event) {
        txtBrandName.clear();
        txtBusNumber.clear();
        cmbBusType.setValue(null);
        txtNoOfSeats.clear();
        cmbBusStatus.setValue(null);
        dateManufactureDate.setValue(null);
        dateInsuranceExpiry.setValue(null);
        dateLicenseRenewal.setValue(null);
        txtCurrentMileage.clear();
        txtSearch.clear();
        tblBus.getSelectionModel().clearSelection();
        loadNextBusId();
        loadAllBuses();
        txtBrandName.requestFocus();
    }


    @FXML
    void handleSearchCustomer(KeyEvent event) {
        String input = txtBusId.getText().trim();
        if (input.isEmpty()) {
            loadAllBuses();
            return;
        }
        try {
            Integer id = Integer.parseInt(input);
            BusDTO dto = busBO.getBusById(id);
            if (dto != null) {
                ObservableList<BusTM> result = FXCollections.observableArrayList(
                        new BusTM(dto.getBusId(), dto.getBusBrandName(), dto.getBusNumber(),
                                dto.getBusType(), dto.getNoOfSeats() != null ? dto.getNoOfSeats() : 0,
                                dto.getBusStatus(), dto.getManufactureDate(),
                                dto.getInsuranceExpiryDate(), dto.getLicenseRenewalDate(),
                                dto.getCurrentMileage(),
                                dto.getCreatedBy() != null ? String.valueOf(dto.getCreatedBy()) : "-",
                                dto.getCreatedAt(), dto.getUpdatedAt())
                );
                tblBus.setItems(result);

                BusTM tm = result.get(0);
                populateForm(tm);
            } else {
                tblBus.setItems(FXCollections.observableArrayList());
            }
        } catch (NumberFormatException ignored) {
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Search error: " + e.getMessage());
        }
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadAllBuses();
            return;
        }
        try {
            List<BusDTO> results = busBO.searchBuses(keyword);
            populateTable(results);
            if (results.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Results",
                        "No buses found for: \"" + keyword + "\"");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Search failed: " + e.getMessage());
        }
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        handleReset(null);
    }


    @FXML
    void handlePrint(ActionEvent event) {
        try {
            List<BusDTO> buses = busBO.getAllBuses();
            if (buses.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data", "No buses to print.");
                return;
            }

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(buses);

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(
                    getClass().getResourceAsStream(
                            "/lk/ijse/busmanagementsystem/reports/BusFleetReport.jasper")
            );

            Map<String, Object> params = new HashMap<>();
            params.put("GeneratedDate",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            params.put("ActiveBuses",      String.valueOf(busBO.getActiveBusCount()));
            params.put("MaintenanceBuses", String.valueOf(busBO.getMaintenanceBusCount()));

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Report Error",
                    "Could not generate report: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    void logout(ActionEvent event) {
        try {
            SessionManager.clearSession();
            lk.ijse.busmanagementsystem.Main.setRoot("login");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Logout failed: " + e.getMessage());
        }
    }


    private boolean validateForm() {
        if (txtBrandName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Brand Name is required!");
            txtBrandName.requestFocus();
            return false;
        }
        if (txtBusNumber.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Bus Number is required!");
            txtBusNumber.requestFocus();
            return false;
        }
        if (cmbBusType.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Please select Bus Type!");
            cmbBusType.requestFocus();
            return false;
        }
        if (txtNoOfSeats.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Number of Seats is required!");
            txtNoOfSeats.requestFocus();
            return false;
        }
        try {
            int seats = Integer.parseInt(txtNoOfSeats.getText().trim());
            if (seats <= 0 || seats > 100) {
                showAlert(Alert.AlertType.WARNING, "Validation",
                        "Number of seats must be between 1 and 100!");
                txtNoOfSeats.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validation",
                    "Number of Seats must be a valid number!");
            txtNoOfSeats.requestFocus();
            return false;
        }
        if (cmbBusStatus.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "Please select Bus Status!");
            cmbBusStatus.requestFocus();
            return false;
        }
        if (dateManufactureDate.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation",
                    "Manufacture Date is required!");
            dateManufactureDate.requestFocus();
            return false;
        }
        if (!txtCurrentMileage.getText().trim().isEmpty()) {
            try {
                int mileage = Integer.parseInt(txtCurrentMileage.getText().trim());
                if (mileage < 0) {
                    showAlert(Alert.AlertType.WARNING, "Validation",
                            "Mileage cannot be negative!");
                    txtCurrentMileage.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Validation",
                        "Current Mileage must be a valid number!");
                txtCurrentMileage.requestFocus();
                return false;
            }
        }
        return true;
    }


    private BusDTO buildDTOFromForm() {
        Integer mileage = null;
        if (!txtCurrentMileage.getText().trim().isEmpty()) {
            mileage = Integer.parseInt(txtCurrentMileage.getText().trim());
        }

        return new BusDTO(
                txtBrandName.getText().trim(),
                txtBusNumber.getText().trim().toUpperCase(),
                cmbBusType.getValue(),
                Integer.parseInt(txtNoOfSeats.getText().trim()),
                cmbBusStatus.getValue(),
                dateManufactureDate.getValue(),
                dateInsuranceExpiry.getValue(),
                dateLicenseRenewal.getValue(),
                mileage,
                SessionManager.getCurrentUserId()
        );
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}