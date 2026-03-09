package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.OtherServices;

import java.sql.SQLException;
import java.util.List;

public interface OtherServicesDAO extends CrudDAO<OtherServices> {

    List<OtherServices> searchServices(String keyword)
            throws SQLException, ClassNotFoundException;

    List<Integer> getAllBusIds() throws SQLException, ClassNotFoundException;
    List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException;

    String getBusDetails(int busId) throws SQLException, ClassNotFoundException;
    String getTripDetails(int tripId) throws SQLException, ClassNotFoundException;

    boolean isBusExists(int busId) throws SQLException, ClassNotFoundException;
    boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException;
}
