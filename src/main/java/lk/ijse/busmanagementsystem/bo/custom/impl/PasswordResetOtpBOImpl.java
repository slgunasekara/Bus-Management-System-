package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.PasswordResetOtpBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.PasswordResetOtpDAO;
import lk.ijse.busmanagementsystem.dto.PasswordResetOtpDTO;
import lk.ijse.busmanagementsystem.entity.PasswordResetOtp;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class PasswordResetOtpBOImpl implements PasswordResetOtpBO {

    private final PasswordResetOtpDAO passwordResetOtpDAO =
            (PasswordResetOtpDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PASSWORD_RESET_OTP);

    @Override
    public boolean saveOTP(PasswordResetOtpDTO dto) throws SQLException, ClassNotFoundException {
        if (dto.getExpiresAt() == null) {
            dto.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        }
        dto.setIsUsed(false);
        return passwordResetOtpDAO.save(toEntity(dto));
    }

    @Override
    public Integer validateOTP(String email, String otpCode) throws SQLException {
        return passwordResetOtpDAO.validateOTP(email, otpCode);
    }

    @Override
    public boolean markOTPAsUsed(String email, String otpCode) throws SQLException {
        return passwordResetOtpDAO.markOTPAsUsed(email, otpCode);
    }

    @Override
    public boolean deleteUserOTPs(int userId) throws SQLException, ClassNotFoundException {
        return passwordResetOtpDAO.delete(userId);
    }

    @Override
    public int deleteExpiredOTPs() throws SQLException {
        return passwordResetOtpDAO.deleteExpiredOTPs();
    }


    private PasswordResetOtp toEntity(PasswordResetOtpDTO dto) {
        PasswordResetOtp e = new PasswordResetOtp();
        e.setOtpId(dto.getOtpId());
        e.setUserId(dto.getUserId());
        e.setOtpCode(dto.getOtpCode());
        e.setEmail(dto.getEmail());
        e.setExpiresAt(dto.getExpiresAt());
        e.setIsUsed(dto.getIsUsed());
        return e;
    }
}
