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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.busmanagementsystem.dto.UserDTO;
import lk.ijse.busmanagementsystem.model.UserModel;
import lk.ijse.busmanagementsystem.util.SessionManager;
import static lk.ijse.busmanagementsystem.Main.setRoot;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private UserModel userModel = new UserModel();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setup hover effect and key handlers after scene is loaded
        Platform.runLater(() -> {
            setupLoginButtonHover();
            setupKeyHandlers();
        });
    }

    private void setupKeyHandlers() {
        // When Enter is pressed in username field, move to password field
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        // When Enter is pressed in password field, submit login
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
        // Find the login button if not injected
        if (loginButton == null) {
            loginButton = (Button) usernameField.getScene().lookup(".button");
        }

        if (loginButton != null) {
            // Store original style
            final String originalStyle = loginButton.getStyle();

            loginButton.setOnMouseEntered(e -> {
                // Scale up button
                loginButton.setScaleX(1.05);
                loginButton.setScaleY(1.05);
                // Make text slightly bigger
                loginButton.setStyle(loginButton.getStyle() + "; -fx-font-size: 17px;");
            });

            loginButton.setOnMouseExited(e -> {
                // Reset to original
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

        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter both username and password!");
            return;
        }

        try {
            // Authenticate user from database
            UserDTO user = userModel.authenticateUser(username, password);

            if (user != null) {
                // Login successful
                System.out.println("Login successful: " + user.getName() + " - " + user.getRole());

                // Store logged in user
                SessionManager.setLoggedInUser(user);

                // Navigate to main layout
                setRoot("Layout");
            } else {
                // Login failed
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


//package lk.ijse.busmanagementsystem.controller;
//
//import java.io.IOException;
//import java.net.URL;
//import java.sql.SQLException;
//import java.util.ResourceBundle;
//
//import javafx.application.Platform;
//import javafx.fxml.FXML;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import lk.ijse.busmanagementsystem.dto.UserDTO;
//import lk.ijse.busmanagementsystem.model.UserModel;
//import lk.ijse.busmanagementsystem.util.SessionManager;
//
//import static lk.ijse.busmanagementsystem.Main.setRoot;
//
//public class LoginController implements Initializable {
//    @FXML
//    private TextField usernameField;
//
//    @FXML
//    private PasswordField passwordField;
//
//    @FXML
//    private Button loginButton;
//
//    private UserModel userModel = new UserModel();
//
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // Setup hover effect after scene is loaded
//        Platform.runLater(() -> {
//            setupLoginButtonHover();
//        });
//    }
//
//    private void setupLoginButtonHover() {
//        // Find the login button if not injected
//        if (loginButton == null) {
//            loginButton = (Button) usernameField.getScene().lookup(".button");
//        }
//
//        if (loginButton != null) {
//            // Store original style
//            final String originalStyle = loginButton.getStyle();
//
//            loginButton.setOnMouseEntered(e -> {
//                // Scale up button
//                loginButton.setScaleX(1.05);
//                loginButton.setScaleY(1.05);
//
//                // Make text slightly bigger
//                loginButton.setStyle(loginButton.getStyle() +
//                        "; -fx-font-size: 17px;");
//            });
//
//            loginButton.setOnMouseExited(e -> {
//                // Reset to original
//                loginButton.setScaleX(1.0);
//                loginButton.setScaleY(1.0);
//                loginButton.setStyle(originalStyle);
//            });
//        }
//    }
//
//    @FXML
//    private void login() throws IOException {
//        String username = usernameField.getText().trim();
//        String password = passwordField.getText().trim();
//
//        // Validation
//        if (username.isEmpty() || password.isEmpty()) {
//            showAlert(Alert.AlertType.WARNING, "Validation Error",
//                    "Please enter both username and password!");
//            return;
//        }
//
//        try {
//            // Authenticate user from database
//            UserDTO user = userModel.authenticateUser(username, password);
//
//            if (user != null) {
//                // Login successful
//                System.out.println("Login successful: " + user.getName() + " - " + user.getRole());
//
//                // Store logged in user
//                SessionManager.setLoggedInUser(user);
//
//                // Navigate to main layout
//                setRoot("Layout");
//            } else {
//                // Login failed
//                showAlert(Alert.AlertType.ERROR, "Login Failed",
//                        "Invalid username or password!");
//                passwordField.clear();
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert(Alert.AlertType.ERROR, "Database Error",
//                    "Error connecting to database: " + e.getMessage());
//        }
//    }
//
//    private void showAlert(Alert.AlertType alertType, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}