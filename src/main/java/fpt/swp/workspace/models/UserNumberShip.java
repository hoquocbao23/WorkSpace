package fpt.swp.workspace.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membership")
@Getter
@Setter
@NoArgsConstructor
public class UserNumberShip {

    @Id
    @Column(name = "membership_id", length = 10, nullable = false)
    private String membershipId;

    @Column(name = "membership_name", length = 20, nullable = false)
    private String membershipName;

    @Column(nullable = false)
    private float discount;

    @Column(name = "utilities", length = 100)
    private String utilities;

    private int amount;

    @OneToMany(mappedBy = "membership", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Customer> customers = new ArrayList<>();

}
