package fpt.swp.workspace.service;

import fpt.swp.workspace.DTO.DashboardDTO;
import fpt.swp.workspace.models.User;

public interface IDashboardService {
    DashboardDTO getTotalBookingInDate(String token);


    DashboardDTO getTotalBookingInWeek(String token);

    DashboardDTO getTotalBookingInMonth(String token);

    DashboardDTO getTotalSpace(String token);

    DashboardDTO roomTypeAnalystByDate(String token);

    DashboardDTO roomTypeAnalystByWeek(String token);

    DashboardDTO roomTypeAnalystByMonth(String token);

    DashboardDTO bookingAnalystByDate(String token);

    DashboardDTO bookingAnalystByWeek(String token);

    DashboardDTO bookingAnalystByMonth(String token);
}
