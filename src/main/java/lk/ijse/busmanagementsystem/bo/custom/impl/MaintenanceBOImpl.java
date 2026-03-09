package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.MaintenanceBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.MaintenanceDAO;
import lk.ijse.busmanagementsystem.dto.MaintenanceDTO;
import lk.ijse.busmanagementsystem.entity.Maintenance;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class MaintenanceBOImpl implements MaintenanceBO {

    private final MaintenanceDAO maintenanceDAO =
            (MaintenanceDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.MAINTENANCE);

    @Override
    public List<MaintenanceDTO> getAllMaintenance() throws SQLException, ClassNotFoundException {
        return maintenanceDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean saveMaintenance(MaintenanceDTO dto) throws SQLException, ClassNotFoundException {
        if (!isBusExists(dto.getBusId())) {
            throw new IllegalArgumentException("Bus ID " + dto.getBusId() + " does not exist!");
        }
        if (dto.getCost() < 0) {
            throw new IllegalArgumentException("Maintenance cost cannot be negative!");
        }
        return maintenanceDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateMaintenance(MaintenanceDTO dto) throws SQLException, ClassNotFoundException {
        if (!isBusExists(dto.getBusId())) {
            throw new IllegalArgumentException("Bus ID " + dto.getBusId() + " does not exist!");
        }
        if (dto.getCost() < 0) {
            throw new IllegalArgumentException("Maintenance cost cannot be negative!");
        }
        return maintenanceDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteMaintenance(String maintenanceId) throws SQLException, ClassNotFoundException {
        return maintenanceDAO.delete(maintenanceId);
    }

    @Override
    public boolean isBusExists(int busId) throws SQLException, ClassNotFoundException {
        return maintenanceDAO.isBusExists(busId);
    }

    @Override
    public List<Integer> getAllBusIds() throws SQLException, ClassNotFoundException {
        return maintenanceDAO.getAllBusIds();
    }

    @Override
    public String getBusDetails(int busId) throws SQLException, ClassNotFoundException {
        return maintenanceDAO.getBusDetails(busId);
    }

    @Override
    public List<MaintenanceDTO> searchMaintenance(String keyword) throws SQLException, ClassNotFoundException {
        return maintenanceDAO.searchMaintenance(keyword).stream().map(this::toDTO).collect(Collectors.toList());
    }


    private Maintenance toEntity(MaintenanceDTO dto) {
        Maintenance m = new Maintenance();
        m.setMaintId(dto.getMaintId());
        m.setBusId(dto.getBusId());
        m.setMaintenanceType(dto.getMaintenanceType());
        m.setServiceDate(dto.getServiceDate());
        m.setMileage(dto.getMileage());
        m.setCost(dto.getCost());
        m.setTechnician(dto.getTechnician());
        m.setDescription(dto.getDescription());
        m.setCreatedBy(dto.getCreatedBy());
        return m;
    }

    private MaintenanceDTO toDTO(Maintenance m) {
        MaintenanceDTO dto = new MaintenanceDTO();
        dto.setMaintId(m.getMaintId());
        dto.setBusId(m.getBusId());
        dto.setMaintenanceType(m.getMaintenanceType());
        dto.setServiceDate(m.getServiceDate());
        dto.setMileage(m.getMileage());
        dto.setCost(m.getCost());
        dto.setTechnician(m.getTechnician());
        dto.setDescription(m.getDescription());
        dto.setCreatedBy(m.getCreatedBy());
        return dto;
    }
}
