package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.busmanagementsystem.dto.EventDTO;
import lk.ijse.busmanagementsystem.bo.BOFactory;
import lk.ijse.busmanagementsystem.bo.custom.EventBO;
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

public class EventController implements Initializable {

    @FXML
    private AnchorPane dailyProfitContent;

    @FXML
    private TextField txtEventId;

    @FXML
    private ComboBox<String> cmbBusId;

    @FXML
    private Label lblBusNumber;

    @FXML
    private TextField txtStartLocation;

    @FXML
    private TextField txtEndLocation;

    @FXML
    private TextField txtEventValue;

    @FXML
    private DatePicker dateEventDate;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtCustomerContact;

    @FXML
    private TextField txtCustomerNic;

    @FXML
    private TextField txtCustomerAddress;

    @FXML
    private TextField txtDescription;

    @FXML
    private CheckBox chkEventCompleted;

    @FXML
    private TextField txtSearch;

    @FXML
    private DatePicker dateSearch;

    @FXML
    private TableView<EventDTO> tableCustomer;

    @FXML
    private TableColumn<EventDTO, Integer> colEventId;

    @FXML
    private TableColumn<EventDTO, String> colBusNumber;

    @FXML
    private TableColumn<EventDTO, String> colStartLocation;

    @FXML
    private TableColumn<EventDTO, String> colEndLocation;

    @FXML
    private TableColumn<EventDTO, Double> colEventValue;

    @FXML
    private TableColumn<EventDTO, LocalDate> colEventDate;

    @FXML
    private TableColumn<EventDTO, String> colCustomerName;

    @FXML
    private TableColumn<EventDTO, String> colCustomerContact;

    @FXML
    private TableColumn<EventDTO, String> colCustomerNic;

    @FXML
    private TableColumn<EventDTO, String> colCustomerAddress;

    @FXML
    private TableColumn<EventDTO, String> colDescription;

    @FXML
    private TableColumn<EventDTO, Boolean> colEventCompleted;

    @FXML
    private TableColumn<EventDTO, Integer> colCreatedBy;

    @FXML
    private Button backBtn;

    private final EventBO eventBO = (EventBO) BOFactory.getInstance().getBO(BOFactory.BOType.EVENT);
    private int currentUserId = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Event Management is loaded");

        setupTableColumns();

        loadBusComboBox();

        loadEventTable();

        txtEventId.setEditable(false);

        cmbBusId.setOnAction(event -> updateBusNumberLabel());

