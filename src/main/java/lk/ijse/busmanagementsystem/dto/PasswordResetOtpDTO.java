package lk.ijse.busmanagementsystem.dto;

import java.time.LocalDateTime;

public class PasswordResetOtpDTO {
    private Integer otpId;
    private Integer userId;
    private String otpCode;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private Boolean isUsed;

    public PasswordResetOtpDTO() {}

    public PasswordResetOtpDTO(Integer otpId, Integer userId, String otpCode,
                               String email, LocalDateTime createdAt,
                               LocalDateTime expiresAt, Boolean isUsed) {
        this.otpId = otpId;
        this.userId = userId;
        this.otpCode = otpCode;
        this.email = email;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.isUsed = isUsed;
    }

    public Integer getOtpId() { return otpId; }
    public void setOtpId(Integer otpId) { this.otpId = otpId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public Boolean getIsUsed() { return isUsed; }
    public void setIsUsed(Boolean isUsed) { this.isUsed = isUsed; }

    @Override
    public String toString() {
        return "PasswordResetOtpDTO{otpId=" + otpId + ", userId=" + userId +
                ", email='" + email + "', expiresAt=" + expiresAt + ", isUsed=" + isUsed + '}';
    }
}