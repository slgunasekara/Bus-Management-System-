package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.dto.BusDTO;

import java.sql.SQLException;
import java.util.List;

public interface BusDAO extends CrudDAO<BusDTO> {

    BusDTO get(Integer id) throws SQLException, ClassNotFoundException;

    Integer getNextId() throws SQLException, ClassNotFoundException;

    List<String> getAllBusNumbers() throws SQLException, ClassNotFoundException;

    BusDTO getByBusNumber(String busNumber) throws SQLException, ClassNotFoundException;

    List<BusDTO> searchBuses(String keyword) throws SQLException, ClassNotFoundException;

    int getCountByStatus(String status) throws SQLException, ClassNotFoundException;

    int getTotalCount() throws SQLException, ClassNotFoundException;
}