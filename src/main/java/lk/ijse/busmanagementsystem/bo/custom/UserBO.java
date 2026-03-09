package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.UserDTO;

import java.sql.SQLException;
import java.util.List;

public interface UserBO extends SuperBO {
    UserDTO authenticateUser(String username, String password) throws SQLException;
    List<UserDTO> getAllUsers() throws SQLException, ClassNotFoundException;
    boolean saveUser(UserDTO user) throws SQLException, ClassNotFoundException;
    boolean updateUser(UserDTO user) throws SQLException, ClassNotFoundException;
    boolean deleteUser(int userId) throws SQLException, ClassNotFoundException;
    boolean isUsernameExists(String username) throws SQLException, ClassNotFoundException;
    boolean isUsernameExistsForUpdate(String username, int userId) throws SQLException;
    boolean isEmailExists(String email) throws SQLException;
    boolean isEmailExistsForUpdate(String email, int userId) throws SQLException;
    UserDTO getUserByEmail(String email) throws SQLException;
    boolean updatePassword(int userId, String newPassword) throws SQLException;
    UserDTO getUserByUsername(String username) throws SQLException;
}