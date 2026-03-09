package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.TripBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.TripDAO;
import lk.ijse.busmanagementsystem.dto.TripDTO;
import lk.ijse.busmanagementsystem.entity.Trip;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class TripBOImpl implements TripBO {

    private final TripDAO tripDAO =
            (TripDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.TRIP);

    @Override
    public List<TripDTO> getAllTrips() throws SQLException, ClassNotFoundException {
        return tripDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean saveTrip(TripDTO dto) throws SQLException, ClassNotFoundException {
        if (!isBusExists(dto.getBusId())) {
            throw new IllegalArgumentException("Bus ID " + dto.getBusId() + " does not exist!");
        }
        if (dto.getTotalIncome() < 0) {
            throw new IllegalArgumentException("Total income cannot be negative!");
        }
        return tripDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateTrip(TripDTO dto) throws SQLException, ClassNotFoundException {
        if (!isBusExists(dto.getBusId())) {
            throw new IllegalArgumentException("Bus ID " + dto.getBusId() + " does not exist!");
        }
        if (dto.getTotalIncome() < 0) {
            throw new IllegalArgumentException("Total income cannot be negative!");
        }
        return tripDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteTrip(String tripId) throws SQLException, ClassNotFoundException {
        return tripDAO.delete(tripId);
    }

    @Override
    public TripDTO searchTrip(String id) throws SQLException, ClassNotFoundException {
        Trip e = tripDAO.search(id);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public boolean isBusExists(int busId) throws SQLException, ClassNotFoundException {
        return tripDAO.isBusExists(busId);
    }

    @Override
    public List<TripDTO> searchTrips(String keyword) throws SQLException, ClassNotFoundException {
        return tripDAO.searchTrips(keyword).stream().map(this::toDTO).collect(Collectors.toList());
    }


    private Trip toEntity(TripDTO dto) {
        Trip e = new Trip();
        e.setTripId(dto.getTripId());
        e.setBusId(dto.getBusId());
        e.setTripCategory(dto.getTripCategory());
        e.setStartLocation(dto.getStartLocation());
        e.setEndLocation(dto.getEndLocation());
        e.setDistance(dto.getDistance());
        e.setTotalIncome(dto.getTotalIncome());
        e.setTripDate(dto.getTripDate());
        e.setDescription(dto.getDescription());
        e.setCreatedBy(dto.getCreatedBy());
        return e;
    }

    private TripDTO toDTO(Trip e) {
        TripDTO dto = new TripDTO();
        dto.setTripId(e.getTripId());
        dto.setBusId(e.getBusId());
        dto.setTripCategory(e.getTripCategory());
        dto.setStartLocation(e.getStartLocation());
        dto.setEndLocation(e.getEndLocation());
        dto.setDistance(e.getDistance());
        dto.setTotalIncome(e.getTotalIncome());
        dto.setTripDate(e.getTripDate());
        dto.setDescription(e.getDescription());
        dto.setCreatedBy(e.getCreatedBy());
        dto.setCreatedByUsername(e.getCreatedByUsername());
        return dto;
    }
}
