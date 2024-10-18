package fpt.swp.workspace.service;

import fpt.swp.workspace.DTO.RoomDTO;
import fpt.swp.workspace.models.Staff;

import java.util.List;

public interface IStaffService {
    List<Staff> getAllStaff();

    List<RoomDTO> getRoomsAssigned();
}
