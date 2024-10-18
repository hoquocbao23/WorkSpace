package fpt.swp.workspace.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orderbookingdetail")
@Data
public class OrderBookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingDetailId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    private OrderBooking booking;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonBackReference
    private ServiceItems service;

    private int bookingServiceQuantity;
    private float bookingServicePrice;


}
