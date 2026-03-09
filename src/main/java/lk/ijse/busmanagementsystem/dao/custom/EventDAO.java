package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.Event;

import java.sql.SQLException;
import java.util.List;

public interface EventDAO extends CrudDAO<Event> {

    List<Event> searchEvents(String keyword)
            throws SQLException, ClassNotFoundException;

    List<String[]> getAllActiveBuses()
            throws SQLException, ClassNotFoundException;

    String getBusNumberById(int busId)
            throws SQLException, ClassNotFoundException;
}
