package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.OrderBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderBookingRepository extends JpaRepository<OrderBooking, String> {
//    @Query("SELECT b FROM OrderBooking b where ( b.checkinDate = ?1 and b.room.roomId = ?2) ")
//    List<OrderBooking> getTimeSlotBookedByRoomAndDate(String checkinDate, String roomId );
//
//    @Query("SELECT b FROM OrderBooking b where ( b.checkinDate = ?1 ) ")
//    List<OrderBooking> getTimeSlotBookedByDate(String checkinDate);

    @Query("SELECT b FROM OrderBooking b WHERE (b.checkinDate <= ?1 AND b.checkoutDate >= ?1) AND (b.room.roomId = ?2)")
    List<OrderBooking> findTimeSlotBookedByRoomAndDate(String checkinDate, String roomId );

    // check date between check in and check out date
    @Query("SELECT b FROM OrderBooking b WHERE  b.checkinDate <= ?1 AND b.checkoutDate >= ?1 ")
    List<OrderBooking> findBookingsByDate(String booking);

    @Query("SELECT b FROM OrderBooking b WHERE  (b.checkinDate <= ?1 AND b.checkoutDate >= ?1) AND b.building.buildingId = ?2 ")
    List<OrderBooking> findBookingsByDate(String booking, String buildingId);


    @Query("SELECT b FROM OrderBooking b WHERE  b.checkinDate <= :checkout AND b.checkoutDate >= :checkin ")
    List<OrderBooking> findBookingsByInOutDate(@Param("checkin") String checkinDate,
                                               @Param("checkout") String checkoutDate);

    @Query("SELECT b FROM OrderBooking b where (b.customer.user.userName = ?1) order by b.createAt desc ")
    List<OrderBooking> getCustomerHistoryBooking(String username );

    @Query("SELECT b FROM OrderBooking b WHERE b.customer.userId = ?1")
    Page<OrderBooking> findByCustomerCustomerId(String userId, Pageable pageable);

    @Query("SELECT b FROM OrderBooking b WHERE b.bookingId = ?1")
    Optional<OrderBooking> findByOrderId(String bookingId);
}
