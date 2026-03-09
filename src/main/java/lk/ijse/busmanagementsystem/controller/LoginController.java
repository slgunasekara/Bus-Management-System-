package lk.ijse.busmanagementsystem.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lk.ijse.busmanagementsystem.dto.UserDTO;
import lk.ijse.busmanagementsystem.bo.BOFactory;
import lk.ijse.busmanagementsystem.bo.custom.UserBO;
import lk.ijse.busmanagementsystem.util.SessionManager;
import static lk.ijse.busmanagementsystem.Main.setRoot;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Hyperlink forgotPasswordLink;

    private final UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            setupLoginButtonHover();
            setupKeyHandlers();
        });
    }

    private void setupKeyHandlers() {
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "An error occurred during login: " + e.getMessage());
                }
            }
        });
    }

    private void setupLoginButtonHover() {
        if (loginButton == null) {
            loginButton = (Button) usernameField.getScene().lookup(".button");
        }

        if (loginButton != null) {
            final String originalStyle = loginButton.getStyle();

            loginButton.setOnMouseEntered(e -> {
                loginButton.setScaleX(1.05);
                loginButton.setScaleY(1.05);
                loginButton.setStyle(loginButton.getStyle() + "; -fx-font-size: 17px;");
            });

            loginButton.setOnMouseExited(e -> {
                loginButton.setScaleX(1.0);
                loginButton.setScaleY(1.0);
                loginButton.setStyle(originalStyle);
            });
        }
    }

    @FXML
    private void login() throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter both username and password!");
            return;
        }

        try {
            UserDTO user = userBO.authenticateUser(username, password);

            if (user != null) {
                System.out.println("Login successful: " + user.getName() + " - " + user.getRole());
                SessionManager.setLoggedInUser(user);
                setRoot("Layout");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed",
                        "Invalid username or password!");
                passwordField.clear();
                usernameField.requestFocus();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error connecting to database: " + e.getMessage());
        }
    }

    @FXML
    private void handleForgotPassword() {
        try {
            setRoot("forgotPassword");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Could not open Forgot Password page!");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}