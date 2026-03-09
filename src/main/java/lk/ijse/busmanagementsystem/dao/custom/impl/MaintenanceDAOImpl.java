package lk.ijse.busmanagementsystem.dao.custom.impl;

import lk.ijse.busmanagementsystem.dao.custom.MaintenanceDAO;
import lk.ijse.busmanagementsystem.entity.Maintenance;
import lk.ijse.busmanagementsystem.enums.MaintenanceType;
import lk.ijse.busmanagementsystem.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceDAOImpl implements MaintenanceDAO {

    @Override
    public List<Maintenance> getAll() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Maintenance ORDER BY maint_id DESC");
        List<Maintenance> list = new ArrayList<>();
        while (rst.next()) {
            try { list.add(mapResultSet(rst)); }
            catch (Exception e) { System.err.println("❌ Error loading maintenance ID: " + rst.getInt("maint_id") + " - " + e.getMessage()); }
        }
        return list;
    }

    @Override
    public boolean save(Maintenance m) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "INSERT INTO Maintenance(bus_id, maintenance_type, date, Mileage, cost, Maintained_by, description, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                m.getBusId(), m.getMaintenanceType().name(), java.sql.Date.valueOf(m.getServiceDate()),
                m.getMileage(), m.getCost(), m.getTechnician(), m.getDescription(), m.getCreatedBy()
        );
    }

    @Override
    public boolean update(Maintenance m) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute(
                "UPDATE Maintenance SET bus_id=?, maintenance_type=?, date=?, Mileage=?, cost=?, Maintained_by=?, description=? WHERE maint_id=?",
                m.getBusId(), m.getMaintenanceType().name(), java.sql.Date.valueOf(m.getServiceDate()),
                m.getMileage(), m.getCost(), m.getTechnician(), m.getDescription(), m.getMaintId()
        );
    }

    @Override
    public boolean delete(String maintId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Maintenance WHERE maint_id=?", maintId);
    }

    @Override
    public boolean delete(int maintId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Maintenance WHERE maint_id=?", maintId);
    }

    @Override
    public boolean exists(String maintId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT maint_id FROM Maintenance WHERE maint_id=?", maintId);
        return rst.next();
    }

    @Override
    public boolean exists(int maintId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT maint_id FROM Maintenance WHERE maint_id=?", maintId);
        return rst.next();
    }

    @Override
    public Maintenance search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Maintenance WHERE maint_id=?", id);
        if (rst.next()) return mapResultSet(rst);
        return null;
    }

    @Override
    public boolean isBusExists(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus WHERE bus_id=?", busId);
        return rst.next();
    }

    @Override
    public List<Integer> getAllBusIds() throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_id FROM Bus ORDER BY bus_id DESC");
        List<Integer> ids = new ArrayList<>();
        while (rst.next()) ids.add(rst.getInt("bus_id"));
        return ids;
    }

    @Override
    public String getBusDetails(int busId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT bus_number FROM Bus WHERE bus_id=?", busId);
        return rst.next() ? "Bus Number: " + rst.getString("bus_number") : "Bus not found";
    }

    @Override
    public List<Maintenance> searchMaintenance(String keyword) throws SQLException, ClassNotFoundException {
        String p = "%" + keyword + "%";
        ResultSet rst = CrudUtil.execute(
                "SELECT * FROM Maintenance WHERE maint_id LIKE ? OR bus_id LIKE ? OR maintenance_type LIKE ? OR Maintained_by LIKE ? OR description LIKE ?",
                p, p, p, p, p);
        List<Maintenance> list = new ArrayList<>();
        while (rst.next()) {
            try { list.add(mapResultSet(rst)); }
            catch (Exception e) { System.err.println("❌ Search error: " + e.getMessage()); }
        }
        return list;
    }

    private Maintenance mapResultSet(ResultSet rst) throws SQLException {
        MaintenanceType type;
        try {
            type = MaintenanceType.valueOf(rst.getString("maintenance_type"));
        } catch (IllegalArgumentException e) {
            type = MaintenanceType.FULL_SERVICE;
        }
        Maintenance m = new Maintenance();
        m.setMaintId(rst.getInt("maint_id"));
        m.setBusId(rst.getInt("bus_id"));
        m.setMaintenanceType(type);
        m.setServiceDate(rst.getDate("date").toLocalDate());
        m.setMileage(rst.getDouble("Mileage"));
        m.setCost(rst.getDouble("cost"));
        m.setTechnician(rst.getString("Maintained_by"));
        m.setDescription(rst.getString("description"));
        m.setCreatedBy(rst.getInt("created_by"));
        return m;
    }
}
