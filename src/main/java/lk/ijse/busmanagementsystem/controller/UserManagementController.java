package lk.ijse.busmanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.busmanagementsystem.dto.UserDTO;
import lk.ijse.busmanagementsystem.bo.BOFactory;
import lk.ijse.busmanagementsystem.bo.custom.UserBO;
import lk.ijse.busmanagementsystem.util.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static lk.ijse.busmanagementsystem.Main.setRoot;

public class UserManagementController {

    @FXML private Label loggedUserLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField contactField;
    @FXML private TextField nicField;
    @FXML private TextField emailField;  // NeW
    @FXML private TextField searchField;

    @FXML private TableView<UserDTO> userTable;
    @FXML private TableColumn<UserDTO, Integer> colUserId;
    @FXML private TableColumn<UserDTO, String> colUsername;
    @FXML private TableColumn<UserDTO, String> colName;
    @FXML private TableColumn<UserDTO, String> colRole;
    @FXML private TableColumn<UserDTO, String> colContact;
    @FXML private TableColumn<UserDTO, String> colNic;
    @FXML private TableColumn<UserDTO, String> colEmail;  // NeW
    @FXML private TableColumn<UserDTO, String> colCreatedAt;

    @FXML private Button saveButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;

    private final UserBO userBO = (UserBO) BOFactory.getInstance().getBO(BOFactory.BOType.USER);
    private ObservableList<UserDTO> userList = FXCollections.observableArrayList();
    private UserDTO selectedUser = null;

    @FXML
    public void initialize() {
        if (!SessionManager.isOwner()) {
            showAlert(Alert.AlertType.ERROR, "Access Denied",
                    "Only Owners can access User Management!");
            try {
                goBack();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }


        loggedUserLabel.setText("Logged: " + SessionManager.getCurrentUserName() +
                " (" + SessionManager.getCurrentUserRole() + ")");

        System.out.println("UserManagement is loaded");

        roleComboBox.setItems(FXCollections.observableArrayList("Owner", "Manager", "Admin"));

        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));  // NEW

        colCreatedAt.setCellValueFactory(cellData -> {
            if (cellData.getValue().getCreatedAt() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formatted = cellData.getValue().getCreatedAt().format(formatter);
                return new javafx.beans.property.SimpleStringProperty(formatted);
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        loadAllUsers();

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedUser = newSelection;
                fillFieldsWithSelectedUser(newSelection);
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterUserTable(newValue);
        });

        updateButton.setDisable(true);
    }

