package fpt.swp.WorkSpace.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import fpt.swp.WorkSpace.models.StaffStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


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

    private StaffStatus status;
}
