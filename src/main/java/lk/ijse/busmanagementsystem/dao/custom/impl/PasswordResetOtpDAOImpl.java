package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.PasswordResetOtpDAO;
import lk.ijse.busmanagementsystem.db.DBConnection;
import lk.ijse.busmanagementsystem.entity.PasswordResetOtp;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PasswordResetOtpDAOImpl implements PasswordResetOtpDAO {

    @Override
    public boolean save(PasswordResetOtp otp) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement(
                "INSERT INTO Password_Reset_OTP(user_id, otp_code, email, expires_at) VALUES (?, ?, ?, ?)");
        pstm.setInt(1, otp.getUserId());
        pstm.setString(2, otp.getOtpCode());
        pstm.setString(3, otp.getEmail());
        pstm.setTimestamp(4, Timestamp.valueOf(otp.getExpiresAt()));
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean update(PasswordResetOtp otp) throws SQLException { return false; } // Not applicable

    @Override
    public boolean delete(String email) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("DELETE FROM Password_Reset_OTP WHERE email=?");
        pstm.setString(1, email);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean delete(int userId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("DELETE FROM Password_Reset_OTP WHERE user_id=?");
        pstm.setInt(1, userId);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public boolean exists(String email) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT otp_id FROM Password_Reset_OTP WHERE email=?");
        pstm.setString(1, email);
        return pstm.executeQuery().next();
    }

    @Override
    public boolean exists(int userId) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT otp_id FROM Password_Reset_OTP WHERE user_id=?");
        pstm.setInt(1, userId);
        return pstm.executeQuery().next();
    }

    @Override
    public PasswordResetOtp search(String id) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT * FROM Password_Reset_OTP WHERE otp_id=?");
        pstm.setInt(1, Integer.parseInt(id));
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) return mapResultSet(rs);
        return null;
    }

    @Override
    public List<PasswordResetOtp> getAll() throws SQLException {
        PreparedStatement pstm = conn().prepareStatement("SELECT * FROM Password_Reset_OTP");
        ResultSet rs = pstm.executeQuery();
        List<PasswordResetOtp> list = new ArrayList<>();
        while (rs.next()) list.add(mapResultSet(rs));
        return list;
    }

    @Override
    public Integer validateOTP(String email, String otpCode) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement(
                "SELECT user_id, expires_at, is_used FROM Password_Reset_OTP WHERE email=? AND otp_code=? ORDER BY created_at DESC LIMIT 1");
        pstm.setString(1, email); pstm.setString(2, otpCode);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            if (rs.getBoolean("is_used")) return null;
            if (LocalDateTime.now().isAfter(rs.getTimestamp("expires_at").toLocalDateTime())) return null;
            return rs.getInt("user_id");
        }
        return null;
    }

    @Override
    public boolean markOTPAsUsed(String email, String otpCode) throws SQLException {
        PreparedStatement pstm = conn().prepareStatement(
                "UPDATE Password_Reset_OTP SET is_used=TRUE WHERE email=? AND otp_code=?");
        pstm.setString(1, email); pstm.setString(2, otpCode);
        return pstm.executeUpdate() > 0;
    }

    @Override
    public int deleteExpiredOTPs() throws SQLException {
        PreparedStatement pstm = conn().prepareStatement(
                "DELETE FROM Password_Reset_OTP WHERE expires_at < NOW() OR is_used=TRUE");
        return pstm.executeUpdate();
    }

    private Connection conn() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }

    private PasswordResetOtp mapResultSet(ResultSet rs) throws SQLException {
        PasswordResetOtp otp = new PasswordResetOtp();
        otp.setOtpId(rs.getInt("otp_id"));
        otp.setUserId(rs.getInt("user_id"));
        otp.setOtpCode(rs.getString("otp_code"));
        otp.setEmail(rs.getString("email"));
        otp.setExpiresAt(rs.getTimestamp("expires_at").toLocalDateTime());
        otp.setIsUsed(rs.getBoolean("is_used"));
        otp.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return otp;
    }
}
