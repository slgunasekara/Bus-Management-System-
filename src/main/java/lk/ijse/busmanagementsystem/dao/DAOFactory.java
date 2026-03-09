package lk.ijse.busmanagementsystem.dao;

import lk.ijse.busmanagementsystem.dao.custom.impl.*;

public class DAOFactory {

    private static DAOFactory instance;

    private DAOFactory() {}

    public static DAOFactory getInstance() {
        return instance == null ? instance = new DAOFactory() : instance;
    }

    public enum DAOType {
        BUS, DAILY_PROFIT, DASHBOARD, EMPLOYEE, EMPLOYEE_SALARY, EVENT, MAINTENANCE,
        MONTHLY_PROFIT, OTHER_SERVICES, PART_PURCHASE, PASSWORD_RESET_OTP,
        REPORT, TRIP, TRIP_EMPLOYEE, TRIP_EXPENSES, UPDATE_PRICES, USER
    }

    public SuperDAO getDAO(DAOType daoType) {
        switch (daoType) {
            case BUS:                return new BusDAOImpl();
            case DAILY_PROFIT:       return new DailyProfitDAOImpl();
            case DASHBOARD:          return new DashboardDAOImpl();
            case EMPLOYEE:           return new EmployeeDAOImpl();
            case EMPLOYEE_SALARY:    return new EmployeeSalaryDAOImpl();
            case EVENT:              return new EventDAOImpl();
            case MAINTENANCE:        return new MaintenanceDAOImpl();
            case MONTHLY_PROFIT:     return new MonthlyProfitDAOImpl();
            case OTHER_SERVICES:     return new OtherServicesDAOImpl();
            case PART_PURCHASE:      return new PartPurchaseDAOImpl();
            case PASSWORD_RESET_OTP: return new PasswordResetOtpDAOImpl();
            case REPORT:             return new ReportDAOImpl();
            case TRIP:               return new TripDAOImpl();
            case TRIP_EMPLOYEE:      return new TripEmployeeDAOImpl();
            case TRIP_EXPENSES:      return new TripExpensesDAOImpl();
            case UPDATE_PRICES:      return new UpdatePricesDAOImpl();
            case USER:               return new UserDAOImpl();
            default:                 return null;
        }
    }
}
