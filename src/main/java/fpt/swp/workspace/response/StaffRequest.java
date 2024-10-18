package fpt.swp.WorkSpace.response;

import fpt.swp.WorkSpace.models.StaffStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffRequest {
    private String userName;
    private String password;
    private String role;
    private String buildingId;

}
