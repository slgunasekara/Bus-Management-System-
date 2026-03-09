package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.EventBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.EventDAO;
import lk.ijse.busmanagementsystem.dto.EventDTO;
import lk.ijse.busmanagementsystem.entity.Event;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class EventBOImpl implements EventBO {

    private final EventDAO eventDAO =
            (EventDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.EVENT);

    @Override
    public List<EventDTO> getAllEvents() throws SQLException, ClassNotFoundException {
        return eventDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean saveEvent(EventDTO dto) throws SQLException, ClassNotFoundException {
        if (dto.getEventValue() <= 0) {
            throw new IllegalArgumentException("Event value must be greater than zero!");
        }
        return eventDAO.save(toEntity(dto));
    }

    @Override
    public boolean updateEvent(EventDTO dto) throws SQLException, ClassNotFoundException {
        if (dto.getEventValue() <= 0) {
            throw new IllegalArgumentException("Event value must be greater than zero!");
        }
        return eventDAO.update(toEntity(dto));
    }

    @Override
    public boolean deleteEvent(String eventId) throws SQLException, ClassNotFoundException {
        return eventDAO.delete(eventId);
    }

    @Override
    public EventDTO searchEvent(String id) throws SQLException, ClassNotFoundException {
        Event e = eventDAO.search(id);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public List<EventDTO> searchEvents(String keyword) throws SQLException, ClassNotFoundException {
        return eventDAO.searchEvents(keyword).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<String[]> getAllActiveBuses() throws SQLException, ClassNotFoundException {
        return eventDAO.getAllActiveBuses();
    }

    @Override
    public String getBusNumberById(int busId) throws SQLException, ClassNotFoundException {
        return eventDAO.getBusNumberById(busId);
    }


    private Event toEntity(EventDTO dto) {
        Event e = new Event();
        e.setEventId(dto.getEventId());
        e.setBusId(dto.getBusId());
        e.setStartLocation(dto.getStartLocation());
        e.setEndLocation(dto.getEndLocation());
        e.setEventValue(dto.getEventValue());
        e.setEventDate(dto.getEventDate());
        e.setCustomerName(dto.getCustomerName());
        e.setCustomerContact(dto.getCustomerContact());
        e.setCustomerNic(dto.getCustomerNic());
        e.setCustomerAddress(dto.getCustomerAddress());
        e.setDescription(dto.getDescription());
        e.setEventCompleted(dto.isEventCompleted());
        e.setCreatedBy(dto.getCreatedBy());
        return e;
    }

    private EventDTO toDTO(Event e) {
        EventDTO dto = new EventDTO();
        dto.setEventId(e.getEventId());
        dto.setBusId(e.getBusId());
        dto.setBusNumber(e.getBusNumber());
        dto.setStartLocation(e.getStartLocation());
        dto.setEndLocation(e.getEndLocation());
        dto.setEventValue(e.getEventValue());
        dto.setEventDate(e.getEventDate());
        dto.setCustomerName(e.getCustomerName());
        dto.setCustomerContact(e.getCustomerContact());
        dto.setCustomerNic(e.getCustomerNic());
        dto.setCustomerAddress(e.getCustomerAddress());
        dto.setDescription(e.getDescription());
        dto.setEventCompleted(e.isEventCompleted());
        dto.setCreatedBy(e.getCreatedBy());
        dto.setCreatedAt(e.getCreatedAt());
        dto.setUpdatedAt(e.getUpdatedAt());
        return dto;
    }
}
