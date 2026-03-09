package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.OtherServicesDAO;
import lk.ijse.busmanagementsystem.entity.OtherServices;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OtherServicesDAOImpl implements OtherServicesDAO {

    @Override
    public List<OtherServices> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Other_Services ORDER BY service_id DESC");
        List<OtherServices> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public boolean save(OtherServices os) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Other_Services(bus_id, trip_id, service_name, cost, date, description, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)",
                os.getBusId(), os.getTripId(), os.getServiceName(),
                os.getCost(), java.sql.Date.valueOf(os.getDate()), os.getDescription(), os.getCreatedBy()
        );
    }

    @Override
    public boolean update(OtherServices os) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Other_Services SET bus_id=?, trip_id=?, service_name=?, cost=?, date=?, description=? WHERE service_id=?",
                os.getBusId(), os.getTripId(), os.getServiceName(),
                os.getCost(), java.sql.Date.valueOf(os.getDate()), os.getDescription(), os.getServiceId()
        );
    }

    @Override
    public boolean delete(String serviceId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Other_Services WHERE service_id=?", serviceId);
    }

    @Override
    public boolean delete(int serviceId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Other_Services WHERE service_id=?", serviceId);
    }

    @Override
    public boolean exists(String serviceId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT service_id FROM Other_Services WHERE service_id=?", serviceId);
        return rst.next();
    }

    @Override
    public boolean exists(int serviceId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT service_id FROM Other_Services WHERE service_id=?", serviceId);
        return rst.next();
    }

    @Override
    public OtherServices search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Other_Services WHERE service_id=?", id);
        if (rst.next()) return mapResultSet(rst);
        return null;
    }

    @Override
    public List<OtherServices> searchServices(String keyword) throws SQLException, ClassNotFoundException {
        String p = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(
                "SELECT * FROM Other_Services WHERE service_name LIKE ? OR description LIKE ? OR CAST(COALESCE(bus_id, '') AS CHAR) LIKE ?",
                p, p, p);
        List<OtherServices> list = new ArrayList<>();
        while (rst.next()) list.add(mapResultSet(rst));
        return list;
    }

    @Override
    public List<Integer> getAllBusIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_status='Active' ORDER BY bus_id");
        List<Integer> ids = new ArrayList<>();
        while (rst.next()) ids.add(rst.getInt("bus_id"));
        return ids;
    }

    @Override
    public List<Integer> getAllTripIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip ORDER BY trip_id DESC");
        List<Integer> ids = new ArrayList<>();
        while (rst.next()) ids.add(rst.getInt("trip_id"));
        return ids;
    }

    @Override
    public String getBusDetails(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_number, bus_brand_name, bus_type FROM Bus WHERE bus_id=?", busId);
        if (rst.next()) return rst.getString("bus_number") + " | " + rst.getString("bus_brand_name") + " | " + rst.getString("bus_type");
        return "Bus not found";
    }

    @Override
    public String getTripDetails(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT start_location, end_location, trip_date FROM Trip WHERE trip_id=?", tripId);
        if (rst.next()) return rst.getString("start_location") + " → " + rst.getString("end_location") + " | " + rst.getDate("trip_date");
        return "Trip not found";
    }

    @Override
    public boolean isBusExists(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_id=?", busId);
        return rst.next();
    }

    @Override
    public boolean isTripExists(int tripId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT trip_id FROM Trip WHERE trip_id=?", tripId);
        return rst.next();
    }

    private OtherServices mapResultSet(ResultSet rst) throws SQLException {
        OtherServices os = new OtherServices();
        os.setServiceId(rst.getInt("service_id"));
        os.setBusId(rst.getObject("bus_id") != null ? rst.getInt("bus_id") : null);
        os.setTripId(rst.getObject("trip_id") != null ? rst.getInt("trip_id") : null);
        os.setServiceName(rst.getString("service_name"));
        os.setCost(rst.getDouble("cost"));
        os.setDate(rst.getDate("date").toLocalDate());
        os.setDescription(rst.getString("description"));
        os.setCreatedBy(rst.getInt("created_by"));
        return os;
    }
}
