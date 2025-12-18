package lk.ijse.busmanagementsystem.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lk.ijse.busmanagementsystem.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {

    @FXML
    private AnchorPane mainContent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Layout is loaded");

        // Use Platform.runLater to ensure scene is fully loaded
        Platform.runLater(() -> {
            setupHoverEffects();
        });
    }

    private void setupHoverEffects() {
        // Check if scene is available
        if (mainContent.getScene() != null) {
            mainContent.getScene().getRoot().lookupAll(".button").forEach(node -> {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    addHoverEffect(button);
                }
            });
        }
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> {
            // Scale up the button slightly
            button.setScaleX(1.05);
            button.setScaleY(1.05);

            // Find and scale up text inside the button
            if (button.getGraphic() instanceof HBox) {
                HBox graphic = (HBox) button.getGraphic();
                graphic.getChildren().forEach(child -> {
                    if (child instanceof Text) {
                        Text text = (Text) child;
                        // Store original size if not already stored
                        if (text.getUserData() == null) {
                            text.setUserData(text.getFont().getSize());
                        }
                        double originalSize = (double) text.getUserData();
                        text.setStyle("-fx-font-size: " + (originalSize + 2) + "px; -fx-font-weight: bold;");
                    }
                });
            }

            // Add glow effect
            String currentStyle = button.getStyle();
            if (!currentStyle.contains("-fx-effect: dropshadow(gaussian, rgba(255,255,255")) {
                button.setStyle(currentStyle + "; -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.6), 20, 0, 0, 0);");
            }
        });

        button.setOnMouseExited(e -> {
            // Reset button scale
            button.setScaleX(1.0);
            button.setScaleY(1.0);

            // Reset text size
            if (button.getGraphic() instanceof HBox) {
                HBox graphic = (HBox) button.getGraphic();
                graphic.getChildren().forEach(child -> {
                    if (child instanceof Text) {
                        Text text = (Text) child;
                        if (text.getUserData() != null) {
                            double originalSize = (double) text.getUserData();
                            text.setStyle("-fx-font-size: " + originalSize + "px; -fx-font-weight: bold;");
                        }
                    }
                });
            }

            // Reset to original style (remove white glow, keep original dropshadow)
            String originalStyle = button.getStyle();
            if (originalStyle.contains("; -fx-effect: dropshadow(gaussian, rgba(255,255,255")) {
                int effectIndex = originalStyle.indexOf("; -fx-effect: dropshadow(gaussian, rgba(255,255,255");
                button.setStyle(originalStyle.substring(0, effectIndex));
            }
        });
    }

    @FXML
    private void clickBusNav() throws IOException {
        Parent BusFXML = Main.loadFXML("ManageBus");
        mainContent.getChildren().setAll(BusFXML);
    }

    @FXML
    private void clickTripNav() throws IOException {
        Parent TripFXML = Main.loadFXML("ManageTrip");
        mainContent.getChildren().setAll(TripFXML);
    }

    @FXML
    private void clickOtherServicesNav() throws IOException {
        Parent OtherServicesFXML = Main.loadFXML("ManageOtherServices");
        mainContent.getChildren().setAll(OtherServicesFXML);
    }

    @FXML
    private void clickTripExpensesNav() throws IOException {
        Parent TripExpensesFXML = Main.loadFXML("ManageTripExpenses");
        mainContent.getChildren().setAll(TripExpensesFXML);
    }

    @FXML
    private void clickEmployeeNav() throws IOException {
        Parent EmployeeFXML = Main.loadFXML("ManageEmployee");
        mainContent.getChildren().setAll(EmployeeFXML);
    }

    @FXML
    private void clickEmployeeSalaryNav() throws IOException {
        Parent EmployeeSalaryFXML = Main.loadFXML("ManageEmployeeSalary");
        mainContent.getChildren().setAll(EmployeeSalaryFXML);
    }

    @FXML
    private void clickMaintenanceNav() throws IOException {
        Parent MaintenanceFXML = Main.loadFXML("ManageMaintenance");
        mainContent.getChildren().setAll(MaintenanceFXML);
    }

    @FXML
    private void clickPartPurchaseNav() throws IOException {
        Parent PartPurchaseFXML = Main.loadFXML("ManagePartPurchase");
        mainContent.getChildren().setAll(PartPurchaseFXML);
    }

    @FXML
    private void clickBusManagementToolsNav() throws IOException {
        Parent ManagementToolFXML = Main.loadFXML("BusManagementTools");
        mainContent.getChildren().setAll(ManagementToolFXML);
    }

    @FXML
    private void clickUpdatePricesNav() throws IOException {
        Parent CheckStatusFXML = Main.loadFXML("UpdatePrices");
        mainContent.getChildren().setAll(CheckStatusFXML);
    }

    @FXML
    private void clickManageReportNav() throws IOException {
        Parent ReportFXML = Main.loadFXML("ManageReport");
        mainContent.getChildren().setAll(ReportFXML);
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {
        Main.setRoot("login");
        System.out.println("Logout");
    }

    @FXML
    private void desktop(ActionEvent event) throws Exception {
        Main.setRoot("Layout");
    }

    @FXML
    private void clickManageUserNav() throws IOException {
        Parent ManageUserFXML = Main.loadFXML("ManageUser");
        mainContent.getChildren().setAll(ManageUserFXML);
    }
}