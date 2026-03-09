package lk.ijse.busmanagementsystem.dao.impl;

import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.dto.PasswordResetOtpDTO;
import java.sql.*;
import java.time.LocalDateTime;

public class PasswordResetOtpDAOImpl {

    // Saves OTP to database
    public boolean saveOTP(PasswordResetOtpDTO otp) throws SQLException {
        String sql = "INSERT INTO Password_Reset_OTP (user_id, otp_code, email, expires_at) VALUES (?, ?, ?, ?)";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setInt(1, otp.getUserId());
        pstm.setString(2, otp.getOtpCode());
        pstm.setString(3, otp.getEmail());
        pstm.setTimestamp(4, Timestamp.valueOf(otp.getExpiresAt()));

        return pstm.executeUpdate() > 0;
    }

    //Validates OTP code
    //Returns user_id if valid, null if invalid/expired
    public Integer validateOTP(String email, String otpCode) throws SQLException {
        String sql = "SELECT user_id, expires_at, is_used FROM Password_Reset_OTP " +
                "WHERE email = ? AND otp_code = ? ORDER BY created_at DESC LIMIT 1";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, email);
        pstm.setString(2, otpCode);

        ResultSet rs = pstm.executeQuery();

        if (rs.next()) {
            boolean isUsed = rs.getBoolean("is_used");
            LocalDateTime expiresAt = rs.getTimestamp("expires_at").toLocalDateTime();
            int userId = rs.getInt("user_id");

            // Check if OTP is already used
            if (isUsed) {
                return null;
            }

            // Check if OTP is expired
            if (LocalDateTime.now().isAfter(expiresAt)) {
                return null;
            }

            return userId;
        }

        return null;
    }

    //Marks OTP as used
    public boolean markOTPAsUsed(String email, String otpCode) throws SQLException {
        String sql = "UPDATE Password_Reset_OTP SET is_used = TRUE WHERE email = ? AND otp_code = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, email);
        pstm.setString(2, otpCode);

        return pstm.executeUpdate() > 0;
    }

    //Deletes all OTPs for a user (cleanup)
    public boolean deleteUserOTPs(int userId) throws SQLException {
        String sql = "DELETE FROM Password_Reset_OTP WHERE user_id = ?";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setInt(1, userId);

        return pstm.executeUpdate() > 0;
    }

    // Deletes expired OTPs (maintenance)
    public int deleteExpiredOTPs() throws SQLException {
        String sql = "DELETE FROM Password_Reset_OTP WHERE expires_at < NOW() OR is_used = TRUE";

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        return pstm.executeUpdate();
    }
}