package fpt.swp.workspace.DTO;

import fpt.swp.workspace.models.Room;
import lombok.Data;

import java.util.List;

@Data
public class BuildingDTO {
    private String buildingId;
    private String buildingName;
    private String buildingLocation;
    private String phoneContact;
    private List<Room> rooms;
}
