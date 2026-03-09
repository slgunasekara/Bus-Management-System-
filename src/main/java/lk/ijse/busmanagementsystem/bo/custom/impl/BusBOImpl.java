package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.BusBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.BusDAO;
import lk.ijse.busmanagementsystem.dto.BusDTO;

import java.sql.SQLException;
import java.util.List;

public class BusBOImpl implements BusBO {

    private final BusDAO busDAO = (BusDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.BUS);

    @Override
    public boolean saveBus(BusDTO dto) throws SQLException, ClassNotFoundException {
        if (isBusNumberExists(dto.getBusNumber())) {
            throw new RuntimeException("Bus number '" + dto.getBusNumber() + "' already exists!");
        }
        return busDAO.save(dto);
    }

    @Override
    public boolean updateBus(BusDTO dto) throws SQLException, ClassNotFoundException {
        if (isBusNumberExistsForUpdate(dto.getBusNumber(), dto.getBusId())) {
            throw new RuntimeException("Bus number '" + dto.getBusNumber() + "' already used by another bus!");
        }
        return busDAO.update(dto);
    }

    @Override
    public boolean deleteBus(Integer busId) throws SQLException, ClassNotFoundException {
        return busDAO.delete(busId);
    }

    @Override
    public BusDTO getBusById(Integer busId) throws SQLException, ClassNotFoundException {
        return busDAO.get(busId);
    }

    @Override
    public List<BusDTO> getAllBuses() throws SQLException, ClassNotFoundException {
        return busDAO.getAll();
    }

    @Override
    public Integer getNextBusId() throws SQLException, ClassNotFoundException {
        return busDAO.getNextId();
    }

    @Override
    public List<String> getAllBusNumbers() throws SQLException, ClassNotFoundException {
        return busDAO.getAllBusNumbers();
    }

    @Override
    public BusDTO getBusByNumber(String busNumber) throws SQLException, ClassNotFoundException {
        return busDAO.getByBusNumber(busNumber);
    }

    @Override
    public List<BusDTO> searchBuses(String keyword) throws SQLException, ClassNotFoundException {
        return busDAO.searchBuses(keyword);
    }

    @Override
    public int getActiveBusCount() throws SQLException, ClassNotFoundException {
        return busDAO.getCountByStatus("Active");
    }

    @Override
    public int getMaintenanceBusCount() throws SQLException, ClassNotFoundException {
        return busDAO.getCountByStatus("Maintenance");
    }

    @Override
    public int getTotalBusCount() throws SQLException, ClassNotFoundException {
        return busDAO.getTotalCount();
    }

    @Override
    public boolean isBusNumberExists(String busNumber) throws SQLException, ClassNotFoundException {
        List<BusDTO> all = busDAO.getAll();
        return all.stream()
                .anyMatch(b -> b.getBusNumber().equalsIgnoreCase(busNumber));
    }

    @Override
    public boolean isBusNumberExistsForUpdate(String busNumber, Integer busId)
            throws SQLException, ClassNotFoundException {
        List<BusDTO> all = busDAO.getAll();
        return all.stream()
                .anyMatch(b -> b.getBusNumber().equalsIgnoreCase(busNumber)
                        && !b.getBusId().equals(busId));
    }
}