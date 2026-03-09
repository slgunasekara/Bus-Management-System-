package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.TripDTO;

import java.sql.SQLException;
import java.util.List;

public interface TripBO extends SuperBO {
    List<TripDTO> getAllTrips() throws SQLException, ClassNotFoundException;
    boolean saveTrip(TripDTO tripDTO) throws SQLException, ClassNotFoundException;
    boolean updateTrip(TripDTO tripDTO) throws SQLException, ClassNotFoundException;
    boolean deleteTrip(String tripId) throws SQLException, ClassNotFoundException;
    TripDTO searchTrip(String id) throws SQLException, ClassNotFoundException;
    boolean isBusExists(int busId) throws SQLException, ClassNotFoundException;
    List<TripDTO> searchTrips(String keyword) throws SQLException, ClassNotFoundException;
}