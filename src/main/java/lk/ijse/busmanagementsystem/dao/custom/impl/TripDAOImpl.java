package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.TripDAO;
import lk.ijse.busmanagementsystem.entity.Trip;
import lk.ijse.busmanagementsystem.enums.TripCategory;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TripDAOImpl implements TripDAO {

    private static final String SELECT_WITH_USER =
            "SELECT t.*, u.username FROM Trip t LEFT JOIN User u ON t.created_by=u.user_id ";

    @Override
    public List<Trip> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(SELECT_WITH_USER + "ORDER BY t.trip_id DESC");
        List<Trip> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public boolean save(Trip trip) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Trip(bus_id, trip_category, start_location, end_location, distance, total_income, trip_date, description, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                trip.getBusId(), trip.getTripCategory().name(), trip.getStartLocation(), trip.getEndLocation(),
                trip.getDistance(), trip.getTotalIncome(), java.sql.Date.valueOf(trip.getTripDate()),
                trip.getDescription(), trip.getCreatedBy()
        );
    }

    @Override
    public boolean update(Trip trip) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Trip SET bus_id=?, trip_category=?, start_location=?, end_location=?, distance=?, total_income=?, trip_date=?, description=? WHERE trip_id=?",
                trip.getBusId(), trip.getTripCategory().name(), trip.getStartLocation(), trip.getEndLocation(),
                trip.getDistance(), trip.getTotalIncome(), java.sql.Date.valueOf(trip.getTripDate()),
                trip.getDescription(), trip.getTripId()
        );
    }

    @Override
    public boolean delete(String tripId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Trip WHERE trip_id=?", tripId);
    }

    @Override
    public boolean delete(int tripId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Trip WHERE trip_id=?", tripId);
    }

    @Override
    public boolean exists(String tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip WHERE trip_id=?", tripId);
        return rst.next();
    }

    @Override
    public boolean exists(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip WHERE trip_id=?", tripId);
        return rst.next();
    }

    @Override
    public Trip search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(SELECT_WITH_USER + "WHERE t.trip_id=?", id);
        if (rst.next()) return mapResultSet(rst);
        return null;
    }

    @Override
    public boolean isBusExists(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_id=?", busId);
        return rst.next();
    }

    @Override
    public List<Trip> searchTrips(String keyword) throws SQLException, ClassNotFoundException {
        String p = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(
                SELECT_WITH_USER + "WHERE t.trip_id LIKE ? OR t.bus_id LIKE ? OR t.trip_category LIKE ? OR " +
                        "t.start_location LIKE ? OR t.end_location LIKE ? OR t.description LIKE ? OR u.username LIKE ? ORDER BY t.trip_id DESC",
                p, p, p, p, p, p, p);
        List<Trip> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    private Trip mapResultSet(ResultSet rst) throws SQLException {
        Trip trip = new Trip();
        trip.setTripId(rst.getInt("trip_id"));
        trip.setBusId(rst.getInt("bus_id"));
        trip.setTripCategory(TripCategory.valueOf(rst.getString("trip_category")));
        trip.setStartLocation(rst.getString("start_location"));
        trip.setEndLocation(rst.getString("end_location"));
        trip.setDistance(rst.getDouble("distance"));
        trip.setTotalIncome(rst.getDouble("total_income"));
        trip.setTripDate(rst.getDate("trip_date").toLocalDate());
        trip.setDescription(rst.getString("description"));
        trip.setCreatedBy(rst.getInt("created_by"));
        trip.setCreatedByUsername(rst.getString("username"));
        return trip;
    }
}
