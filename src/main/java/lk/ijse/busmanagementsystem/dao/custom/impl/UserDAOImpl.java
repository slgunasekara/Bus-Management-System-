package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.UserDAO;
import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public List<User> getAll() throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT * FROM User ORDER BY created_at DESC");
        ResultSet rs = pstm.executeQuery();
        List<User> list = new ArrayList<>();
        while (rs.next()) list.add(mapResultSet(rs));
        return list;
    }

    @Override
    public boolean save(User user) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement(
                "INSERT INTO User(username, password, name, role, contact, NIC, email) VALUES (?, ?, ?, ?, ?, ?, ?)");
        pstm.setString(1, user.getUsername()); pstm.setString(2, user.getPassword());
        pstm.setString(3, user.getName()); pstm.setString(4, user.getRole());
        pstm.setString(5, user.getContact()); pstm.setString(6, user.getNic());
        pstm.setString(7, user.getEmail());
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean update(User user) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement(
                "UPDATE User SET username=?, password=?, name=?, role=?, contact=?, NIC=?, email=? WHERE user_id=?");
        pstm.setString(1, user.getUsername()); pstm.setString(2, user.getPassword());
        pstm.setString(3, user.getName()); pstm.setString(4, user.getRole());
        pstm.setString(5, user.getContact()); pstm.setString(6, user.getNic());
        pstm.setString(7, user.getEmail()); pstm.setInt(8, user.getUserId());
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(String userId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("DELETE FROM User WHERE user_id=?");
        pstm.setInt(1, Integer.parseInt(userId));
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(int userId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("DELETE FROM User WHERE user_id=?");
        pstm.setInt(1, userId);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean exists(String username) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT COUNT(*) FROM User WHERE username=?");
        pstm.setString(1, username);
        ResultSet rs = pstm.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    }

    @Override
    public boolean exists(int userId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT user_id FROM User WHERE user_id=?");
        pstm.setInt(1, userId);
        return pstm.executeQuery().next();
    }

    @Override
    public User search(String id) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT * FROM User WHERE user_id=?");
        pstm.setInt(1, Integer.parseInt(id));
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) return mapResultSet(rs);
        return null;
    }

    @Override
    public User authenticateUser(String username, String password) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT * FROM User WHERE username=? AND password=?");
        pstm.setString(1, username); pstm.setString(2, password);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) return mapResultSet(rs);
        return null;
    }

    @Override
    public boolean isUsernameExistsForUpdate(String username, int userId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT COUNT(*) FROM User WHERE username=? AND user_id!=?");
        pstm.setString(1, username); pstm.setInt(2, userId);
        ResultSet rs = pstm.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    }

    @Override
    public boolean isEmailExists(String email) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT COUNT(*) FROM User WHERE email=?");
        pstm.setString(1, email);
        ResultSet rs = pstm.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    }

    @Override
    public boolean isEmailExistsForUpdate(String email, int userId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT COUNT(*) FROM User WHERE email=? AND user_id!=?");
        pstm.setString(1, email); pstm.setInt(2, userId);
        ResultSet rs = pstm.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT * FROM User WHERE email=?");
        pstm.setString(1, email);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) return mapResultSet(rs);
        return null;
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("UPDATE User SET password=? WHERE user_id=?");
        pstm.setString(1, newPassword); pstm.setInt(2, userId);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT * FROM User WHERE username=?");
        pstm.setString(1, username);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) return mapResultSet(rs);
        return null;
    }

    private Connection conn() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    private User mapResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setName(rs.getString("name"));
        user.setRole(rs.getString("role"));
        user.setContact(rs.getString("contact"));
        user.setNic(rs.getString("NIC"));
        user.setEmail(rs.getString("email"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return user;
    }
}
