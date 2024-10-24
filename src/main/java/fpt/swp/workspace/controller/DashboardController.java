package fpt.swp.workspace.controller;

import fpt.swp.workspace.response.ResponseHandler;
import fpt.swp.workspace.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
