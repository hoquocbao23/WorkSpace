package fpt.swp.WorkSpace.response;

import fpt.swp.WorkSpace.models.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBookingStaffTracking {
    private String customerId;
    private String customerName;
    private String bookingId;
    private String roomId;
    private List<Integer> serviceIds;
    private List<Integer> slotIds;
    private BookingStatus status;
}
