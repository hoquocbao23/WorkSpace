package fpt.swp.WorkSpace.DTO;

import fpt.swp.WorkSpace.models.Room;
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
