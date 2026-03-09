package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.PasswordResetOtpDTO;

import java.sql.SQLException;

public interface PasswordResetOtpBO extends SuperBO {
    boolean saveOTP(PasswordResetOtpDTO otp) throws SQLException, ClassNotFoundException;
    Integer validateOTP(String email, String otpCode) throws SQLException;
    boolean markOTPAsUsed(String email, String otpCode) throws SQLException;
    boolean deleteUserOTPs(int userId) throws SQLException, ClassNotFoundException;
    int deleteExpiredOTPs() throws SQLException;
}