        tableCustomer.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFieldsFromSelectedEvent(newSelection);
            }
        });
    }

    private void setupTableColumns() {
        colEventId.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colBusNumber.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        colStartLocation.setCellValueFactory(new PropertyValueFactory<>("startLocation"));
        colEndLocation.setCellValueFactory(new PropertyValueFactory<>("endLocation"));
        colEventValue.setCellValueFactory(new PropertyValueFactory<>("eventValue"));
        colEventDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCustomerContact.setCellValueFactory(new PropertyValueFactory<>("customerContact"));
        colCustomerNic.setCellValueFactory(new PropertyValueFactory<>("customerNic"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colEventCompleted.setCellValueFactory(new PropertyValueFactory<>("eventCompleted"));
        colCreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
    }

    private void loadBusComboBox() {
        try {
            List<String[]> busList = eventBO.getAllActiveBuses();
            ObservableList<String> busItems = FXCollections.observableArrayList();

            for (String[] bus : busList) {
                busItems.add(bus[0] + " - " + bus[1]);
            }

            cmbBusId.setItems(busItems);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load buses: " + e.getMessage()).show();
        }
    }

    private void updateBusNumberLabel() {
        String selected = cmbBusId.getValue();
        if (selected != null && !selected.isEmpty()) {
            String[] parts = selected.split(" - ");
            if (parts.length > 1) {
                lblBusNumber.setText(parts[1]);
            }
        }
    }

    private void loadEventTable() {
        try {
            List<EventDTO> eventList = eventBO.getAllEvents();
            ObservableList<EventDTO> obList = FXCollections.observableArrayList(eventList);
            tableCustomer.setItems(obList);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load events: " + e.getMessage()).show();
        }
    }

    private void fillFieldsFromSelectedEvent(EventDTO event) {
        txtEventId.setText(String.valueOf(event.getEventId()));
        cmbBusId.setValue(event.getBusId() + " - " + event.getBusNumber());
        lblBusNumber.setText(event.getBusNumber());
        txtStartLocation.setText(event.getStartLocation());
        txtEndLocation.setText(event.getEndLocation());
        txtEventValue.setText(String.valueOf(event.getEventValue()));
        dateEventDate.setValue(event.getEventDate());
        txtCustomerName.setText(event.getCustomerName());
        txtCustomerContact.setText(event.getCustomerContact());
        txtCustomerNic.setText(event.getCustomerNic());
        txtCustomerAddress.setText(event.getCustomerAddress());
        txtDescription.setText(event.getDescription());
        chkEventCompleted.setSelected(event.isEventCompleted());
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateFields()) {
            return;
        }

        try {
            String selected = cmbBusId.getValue();
            int busId = Integer.parseInt(selected.split(" - ")[0]);

            EventDTO eventDTO = new EventDTO(
                    0, // ID will be auto-generated
                    busId,
                    txtStartLocation.getText().trim(),
                    txtEndLocation.getText().trim(),
                    Double.parseDouble(txtEventValue.getText().trim()),
                    dateEventDate.getValue(),
                    txtCustomerName.getText().trim(),
                    txtCustomerContact.getText().trim(),
                    txtCustomerNic.getText().trim(),
                    txtCustomerAddress.getText().trim(),
                    txtDescription.getText().trim(),
                    chkEventCompleted.isSelected(),
                    currentUserId
            );

            boolean isSaved = eventBO.saveEvent(eventDTO);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Event saved successfully!").show();
                cleanFields();
                loadEventTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save event!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error saving event: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (txtEventId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an event to update!").show();
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            String selected = cmbBusId.getValue();
            int busId = Integer.parseInt(selected.split(" - ")[0]);
            int eventId = Integer.parseInt(txtEventId.getText().trim());

            EventDTO eventDTO = new EventDTO(
                    eventId,
                    busId,
                    txtStartLocation.getText().trim(),
                    txtEndLocation.getText().trim(),
                    Double.parseDouble(txtEventValue.getText().trim()),
                    dateEventDate.getValue(),
                    txtCustomerName.getText().trim(),
                    txtCustomerContact.getText().trim(),
                    txtCustomerNic.getText().trim(),
                    txtCustomerAddress.getText().trim(),
                    txtDescription.getText().trim(),
                    chkEventCompleted.isSelected(),
                    currentUserId
            );

            boolean isUpdated = eventBO.updateEvent(eventDTO);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Event updated successfully!").show();
                cleanFields();
                loadEventTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update event!").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error updating event: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        if (txtEventId.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select an event to delete!").show();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Event");
        confirmAlert.setContentText("Are you sure you want to delete this event?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = txtEventId.getText();

            try {
                boolean isDeleted = eventBO.deleteEvent(id);

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Event deleted successfully!").show();
                    cleanFields();
                    loadEventTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete event!").show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error deleting event: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleReset(ActionEvent event) {
        cleanFields();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadEventTable();
            return;
        }

        try {
            List<EventDTO> searchResults = eventBO.searchEvents(keyword);
            ObservableList<EventDTO> obList = FXCollections.observableArrayList(searchResults);
            tableCustomer.setItems(obList);

            if (searchResults.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No events found matching: " + keyword).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error searching events: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        txtSearch.clear();
        dateSearch.setValue(null);
        loadEventTable();
        new Alert(Alert.AlertType.INFORMATION, "Event list refreshed successfully!").show();
    }

    @FXML
    private void handlePrint(ActionEvent event) {
        try {
            // Get the current table data
            ObservableList<EventDTO> eventList = tableCustomer.getItems();

            if (eventList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Data", "No events to generate report!");
                return;
            }

            // Load the JRXML file
            InputStream reportStream = getClass().getResourceAsStream("/lk/ijse/busmanagementsystem/reports/EventManagementReport.jrxml");

            if (reportStream == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Report template not found!");
                return;
            }

            // Compile the report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Calculate statistics
            int totalEvents = eventList.size();
            long completedEvents = eventList.stream()
                    .filter(EventDTO::isEventCompleted)
                    .count();
            long pendingEvents = totalEvents - completedEvents;

            double totalRevenue = eventList.stream()
                    .mapToDouble(EventDTO::getEventValue)
                    .sum();

            // Create parameters map
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("GeneratedDate", LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")));
            parameters.put("TotalEvents", String.valueOf(totalEvents));
            parameters.put("CompletedEvents", String.valueOf(completedEvents));
            parameters.put("PendingEvents", String.valueOf(pendingEvents));
            parameters.put("TotalRevenue", String.format("%,.2f", totalRevenue));

            // Convert ObservableList to JRBeanCollectionDataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(eventList);

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String outputPath = "EventManagementReport_" + timestamp + ".pdf";

            // Export to PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Event Report generated successfully!\n" +
                            "Total Events: " + totalEvents + "\n" +
                            "Completed: " + completedEvents + "\n" +
                            "Pending: " + pendingEvents + "\n" +
                            "Total Revenue: Rs. " + String.format("%,.2f", totalRevenue) + "\n\n" +
                            "Saved as: " + outputPath);

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

    @FXML
    private void handleSearchEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String id = txtEventId.getText();

            if (id.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter Event ID!").show();
                return;
            }

            try {
                EventDTO eventDTO = eventBO.searchEvent(id);

                if (eventDTO != null) {
                    fillFieldsFromSelectedEvent(eventDTO);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Event not found!").show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error searching event: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
        System.out.println("Closing Event window");
    }

    private boolean validateFields() {
        if (cmbBusId.getValue() == null || cmbBusId.getValue().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a bus!").show();
            return false;
        }

        if (txtStartLocation.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter start location!").show();
            return false;
        }

        if (txtEndLocation.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter end location!").show();
            return false;
        }

        if (txtEventValue.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter event value!").show();
            return false;
        }

        try {
            double value = Double.parseDouble(txtEventValue.getText().trim());
            if (value <= 0) {
                new Alert(Alert.AlertType.WARNING, "Event value must be positive!").show();
                return false;
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Event value must be a valid number!").show();
            return false;
        }

        if (dateEventDate.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select event date!").show();
            return false;
        }

        if (txtCustomerName.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter customer name!").show();
            return false;
        }

        if (txtCustomerContact.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter customer contact!").show();
            return false;
        }

        if (txtCustomerNic.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter customer NIC!").show();
            return false;
        }

        if (txtCustomerAddress.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter customer address!").show();
            return false;
        }

        return true;
    }

    private void cleanFields() {
        txtEventId.setText("");
        cmbBusId.setValue(null);
        lblBusNumber.setText("");
        txtStartLocation.setText("");
        txtEndLocation.setText("");
        txtEventValue.setText("");
        dateEventDate.setValue(null);
        txtCustomerName.setText("");
        txtCustomerContact.setText("");
        txtCustomerNic.setText("");
        txtCustomerAddress.setText("");
        txtDescription.setText("");
        chkEventCompleted.setSelected(false);
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }
}