package fpt.swp.WorkSpace.controller;

import fpt.swp.WorkSpace.DTO.BookedSlotDTO;
import fpt.swp.WorkSpace.DTO.CustomerServiceDTO;
import fpt.swp.WorkSpace.DTO.OrderBookingDetailDTO;
import fpt.swp.WorkSpace.models.OrderBooking;
import fpt.swp.WorkSpace.response.OrderBookingResponse;
import fpt.swp.WorkSpace.response.ResponseHandler;
import fpt.swp.WorkSpace.service.IOrderBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderBookingController {

    @Autowired
    private IOrderBookingService orderBookingService;



    @GetMapping("/check-booked-slot/{roomId}/{checkin-date}")
    public ResponseEntity<Object> checkBookedSlot(@PathVariable("roomId") String roomId,
                                                @PathVariable("checkinDate") String checkinDate) {
        try{
            List<OrderBookingDetailDTO> bookedList = orderBookingService.getBookedSlotByRoomAndDate(checkinDate, roomId);
            return ResponseHandler.responseBuilder("ok", HttpStatus.OK, bookedList);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/check-booked-slot")
    public ResponseEntity<Object> getBookedSlot(@RequestParam("roomId") String roomId,
                                                @RequestParam("checkinDate") String checkinDate) {
        try{
            List<OrderBookingDetailDTO> bookedList = orderBookingService.getBookedSlotByRoomAndDate(checkinDate, roomId);
            return ResponseHandler.responseBuilder("ok", HttpStatus.OK, bookedList);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/check-booked-slot-date/{checkinDate}")
    public ResponseEntity<Object> checkBookedSlotByDate(@PathVariable("checkinDate") String checkinDate) {
        try{
            List<OrderBookingDetailDTO> bookedList = orderBookingService.getBookedSlotByDate(checkinDate);
            return ResponseHandler.responseBuilder("ok", HttpStatus.OK, bookedList);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/check-booked-slot-date")
    public ResponseEntity<Object> getBookedSlotByDate(@RequestParam("checkinDate") String checkinDate) {
        try{
            List<OrderBookingDetailDTO> bookedList = orderBookingService.getBookedSlotByDate(checkinDate);
            return ResponseHandler.responseBuilder("ok", HttpStatus.OK, bookedList);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/check-booked-slot-in-out")
    public ResponseEntity<Object> getBookedSlotByInOut(@RequestParam("checkinDate") String checkinDate,
                                                       @RequestParam("checkoutDate") String checkoutDate,
                                                       @RequestParam("roomId") String roomId) {
        try{
            List<OrderBookingDetailDTO> bookedList = orderBookingService.getBookedSlotByCheckinAndCheckout(checkinDate, checkoutDate, roomId);
            return ResponseHandler.responseBuilder("ok", HttpStatus.OK, bookedList);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/check-booked-slot-each-date")
    public ResponseEntity<Object> getBookedSlotEachDate(@RequestParam("checkinDate") String checkinDate,
                                                       @RequestParam("checkoutDate") String checkoutDate,
                                                       @RequestParam("roomId") String roomId,
                                                        @RequestParam("buildingId") String buildingId) {
        try{
            BookedSlotDTO bookedList = orderBookingService.getBookedSlotByEachDay(checkinDate, checkoutDate, roomId, buildingId);
            return ResponseHandler.responseBuilder("ok", HttpStatus.OK, bookedList);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping("/customer/create-booking")
    public ResponseEntity<Object> createBooking(@RequestHeader("Authorization") String token,
                                                @RequestParam("roomId") String roomId,
                                                @RequestParam(value = "checkinDate", required = false) String checkInDay,
                                                @RequestParam("slots") List<Integer> slot,
                                                @RequestParam(value = "note", required = false) String note) {
        String jwtToken = token.substring(7);
        System.out.println(jwtToken);
        OrderBooking bookingResponse = orderBookingService.createOrderBooking(jwtToken, roomId, checkInDay, slot, note);
        return ResponseHandler.responseBuilder("ok", HttpStatus.CREATED, bookingResponse);
    }

    @PostMapping("/customer/create-multi-booking")
    public ResponseEntity<Object> createMultiBooking(@RequestHeader("Authorization") String token,
                                                     @RequestParam("buildingId") String buildingId,
                                                     @RequestParam("roomId") String roomId,
                                                     @RequestParam("checkinDate") String checkInDate,
                                                     @RequestParam("checkoutDate") String checkoutDate,
                                                     @RequestParam("slots") List<Integer> slots,
                                                     @RequestParam(required = false) MultiValueMap<String, String> items,
                                                     @RequestParam(value = "note", required = false) String note) {
        String jwtToken = token.substring(7);
        System.out.println(jwtToken);
        MultiValueMap<Integer, Integer> convertedItems = new LinkedMultiValueMap<>();

        // Chuyển đổi từ MultiValueMap<String, String> sang MultiValueMap<Integer, Integer>
        for (Map.Entry<String, List<String>> entry : items.entrySet()) {
            if (entry.getKey().startsWith("items[")) {
                // Tách lấy số từ khóa items[1], items[2], v.v.
                String keyString = entry.getKey().replace("items[", "").replace("]", "");
                Integer key = Integer.valueOf(keyString);
                // Chuyển đổi giá trị từ String sang Integer và thêm vào MultiValueMap
                for (String value : entry.getValue()) {
                    Integer quantity = Integer.valueOf(value);
                    convertedItems.add(key, quantity);  // Thêm vào MultiValueMap
                }
            }
        }

        OrderBooking bookingResponse = orderBookingService.createMultiOrderBooking(jwtToken, buildingId, roomId, checkInDate, checkoutDate, slots, convertedItems, note);
        return ResponseHandler.responseBuilder("ok", HttpStatus.CREATED, bookingResponse);
    }

    @GetMapping("/customer/history-booking")
    public ResponseEntity<Object> getCustomerHistoryBooking(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        try{
            List<OrderBookingDetailDTO> bookedList = orderBookingService.getCustomerHistoryBooking(jwtToken);
            return ResponseHandler.responseBuilder("ok", HttpStatus.OK, bookedList);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/customer/create-booking-service")
    public ResponseEntity<Object> createBookingService(@RequestHeader("Authorization") String token,
                                                       @RequestParam("roomId") String roomId,
                                                       @RequestParam(value = "checkin-date", required = false) String checkInDate,
                                                       @RequestParam("slots") List<Integer> slots,
                                                       @RequestParam(required = false) MultiValueMap<String, String> items,
                                                       @RequestParam(value = "note", required = false) String note) {
        String jwtToken = token.substring(7);
        System.out.println(jwtToken);
        System.out.println(roomId);
        System.out.println(slots);
        System.out.println(checkInDate);
        System.out.println("itemms:"+items);
        // Tạo một MultiValueMap<Integer, Integer> để chứa dữ liệu đã chuyển đổi
        MultiValueMap<Integer, Integer> convertedItems = new LinkedMultiValueMap<>();

        // Chuyển đổi từ MultiValueMap<String, String> sang MultiValueMap<Integer, Integer>
        for (Map.Entry<String, List<String>> entry : items.entrySet()) {
            if (entry.getKey().startsWith("items[")) {
                // Tách lấy số từ khóa items[1], items[2], v.v.
                String keyString = entry.getKey().replace("items[", "").replace("]", "");
                Integer key = Integer.valueOf(keyString);
                // Chuyển đổi giá trị từ String sang Integer và thêm vào MultiValueMap
                for (String value : entry.getValue()) {
                    Integer quantity = Integer.valueOf(value);
                    convertedItems.add(key, quantity);  // Thêm vào MultiValueMap
                }
            }
        }

        try{
            OrderBooking bookingResponse = orderBookingService.createOrderBookingService(jwtToken, roomId, checkInDate, slots, convertedItems, note);
            return ResponseHandler.responseBuilder("ok", HttpStatus.CREATED, bookingResponse );
        }catch (Exception e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/customer/create-multi-booking-without")
    public ResponseEntity<Object> createMultiBookingWithout(@RequestHeader("Authorization") String token,
                                                            @RequestParam("buildingId") String buildingId,
                                                            @RequestParam("roomId") String roomId,
                                                            @RequestParam("checkinDate") String checkInDate,
                                                            @RequestParam("checkoutDate") String checkoutDate,
                                                            @RequestParam("slots") Integer[] slots,
                                                            @RequestParam(value = "note", required = false) String note) {
        String jwtToken = token.substring(7);
        System.out.println(jwtToken);


        OrderBooking bookingResponse = orderBookingService.createOrderBookingWithout(jwtToken, buildingId, roomId, checkInDate, checkoutDate, slots, note);
        return ResponseHandler.responseBuilder("ok", HttpStatus.CREATED, bookingResponse);
    }


    @PutMapping("/customer/update-service")
    public ResponseEntity<Object> updateBookingService(@RequestParam("bookingId") String bookingId,
                                                       @RequestParam(required = false) MultiValueMap<String, String> items){
        MultiValueMap<Integer, Integer> convertedItems = new LinkedMultiValueMap<>();
        System.out.println(items);
        // Chuyển đổi từ MultiValueMap<String, String> sang MultiValueMap<Integer, Integer>
        for (Map.Entry<String, List<String>> entry : items.entrySet()) {
            if (entry.getKey().startsWith("items[")) {
                // Tách lấy số từ khóa items[1], items[2], v.v.
                String keyString = entry.getKey().replace("items[", "").replace("]", "");
                Integer key = Integer.valueOf(keyString);
                // Chuyển đổi giá trị từ String sang Integer và thêm vào MultiValueMap
                for (String value : entry.getValue()) {
                    Integer quantity = Integer.valueOf(value);
                    convertedItems.add(key, quantity);  // Thêm vào MultiValueMap
                }
            }
        }
        try{
             orderBookingService.updateServiceBooking(bookingId, convertedItems);
            return ResponseHandler.responseBuilder("Update success", HttpStatus.OK );
        }catch (Exception e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/customer/cancel-booking")
    public ResponseEntity<Object> cancelBooking(@RequestHeader("Authorization") String token,
                                                @RequestParam("bookingId") String bookingId) {
        String jwtToken = token.substring(7);
        System.out.println(jwtToken);
        try {
            String cancellationMessage = orderBookingService.cancelOrderBooking(jwtToken, bookingId);
            return ResponseHandler.responseBuilder("ok", HttpStatus.OK, cancellationMessage);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }
}







