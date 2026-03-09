package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.BusDTO;

import java.sql.SQLException;
import java.util.List;

public interface BusBO extends SuperBO {

    boolean saveBus(BusDTO dto) throws SQLException, ClassNotFoundException;

    boolean updateBus(BusDTO dto) throws SQLException, ClassNotFoundException;

    boolean deleteBus(Integer busId) throws SQLException, ClassNotFoundException;

    BusDTO getBusById(Integer busId) throws SQLException, ClassNotFoundException;

    List<BusDTO> getAllBuses() throws SQLException, ClassNotFoundException;

    Integer getNextBusId() throws SQLException, ClassNotFoundException;

    List<String> getAllBusNumbers() throws SQLException, ClassNotFoundException;

    BusDTO getBusByNumber(String busNumber) throws SQLException, ClassNotFoundException;

    List<BusDTO> searchBuses(String keyword) throws SQLException, ClassNotFoundException;

    int getActiveBusCount() throws SQLException, ClassNotFoundException;

    int getMaintenanceBusCount() throws SQLException, ClassNotFoundException;

    int getTotalBusCount() throws SQLException, ClassNotFoundException;

    // Validation
    boolean isBusNumberExists(String busNumber) throws SQLException, ClassNotFoundException;

    boolean isBusNumberExistsForUpdate(String busNumber, Integer busId) throws SQLException, ClassNotFoundException;
}