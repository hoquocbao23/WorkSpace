package fpt.swp.workspace.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OrderBookingResponse {

    private String bookingId;

    private String roomId;

    private List<Integer> slotId;

    private String customerId;

    private String createAt;

    private String checkinDate;

    private BigDecimal totalPrice;

    private String note;

    private String status;


}
