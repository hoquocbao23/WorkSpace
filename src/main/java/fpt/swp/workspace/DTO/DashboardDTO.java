package fpt.swp.workspace.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import fpt.swp.workspace.models.BookingStatus;
import lombok.Data;

import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DashboardDTO {
    private int totalSpace;
    private int totalFreeSpace;
    private int totalBookingSlot;
    private int totalRoom;
    private Float revenue;

    private Map<String, Integer> roomTypeAnalyst;
    private Map<String, Integer> bookingAnalyst;

}
