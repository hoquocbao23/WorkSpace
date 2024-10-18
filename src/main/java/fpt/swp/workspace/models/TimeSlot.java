package fpt.swp.WorkSpace.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "timeslot")
@Data
public class TimeSlot {

    @Id
    private int timeSlotId;

    @Column(name = "time_start", nullable = false)
    private Time timeStart ;

    @Column(name = "time_end", nullable = false)
    private Time timeEnd;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
        public TimeSlotStatus status = TimeSlotStatus.AVAILABLE;

    @ManyToMany(mappedBy = "slot")
    @JsonIgnore
    private List<OrderBooking> orderBookings ;
}
