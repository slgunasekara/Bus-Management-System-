package lk.ijse.busmanagementsystem.dao.impl;

import lk.ijse.busmanagementsystem.dto.OtherServicesDTO;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OtherServicesDAOImpl {

    public List<OtherServicesDTO> getAllServices() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Other_Services");
        List<OtherServicesDTO> servicesList = new ArrayList<>();

        while (rst.next()) {
            Integer busId = rst.getObject("bus_id") != null ? rst.getInt("bus_id") : null;
            Integer tripId = rst.getObject("trip_id") != null ? rst.getInt("trip_id") : null;

            OtherServicesDTO serviceDTO = new OtherServicesDTO(
                    rst.getInt("service_id"),
                    busId,
                    tripId,
                    rst.getString("service_name"),
                    rst.getDouble("cost"),
                    rst.getDate("date").toLocalDate(),
                    rst.getString("description"),
                    rst.getInt("created_by")
            );
            servicesList.add(serviceDTO);
        }
        return servicesList;
    }

    public boolean saveService(OtherServicesDTO serviceDTO) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Other_Services(bus_id, trip_id, service_name, cost, date, description, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                serviceDTO.getBusId(),
                serviceDTO.getTripId(),
                serviceDTO.getServiceName(),
                serviceDTO.getCost(),
                java.sql.Date.valueOf(serviceDTO.getDate()),
                serviceDTO.getDescription(),
                serviceDTO.getCreatedBy()
        );
    }

    public boolean updateService(OtherServicesDTO serviceDTO) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Other_Services SET bus_id=?, trip_id=?, service_name=?, cost=?, date=?, description=? WHERE service_id=?";
        return CrudUtil.execute(sql,
                serviceDTO.getBusId(),
                serviceDTO.getTripId(),
                serviceDTO.getServiceName(),
                serviceDTO.getCost(),
                java.sql.Date.valueOf(serviceDTO.getDate()),
                serviceDTO.getDescription(),
                serviceDTO.getServiceId()
        );
    }

    public boolean deleteService(String serviceId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Other_Services WHERE service_id=?", serviceId);
    }

    public OtherServicesDTO searchService(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Other_Services WHERE service_id=?", id);

        if (rst.next()) {
            Integer busId = rst.getObject("bus_id") != null ? rst.getInt("bus_id") : null;
            Integer tripId = rst.getObject("trip_id") != null ? rst.getInt("trip_id") : null;

            return new OtherServicesDTO(
                    rst.getInt("service_id"),
                    busId,
                    tripId,
                    rst.getString("service_name"),
                    rst.getDouble("cost"),
                    rst.getDate("date").toLocalDate(),
                    rst.getString("description"),
                    rst.getInt("created_by")
            );
        }
        return null;
    }

    public List<OtherServicesDTO> searchServices(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Other_Services WHERE service_name LIKE ? OR description LIKE ? OR CAST(COALESCE(bus_id, '') AS CHAR) LIKE ?";
        String searchPattern = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(sql, searchPattern, searchPattern, searchPattern);

        List<OtherServicesDTO> servicesList = new ArrayList<>();

        while (rst.next()) {
            Integer busId = rst.getObject("bus_id") != null ? rst.getInt("bus_id") : null;
            Integer tripId = rst.getObject("trip_id") != null ? rst.getInt("trip_id") : null;

            OtherServicesDTO serviceDTO = new OtherServicesDTO(
                    rst.getInt("service_id"),
                    busId,
                    tripId,
                    rst.getString("service_name"),
                    rst.getDouble("cost"),
                    rst.getDate("date").toLocalDate(),
                    rst.getString("description"),
                    rst.getInt("created_by")
            );
            servicesList.add(serviceDTO);
        }
        return servicesList;
    }

    // Get all available Bus IDs
    public List<Integer> getAllBusIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_status = 'Active' ORDER BY bus_id");
        List<Integer> busIds = new ArrayList<>();
        while (rst.next()) {
            busIds.add(rst.getInt("bus_id"));
        }
        return busIds;
    }

    // Get all available Trip IDs
    public List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip ORDER BY trip_id DESC");
        List<Integer> tripIds = new ArrayList<>();
        while (rst.next()) {
            tripIds.add(rst.getInt("trip_id"));
        }
        return tripIds;
    }

    // Get Bus Details by ID
    public String getBusDetails(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT bus_number, bus_brand_name, bus_type FROM Bus WHERE bus_id=?",
                busId
        );

        if (rst.next()) {
            return String.format("%s | %s | %s",
                    rst.getString("bus_number"),
                    rst.getString("bus_brand_name"),
                    rst.getString("bus_type")
            );
        }
        return "Bus not found";
    }

    // Get Trip Details by ID
    public String getTripDetails(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute(
                "SELECT start_location, end_location, trip_date FROM Trip WHERE trip_id=?",
                tripId
        );

        if (rst.next()) {
            return String.format("%s → %s | %s",
                    rst.getString("start_location"),
                    rst.getString("end_location"),
                    rst.getDate("trip_date")
            );
        }
        return "Trip not found";
    }

    public boolean isBusExists(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_id=?", busId);
        return rst.next();
    }

    public boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip WHERE trip_id=?", tripId);
        return rst.next();
    }
}