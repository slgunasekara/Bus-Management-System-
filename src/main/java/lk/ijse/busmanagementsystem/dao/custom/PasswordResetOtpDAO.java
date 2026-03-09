package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.PasswordResetOtp;

import java.sql.SQLException;

public interface PasswordResetOtpDAO extends CrudDAO<PasswordResetOtp> {


    Integer validateOTP(String email, String otpCode) throws SQLException;

    boolean markOTPAsUsed(String email, String otpCode) throws SQLException;

    int deleteExpiredOTPs() throws SQLException;
}
