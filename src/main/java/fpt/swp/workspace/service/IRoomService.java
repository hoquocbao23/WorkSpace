package fpt.swp.workspace.service;

import fpt.swp.workspace.DTO.RoomDTO;
import fpt.swp.workspace.models.Room;
import fpt.swp.workspace.models.RoomType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IRoomService {

    Room addNewRoom(String buildingId, String romeTypeId, String roomName, String price, List<String> staffIdList, MultipartFile img, String description, String status);

    Room addNewRoomImg(String buildingId, String romeTypeId, String roomName, String price, List<String> staffIdList, MultipartFile[] img, String description, String status);

    RoomDTO getRoomImg(String roomID);

    List<Room> getAllRooms(String jwt);

    List<RoomDTO> getAllRoomsDTO();

    RoomDTO getRoomsDTO(String id);

    Room getRoomById(String id);

    List<Room> getRoomsByBuildingId(String token);

    List<RoomDTO> viewRoomsByBuildingId(String buildingId);

    List<RoomDTO> getRoomsByBuildingAndStatus(String buildingId, String status);

    List<RoomDTO> getRoomsByRoomType(String roomTypeName);


    List<Room> getRoomsByBuildingAndRoomType(String buildingId, String roomTypeId);

    List<RoomType> getAllRoomType();

//    Room updateRoom(String roomId, String roomName, String price, String status, String[] staffId, String description);

    Room updateRoom(String roomId, String roomName, String price, String status, MultipartFile[] file, List<String> staffIdList, String description);

    void updateRoomStatus(String roomId, String roomStatus);

    void deleteRoom(String id);

}
