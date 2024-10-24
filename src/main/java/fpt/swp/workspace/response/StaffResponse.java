package fpt.swp.workspace.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import fpt.swp.workspace.models.UserStatus;
import fpt.swp.workspace.models.WorkShift;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffResponse {
    private String userId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String  dateOfBirth;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createAt;

    private WorkShift workShift;

    private String startTime;
    private String endTime;

    private String workDays;
    private String buildingId;

    private UserStatus status;
}
