package fpt.swp.WorkSpace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.swp.WorkSpace.auth.LoginRequest;
import fpt.swp.WorkSpace.auth.RegisterRequest;
import fpt.swp.WorkSpace.service.AuthService;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.NoSuchElementException;

import static io.restassured.RestAssured.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.*;
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // TEST CASE 01
    // DESCRIPTION: CHECK THE register() METHOD WITH A VALID REQUEST
    //              Ensure that a valid registration request returns an HTTP 200 status code.
    // STEPS/PROCEDURES: CALL register() WITH ARG registerRequest INCLUDING username, password, fullName, phoneNumber, and role.
    // EXPECTED RESULT: RETURN HTTP STATUS CODE 200 AND STATUS CODE 200.

//    @Test
//    public void register_ShouldReturnOK_WhenValidRequest() throws Exception {
//        // Tạo một yêu cầu đăng ký hợp lệ
//        RegisterRequest registerRequest = new RegisterRequest();
//        registerRequest.setUserName("bao02");
//        registerRequest.setPassword("123456");
//        registerRequest.setFullName("Quoc Bao");
//        registerRequest.setPhoneNumber("091273485");
//        registerRequest.setRole("CUSTOMER");
//
//        // Chuyển request thành JSON
//        String requestJson = objectMapper.writeValueAsString(registerRequest);
//
//        // Thực hiện POST request và kiểm tra phản hồi
//        mockMvc.perform(post("/api/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isOk()) // Kiểm tra mã trạng thái HTTP là 200
//                .andExpect(jsonPath("$.statusCode").value(200));
//    }


    // TEST CASE 02
    // DESCRIPTION: CHECK THE register() METHOD WITH AN INVALID REQUEST
    //              Ensure that a request missing necessary information returns an HTTP 400 status code.
    // STEPS/PROCEDURES: CALL register() WITH ARG registerRequest MISSING username.
    // EXPECTED RESULT: RETURN HTTP STATUS CODE 400 AND STATUS CODE 400.

    @Test
    public void register_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        // Tạo yêu cầu không hợp lệ (ví dụ thiếu username)
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword("123456"); // Thiếu username

        String requestJson = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest()) // Kiểm tra mã trạng thái HTTP 400
                .andExpect(jsonPath("$.statusCode").value(400));
    }


    // TEST CASE 03
    // DESCRIPTION: CHECK THE login() METHOD WITH A VALID LOGIN REQUEST
    //              Ensure that a valid login request returns an HTTP 200 status code.
    // STEPS/PROCEDURES: CALL login() WITH ARG loginRequest INCLUDING valid username and password.
    // EXPECTED RESULT: RETURN HTTP STATUS CODE 200 AND roleName "CUSTOMER".

    @Test
    public void login_ShouldReturnOK_WhenValidLogin() throws Exception {


        // Tạo yêu cầu login hợp lệ
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("bao1");
        loginRequest.setPassword("123456");

        String requestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk()) // Kiểm tra mã trạng thái HTTP 200
                .andExpect(jsonPath("$.data.roleName").value("CUSTOMER")); // Kiểm tra roleName
    }

    // TEST CASE 04
    // DESCRIPTION: CHECK THE login() METHOD WITH A NON-EXISTENT USER
    //              Ensure that a login request with a non-existent user returns an HTTP 404 status code.
    // STEPS/PROCEDURES: CALL login() WITH ARG loginRequest INCLUDING non-existent username and password.
    // EXPECTED RESULT: RETURN HTTP STATUS CODE 404 AND STATUS CODE 404.
    @Test
    public void login_ShouldReturnNotFound_WhenUserNotFound() throws Exception {
        // Tạo yêu cầu login không tồn tại
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("nonexistentuser");
        loginRequest.setPassword("wrongpassword");

        String requestJson = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound()) // Kiểm tra mã trạng thái HTTP 404
                .andExpect(jsonPath("$.statusCode").value(404)); // Kiểm tra statusCode là 404
    }

    // TEST CASE 05
    // DESCRIPTION: CHECK THE login() METHOD TO TRIGGER NullPointerException
    //              Ensure that a login request with invalid data triggers a NullPointerException.
    // STEPS/PROCEDURES: CALL login() WITH ARG loginRequest INCLUDING valid username and password.
    // EXPECTED RESULT: SHOULD TRIGGER NullPointerException.


    @Test
    public void login_ShouldReturnNullPointerException_WhenUserNotFound() throws Exception {
        // Tạo yêu cầu login không tồn tại
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("bao1");
        loginRequest.setPassword("123456");

        String requestJson = objectMapper.writeValueAsString(loginRequest);

        //authService.login(loginRequest);
//        assertThrows(NullPointerException.class, () -> authService.login(loginRequest));

//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestJson))
//                .andExpect(status().isNotFound()) // Kiểm tra mã trạng thái HTTP 404
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NullPointerException)); // Kiểm tra nếu có NullPointerException
    }

    // TEST CASE 06
    // DESCRIPTION: CHECK THE login() METHOD WITH A VALID LOGIN REQUEST USING RestAssured
    //              Ensure that a valid login request returns an HTTP 200 status code.
    // STEPS/PROCEDURES: CALL login() WITH ARG INCLUDING valid username and password via RestAssured.
    // EXPECTED RESULT: RETURN HTTP STATUS CODE 200.

    @Test
    public void login_returnValidResponse_WhenValidLogin() throws Exception {
        RequestSpecification request = given();
        request.baseUri("http://localhost:8080/api/auth")
                .accept("application/json")
                .contentType("application/json")
                .body("{\n" +
                        "  \"userName\": \"bao1\",\n" +
                        "  \"password\": \"123456\"\n" +
                        "}");

        //Thực hiện phương thức post() để gửi dữ liệu đi
        Response response = request.when().post("/login");
        response.prettyPrint();

        response.then().statusCode(200);
    }


    // TEST CASE 07
    // DESCRIPTION: CHECK THE login() METHOD WITH AN INVALID LOGIN REQUEST USING RestAssured
    //              Ensure that an invalid login request returns an HTTP 404 status code.
    // STEPS/PROCEDURES: CALL login() WITH ARG INCLUDING invalid username and password via RestAssured.
    // EXPECTED RESULT: RETURN HTTP STATUS CODE 404.
    @Test
    public void login_returnInvalidValidResponse_WhenInvalidValidLogin() throws Exception {
        RequestSpecification request = given();
        request.baseUri("http://localhost:8080/api/auth")
                .accept("application/json")
                .contentType("application/json")
                .body("{\n" +
                        "  \"userName\": \"bao\",\n" +
                        "  \"password\": \"123456\"\n" +
                        "}");

        //Thực hiện phương thức post() để gửi dữ liệu đi
        Response response = request.when().post("/login");
        response.prettyPrint();

        response.then().statusCode(404);
    }







}