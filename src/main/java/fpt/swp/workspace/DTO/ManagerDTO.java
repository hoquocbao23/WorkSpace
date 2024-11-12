package fpt.swp.workspace.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fpt.swp.workspace.models.User;
import fpt.swp.workspace.models.UserStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class ManagerDTO {

    private String userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Date dateOfBirth;
    private String roleName;
    private UserStatus status ;
    private String buildingId;
}
