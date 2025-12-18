package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.dto.UserDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserModel {

    public UserDTO authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM User WHERE username = ? AND password = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, username);
        pstm.setString(2, password);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            return new UserDTO(
                    resultSet.getInt("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("role"),
                    resultSet.getString("contact"),
                    resultSet.getString("NIC"),
                    resultSet.getString("email"),  // NEW
                    resultSet.getTimestamp("created_at").toLocalDateTime()
            );
        }

        return null;
    }

    public List<UserDTO> getAllUsers() throws SQLException {
        List<UserDTO> users = new ArrayList<>();
        String sql = "SELECT * FROM User ORDER BY created_at DESC";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()) {
            UserDTO user = new UserDTO(
                    resultSet.getInt("user_id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getString("name"),
                    resultSet.getString("role"),
                    resultSet.getString("contact"),
                    resultSet.getString("NIC"),
                    resultSet.getString("email"),  // NEW
                    resultSet.getTimestamp("created_at").toLocalDateTime()
            );
            users.add(user);
        }

        return users;
    }

    public boolean saveUser(UserDTO user) throws SQLException {
        String sql = "INSERT INTO User (username, password, name, role, contact, NIC, email) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, user.getUsername());
        pstm.setString(2, user.getPassword());
        pstm.setString(3, user.getName());
        pstm.setString(4, user.getRole());
        pstm.setString(5, user.getContact());
        pstm.setString(6, user.getNic());
        pstm.setString(7, user.getEmail());  // NEW

        return pstm.executeUpdate() > 0;
    }

    public boolean updateUser(UserDTO user) throws SQLException {
        String sql = "UPDATE User SET username = ?, password = ?, name = ?, role = ?, contact = ?, NIC = ?, email = ? WHERE user_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, user.getUsername());
        pstm.setString(2, user.getPassword());
        pstm.setString(3, user.getName());
        pstm.setString(4, user.getRole());
        pstm.setString(5, user.getContact());
        pstm.setString(6, user.getNic());
        pstm.setString(7, user.getEmail());  // NEW
        pstm.setInt(8, user.getUserId());

        return pstm.executeUpdate() > 0;
    }

    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM User WHERE user_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setInt(1, userId);

        return pstm.executeUpdate() > 0;
    }

    public boolean isUsernameExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM User WHERE username = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, username);

        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    public boolean isUsernameExistsForUpdate(String username, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM User WHERE username = ? AND user_id != ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, username);
        pstm.setInt(2, userId);

        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    // NEW METHOD - Check if email exists
    public boolean isEmailExists(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM User WHERE email = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, email);

        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    // NEW METHOD - Check if email exists for update
    public boolean isEmailExistsForUpdate(String email, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM User WHERE email = ? AND user_id != ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, email);
        pstm.setInt(2, userId);

        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }
}