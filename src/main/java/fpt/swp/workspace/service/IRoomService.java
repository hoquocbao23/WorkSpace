package fpt.swp.WorkSpace.service;

import fpt.swp.WorkSpace.DTO.RoomDTO;
import fpt.swp.WorkSpace.models.Room;
import fpt.swp.WorkSpace.models.RoomType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IRoomService {

    Room addNewRoom(String buildingId, String romeTypeId, String roomName, String price, String[] staffID, MultipartFile img, String description, String status);

    Room addNewRoomImg(String buildingId, String romeTypeId, String roomName, String price, String[] staffID, MultipartFile[] img, String description, String status);

    RoomDTO getRoomImg(String roomID);

    List<Room> getAllRooms();

    List<RoomDTO> getAllRoomsDTO();

    RoomDTO getRoomsDTO(String id);

    Room getRoomById(String id);

    List<Room> getRoomsByBuildingId(String buildingId);

    List<RoomDTO> viewRoomsByBuildingId(String buildingId);

    List<RoomDTO> getRoomsByBuildingAndStatus(String buildingId, String status);

    List<RoomDTO> getRoomsByRoomType(String roomTypeName);


    List<Room> getRoomsByBuildingAndRoomType(String buildingId, String roomTypeId);

    List<RoomType> getAllRoomType();

    Room updateRoom(String roomId, String roomName, String price, String status, String[] staffId, String description);

    void deleteRoom(String id);

}
