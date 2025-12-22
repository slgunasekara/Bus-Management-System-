package lk.ijse.busmanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lk.ijse.busmanagementsystem.dto.PasswordResetOtpDTO;
import lk.ijse.busmanagementsystem.dto.UserDTO;
import lk.ijse.busmanagementsystem.model.PasswordResetOtpModel;
import lk.ijse.busmanagementsystem.model.UserModel;
import lk.ijse.busmanagementsystem.services.EmailService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static lk.ijse.busmanagementsystem.Main.setRoot;

public class ForgotPasswordController implements Initializable {

    @FXML private TextField emailField;
    @FXML private Button sendOtpButton;
    @FXML private VBox otpSection;
    @FXML private TextField otpField;
    @FXML private Button verifyOtpButton;
    @FXML private VBox passwordSection;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button resetPasswordButton;
    @FXML private Hyperlink backToLoginLink;

    private UserModel userModel = new UserModel();
    private PasswordResetOtpModel otpModel = new PasswordResetOtpModel();

    private String currentEmail;
    private String currentOtp;
    private Integer currentUserId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initially hide OTP and password sections
        otpSection.setVisible(false);
        otpSection.setManaged(false);
        passwordSection.setVisible(false);
        passwordSection.setManaged(false);
    }

    @FXML
    private void handleSendOtp() {
        String email = emailField.getText().trim();

        // Validation
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter your email address!");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Email", "Please enter a valid email address!");
            return;
        }

        try {
            // Check if email exists in database
            UserDTO user = userModel.getUserByEmail(email);

            if (user == null) {
                showAlert(Alert.AlertType.ERROR, "Email Not Found",
                        "No account found with this email address!");
                return;
            }

            // Generate OTP
            String otp = EmailService.generateOTP();

            // Save OTP to database
            PasswordResetOtpDTO otpDTO = new PasswordResetOtpDTO();
            otpDTO.setUserId(user.getUserId());
            otpDTO.setOtpCode(otp);
            otpDTO.setEmail(email);
            otpDTO.setExpiresAt(LocalDateTime.now().plusMinutes(10)); // Valid for 10 minutes

            boolean saved = otpModel.saveOTP(otpDTO);

            if (!saved) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate OTP. Please try again!");
                return;
            }

            // Send OTP via email
            boolean emailSent = EmailService.sendOTPEmail(email, otp, user.getName());

            if (emailSent) {
                currentEmail = email;
                currentUserId = user.getUserId();

                showAlert(Alert.AlertType.INFORMATION, "OTP Sent",
                        "A 6-digit OTP has been sent to your email address.\nPlease check your inbox.");

                // Show OTP section
                otpSection.setVisible(true);
                otpSection.setManaged(true);
                emailField.setDisable(true);
                sendOtpButton.setDisable(true);
            } else {
                showAlert(Alert.AlertType.ERROR, "Email Error",
                        "Failed to send OTP email. Please check your email configuration!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error connecting to database: " + e.getMessage());
        }
    }

    @FXML
    private void handleVerifyOtp() {
        String otp = otpField.getText().trim();

        // Validation
        if (otp.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter the OTP!");
            return;
        }

        if (otp.length() != 6) {
            showAlert(Alert.AlertType.WARNING, "Invalid OTP", "OTP must be 6 digits!");
            return;
        }

        try {
            // Validate OTP
            Integer userId = otpModel.validateOTP(currentEmail, otp);

            if (userId == null) {
                showAlert(Alert.AlertType.ERROR, "Invalid OTP",
                        "Invalid or expired OTP. Please try again!");
                otpField.clear();
                return;
            }

            // OTP is valid
            currentOtp = otp;

            showAlert(Alert.AlertType.INFORMATION, "OTP Verified",
                    "OTP verified successfully! Please enter your new password.");

            // Show password section
            passwordSection.setVisible(true);
            passwordSection.setManaged(true);
            otpField.setDisable(true);
            verifyOtpButton.setDisable(true);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error verifying OTP: " + e.getMessage());
        }
    }

    @FXML
    private void handleResetPassword() {
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Validation
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please enter both password fields!");
            return;
        }

        if (newPassword.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Weak Password",
                    "Password must be at least 6 characters long!");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Password Mismatch",
                    "Passwords do not match!");
            confirmPasswordField.clear();
            return;
        }

        try {
            // Update password
            boolean updated = userModel.updatePassword(currentUserId, newPassword);

            if (updated) {
                // Mark OTP as used
                otpModel.markOTPAsUsed(currentEmail, currentOtp);

                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Password reset successfully! You can now login with your new password.");

                // Navigate back to login
                setRoot("login");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to reset password. Please try again!");
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Could not navigate to login page!");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}