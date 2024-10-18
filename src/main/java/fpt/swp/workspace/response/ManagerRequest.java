package fpt.swp.WorkSpace.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerRequest {
    private String email;
    private String fullName;
    private String phoneNumber;
    private Date dateOfBirth;
    private String roleName;
    private String buildingId;
    private String userId;
}
