package lk.ijse.busmanagementsystem.model;

import lk.ijse.busmanagementsystem.dto.EventDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventModel {

    public List<EventDTO> getAllEvents() throws SQLException, ClassNotFoundException {
        String sql = "SELECT e.*, b.bus_number FROM Event e " +
                "LEFT JOIN Bus b ON e.bus_id = b.bus_id " +
                "ORDER BY e.event_id DESC";
        ResultSet rst = CrudUtil.execute(sql);
        List<EventDTO> eventList = new ArrayList<>();

        while (rst.next()) {
            EventDTO eventDTO = new EventDTO(
                    rst.getInt("event_id"),
                    rst.getInt("bus_id"),
                    rst.getString("bus_number"),
                    rst.getString("start_location"),
                    rst.getString("end_location"),
                    rst.getDouble("event_value"),
                    rst.getDate("event_date") != null ? rst.getDate("event_date").toLocalDate() : null,
                    rst.getString("customer_name"),
                    rst.getString("customer_contact"),
                    rst.getString("customer_nic"),
                    rst.getString("customer_address"),
                    rst.getString("description"),
                    rst.getBoolean("event_completed"),
                    rst.getInt("created_by"),
                    rst.getTimestamp("created_at") != null ? rst.getTimestamp("created_at").toLocalDateTime() : null,
                    rst.getTimestamp("updated_at") != null ? rst.getTimestamp("updated_at").toLocalDateTime() : null
            );
            eventList.add(eventDTO);
        }
        return eventList;
    }

    public boolean saveEvent(EventDTO eventDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Event(bus_id, start_location, end_location, event_value, " +
                "event_date, customer_name, customer_contact, customer_nic, customer_address, " +
                "description, event_completed, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return CrudUtil.execute(sql,
                eventDTO.getBusId(),
                eventDTO.getStartLocation(),
                eventDTO.getEndLocation(),
                eventDTO.getEventValue(),
                eventDTO.getEventDate() != null ? java.sql.Date.valueOf(eventDTO.getEventDate()) : null,
                eventDTO.getCustomerName(),
                eventDTO.getCustomerContact(),
                eventDTO.getCustomerNic(),
                eventDTO.getCustomerAddress(),
                eventDTO.getDescription(),
                eventDTO.isEventCompleted(),
                eventDTO.getCreatedBy()
        );
    }

    public boolean updateEvent(EventDTO eventDTO) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Event SET bus_id=?, start_location=?, end_location=?, " +
                "event_value=?, event_date=?, customer_name=?, customer_contact=?, " +
                "customer_nic=?, customer_address=?, description=?, event_completed=? " +
                "WHERE event_id=?";

        return CrudUtil.execute(sql,
                eventDTO.getBusId(),
                eventDTO.getStartLocation(),
                eventDTO.getEndLocation(),
                eventDTO.getEventValue(),
                eventDTO.getEventDate() != null ? java.sql.Date.valueOf(eventDTO.getEventDate()) : null,
                eventDTO.getCustomerName(),
                eventDTO.getCustomerContact(),
                eventDTO.getCustomerNic(),
                eventDTO.getCustomerAddress(),
                eventDTO.getDescription(),
                eventDTO.isEventCompleted(),
                eventDTO.getEventId()
        );
    }

    public boolean deleteEvent(String eventId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Event WHERE event_id=?", eventId);
    }

    public EventDTO searchEvent(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT e.*, b.bus_number FROM Event e " +
                "LEFT JOIN Bus b ON e.bus_id = b.bus_id " +
                "WHERE e.event_id=?";
        ResultSet rst = CrudUtil.execute(sql, id);

        if (rst.next()) {
            return new EventDTO(
                    rst.getInt("event_id"),
                    rst.getInt("bus_id"),
                    rst.getString("bus_number"),
                    rst.getString("start_location"),
                    rst.getString("end_location"),
                    rst.getDouble("event_value"),
                    rst.getDate("event_date") != null ? rst.getDate("event_date").toLocalDate() : null,
                    rst.getString("customer_name"),
                    rst.getString("customer_contact"),
                    rst.getString("customer_nic"),
                    rst.getString("customer_address"),
                    rst.getString("description"),
                    rst.getBoolean("event_completed"),
                    rst.getInt("created_by"),
                    rst.getTimestamp("created_at") != null ? rst.getTimestamp("created_at").toLocalDateTime() : null,
                    rst.getTimestamp("updated_at") != null ? rst.getTimestamp("updated_at").toLocalDateTime() : null
            );
        }
        return null;
    }

    public List<EventDTO> searchEvents(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT e.*, b.bus_number FROM Event e " +
                "LEFT JOIN Bus b ON e.bus_id = b.bus_id " +
                "WHERE e.start_location LIKE ? OR e.end_location LIKE ? " +
                "OR e.customer_name LIKE ? OR e.customer_nic LIKE ? OR b.bus_number LIKE ? " +
                "ORDER BY e.event_id DESC";
        String searchPattern = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(sql, searchPattern, searchPattern, searchPattern,
                searchPattern, searchPattern);

        List<EventDTO> eventList = new ArrayList<>();

        while (rst.next()) {
            EventDTO eventDTO = new EventDTO(
                    rst.getInt("event_id"),
                    rst.getInt("bus_id"),
                    rst.getString("bus_number"),
                    rst.getString("start_location"),
                    rst.getString("end_location"),
                    rst.getDouble("event_value"),
                    rst.getDate("event_date") != null ? rst.getDate("event_date").toLocalDate() : null,
                    rst.getString("customer_name"),
                    rst.getString("customer_contact"),
                    rst.getString("customer_nic"),
                    rst.getString("customer_address"),
                    rst.getString("description"),
                    rst.getBoolean("event_completed"),
                    rst.getInt("created_by"),
                    rst.getTimestamp("created_at") != null ? rst.getTimestamp("created_at").toLocalDateTime() : null,
                    rst.getTimestamp("updated_at") != null ? rst.getTimestamp("updated_at").toLocalDateTime() : null
            );
            eventList.add(eventDTO);
        }
        return eventList;
    }

    // Get all active buses for ComboBox
    public List<String[]> getAllActiveBuses() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT bus_id, bus_number FROM Bus WHERE bus_status='Active' ORDER BY bus_number"
        );
        List<String[]> busList = new ArrayList<>();

        while (rst.next()) {
            busList.add(new String[]{
                    String.valueOf(rst.getInt("bus_id")),
                    rst.getString("bus_number")
            });
        }
        return busList;
    }

    // Get bus details by ID
    public String getBusNumberById(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_number FROM Bus WHERE bus_id=?", busId);
        if (rst.next()) {
            return rst.getString("bus_number");
        }
        return null;
    }
}