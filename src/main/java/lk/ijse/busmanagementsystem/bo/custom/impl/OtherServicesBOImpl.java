package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.OtherServicesBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.OtherServicesDAO;
import lk.ijse.busmanagementsystem.dto.OtherServicesDTO;
import lk.ijse.busmanagementsystem.entity.OtherServices;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class OtherServicesBOImpl implements OtherServicesBO {

    private final OtherServicesDAO otherServicesDAO =
            (OtherServicesDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.OTHER_SERVICES);

    @Override
    public List<OtherServicesDTO> getAllServices() throws SQLException, ClassNotFoundException {
        return otherServicesDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean saveService(OtherServicesDTO dto) throws SQLException, ClassNotFoundException {
        if (dto.getCost() <= 0) {
            throw new IllegalArgumentException("Service cost must be greater than zero!");
        }
        if (dto.getBusId() != null && !isBusExists(dto.getBusId())) {
            throw new IllegalArgumentException("Bus ID " + dto.getBusId() + " does not exist!");
        }
        if (dto.getTripId() != null && !isTripExists(dto.getTripId())) {
            throw new IllegalArgumentException("Trip ID " + dto.getTripId() + " does not exist!");
        }
        return otherServicesDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateService(OtherServicesDTO dto) throws SQLException, ClassNotFoundException {
        if (dto.getCost() <= 0) {
            throw new IllegalArgumentException("Service cost must be greater than zero!");
        }
        if (dto.getBusId() != null && !isBusExists(dto.getBusId())) {
            throw new IllegalArgumentException("Bus ID " + dto.getBusId() + " does not exist!");
        }
        if (dto.getTripId() != null && !isTripExists(dto.getTripId())) {
            throw new IllegalArgumentException("Trip ID " + dto.getTripId() + " does not exist!");
        }
        return otherServicesDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteService(String serviceId) throws SQLException, ClassNotFoundException {
        return otherServicesDAO.delete(serviceId);
    }

    @Override
    public OtherServicesDTO searchService(String id) throws SQLException, ClassNotFoundException {
        OtherServices e = otherServicesDAO.search(id);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public List<OtherServicesDTO> searchServices(String keyword) throws SQLException, ClassNotFoundException {
        return otherServicesDAO.searchServices(keyword).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getAllBusIds() throws SQLException, ClassNotFoundException {
        return otherServicesDAO.getAllBusIds();
    }

    @Override
    public List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException {
        return otherServicesDAO.getAllTripIds();
    }

    @Override
    public String getBusDetails(int busId) throws SQLException, ClassNotFoundException {
        return otherServicesDAO.getBusDetails(busId);
    }

    @Override
    public String getTripDetails(int tripId) throws SQLException, ClassNotFoundException {
        return otherServicesDAO.getTripDetails(tripId);
    }

    @Override
    public boolean isBusExists(int busId) throws SQLException, ClassNotFoundException {
        return otherServicesDAO.isBusExists(busId);
    }

    @Override
    public boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException {
        return otherServicesDAO.isTripExists(tripId);
    }


    private OtherServices toEntity(OtherServicesDTO dto) {
        OtherServices e = new OtherServices();
        e.setServiceId(dto.getServiceId());
        e.setBusId(dto.getBusId());
        e.setTripId(dto.getTripId());
        e.setServiceName(dto.getServiceName());
        e.setCost(dto.getCost());
        e.setDate(dto.getDate());
        e.setDescription(dto.getDescription());
        e.setCreatedBy(dto.getCreatedBy());
        return e;
    }

    private OtherServicesDTO toDTO(OtherServices e) {
        OtherServicesDTO dto = new OtherServicesDTO();
        dto.setServiceId(e.getServiceId());
        dto.setBusId(e.getBusId());
        dto.setTripId(e.getTripId());
        dto.setServiceName(e.getServiceName());
        dto.setCost(e.getCost());
        dto.setDate(e.getDate());
        dto.setDescription(e.getDescription());
        dto.setCreatedBy(e.getCreatedBy());
        return dto;
    }
}
