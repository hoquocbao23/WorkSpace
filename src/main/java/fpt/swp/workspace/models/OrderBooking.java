package fpt.swp.WorkSpace.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "orderbooking")
@Data
public class OrderBooking {

        @Id
        private String bookingId;


        @ManyToOne
        @JoinColumn(name = "room_id")
        @JsonManagedReference
        private Room room;

        @ManyToOne
        @JoinColumn(name = "building_id")
        @JsonIgnore
        private Building building;

        @ManyToMany
        @JoinTable(name = "OrderBooking_TimeSlot",
                joinColumns = @JoinColumn(name = "booking_id"),
                inverseJoinColumns = @JoinColumn(name = "timeslot_id"))
        private List<TimeSlot> slot;


        @ManyToOne
        @JoinColumn(name = "customer_id" )
        @JsonBackReference
        private Customer customer;

        private String createAt;

        @Column(nullable = false)
        private String checkinDate;

        @Column(nullable = false)
        private String checkoutDate;

        @Column(nullable = false)
        private float totalPrice;

        private String note;

        @Enumerated(EnumType.STRING)
        private BookingStatus status ;

        @OneToMany(mappedBy = "booking")
        @JsonIgnore
        private List<OrderBookingDetail> orderBookingDetails;
}
