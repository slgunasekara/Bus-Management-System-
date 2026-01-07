package lk.ijse.busmanagementsystem.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lk.ijse.busmanagementsystem.Main;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class BusManagementToolsController implements Initializable {


    @FXML
    private TextField txtDisplay;

    @FXML
    private Label lblOperation;

    @FXML
    private Button btnClear, btnDelete, btnPercent, btnDivide;
    @FXML
    private Button btn7, btn8, btn9, btnMultiply;
    @FXML
    private Button btn4, btn5, btn6, btnMinus;
    @FXML
    private Button btn1, btn2, btn3, btnPlus;
    @FXML
    private Button btn0, btnDot, btnEquals;


    @FXML
    private TextField txtDistance;
    @FXML
    private TextField txtFuelPrice;
    @FXML
    private TextField txtMileage;
    @FXML
    private Label lblFuelResult;


    @FXML
    private TextField txtRevenue;
    @FXML
    private TextField txtExpenses;
    @FXML
    private Label lblProfitResult;


    @FXML
    private TextField txtStartTime;
    @FXML
    private TextField txtEndTime;
    @FXML
    private Label lblTimeResult;


    @FXML
    private TextField txtFromLocation;
    @FXML
    private TextField txtToLocation;
    @FXML
    private Label lblRouteResult;


    private double num1 = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("BusManagementTools is loaded");

        Platform.runLater(() -> {
            setupHoverEffects();
        });
    }

    private void setupHoverEffects() {
        if (txtDisplay.getScene() != null) {
            txtDisplay.getScene().getRoot().lookupAll(".button").forEach(node -> {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    addHoverEffect(button);
                }
            });
        }
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> {
            button.setScaleX(1.05);
            button.setScaleY(1.05);

            if (button.getGraphic() instanceof HBox) {
                HBox graphic = (HBox) button.getGraphic();
                graphic.getChildren().forEach(child -> {
                    if (child instanceof Text) {
                        Text text = (Text) child;
                        if (text.getUserData() == null) {
                            text.setUserData(text.getFont().getSize());
                        }
                        double originalSize = (double) text.getUserData();
                        text.setStyle("-fx-font-size: " + (originalSize + 2) + "px; -fx-font-weight: bold;");
                    }
                });
            }

            String currentStyle = button.getStyle();
            if (!currentStyle.contains("-fx-effect: dropshadow(gaussian, rgba(255,255,255")) {
                button.setStyle(currentStyle + "; -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.6), 20, 0, 0, 0);");
            }
        });

        button.setOnMouseExited(e -> {
            button.setScaleX(1.0);
            button.setScaleY(1.0);

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

            String originalStyle = button.getStyle();
            if (originalStyle.contains("; -fx-effect: dropshadow(gaussian, rgba(255,255,255")) {
                int effectIndex = originalStyle.indexOf("; -fx-effect: dropshadow(gaussian, rgba(255,255,255");
                button.setStyle(originalStyle.substring(0, effectIndex));
            }
        });
    }


    @FXML
    private void handleNumber(ActionEvent event) {
        Button button = (Button) event.getSource();
        String digit = button.getText();

        if (startNewNumber) {
            txtDisplay.setText(digit);
            startNewNumber = false;
        } else {
            txtDisplay.setText(txtDisplay.getText() + digit);
        }
    }

    @FXML
    private void handleDot(ActionEvent event) {
        if (!txtDisplay.getText().contains(".")) {
            txtDisplay.setText(txtDisplay.getText() + ".");
        }
        startNewNumber = false;
    }

    @FXML
    private void handlePlus(ActionEvent event) {
        calculatePendingOperation();
        operator = "+";
        lblOperation.setText(num1 + " +");
        startNewNumber = true;
    }

    @FXML
    private void handleMinus(ActionEvent event) {
        calculatePendingOperation();
        operator = "-";
        lblOperation.setText(num1 + " -");
        startNewNumber = true;
    }

    @FXML
    private void handleMultiply(ActionEvent event) {
        calculatePendingOperation();
        operator = "×";
        lblOperation.setText(num1 + " ×");
        startNewNumber = true;
    }

    @FXML
    private void handleDivide(ActionEvent event) {
        calculatePendingOperation();
        operator = "÷";
        lblOperation.setText(num1 + " ÷");
        startNewNumber = true;
    }

    @FXML
    private void handlePercent(ActionEvent event) {
        double current = Double.parseDouble(txtDisplay.getText());
        txtDisplay.setText(String.valueOf(current / 100));
    }

    @FXML
    private void handleEquals(ActionEvent event) {
        calculatePendingOperation();
        lblOperation.setText("");
        operator = "";
        startNewNumber = true;
    }

    @FXML
    private void handleClear(ActionEvent event) {
        txtDisplay.setText("0");
        lblOperation.setText("");
        num1 = 0;
        operator = "";
        startNewNumber = true;
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        String current = txtDisplay.getText();
        if (current.length() > 1) {
            txtDisplay.setText(current.substring(0, current.length() - 1));
        } else {
            txtDisplay.setText("0");
            startNewNumber = true;
        }
    }

    private void calculatePendingOperation() {
        double num2 = Double.parseDouble(txtDisplay.getText());

        if (operator.isEmpty()) {
            num1 = num2;
        } else {
            switch (operator) {
                case "+":
                    num1 = num1 + num2;
                    break;
                case "-":
                    num1 = num1 - num2;
                    break;
                case "×":
                    num1 = num1 * num2;
                    break;
                case "÷":
                    if (num2 != 0) {
                        num1 = num1 / num2;
                    } else {
                        txtDisplay.setText("Error");
                        num1 = 0;
                        operator = "";
                        startNewNumber = true;
                        return;
                    }
                    break;
            }
        }

        if (num1 == (long) num1) {
            txtDisplay.setText(String.valueOf((long) num1));
        } else {
            txtDisplay.setText(String.format("%.2f", num1));
        }
    }

    // FUEL CALCULATOR METHODS
    @FXML
    private void calculateFuel(ActionEvent event) {
        try {
            double distance = Double.parseDouble(txtDistance.getText());
            double fuelPrice = Double.parseDouble(txtFuelPrice.getText());
            double mileage = Double.parseDouble(txtMileage.getText());

            if (mileage == 0) {
                lblFuelResult.setText("❌ Mileage cannot be zero!");
                lblFuelResult.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            double fuelNeeded = distance / mileage;
            double totalCost = fuelNeeded * fuelPrice;

            lblFuelResult.setText(String.format("✓ Fuel: %.2f L | Cost: Rs %.2f", fuelNeeded, totalCost));
            lblFuelResult.setStyle("-fx-text-fill: #10b981;");

        } catch (NumberFormatException e) {
            lblFuelResult.setText("❌ Please enter valid numbers!");
            lblFuelResult.setStyle("-fx-text-fill: #ef4444;");
        }
    }

    // PROFIT CALCULATOR METHODS
    @FXML
    private void calculateProfit(ActionEvent event) {
        try {
            double revenue = Double.parseDouble(txtRevenue.getText());
            double expenses = Double.parseDouble(txtExpenses.getText());

            double profit = revenue - expenses;
            double margin = (revenue != 0) ? (profit / revenue) * 100 : 0;

            String resultText = String.format("✓ Profit: Rs %.2f | Margin: %.2f%%", profit, margin);
            lblProfitResult.setText(resultText);

            if (profit >= 0) {
                lblProfitResult.setStyle("-fx-text-fill: #10b981;");
            } else {
                lblProfitResult.setStyle("-fx-text-fill: #ef4444;");
            }

        } catch (NumberFormatException e) {
            lblProfitResult.setText("❌ Please enter valid numbers!");
            lblProfitResult.setStyle("-fx-text-fill: #ef4444;");
        }
    }

    // TIME CALCULATOR METHODS
    @FXML
    private void calculateTime(ActionEvent event) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime start = LocalTime.parse(txtStartTime.getText(), formatter);
            LocalTime end = LocalTime.parse(txtEndTime.getText(), formatter);

            long minutes = start.until(end, ChronoUnit.MINUTES);

            if (minutes < 0) {
                minutes += 24 * 60;
            }

            long hours = minutes / 60;
            long mins = minutes % 60;

            lblTimeResult.setText(String.format("✓ Duration: %d hours %d minutes", hours, mins));
            lblTimeResult.setStyle("-fx-text-fill: #3b82f6;");

        } catch (Exception e) {
            lblTimeResult.setText("❌ Invalid time format! Use HH:MM (e.g., 14:30)");
            lblTimeResult.setStyle("-fx-text-fill: #ef4444;");
        }
    }

    // ROUTE FINDER METHODS
    @FXML
    private void findRoute(ActionEvent event) {
        try {
            String fromLocation = txtFromLocation.getText().trim();
            String toLocation = txtToLocation.getText().trim();

            if (fromLocation.isEmpty() || toLocation.isEmpty()) {
                lblRouteResult.setText("❌ Please enter both locations!");
                lblRouteResult.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            // URL encode the locations for Google Maps
            String encodedFrom = URLEncoder.encode(fromLocation, StandardCharsets.UTF_8);
            String encodedTo = URLEncoder.encode(toLocation, StandardCharsets.UTF_8);

            // Build Google Maps directions URL
            String mapsUrl = String.format(
                    "https://www.google.com/maps/dir/?api=1&origin=%s&destination=%s&travelmode=driving",
                    encodedFrom, encodedTo
            );

            // Open in default browser
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(mapsUrl));

                lblRouteResult.setText("✓ Opening Google Maps in browser...");
                lblRouteResult.setStyle("-fx-text-fill: #10b981;");
            } else {
                lblRouteResult.setText("❌ Cannot open browser on this system!");
                lblRouteResult.setStyle("-fx-text-fill: #ef4444;");
            }

        } catch (Exception e) {
            lblRouteResult.setText("❌ Error: " + e.getMessage());
            lblRouteResult.setStyle("-fx-text-fill: #ef4444;");
            e.printStackTrace();
        }
    }

    @FXML
    private void swapLocations(ActionEvent event) {
        String temp = txtFromLocation.getText();
        txtFromLocation.setText(txtToLocation.getText());
        txtToLocation.setText(temp);

        lblRouteResult.setText("↔ Locations swapped!");
        lblRouteResult.setStyle("-fx-text-fill: #3b82f6;");
    }


    @FXML
    private void logout(ActionEvent event) throws Exception {
        Main.setRoot("login");
        System.out.println("Logout");
    }
}