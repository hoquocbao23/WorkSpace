package fpt.swp.workspace.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "manager")
@NoArgsConstructor
@Getter
@Setter
public class Manager {
    @Id
    private String userId;
    @OneToOne
    @MapsId // This ensures the `userId` is shared as the primary key
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonBackReference
    private User user;


    private String email;


    private String fullName;


    private String phoneNumber;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String roleName;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.AVAIABLE;

    
    private String buildingId;

}
