package fpt.swp.WorkSpace.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildingRequest {
    private String buildingName;
    private String buildingLocation;
    private String phoneContact;
}
