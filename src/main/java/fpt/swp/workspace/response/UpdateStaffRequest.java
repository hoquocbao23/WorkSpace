package fpt.swp.workspace.response;

import fpt.swp.workspace.models.StaffStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStaffRequest {
    private String fullName;
    private String phoneNumber;
    private Date dateOfBirth;
    private String email;
    private String workShift;
    private String workDays;
    private String buildingId;
    private StaffStatus status;
}
