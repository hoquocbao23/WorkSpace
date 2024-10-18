package fpt.swp.WorkSpace.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.swp.WorkSpace.models.Room;
import fpt.swp.WorkSpace.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest  extends AbstractTestNGSpringContextTests {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RoomService roomService; // Giả lập service

    @InjectMocks
    private RoomController roomController; // Controller

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TEST CASE 01
    // DESCRIPTION: CHECK THE addNewRoomImg() METHOD WITH A VALID REQUEST
    //              Ensure that adding a new room image returns an HTTP 200 status code and the message "Them phong thanh cong".
    // STEPS/PROCEDURES: CALL addNewRoomImg() WITH VALID PARAMETERS INCLUDING A MOCK IMAGE FILE.
    // EXPECTED RESULT: RETURN HTTP STATUS CODE 200 AND THE MESSAGE "Them phong thanh cong".

//    @Test
//    public void addNewRoomImg_ShouldReturnOK_WhenValidRequest() throws Exception {
//        // Tạo một tệp hình ảnh giả lập
//        MockMultipartFile imageFile = new MockMultipartFile("image", "room.jpg", "image/jpeg", "image content".getBytes());
//
//        // Giả lập hành vi của roomService
//        Room mockRoom = new Room(); // Tạo một Room giả lập
//        mockRoom.setRoomId("1");
//
//        // Giả lập phương thức roomService.addNewRoomImg
//        when(roomService.addNewRoomImg(anyString(), anyString(), anyString(), anyString(), any(), any(), anyString(), anyString()))
//                .thenReturn(mockRoom);
//
//        // Thực hiện yêu cầu POST đến /manager/add-new-room-img
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/manager/add-new-room-img")
//                        .file(imageFile) // Gửi tệp hình ảnh
//                        .param("buildingId", "BD001")
//                        .param("roomTypeId", "RT001")
//                        .param("roomName", "Conference Room")
//                        .param("price", "100")
//                        .param("status", "Available")
//                        .param("description", "A spacious conference room")
//                        .param("listStaffID", ""))
//                .andExpect(status().isOk()) // Kiểm tra mã trạng thái 200
//                .andExpect(jsonPath("$.message").value("Them phong thanh cong")); // Kiểm tra thông điệp
//    }

    // TEST CASE 02
    // DESCRIPTION: CHECK THE getAllRoom() METHOD WHEN NO ROOMS ARE AVAILABLE
    //              Ensure that a GET request when no rooms are available returns an HTTP 204 status code.
    // STEPS/PROCEDURES: CALL getAllRoom() WITH AN EMPTY ROOM LIST.
    // EXPECTED RESULT: RETURN HTTP STATUS CODE 204 AND THE MESSAGE "Khong co phong. Vui long them phong".
    @Test
    public void getAllRoom_ShouldReturnNotFound_WhenNoRoomsAvailable() throws Exception {
        // Giả lập hành vi của roomService
        when(roomService.getAllRooms()).thenThrow(new NotFoundException("Chưa có phòng nào!!!"));// Trả về danh sách trống

        // Thực hiện yêu cầu GET đến /get-all-room
        mockMvc.perform(get("/api/get-all-room"))
                .andExpect(status().isNoContent()) // Kiểm tra mã trạng thái 204
                .andExpect(jsonPath("$.message").value("Khong co phong . Vui long them phong")); // Kiểm tra thông điệp
    }

    // TEST CASE 03
    // DESCRIPTION: CHECK THE getAllRoom() METHOD WHEN ROOMS ARE AVAILABLE
    //              Ensure that a GET request when rooms are available returns an HTTP 200 status code and a success message.
    // STEPS/PROCEDURES: CALL getAllRoom() WITH A MOCK LIST OF ROOMS.
    // EXPECTED RESULT: RETURN HTTP STATUS CODE 200 AND THE MESSAGE "Success".

    @Test
    public void getAllRoom_ShouldReturnOK_WhenRoomsAreAvailable() throws Exception {
        // Tạo danh sách phòng giả lập
        Room room1 = new Room();
        room1.setRoomId("S001");
        room1.setRoomName("Phòng đơn 1");
        //room1.setPrice(50);

        Room room2 = new Room();
        room2.setRoomId("S002");
        List<Room> mockRoomList = List.of(room1, room2); // Danh sách phòng giả lập

        // Giả lập hành vi của roomService
        when(roomService.getAllRooms()).thenReturn(mockRoomList); // Trả về danh sách giả lập

        // Thực hiện yêu cầu GET đến /get-all-room
        mockMvc.perform(get("/api/get-all-room"))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái 200
                .andExpect(jsonPath("$.message").value("Success"));
    }




}