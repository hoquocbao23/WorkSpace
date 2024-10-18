package fpt.swp.WorkSpace.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "customer")
@Getter
@Setter

public class Customer  {

    @Id
    private String userId;
    @OneToOne
    @MapsId // This ensures the `userId` is shared as the primary key
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private String fullName;

    private String phoneNumber;
    private String email;
    private Date dateOfBirth;
    private String roleName;

    private String imgUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_id")
    @JsonManagedReference
    private Wallet wallet;



    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "membership_id", referencedColumnName = "membership_id")
    private UserNumberShip membership;

    public String getRoleName() {
        return user.getRoleName();
    }

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<OrderBooking> bookingsList;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Payment> payments = new ArrayList<>();
}
