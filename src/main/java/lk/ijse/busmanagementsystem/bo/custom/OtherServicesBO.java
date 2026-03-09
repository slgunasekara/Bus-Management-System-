package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.OtherServicesDTO;

import java.sql.SQLException;
import java.util.List;

public interface OtherServicesBO extends SuperBO {
    List<OtherServicesDTO> getAllServices() throws SQLException, ClassNotFoundException;
    boolean saveService(OtherServicesDTO serviceDTO) throws SQLException, ClassNotFoundException;
    boolean updateService(OtherServicesDTO serviceDTO) throws SQLException, ClassNotFoundException;
    boolean deleteService(String serviceId) throws SQLException, ClassNotFoundException;
    OtherServicesDTO searchService(String id) throws SQLException, ClassNotFoundException;
    List<OtherServicesDTO> searchServices(String keyword) throws SQLException, ClassNotFoundException;
    List<Integer> getAllBusIds() throws SQLException, ClassNotFoundException;
    List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException;
    String getBusDetails(int busId) throws SQLException, ClassNotFoundException;
    String getTripDetails(int tripId) throws SQLException, ClassNotFoundException;
    boolean isBusExists(int busId) throws SQLException, ClassNotFoundException;
    boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException;
}