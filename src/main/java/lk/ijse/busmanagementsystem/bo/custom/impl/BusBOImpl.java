package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.BusBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.BusDAO;
import lk.ijse.busmanagementsystem.dto.BusDTO;
import lk.ijse.busmanagementsystem.entity.Bus;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BusBOImpl implements BusBO {

    private final BusDAO busDAO =
            (BusDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.BUS);

    @Override
    public boolean saveBus(BusDTO dto) throws SQLException, ClassNotFoundException {
        if (isBusNumberExists(dto.getBusNumber())) {
            throw new RuntimeException("Bus number '" + dto.getBusNumber() + "' already exists!");
        }
        return busDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateBus(BusDTO dto) throws SQLException, ClassNotFoundException {
        if (isBusNumberExistsForUpdate(dto.getBusNumber(), dto.getBusId())) {
            throw new RuntimeException("Bus number '" + dto.getBusNumber() + "' already used by another bus!");
        }
        return busDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteBus(Integer busId) throws SQLException, ClassNotFoundException {
        return busDAO.delete(busId);
    }

    @Override
    public BusDTO getBusById(Integer busId) throws SQLException, ClassNotFoundException {
        Bus bus = busDAO.get(busId);
        return bus != null ? toDTO(bus) : null;
    }

    @Override
    public List<BusDTO> getAllBuses() throws SQLException, ClassNotFoundException {
        return busDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
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
        Bus bus = busDAO.getByBusNumber(busNumber);
        return bus != null ? toDTO(bus) : null;
    }

    @Override
    public List<BusDTO> searchBuses(String keyword) throws SQLException, ClassNotFoundException {
        return busDAO.searchBuses(keyword).stream().map(this::toDTO).collect(Collectors.toList());
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
        return busDAO.getAll().stream()
                .anyMatch(b -> b.getBusNumber().equalsIgnoreCase(busNumber));
    }

    @Override
    public boolean isBusNumberExistsForUpdate(String busNumber, Integer busId)
            throws SQLException, ClassNotFoundException {
        return busDAO.getAll().stream()
                .anyMatch(b -> b.getBusNumber().equalsIgnoreCase(busNumber)
                        && b.getBusId() != busId);
    }

    private Bus toEntity(BusDTO dto) {
        Bus bus = new Bus();
        bus.setBusId(dto.getBusId() != null ? dto.getBusId() : 0);
        bus.setBusBrandName(dto.getBusBrandName());
        bus.setBusNumber(dto.getBusNumber());
        bus.setBusType(dto.getBusType());
        bus.setNoOfSeats(dto.getNoOfSeats() != null ? dto.getNoOfSeats() : 0);
        bus.setBusStatus(dto.getBusStatus());
        bus.setManufactureDate(dto.getManufactureDate());
        bus.setInsuranceExpiryDate(dto.getInsuranceExpiryDate());
        bus.setLicenseRenewalDate(dto.getLicenseRenewalDate());
        bus.setCurrentMileage(dto.getCurrentMileage() != null ? dto.getCurrentMileage() : 0);
        bus.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : 0);
        bus.setCreatedAt(dto.getCreatedAt());
        bus.setUpdatedAt(dto.getUpdatedAt());
        return bus;
    }

    private BusDTO toDTO(Bus bus) {
        BusDTO dto = new BusDTO();
        dto.setBusId(bus.getBusId());
        dto.setBusBrandName(bus.getBusBrandName());
        dto.setBusNumber(bus.getBusNumber());
        dto.setBusType(bus.getBusType());
        dto.setNoOfSeats(bus.getNoOfSeats());
        dto.setBusStatus(bus.getBusStatus());
        dto.setManufactureDate(bus.getManufactureDate());
        dto.setInsuranceExpiryDate(bus.getInsuranceExpiryDate());
        dto.setLicenseRenewalDate(bus.getLicenseRenewalDate());
        dto.setCurrentMileage(bus.getCurrentMileage());
        dto.setCreatedBy(bus.getCreatedBy());
        dto.setCreatedAt(bus.getCreatedAt());
        dto.setUpdatedAt(bus.getUpdatedAt());
        return dto;
    }
}