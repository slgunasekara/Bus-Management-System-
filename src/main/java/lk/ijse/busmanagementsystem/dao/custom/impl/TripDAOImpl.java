package lk.ijse.busmanagementsystem.dao.impl;

import lk.ijse.busmanagementsystem.dto.TripDTO;
import lk.ijse.busmanagementsystem.enums.TripCategory;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TripDAOImpl {

    //Get all trips with JOIN to User table to get username
    public List<TripDTO> getAllTrips() throws SQLException, ClassNotFoundException {
        String sql = "SELECT t.*, u.username " +
                "FROM Trip t " +
                "LEFT JOIN User u ON t.created_by = u.user_id " +
                "ORDER BY t.trip_id DESC";

        ResultSet rst = CrudUtil.execute(sql);
        List<TripDTO> tripList = new ArrayList<>();

        while (rst.next()) {
            TripDTO tripDTO = new TripDTO(
                    rst.getInt("trip_id"),
                    rst.getInt("bus_id"),
                    TripCategory.valueOf(rst.getString("trip_category")),
                    rst.getString("start_location"),
                    rst.getString("end_location"),
                    rst.getDouble("distance"),
                    rst.getDouble("total_income"),
                    rst.getDate("trip_date").toLocalDate(),
                    rst.getString("description"),
                    rst.getInt("created_by"),
                    rst.getString("username")
            );
            tripList.add(tripDTO);
        }
        return tripList;
    }

    public boolean saveTrip(TripDTO tripDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Trip(bus_id, trip_category, start_location, end_location, " +
                "distance, total_income, trip_date, description, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                tripDTO.getBusId(),
                tripDTO.getTripCategory().name(),
                tripDTO.getStartLocation(),
                tripDTO.getEndLocation(),
                tripDTO.getDistance(),
                tripDTO.getTotalIncome(),
                java.sql.Date.valueOf(tripDTO.getTripDate()),
                tripDTO.getDescription(),
                tripDTO.getCreatedBy()
        );
    }

    public boolean updateTrip(TripDTO tripDTO) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Trip SET bus_id=?, trip_category=?, start_location=?, " +
                "end_location=?, distance=?, total_income=?, trip_date=?, " +
                "description=? WHERE trip_id=?";
        return CrudUtil.execute(sql,
                tripDTO.getBusId(),
                tripDTO.getTripCategory().name(),
                tripDTO.getStartLocation(),
                tripDTO.getEndLocation(),
                tripDTO.getDistance(),
                tripDTO.getTotalIncome(),
                java.sql.Date.valueOf(tripDTO.getTripDate()),
                tripDTO.getDescription(),
                tripDTO.getTripId()
        );
    }

    public boolean deleteTrip(String tripId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Trip WHERE trip_id=?", tripId);
    }

    //Search trip with username
    public TripDTO searchTrip(String id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT t.*, u.username " +
                "FROM Trip t " +
                "LEFT JOIN User u ON t.created_by = u.user_id " +
                "WHERE t.trip_id=?";

        ResultSet rst = CrudUtil.execute(sql, id);

        if (rst.next()) {
            return new TripDTO(
                    rst.getInt("trip_id"),
                    rst.getInt("bus_id"),
                    TripCategory.valueOf(rst.getString("trip_category")),
                    rst.getString("start_location"),
                    rst.getString("end_location"),
                    rst.getDouble("distance"),
                    rst.getDouble("total_income"),
                    rst.getDate("trip_date").toLocalDate(),
                    rst.getString("description"),
                    rst.getInt("created_by"),
                    rst.getString("username")
            );
        }
        return null;
    }

    public boolean isBusExists(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_id=?", busId);
        return rst.next();
    }

    //Search trips with username
    public List<TripDTO> searchTrips(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT t.*, u.username " +
                "FROM Trip t " +
                "LEFT JOIN User u ON t.created_by = u.user_id " +
                "WHERE t.trip_id LIKE ? OR " +
                "t.bus_id LIKE ? OR " +
                "t.trip_category LIKE ? OR " +
                "t.start_location LIKE ? OR " +
                "t.end_location LIKE ? OR " +
                "t.description LIKE ? OR " +
                "u.username LIKE ? " +
                "ORDER BY t.trip_id DESC";

        String searchPattern = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(sql,
                searchPattern, searchPattern, searchPattern,
                searchPattern, searchPattern, searchPattern, searchPattern);

        List<TripDTO> tripList = new ArrayList<>();

        while (rst.next()) {
            TripDTO tripDTO = new TripDTO(
                    rst.getInt("trip_id"),
                    rst.getInt("bus_id"),
                    TripCategory.valueOf(rst.getString("trip_category")),
                    rst.getString("start_location"),
                    rst.getString("end_location"),
                    rst.getDouble("distance"),
                    rst.getDouble("total_income"),
                    rst.getDate("trip_date").toLocalDate(),
                    rst.getString("description"),
                    rst.getInt("created_by"),
                    rst.getString("username")
            );
            tripList.add(tripDTO);
        }
        return tripList;
    }
}