    @FXML
    private void loadAllUsers() {
        try {
            List<UserDTO> users = userBO.getAllUsers();
            userList.clear();
            userList.addAll(users);
            userTable.setItems(userList);

            System.out.println("Loaded " + users.size() + " users successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Failed to load users: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillFieldsWithSelectedUser(UserDTO user) {
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        nameField.setText(user.getName());
        roleComboBox.setValue(user.getRole());
        contactField.setText(user.getContact());
        nicField.setText(user.getNic());
        emailField.setText(user.getEmail());

        saveButton.setDisable(true);
        updateButton.setDisable(false);
    }

    @FXML
    private void saveUser() {
        if (!validateFields()) {
            return;
        }

        try {
            if (userBO.isUsernameExists(usernameField.getText().trim())) {
                showAlert(Alert.AlertType.WARNING, "Username Exists",
                        "This username is already taken. Please choose another.");
                return;
            }

            if (userBO.isEmailExists(emailField.getText().trim())) {
                showAlert(Alert.AlertType.WARNING, "Email Exists",
                        "This email is already registered!");
                return;
            }

            UserDTO user = new UserDTO();
            user.setUsername(usernameField.getText().trim());
            user.setPassword(passwordField.getText());
            user.setName(nameField.getText().trim());
            user.setRole(roleComboBox.getValue());
            user.setContact(contactField.getText().trim());
            user.setNic(nicField.getText().trim());
            user.setEmail(emailField.getText().trim());  // NEW

            boolean saved = userBO.saveUser(user);

            if (saved) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "User saved successfully!");
                clearFields();
                loadAllUsers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to save user!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error saving user: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void updateUser() {
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a user to update!");
            return;
        }

        if (!validateFields()) {
            return;
        }

        try {
            if (userBO.isUsernameExistsForUpdate(usernameField.getText().trim(), selectedUser.getUserId())) {
                showAlert(Alert.AlertType.WARNING, "Username Exists",
                        "This username is already taken. Please choose another.");
                return;
            }

            if (userBO.isEmailExistsForUpdate(emailField.getText().trim(), selectedUser.getUserId())) {
                showAlert(Alert.AlertType.WARNING, "Email Exists",
                        "This email is already registered!");
                return;
            }

            if (selectedUser.getUserId().equals(SessionManager.getCurrentUserId())) {
                if (!roleComboBox.getValue().equals(selectedUser.getRole())) {
                    showAlert(Alert.AlertType.WARNING, "Invalid Operation",
                            "You cannot change your own role!");
                    return;
                }
            }

            selectedUser.setUsername(usernameField.getText().trim());
            selectedUser.setPassword(passwordField.getText());
            selectedUser.setName(nameField.getText().trim());
            selectedUser.setRole(roleComboBox.getValue());
            selectedUser.setContact(contactField.getText().trim());
            selectedUser.setNic(nicField.getText().trim());
            selectedUser.setEmail(emailField.getText().trim());  // NEW

            boolean updated = userBO.updateUser(selectedUser);

            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "User updated successfully!");
                clearFields();
                loadAllUsers();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "Failed to update user!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error updating user: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void deleteUser() {
        UserDTO user = userTable.getSelectionModel().getSelectedItem();

        if (user == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a user to delete!");
            return;
        }

        if (user.getUserId().equals(SessionManager.getCurrentUserId())) {
            showAlert(Alert.AlertType.WARNING, "Invalid Operation",
                    "You cannot delete yourself!");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete User?");
        confirmAlert.setContentText("Are you sure you want to delete user: " + user.getName() + "?\n\nThis action cannot be undone!");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean deleted = userBO.deleteUser(user.getUserId());

                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "User deleted successfully!");
                    clearFields();
                    loadAllUsers();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "Failed to delete user!");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error",
                        "Error deleting user: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        nameField.clear();
        roleComboBox.setValue(null);
        contactField.clear();
        nicField.clear();
        emailField.clear();

        selectedUser = null;
        userTable.getSelectionModel().clearSelection();

        saveButton.setDisable(false);
        updateButton.setDisable(true);
    }

    private boolean validateFields() {
        if (usernameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Username is required!");
            usernameField.requestFocus();
            return false;
        }

        if (usernameField.getText().trim().length() < 3) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Username must be at least 3 characters!");
            usernameField.requestFocus();
            return false;
        }

        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Password is required!");
            passwordField.requestFocus();
            return false;
        }

        if (passwordField.getText().length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Password must be at least 6 characters!");
            passwordField.requestFocus();
            return false;
        }

        if (nameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Name is required!");
            nameField.requestFocus();
            return false;
        }

        if (roleComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please select a role!");
            roleComboBox.requestFocus();
            return false;
        }

        if (contactField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Contact is required!");
            contactField.requestFocus();
            return false;
        }

        if (!contactField.getText().trim().matches("^0\\d{9}$")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Invalid contact number! Format: 0771234567");
            contactField.requestFocus();
            return false;
        }

        if (nicField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "NIC is required!");
            nicField.requestFocus();
            return false;
        }

        if (!nicField.getText().trim().matches("^(\\d{9}[vVxX]|\\d{12})$")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Invalid NIC format! Use: 123456789V or 200012345678");
            nicField.requestFocus();
            return false;
        }

        // NEW EMAIL VALIDATION
        if (emailField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Email is required!");
            emailField.requestFocus();
            return false;
        }

        if (!emailField.getText().trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Invalid email format! Use: user@example.com");
            emailField.requestFocus();
            return false;
        }

        return true;
    }

    private void filterUserTable(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            userTable.setItems(userList);
            return;
        }

        String search = searchText.toLowerCase().trim();
        ObservableList<UserDTO> filtered = FXCollections.observableArrayList();

        for (UserDTO user : userList) {
            if (user.getName().toLowerCase().contains(search) ||
                    user.getUsername().toLowerCase().contains(search) ||
                    user.getRole().toLowerCase().contains(search) ||
                    user.getContact().contains(search) ||
                    user.getNic().toLowerCase().contains(search) ||
                    user.getEmail().toLowerCase().contains(search)) {  // NEW
                filtered.add(user);
            }
        }

        userTable.setItems(filtered);
    }

    @FXML
    private void goBack() throws IOException {
        setRoot("Layout");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}