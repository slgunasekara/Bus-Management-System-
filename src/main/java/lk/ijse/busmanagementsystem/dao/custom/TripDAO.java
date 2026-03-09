package lk.ijse.busmanagementsystem.dao.custom;

import lk.ijse.busmanagementsystem.dao.CrudDAO;
import lk.ijse.busmanagementsystem.entity.Trip;

import java.sql.SQLException;
import java.util.List;

public interface TripDAO extends CrudDAO<Trip> {

    boolean isBusExists(int busId)
            throws SQLException, ClassNotFoundException;

    List<Trip> searchTrips(String keyword)
            throws SQLException, ClassNotFoundException;
}
