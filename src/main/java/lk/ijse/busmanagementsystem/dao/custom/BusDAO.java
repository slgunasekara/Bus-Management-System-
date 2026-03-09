package lk.ijse.busmanagementsystem.dao;

import lk.ijse.busmanagementsystem.dto.BusDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface BusDAO {
    public List<BusDTO> getAllBuses() throws SQLException, ClassNotFoundException ;

    public boolean saveBus(BusDTO busDTO) throws SQLException, ClassNotFoundException ;

    public boolean updateBus(BusDTO busDTO) throws SQLException, ClassNotFoundException ;

    public boolean deleteBus(String busId) throws SQLException, ClassNotFoundException ;

    public BusDTO searchBus(String id) throws SQLException, ClassNotFoundException ;

    public boolean isBusNumberExists(String busNumber) throws SQLException, ClassNotFoundException ;

    public boolean isBusNumberExistsForUpdate(String busNumber, int busId) throws SQLException, ClassNotFoundException ;

    public List<BusDTO> searchBuses(String keyword) throws SQLException, ClassNotFoundException ;
}
