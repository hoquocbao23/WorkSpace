package fpt.swp.workspace.service;

import fpt.swp.workspace.DTO.BookedSlotDTO;
import fpt.swp.workspace.DTO.CustomerServiceDTO;
import fpt.swp.workspace.DTO.OrderBookingDetailDTO;
import fpt.swp.workspace.models.BookingStatus;
import fpt.swp.workspace.models.OrderBooking;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface IOrderBookingService {
    List<OrderBookingDetailDTO> getBookedSlotByRoomAndDate(String date, String roomId);

    List<OrderBookingDetailDTO> getBookedSlotByDate(String date);

    List<OrderBookingDetailDTO> getBookedSlotByCheckinAndCheckout(String checkin, String checkout, String roomId);

    BookedSlotDTO getBookedSlotByEachDay(String checkin, String checkout, String roomId, String buildingId);

    OrderBooking createOrderBooking(String jwttoken, String roomId, String date, List<Integer> slotBooking, String note);

    OrderBooking createOrderBookingWithout(String jwttoken, String buildingId, String roomId, String checkin, String checkout, Integer[] slotBooking, String note);

    OrderBooking createMultiOrderBooking(String jwttoken, String buildingId, String roomId, String checkin, String checkout, List<Integer> slotBooking, MultiValueMap<Integer, Integer> items, String note);

    List<OrderBookingDetailDTO> getCustomerHistoryBooking(String jwttoken);

    OrderBooking createOrderBookingService(String jwttoken, String roomId, String date, List<Integer> slotBooking, MultiValueMap<Integer, Integer> items, String note);


    void updateServiceBooking(String orderBookingId, MultiValueMap<Integer, Integer> items);

    CustomerServiceDTO getCustomerService(String orderBookingId);

    void cancelOrderBooking(String jwttoken, String orderBookingId);

    List<OrderBookingDetailDTO> getPendingBooking();



}
