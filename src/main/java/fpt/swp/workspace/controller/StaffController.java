package fpt.swp.workspace.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import fpt.swp.workspace.DTO.RoomDTO;
import fpt.swp.workspace.auth.AuthenticationResponse;
import fpt.swp.workspace.models.BookingStatus;
import fpt.swp.workspace.models.Staff;
import fpt.swp.workspace.response.*;
import fpt.swp.workspace.service.OrderBookingService;
import fpt.swp.workspace.service.RoomService;
import fpt.swp.workspace.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping("/api/auth/staffs")
public class StaffController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private OrderBookingService orderBookingService;

    @PostMapping()
    public ResponseEntity<AuthenticationResponse> createStaff(@Valid @RequestBody StaffRequest request) {
        AuthenticationResponse response = staffService.createStaff(request);
        if (response.getStatusCode() == 400) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public Page<StaffResponse> getAllStaffs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return staffService.getAllStaffs(page, size);
    }

    @GetMapping("/{id}")
    public StaffResponse getStaffById(@PathVariable String id) {
        return staffService.getStaffById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Staff>> updateManager(@PathVariable String id,@RequestBody UpdateStaffRequest request) {
        try {
            Staff updateStaff = staffService.updateStaff(id, request);
            APIResponse<Staff> response = new APIResponse<>("Staff updated successfully", updateStaff);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            APIResponse<Staff> response = new APIResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> deleteStaff(@PathVariable String id) {
        try {
            staffService.deleteStaff(id);
            APIResponse<Void> response = new APIResponse<>("Staff deleted successfully", null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            APIResponse<Void> response = new APIResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/tracking/order/{userId}")
    public ResponseEntity<Page<OrderBookingStaffTracking>> getOrderBookingsByCustomerId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<OrderBookingStaffTracking> bookings = staffService.getOrderBookingsByCustomerId(userId, page, size);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("status")
    public ResponseEntity<APIResponse<List<RoomStatusResponse>>> getAllRoomStatus() {
        try {
            List<RoomStatusResponse> roomStatuses = staffService.getAllRoomStatus();
            APIResponse<List<RoomStatusResponse>> response = new APIResponse<>("Successfully", roomStatuses);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            APIResponse<List<RoomStatusResponse>> response = new APIResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

//    @PutMapping("status/{roomId}")
//    public ResponseEntity<APIResponse<RoomStatusResponse>> updateRoomStatus(@PathVariable String roomId, @RequestBody RoomStatusRequest request) {
//        try {
//            RoomStatusResponse updatedRoomStatus = staffService.updateRoomStatus(roomId, request);
//            APIResponse<RoomStatusResponse> response = new APIResponse<>("Room status updated successfully", updatedRoomStatus);
//            return ResponseEntity.status(HttpStatus.OK).body(response);
//        } catch (RuntimeException e) {
//            APIResponse<RoomStatusResponse> response = new APIResponse<>(e.getMessage(), null);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//    }

    @PutMapping("room-status/{roomId}")
    public ResponseEntity<Object> updateRoomStatus(@PathVariable String roomId,
                                           @RequestParam("status") String status) {
        try {
            roomService.updateRoomStatus(roomId, status);
            return ResponseHandler.responseBuilder("Room status updated successfully", HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("status/booking/{bookingId}")
    public ResponseEntity<OrderStatusResponse> updateOrderStatus(@PathVariable String bookingId, @RequestBody UpdateOrderBookingStatusRequest request) {
        try {
            OrderStatusResponse updatedOrderStatus = staffService.updateOrderStatus(bookingId, request);
            return ResponseEntity.ok(updatedOrderStatus);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/room-assign")
    public ResponseEntity<Object> getRoomsAssigned(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        List<RoomDTO> listRoomAssign = staffService.getRoomsAssigned(jwtToken);
        try{
            return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, listRoomAssign);
        }catch (NullPointerException e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/check-order-booking")
    public ResponseEntity<Object> getAllOrderBooking() {
        try {
            return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, staffService.getOrderBookingDetails());
        }catch (NullPointerException e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/check-customer-order-booking-email")
    public ResponseEntity<Object> getCustomerOrderBookingByEmail(@RequestParam("date") String date, @RequestParam("email") String email) {
        try {
            return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, staffService.checkinByEmail(date, email));
        }catch (NullPointerException e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/check-customer-order-booking-phone")
    public ResponseEntity<Object> getCustomerOrderBookingByPhone(@RequestParam("date") String date, @RequestParam("phonenumber") String phonenumber) {
        try {
            return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, staffService.checkinByPhonenumber(date, phonenumber));
        }catch (NullPointerException e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-booking-status")
        public ResponseEntity<Object> updateOrderStatus(@RequestParam("bookingId") String bookingId, @RequestParam("status") String status) {
        try {
             staffService.updateOrderStatus(bookingId, status);
            return ResponseHandler.responseBuilder("Cập nhập thành công", HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-pending-booking")
    public ResponseEntity<Object> getPendingBooking() {
        try {
            return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, orderBookingService.getPendingBooking());
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/accept-pending-booking")
        public ResponseEntity<Object> acceptPendingBooking(@RequestParam("bookingId") String bookingId) {
        try {
            staffService.acceptPendingBooking(bookingId);
            return ResponseHandler.responseBuilder("Chấp nhận booking", HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reject-pending-booking")
    public ResponseEntity<Object> rejectPendingBooking(@RequestParam("bookingId") String bookingId) {
        try {
            staffService.rejectPendingBooking(bookingId);
            return ResponseHandler.responseBuilder("Đã từ chối booking", HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}

