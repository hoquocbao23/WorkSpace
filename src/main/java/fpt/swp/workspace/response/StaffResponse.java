package fpt.swp.workspace.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fpt.swp.workspace.models.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffResponse {
    private String userId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String  dateOfBirth;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createAt;

    private String workShift;
    private String workDays;
    private String buildingId;

    private UserStatus status;
}
