package lk.ijse.busmanagementsystem.entity;

import java.time.LocalDateTime;

public class User {

    private Integer userId;
    private String username;
    private String password;
    private String name;
    private String role;
    private String contact;
    private String nic;
    private String email;
    private LocalDateTime createdAt;


    public User() {}

    public User(Integer userId, String username, String password, String name,
                   String role, String contact, String nic, String email, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.contact = contact;
        this.nic = nic;
        this.email = email;
        this.createdAt = createdAt;
    }


    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }
    public String getEmail() { return email; }  // NEW
    public void setEmail(String email) { this.email = email; }  // NEW
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
