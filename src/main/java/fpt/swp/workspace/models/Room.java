package fpt.swp.WorkSpace.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity
@Table(name = "room")
@Data
@NoArgsConstructor
public class Room {

    @Id
    private String roomId;

    private String roomName;

    private float price;

    @Column(nullable = false)
    private String creationTime;

    private String roomImg;



    @ManyToMany
    @JoinTable(name = "Room_Staff",
            joinColumns = @JoinColumn(name = "roomId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private List<Staff> staff;

    private String description;

    private String status;

    @ManyToOne
    @JoinColumn(name = "buildingId")
    @JsonManagedReference// Ngăn ngừa vòng lặp
    private Building building;

    @ManyToOne
    @JoinColumn(name = "roomTypeID")
    @JsonManagedReference// Ngăn ngừa vòng lặp
    private RoomType roomType;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<OrderBooking> bookingList;




}
