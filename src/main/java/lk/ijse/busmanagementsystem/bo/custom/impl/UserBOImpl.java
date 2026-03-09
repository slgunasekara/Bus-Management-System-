package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.UserBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.UserDAO;
import lk.ijse.busmanagementsystem.dto.UserDTO;
import lk.ijse.busmanagementsystem.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserBOImpl implements UserBO {

    private final UserDAO userDAO =
            (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    @Override
    public UserDTO authenticateUser(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty!");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty!");
        }
        User e = userDAO.authenticateUser(username, password);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public List<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException {
        return userDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean saveUser(UserDTO dto) throws SQLException, ClassNotFoundException {
        if (isUsernameExists(dto.getUsername())) {
            throw new IllegalArgumentException("Username '" + dto.getUsername() + "' already exists!");
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty() && isEmailExists(dto.getEmail())) {
            throw new IllegalArgumentException("Email '" + dto.getEmail() + "' already exists!");
        }
        return userDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateUser(UserDTO dto) throws SQLException, ClassNotFoundException {
        if (isUsernameExistsForUpdate(dto.getUsername(), dto.getUserId())) {
            throw new IllegalArgumentException("Username '" + dto.getUsername() + "' already exists!");
        }
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()
                && isEmailExistsForUpdate(dto.getEmail(), dto.getUserId())) {
            throw new IllegalArgumentException("Email '" + dto.getEmail() + "' already exists!");
        }
        return userDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteUser(int userId) throws SQLException, ClassNotFoundException {
        return userDAO.delete(userId);
    }

    @Override
    public boolean isUsernameExists(String username) throws SQLException, ClassNotFoundException {
        return userDAO.exists(username);
    }

    @Override
    public boolean isUsernameExistsForUpdate(String username, int userId) throws SQLException {
        return userDAO.isUsernameExistsForUpdate(username, userId);
    }

    @Override
    public boolean isEmailExists(String email) throws SQLException {
        return userDAO.isEmailExists(email);
    }

    @Override
    public boolean isEmailExistsForUpdate(String email, int userId) throws SQLException {
        return userDAO.isEmailExistsForUpdate(email, userId);
    }

    @Override
    public UserDTO getUserByEmail(String email) throws SQLException {
        User e = userDAO.getUserByEmail(email);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty!");
        }
        return userDAO.updatePassword(userId, newPassword);
    }

    @Override
    public UserDTO getUserByUsername(String username) throws SQLException {
        User e = userDAO.getUserByUsername(username);
        return e != null ? toDTO(e) : null;
    }


    private User toEntity(UserDTO dto) {
        User e = new User();
        e.setUserId(dto.getUserId());
        e.setUsername(dto.getUsername());
        e.setPassword(dto.getPassword());
        e.setName(dto.getName());
        e.setRole(dto.getRole());
        e.setContact(dto.getContact());
        e.setNic(dto.getNic());
        e.setEmail(dto.getEmail());
        return e;
    }

    private UserDTO toDTO(User e) {
        UserDTO dto = new UserDTO();
        dto.setUserId(e.getUserId());
        dto.setUsername(e.getUsername());
        dto.setPassword(e.getPassword());
        dto.setName(e.getName());
        dto.setRole(e.getRole());
        dto.setContact(e.getContact());
        dto.setNic(e.getNic());
        dto.setEmail(e.getEmail());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }
}
