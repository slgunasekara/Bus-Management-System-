package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.EventDAO;
import lk.ijse.busmanagementsystem.entity.Event;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAOImpl implements EventDAO {

    private static final String SELECT_WITH_BUS =
            "SELECT e.*, b.bus_number FROM Event e LEFT JOIN Bus b ON e.bus_id = b.bus_id ";

    @Override
    public List<Event> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(SELECT_WITH_BUS + "ORDER BY e.event_id DESC");
        List<Event> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public boolean save(Event event) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Event(bus_id, start_location, end_location, event_value, event_date, " +
                        "customer_name, customer_contact, customer_nic, customer_address, description, event_completed, created_by) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                event.getBusId(), event.getStartLocation(), event.getEndLocation(), event.getEventValue(),
                event.getEventDate() != null ? java.sql.Date.valueOf(event.getEventDate()) : null,
                event.getCustomerName(), event.getCustomerContact(), event.getCustomerNic(),
                event.getCustomerAddress(), event.getDescription(), event.isEventCompleted(), event.getCreatedBy()
        );
    }

    @Override
    public boolean update(Event event) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Event SET bus_id=?, start_location=?, end_location=?, event_value=?, event_date=?, " +
                        "customer_name=?, customer_contact=?, customer_nic=?, customer_address=?, description=?, event_completed=? " +
                        "WHERE event_id=?",
                event.getBusId(), event.getStartLocation(), event.getEndLocation(), event.getEventValue(),
                event.getEventDate() != null ? java.sql.Date.valueOf(event.getEventDate()) : null,
                event.getCustomerName(), event.getCustomerContact(), event.getCustomerNic(),
                event.getCustomerAddress(), event.getDescription(), event.isEventCompleted(), event.getEventId()
        );
    }

    @Override
    public boolean delete(String eventId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Event WHERE event_id=?", eventId);
    }

    @Override
    public boolean delete(int eventId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Event WHERE event_id=?", eventId);
    }

    @Override
    public boolean exists(String eventId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT event_id FROM Event WHERE event_id=?", eventId);
        return rst.next();
    }

    @Override
    public boolean exists(int eventId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT event_id FROM Event WHERE event_id=?", eventId);
        return rst.next();
    }

    @Override
    public Event search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(SELECT_WITH_BUS + "WHERE e.event_id=?", id);
        if (rst.next()) return mapResultSet(rst);
        return null;
    }

    @Override
    public List<Event> searchEvents(String keyword) throws SQLException, ClassNotFoundException {
        String p = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(
                SELECT_WITH_BUS + "WHERE e.start_location LIKE ? OR e.end_location LIKE ? OR " +
                        "e.customer_name LIKE ? OR e.customer_nic LIKE ? OR b.bus_number LIKE ? ORDER BY e.event_id DESC",
                p, p, p, p, p);
        List<Event> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public List<String[]> getAllActiveBuses() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id, bus_number FROM Bus WHERE bus_status='Active' ORDER BY bus_number");
        List<String[]> list = new ArrayList<>();
        while (rst.next()) list.add(new String[]{String.valueOf(rst.getInt("bus_id")), rst.getString("bus_number")});
        return list;
    }

    @Override
    public String getBusNumberById(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_number FROM Bus WHERE bus_id=?", busId);
        return rst.next() ? rst.getString("bus_number") : null;
    }

    private Event mapResultSet(ResultSet rst) throws SQLException {
        Event event = new Event();
        event.setEventId(rst.getInt("event_id"));
        event.setBusId(rst.getInt("bus_id"));
        event.setBusNumber(rst.getString("bus_number"));
        event.setStartLocation(rst.getString("start_location"));
        event.setEndLocation(rst.getString("end_location"));
        event.setEventValue(rst.getDouble("event_value"));
        event.setEventDate(rst.getDate("event_date") != null ? rst.getDate("event_date").toLocalDate() : null);
        event.setCustomerName(rst.getString("customer_name"));
        event.setCustomerContact(rst.getString("customer_contact"));
        event.setCustomerNic(rst.getString("customer_nic"));
        event.setCustomerAddress(rst.getString("customer_address"));
        event.setDescription(rst.getString("description"));
        event.setEventCompleted(rst.getBoolean("event_completed"));
        event.setCreatedBy(rst.getInt("created_by"));
        event.setCreatedAt(rst.getTimestamp("created_at") != null ? rst.getTimestamp("created_at").toLocalDateTime() : null);
        event.setUpdatedAt(rst.getTimestamp("updated_at") != null ? rst.getTimestamp("updated_at").toLocalDateTime() : null);
        return event;
    }
}
