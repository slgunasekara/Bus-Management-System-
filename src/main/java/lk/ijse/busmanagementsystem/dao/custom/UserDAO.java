package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.User;

import java.sql.SQLException;

public interface UserDAO extends CrudDAO<User> {


    User authenticateUser(String username, String password)
            throws SQLException;

    boolean isUsernameExistsForUpdate(String username, int userId)
            throws SQLException;

    boolean isEmailExists(String email)
            throws SQLException;

    boolean isEmailExistsForUpdate(String email, int userId)
            throws SQLException;

    User getUserByEmail(String email)
            throws SQLException;

    boolean updatePassword(int userId, String newPassword)
            throws SQLException;

    User getUserByUsername(String username)
            throws SQLException;
}
