package fpt.swp.WorkSpace.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import fpt.swp.WorkSpace.DTO.RoomDTO;
import fpt.swp.WorkSpace.models.Room;
import fpt.swp.WorkSpace.models.RoomType;
import fpt.swp.WorkSpace.response.ResponseHandler;
import fpt.swp.WorkSpace.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
//@PreAuthorize("hasAuthority('MANAGER')")
public class RoomController {
    @Autowired
    private IRoomService roomService;


    @PostMapping("/manager/add-new-room")
    public ResponseEntity<Object> addNewRoom(@RequestParam(value = "buildingId", required = false) String buildingId,
                                             @RequestParam(value = "roomTypeId", required = false) String roomTypeId,
                                             @RequestParam(value = "roomName", required = false) String roomName,
                                             @RequestParam(value = "price", required = false) String price,
                                            @RequestParam(value = "image", required = false) MultipartFile image,
                                             @RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "description", required = false) String description,
                                             @RequestParam(value = "listStaffID", required = false) String[] listStaffID){
        // Use parameter instead of body because image is a file
        // file should be multipart/form-data
        // JSON cann't handle this file

        try{
            System.out.println(listStaffID.length);
            Room newRoom = roomService.addNewRoom(buildingId, roomTypeId, roomName, price, listStaffID, image ,description, status);
            return ResponseHandler.responseBuilder("Them phong thanh cong", HttpStatus.OK, newRoom);
        } catch (NullPointerException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/manager/add-new-room-img")
    public ResponseEntity<Object> addNewRoomImg(@RequestParam(value = "buildingId", required = false) String buildingId,
                                             @RequestParam(value = "roomTypeId", required = false) String roomTypeId,
                                             @RequestParam(value = "roomName", required = false) String roomName,
                                             @RequestParam(value = "price", required = false) String price,
                                             @RequestParam(value = "image", required = false) MultipartFile[] image,
                                             @RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "description", required = false) String description,
                                             @RequestParam(value = "listStaffID", required = false) String[] listStaffID){
        // Use parameter instead of body because image is a file
        // file should be multipart/form-data
        // JSON cann't handle this file

        try{
            System.out.println(listStaffID.length);
            Room newRoom = roomService.addNewRoomImg(buildingId, roomTypeId, roomName, price, listStaffID, image ,description, status);
            return ResponseHandler.responseBuilder("Them phong thanh cong", HttpStatus.OK, newRoom);
        } catch (NullPointerException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/room/{roomId}/img")
//    public ResponseEntity<Object> getRoomImg(@PathVariable String roomId){
//        try{
//
//            RoomDTO roomDTO = roomService.getRoomImg(roomId);
//            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, roomDTO);
//        } catch (NullPointerException e) {
//            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//    }

    @GetMapping("/room/img/{roomId}")
    public ResponseEntity<Object> getRoomImg(@PathVariable("roomId") String roomId){
        try{
            return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, roomService.getRoomImg(roomId));
        }catch (NoSuchElementException e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/get-all-room")
    public ResponseEntity<Object> getAllRoom(){
        List<Room> roomList = roomService.getAllRooms();
        if (roomList.isEmpty()){
            return ResponseHandler.responseBuilder("Khong co phong . Vui long them phong", HttpStatus.NO_CONTENT);
        }
        return ResponseHandler.responseBuilder("Success", HttpStatus.OK, roomList);
    }


    @GetMapping("/get-room-by-id/{roomId}")
    public ResponseEntity<Object> getRoomById(@PathVariable("roomId") String roomId){

        try {
            Room findRoom = roomService.getRoomById(roomId);
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, findRoom);
        }catch (Exception e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("customer/get-all-room-dto")
    public ResponseEntity<Object> getAllRoomDTO(){
        List<RoomDTO> roomList = roomService.getAllRoomsDTO();
        if (roomList.isEmpty()){
            return ResponseHandler.responseBuilder("Khong co phong . Vui long them phong", HttpStatus.NOT_FOUND);
        }
        return ResponseHandler.responseBuilder("Success", HttpStatus.OK, roomList);
    }

    @GetMapping("customer/get-room-by-building-status")
    public ResponseEntity<Object> getRoomByBuildingIdAndStatus(@RequestParam(value = "buildingId", required = false) String buildingId){
        try{
            List<RoomDTO> listRoom = roomService.getRoomsByBuildingAndStatus(buildingId, "AVAILABLE");
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, listRoom);
        }catch (NotFoundException e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("customer/room-detail/{roomId}")
    public ResponseEntity<Object> getRoomDetail(@PathVariable("roomId") String roomId){
        try {
            RoomDTO findRoom = roomService.getRoomsDTO(roomId);
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, findRoom);
        }catch (Exception e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("customer/get-room-by-building")
    public ResponseEntity<Object> viewRoomByBuildingId(@RequestParam(value = "buildingId", required = false) String buildingId){
        try{
            List<RoomDTO> listRoom = roomService.viewRoomsByBuildingId(buildingId);
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, listRoom);
        }catch (NotFoundException e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-room-by-building")
        public ResponseEntity<Object> getRoomByBuildingId(@RequestParam(value = "buildingId", required = false) String buildingId){
        try{
            List<Room> listRoom = roomService.getRoomsByBuildingId(buildingId);
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, listRoom);
        }catch (NotFoundException e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-room-by-type")
    public ResponseEntity<Object> getRoomByType(@RequestParam(value = "roomTypeName", required = false) String roomTypeName){
        try{
            List<RoomDTO> listRoom = roomService.getRoomsByRoomType(roomTypeName);
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, listRoom);
        }catch (NotFoundException e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/manager/update-room/{roomId}")
    public ResponseEntity<Object> updateRoom(@PathVariable String roomId,
                                             @RequestParam(value = "roomName", required = false) String roomName,
                                             @RequestParam(value = "price", required = false) String price,
//                                             @RequestParam(value = "image", required = false) MultipartFile image,
                                             @RequestParam(value = "status", required = false) String status,
                                             @RequestParam(value = "listStaffID", required = false) String[] listStaffID,
                                             @RequestParam(value = "description", required = false) String description) {

        try {

            Room newRoom = roomService.updateRoom(roomId, roomName, price, status, listStaffID, description);
            return ResponseHandler.responseBuilder("Cap nhap thanh cong", HttpStatus.OK, newRoom);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/manager/delete-room/{roomId}")
    public ResponseEntity<Object> deleteRoom(@PathVariable String roomId){
        try {
            roomService.deleteRoom(roomId);
            return ResponseHandler.responseBuilder("Da xoa thanh cong", HttpStatus.OK);
        }catch (Exception e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/manager/get-room-type")
    public ResponseEntity<Object> getRoomType(){
        try {
            List<RoomType> roomTypeList = roomService.getAllRoomType();
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, roomTypeList);
        }catch (Exception e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-room-building-and-type")
    public ResponseEntity<Object> getRoomBuildingAndType(@RequestParam("buildingId") String buildingId,
                                                         @RequestParam("roomTypeId") String roomTypeId){
        try {
            List<Room> roomList = roomService.getRoomsByBuildingAndRoomType(buildingId, roomTypeId);
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, roomList);
        }catch (Exception e){
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }


    }




}