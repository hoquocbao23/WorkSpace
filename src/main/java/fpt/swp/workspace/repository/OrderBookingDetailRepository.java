package fpt.swp.workspace.repository;

import fpt.swp.workspace.models.OrderBookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderBookingDetailRepository extends JpaRepository<OrderBookingDetail, Integer> {

    @Query("select b from OrderBookingDetail b where b.booking.bookingId = ?1")
    List<OrderBookingDetail> findDetailByBookingId(String bookingId);

    @Query("select b from OrderBookingDetail b where b.booking.bookingId = ?1 and b.service.serviceId = ?2")
    OrderBookingDetail findDetailByBookingIdAndServiceId(String bookingId, int serviceId);
}
