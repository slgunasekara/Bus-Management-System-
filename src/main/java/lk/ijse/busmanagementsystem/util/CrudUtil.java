package lk.ijse.busmanagementsystem.util;

import lk.ijse.busmanagementsystem.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    public static <T> T execute(String sql, Object... args) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        for (int i = 0; i < args.length; i++) {
            pstm.setObject((i + 1), args[i]);
        }

        if (sql.startsWith("SELECT") || sql.startsWith("select")) {
            return (T) pstm.executeQuery();
        } else {
            return (T) (Boolean) (pstm.executeUpdate() > 0);
        }
    }
}

//----------------------------------------------------------------------------

//import lk.ijse.busSystem.db.DBConnection;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;

//public class CrudUtil {
//
//    public static boolean execute(String sql, Object... params) throws SQLException {
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        for (int i = 0; i < params.length; i++) {
//            pstm.setObject(i + 1, params[i]);
//        }
//        return pstm.executeUpdate() > 0;
//    }
//
//    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        for (int i = 0; i < params.length; i++) {
//            pstm.setObject(i + 1, params[i]);
//        }
//        return pstm.executeQuery();
//    }
//}