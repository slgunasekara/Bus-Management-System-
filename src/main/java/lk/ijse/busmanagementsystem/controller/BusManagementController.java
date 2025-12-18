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

import java.net.URL;
import java.util.ResourceBundle;

public class BusManagementController implements Initializable {

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

    private double num1 = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("BusManagementTools is loaded");

        // Use Platform.runLater to ensure scene is fully loaded
        Platform.runLater(() -> {
            setupHoverEffects();
        });
    }

    private void setupHoverEffects() {
        // Check if scene is available
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

    // Calculator Methods
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

        // Format the result
        if (num1 == (long) num1) {
            txtDisplay.setText(String.valueOf((long) num1));
        } else {
            txtDisplay.setText(String.valueOf(num1));
        }
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {
        Main.setRoot("login");
        System.out.println("Logout");
    }
}