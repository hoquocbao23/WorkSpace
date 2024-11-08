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

    DashboardDTO getRevenue(String token);

    // Owner
    DashboardDTO getTotalBookingInDateOwner(String buildingId);

    DashboardDTO getTotalBookingInWeekOwner(String buildingId);

    DashboardDTO getTotalBookingInMonthOwner(String buildingId);

    DashboardDTO getTotalSpaceOwner(String buildingId);

    DashboardDTO roomTypeAnalystByDateOwner(String buildingId);

    DashboardDTO roomTypeAnalystByWeekOwner(String buildingId);

    DashboardDTO roomTypeAnalystByMonthOwner(String buildingId);

    DashboardDTO bookingAnalystByDateOwner(String buildingId);

    DashboardDTO bookingAnalystByWeekOwner(String buildingId);

    DashboardDTO bookingAnalystByMonthOwner(String buildingId);

    DashboardDTO getRevenueOwner(String buildingId);
}
