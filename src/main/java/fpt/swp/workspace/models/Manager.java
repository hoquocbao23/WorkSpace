package fpt.swp.WorkSpace.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "mangager")
@NoArgsConstructor
@Getter
@Setter
public class Manager {
    @Id
    @Column(name = "manager_id", length = 30, nullable = false)
    private String managerId;

    @Column(name = "Email", length = 50, nullable = false)
    private String email;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "Create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "role_name", length = 45, nullable = false)
    private String roleName;

    @Column(name = "building_WsID")
    private String buildingId;

    @Column(name = "user_UserID")
    private String userId;
}
