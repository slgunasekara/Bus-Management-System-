package lk.ijse.busmanagementsystem.bo.custom;

import lk.ijse.busmanagementsystem.bo.SuperBO;
import lk.ijse.busmanagementsystem.dto.EventDTO;

import java.sql.SQLException;
import java.util.List;

public interface EventBO extends SuperBO {
    List<EventDTO> getAllEvents() throws SQLException, ClassNotFoundException;
    boolean saveEvent(EventDTO eventDTO) throws SQLException, ClassNotFoundException;
    boolean updateEvent(EventDTO eventDTO) throws SQLException, ClassNotFoundException;
    boolean deleteEvent(String eventId) throws SQLException, ClassNotFoundException;
    EventDTO searchEvent(String id) throws SQLException, ClassNotFoundException;
    List<EventDTO> searchEvents(String keyword) throws SQLException, ClassNotFoundException;
    List<String[]> getAllActiveBuses() throws SQLException, ClassNotFoundException;
    String getBusNumberById(int busId) throws SQLException, ClassNotFoundException;
}