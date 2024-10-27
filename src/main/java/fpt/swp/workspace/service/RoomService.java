
package fpt.swp.workspace.service;

import com.amazonaws.services.kms.model.NotFoundException;
import fpt.swp.workspace.DTO.RoomDTO;
import fpt.swp.workspace.models.Building;
import fpt.swp.workspace.models.Room;
import fpt.swp.workspace.models.RoomType;
import fpt.swp.workspace.models.Staff;
import fpt.swp.workspace.repository.BuildingRepository;
import fpt.swp.workspace.repository.RoomRepository;
import fpt.swp.workspace.repository.RoomTypeRepository;
import fpt.swp.workspace.repository.StaffRepository;
import fpt.swp.workspace.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RoomService implements IRoomService{

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;


    @Override
    public Room addNewRoom(String buildingId, String romeTypeId, String roomName, String price, List<String> staffIdList, MultipartFile file, String description, String status) {
        String imgUrl = "";
        if (imgUrl != null && imgUrl.length() > 0) {
            imgUrl = awsS3Service.saveImgToS3(file);
        }

        Building findBuilding = buildingRepository.findById(buildingId).orElseThrow();
        RoomType roomType = roomTypeRepository.findById(romeTypeId).orElseThrow();
        if (findBuilding == null) {
            throw new NotFoundException("Khong tim thay co so");
        }
        if (roomType == null) {
            throw new NotFoundException("Loai phong khong hop le");
        }
        Room room = new Room();
        room.setRoomId(Helper.generateRoomId());
        room.setRoomName(roomName);
        room.setPrice(Float.parseFloat(price));
        room.setRoomImg(imgUrl);

        // set local day time
        String creationTime = Helper.convertLocalDateTime();
        room.setCreationTime(creationTime);

        // conver array to string

        List<Staff> staffList = new ArrayList<>();
        for(String staffID : staffIdList) {
            Staff staff = staffRepository.findById(staffID).get();
            staffList.add(staff);
        }
        room.setStaff(staffList);

        room.setStatus(status);
        room.setBuilding(findBuilding);
        room.setRoomType(roomType);
        room.setDescription(description);
        roomType.setQuantity(roomType.getQuantity() + 1);    // update quantity in roomtype
        roomTypeRepository.save(roomType);
        Room savedRoom = roomRepository.save(room);
        return savedRoom;
    }

    @Override
    public Room addNewRoomImg(String buildingId, String romeTypeId, String roomName, String price, List<String> staffIdList, MultipartFile[] img, String description, String status) {
        String imgUrl = "";
        if (img != null && img.length > 0) {
            imgUrl = awsS3Service.saveMultiImgToS3(img);
        }
        Building findBuilding = buildingRepository.findById(buildingId).orElseThrow();
        RoomType roomType = roomTypeRepository.findById(romeTypeId).orElseThrow();

        if (findBuilding == null) {
            throw new NotFoundException("Khong tim thay co so");
        }
        if (roomType == null) {
            throw new NotFoundException("Loai phong khong hop le");
        }
        Room room = new Room();
        room.setRoomId(Helper.generateRoomId());
        room.setRoomName(roomName);
        room.setPrice(Float.parseFloat(price));
        room.setRoomImg(imgUrl);

        // set local day time
        String creationTime = Helper.convertLocalDateTime();
        room.setCreationTime(creationTime);

        // conver array to string
        List<Staff> staffList = new ArrayList<>();
        for(String staffID : staffIdList) {
            Staff staff = staffRepository.findById(staffID).get();
            staffList.add(staff);
        }
        room.setStaff(staffList);

        room.setStatus(status);
        room.setBuilding(findBuilding);
        room.setRoomType(roomType);
        room.setDescription(description);
        roomType.setQuantity(roomType.getQuantity() + 1);    // update quantity in roomtype
        roomTypeRepository.save(roomType);
        Room savedRoom = roomRepository.save(room);
        return savedRoom;
    }

    @Override
    public RoomDTO getRoomImg(String roomID) throws NoSuchElementException {
        Room room = roomRepository.findById(roomID).orElseThrow(() -> new NoSuchElementException("Phong khong ton tai"));
//        if (room == null) {
//            throw new  NoSuchElementException("Phong khong ton tai");
//        }else {
        RoomDTO roomDTO = new RoomDTO();
        if (room.getRoomImg() != null && !room.getRoomImg().isEmpty()) {
            roomDTO.setRoomImg(room.getRoomImg().split(", "));
        } else {
            roomDTO.setRoomImg(new String[]{""});
        }
        return roomDTO;
        //}
    }

//    @Override
//    public RoomDTO getRoomImg(String roomID) {
//        Room room = roomRepository.findById(roomID).orElseThrow();
//        return Helper.mapRoomToDTO(room);
//
//    }


    @Override
    public List<Room> getAllRooms() {
        List<Room> roomList = roomRepository.findAll();
        try {
            if (roomList.isEmpty()) {
                throw new NotFoundException("Chua co phong nao!!!");
            }
        } catch(NotFoundException e) {

        }
        return roomList;
    }

    @Override
    public List<RoomDTO> getAllRoomsDTO() {
        List<Room> roomList = roomRepository.findAll();
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for (Room room : roomList) {
            RoomDTO roomDTO = Helper.mapRoomToDTO(room);
            roomDTOList.add(roomDTO);
        }
        return roomDTOList;
    }

    @Override
    public RoomDTO getRoomsDTO(String id) {
        Room room = roomRepository.findById(id).orElseThrow();
        RoomDTO roomDTO = new RoomDTO();
        if (room != null) {
            roomDTO = Helper.mapRoomToDTO(room);
        }
        return roomDTO;
    }


    @Override
    public Room getRoomById(String id) {
        Room room = roomRepository.findById(id).orElseThrow();
        if (room == null) {
            throw new NotFoundException("Phong khong ton tai");
        }
        return room;
    }



    @Override
    public List<Room> getRoomsByBuildingId(String buildingId) {
        List<Room> roomList = roomRepository.getRoomByBuilding(buildingId);
        if (roomList.isEmpty()) {
            throw new NotFoundException("Co so nay chua co phong");
        }
        return roomList;

    }

    @Override
    public List<RoomDTO> viewRoomsByBuildingId(String buildingId) {
        List<Room> roomList = roomRepository.getRoomByBuilding(buildingId);
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for (Room room : roomList) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setRoomId(room.getRoomId());
            roomDTO.setRoomName(room.getRoomName());
            roomDTO.setPrice(room.getPrice());
            roomDTO.setRoomImg(room.getRoomImg().split(","));
            roomDTO.setDescription(room.getDescription());
            roomDTOList.add(roomDTO);
        }
        return roomDTOList;
    }

    @Override
    public List<RoomDTO> getRoomsByBuildingAndStatus(String buildingId, String status) {
        List<Room> roomList = roomRepository.getRoomByBuildingAndStatus(buildingId, status);
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for (Room room : roomList) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setRoomId(room.getRoomId());
            roomDTO.setRoomName(room.getRoomName());
            roomDTO.setPrice(room.getPrice());
            roomDTO.setRoomImg(room.getRoomImg().split(","));
            roomDTO.setDescription(room.getDescription());
            roomDTOList.add(roomDTO);
        }
        return roomDTOList;
    }

    public List<RoomDTO> getRoomsByRoomType(String roomType) {
        List<Room> roomList = roomRepository.getRoomsByRoomType(roomType);
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for (Room room : roomList) {
            RoomDTO roomDTO = Helper.mapRoomToDTO(room);
            roomDTOList.add(roomDTO);
        }
        return roomDTOList;
    }



    @Override
    public List<Room> getRoomsByBuildingAndRoomType(String buildingId, String roomTypeId) {
        List<Room> roomList = roomRepository.getRoomsByBuildingAndRoomType(buildingId, roomTypeId);
        if (roomList.isEmpty()) {
            throw new NotFoundException("Khong co phong hop le");
        }
        return roomList;
    }


    @Override
    public Room updateRoom(String roomId, String roomName, String price, String status, MultipartFile[] file, List<String> staffIdList, String description) {
        String imgUrl = "";
        if (file != null && file.length > 0) {
            imgUrl = awsS3Service.saveMultiImgToS3(file);
        }
        Room room = roomRepository.findById(roomId).orElseThrow();
        if (roomName != null){
            room.setRoomName(roomName);
        }
        if (price != null) {
            room.setPrice(Float.parseFloat(price));
        }
        if (imgUrl.length() > 0){
            String roomImg = room.getRoomImg().concat(", ");
            roomImg = roomImg.concat(imgUrl);
            room.setRoomImg(roomImg);
        }
        if (status != null) {
            room.setStatus(status);
        }
        if (staffIdList != null) {
            List<Staff> staffList = new ArrayList<>();
            for(String staffID : staffIdList) {
                Staff staff = staffRepository.findById(staffID).get();
                staffList.add(staff);
            }
            room.setStaff(staffList);
        }
        if (description != null) {
            room.setDescription(description);
        }
        return roomRepository.save(room);
    }

    @Override
    public void updateRoomStatus(String roomId, String roomStatus){
        Room room = roomRepository.findById(roomId)
                .orElseThrow( () -> new NotFoundException("Room not found"));
        if( !room.getStatus().equals(roomStatus) ){
            room.setStatus(roomStatus);
            roomRepository.save(room);
        }else {
            throw new RuntimeException("Status has been set");
        }
    }


    @Override
    public void deleteRoom(String id) {
        Room room = roomRepository.findById(id).orElseThrow();
        if (room == null) {
            throw new RuntimeException("Room not found");
        }
        roomRepository.delete(room);
    }

    public List<RoomType> getAllRoomType(){
        return roomTypeRepository.findAll();
    }
}
