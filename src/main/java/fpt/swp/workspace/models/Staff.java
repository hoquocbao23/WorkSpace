package fpt.swp.workspace.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "staff")
@NoArgsConstructor
@Getter
@Setter
public class Staff {

    @Id
    private String userId;
    @OneToOne(cascade = CascadeType.ALL)
    @MapsId // This ensures the `userId` is shared as the primary key
    @JoinColumn(name = "user_id", referencedColumnName = "user_Id")
    @JsonIgnore
    private User user;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "email", length = 45)
    private String email;

    @Column(name = "work_shift", length = 30)
    @Enumerated(EnumType.STRING)
    private WorkShift workShift;

    @Column(name = "work_days", length = 100)
    private String workDays;

    @Column(name = "building_WsID")
    private String buildingId;


    private String roleName;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.AVAILABLE;

    @ManyToMany(mappedBy = "staff")
    @JsonIgnore
    private List<Room> rooms;
}
