package fpt.swp.WorkSpace.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import fpt.swp.WorkSpace.models.Customer;
import fpt.swp.WorkSpace.models.Room;
import fpt.swp.WorkSpace.models.TimeSlot;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
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
