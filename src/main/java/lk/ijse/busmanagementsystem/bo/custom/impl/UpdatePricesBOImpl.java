package lk.ijse.busmanagementsystem.bo.custom.impl;

import lk.ijse.busmanagementsystem.bo.custom.UpdatePricesBO;
import lk.ijse.busmanagementsystem.dao.DAOFactory;
import lk.ijse.busmanagementsystem.dao.custom.UpdatePricesDAO;
import lk.ijse.busmanagementsystem.dto.UpdatePricesDTO;
import lk.ijse.busmanagementsystem.entity.UpdatePrices;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UpdatePricesBOImpl implements UpdatePricesBO {

    private final UpdatePricesDAO updatePricesDAO =
            (UpdatePricesDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.UPDATE_PRICES);

    @Override
    public List<UpdatePricesDTO> getAllUpdatePrices() throws SQLException, ClassNotFoundException {
        return updatePricesDAO.getAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean saveUpdatePrice(UpdatePricesDTO dto) throws SQLException, ClassNotFoundException {
        double changeAmount = calculateChangeAmount(dto.getPreviousValue(), dto.getNewValue());
        double percentageChange = calculatePercentageChange(dto.getPreviousValue(), dto.getNewValue());
        dto.setChangeAmount(changeAmount);
        dto.setPercentageChange(percentageChange);

        if (dto.getChangeType() == null || dto.getChangeType().isEmpty()) {
            dto.setChangeType(changeAmount >= 0 ? "INCREMENT" : "DECREMENT");
        }
        return updatePricesDAO.save(toEntity(dto));
    }

    @Override
    public boolean deleteUpdatePrice(String priceId) throws SQLException, ClassNotFoundException {
        return updatePricesDAO.delete(priceId);
    }

    @Override
    public UpdatePricesDTO searchUpdatePrice(String id) throws SQLException, ClassNotFoundException {
        UpdatePrices e = updatePricesDAO.search(id);
        return e != null ? toDTO(e) : null;
    }

    @Override
    public List<UpdatePricesDTO> searchUpdatePrices(String keyword) throws SQLException, ClassNotFoundException {
        return updatePricesDAO.searchUpdatePrices(keyword).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public double calculateChangeAmount(double previousValue, double newValue) {
        return newValue - previousValue;
    }

    @Override
    public double calculatePercentageChange(double previousValue, double newValue) {
        if (previousValue == 0) return 0;
        return ((newValue - previousValue) / previousValue) * 100;
    }

    @Override
    public Double getLatestPrice(String updateType) throws SQLException, ClassNotFoundException {
        return updatePricesDAO.getLatestPrice(updateType);
    }


    private UpdatePrices toEntity(UpdatePricesDTO dto) {
        UpdatePrices e = new UpdatePrices();
        e.setUpdatePricesId(dto.getUpdatePricesId());
        e.setUpdateType(dto.getUpdateType());
        e.setChangeType(dto.getChangeType());
        e.setPreviousValue(dto.getPreviousValue());
        e.setNewValue(dto.getNewValue());
        e.setChangeAmount(dto.getChangeAmount());
        e.setPercentageChange(dto.getPercentageChange());
        e.setChangeDate(dto.getChangeDate());
        e.setDescription(dto.getDescription());
        e.setCreatedBy(dto.getCreatedBy());
        return e;
    }

    private UpdatePricesDTO toDTO(UpdatePrices e) {
        UpdatePricesDTO dto = new UpdatePricesDTO();
        dto.setUpdatePricesId(e.getUpdatePricesId());
        dto.setUpdateType(e.getUpdateType());
        dto.setChangeType(e.getChangeType());
        dto.setPreviousValue(e.getPreviousValue());
        dto.setNewValue(e.getNewValue());
        dto.setChangeAmount(e.getChangeAmount());
        dto.setPercentageChange(e.getPercentageChange());
        dto.setChangeDate(e.getChangeDate());
        dto.setDescription(e.getDescription());
        dto.setCreatedBy(e.getCreatedBy());
        return dto;
    }
}
