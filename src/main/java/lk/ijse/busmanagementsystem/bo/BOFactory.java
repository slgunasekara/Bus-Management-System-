package lk.ijse.busmanagementsystem.bo;

import lk.ijse.busmanagementsystem.bo.custom.impl.*;

public class BOFactory {

    private static BOFactory instance;

    private BOFactory() {}

    public static BOFactory getInstance() {
        return instance == null ? instance = new BOFactory() : instance;
    }

    public enum BOType {
        BUS, DAILY_PROFIT, DASHBOARD, EMPLOYEE, EMPLOYEE_SALARY,
        EVENT, MAINTENANCE, MONTHLY_PROFIT, OTHER_SERVICES,
        PART_PURCHASE, PASSWORD_RESET_OTP, REPORT, TRIP,
        TRIP_EMPLOYEE, TRIP_EXPENSES, UPDATE_PRICES, USER
    }

    public SuperBO getBO(BOType boType) {
        switch (boType) {
            case BUS:            return new BusBOImpl();
            case DAILY_PROFIT:   return new DailyProfitBOImpl();
            case DASHBOARD:      return new DashboardBOImpl();
            case EMPLOYEE:       return new EmployeeBOImpl();
            case EMPLOYEE_SALARY:return new EmployeeSalaryBOImpl();
            case EVENT:          return new EventBOImpl();
            case MAINTENANCE:    return new MaintenanceBOImpl();
            case MONTHLY_PROFIT: return new MonthlyProfitBOImpl();
            case OTHER_SERVICES: return new OtherServicesBOImpl();
            case PART_PURCHASE:  return new PartPurchaseBOImpl();
            case PASSWORD_RESET_OTP: return new PasswordResetOtpBOImpl();
            case REPORT:         return new ReportBOImpl();
            case TRIP:           return new TripBOImpl();
            case TRIP_EMPLOYEE:  return new TripEmployeeBOImpl();
            case TRIP_EXPENSES:  return new TripExpensesBOImpl();
            case UPDATE_PRICES:  return new UpdatePricesBOImpl();
            case USER:           return new UserBOImpl();
            default:             return null;
        }
    }
}
