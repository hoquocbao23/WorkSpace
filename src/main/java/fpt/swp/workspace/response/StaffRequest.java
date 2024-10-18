package fpt.swp.workspace.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
