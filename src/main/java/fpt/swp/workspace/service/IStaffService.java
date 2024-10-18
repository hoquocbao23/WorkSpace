package fpt.swp.WorkSpace.service;

import fpt.swp.WorkSpace.DTO.RoomDTO;
import fpt.swp.WorkSpace.models.Staff;

import java.util.List;

public interface IStaffService {
    List<Staff> getAllStaff();

    List<RoomDTO> getRoomsAssigned();
}
