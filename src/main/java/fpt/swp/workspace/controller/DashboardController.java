package fpt.swp.workspace.controller;

import fpt.swp.workspace.response.ResponseHandler;
import fpt.swp.workspace.service.DashboardService;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/total-booking-in-date")
    public ResponseEntity<Object> totalBookingInDate(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.getTotalBookingInDate(jwt));
    }
    @GetMapping("/total-booking-in-week")
    public ResponseEntity<Object> totalBookingInWeek(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.getTotalBookingInWeek(jwt));
    }
    @GetMapping("/total-booking-in-moth")
    public ResponseEntity<Object> totalBookingInMonth(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.getTotalBookingInMonth(jwt));
    }
    @GetMapping("/total-space")
    public ResponseEntity<Object> totalSpace(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.getTotalSpace(jwt));
    }
    @GetMapping("/room-type-analyst-date")
    public ResponseEntity<Object> roomTypeAnalystDate(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.roomTypeAnalystByDate(jwt));
    }
    @GetMapping("/room-type-analyst-week")
    public ResponseEntity<Object> roomTypeAnalystWeek(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.roomTypeAnalystByWeek(jwt));
    }
    @GetMapping("/room-type-analyst-month")
    public ResponseEntity<Object> roomTypeAnalystMonth(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.roomTypeAnalystByMonth(jwt));
    }
    @GetMapping("/booking-analyst-date")
    public ResponseEntity<Object> bookingAnalystDate(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.bookingAnalystByDate(jwt));
    }
    @GetMapping("/booking-analyst-week")
    public ResponseEntity<Object> bookingAnalystWeek(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.bookingAnalystByWeek(jwt));
    }
    @GetMapping("/booking-analyst-month")
    public ResponseEntity<Object> bookingAnalystMonth(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.bookingAnalystByMonth(jwt));
    }

    @GetMapping("/revenue")
    public ResponseEntity<Object> getMonthlyRevenue(@RequestHeader("Authorization") String token) {
        String jwt = token.substring(7);
        try{
            return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.getRevenue(jwt));
        }catch (AopInvocationException e){
            return ResponseHandler.responseBuilder("Chưa có doanh thu", HttpStatus.OK);
        }
    }

    // owner
    @GetMapping("owner/total-booking-in-date/{buildingId}")
    public ResponseEntity<Object> totalBookingInDateOwner(@PathVariable("buildingId") String buildinggId) {
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.getTotalBookingInDateOwner(buildinggId));
    }

    @GetMapping("owner/total-booking-in-week/{buildingId}")
    public ResponseEntity<Object> totalBookingInWeekOwner(@PathVariable("buildingId") String buildinggId) {
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.getTotalBookingInWeekOwner(buildinggId));
    }

    @GetMapping("owner/total-booking-in-month/{buildingId}")
    public ResponseEntity<Object> totalBookingInMonthOwner(@PathVariable("buildingId") String buildinggId) {

        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.getTotalBookingInMonthOwner(buildinggId));
    }

    @GetMapping("owner/total-space/{buildingId}")
    public ResponseEntity<Object> totalSpaceOwner(@PathVariable("buildingId") String buildinggId) {
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.getTotalSpaceOwner(buildinggId));
    }

    @GetMapping("owner/room-type-analyst-date/{buildingId}")
    public ResponseEntity<Object> roomTypeAnalystDateOwner(@PathVariable("buildingId") String buildinggId) {

        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.roomTypeAnalystByDateOwner(buildinggId));
    }

    @GetMapping("owner/room-type-analyst-week/{buildingId}")
    public ResponseEntity<Object> roomTypeAnalystWeekOwner(@PathVariable("buildingId") String buildinggId) {
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.roomTypeAnalystByWeekOwner(buildinggId));
    }

    @GetMapping("owner/room-type-analyst-month/{buildingId}")
    public ResponseEntity<Object> roomTypeAnalystMonthOwner(@PathVariable("buildingId") String buildinggId) {

        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.roomTypeAnalystByMonthOwner(buildinggId));
    }

    @GetMapping("owner/booking-analyst-date/{buildingId}")
    public ResponseEntity<Object> bookingAnalystDateOwner(@PathVariable("buildingId") String buildinggId) {

        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.bookingAnalystByDateOwner(buildinggId));
    }

    @GetMapping("owner/booking-analyst-week/{buildingId}")
    public ResponseEntity<Object> bookingAnalystWeekOwner(@PathVariable("buildingId") String buildinggId) {
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.bookingAnalystByWeekOwner(buildinggId));
    }

    @GetMapping("owner/booking-analyst-month/{buildingId}")
    public ResponseEntity<Object> bookingAnalystMonthOwner(@PathVariable("buildingId") String buildinggId) {

        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.bookingAnalystByMonthOwner(buildinggId));
    }

    @GetMapping("owner/revenue/{buildingId}")
    public ResponseEntity<Object> getMonthlyRevenueByOwner(@PathVariable("buildingId") String buildinggId) {
        try{
            return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, dashboardService.getRevenueOwner(buildinggId));
        }catch (AopInvocationException e){
            return ResponseHandler.responseBuilder("Chưa có doanh thu", HttpStatus.OK);
        }

    }


}
