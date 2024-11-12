package fpt.swp.workspace.controller;

import fpt.swp.workspace.auth.AuthenticationResponse;
import fpt.swp.workspace.auth.LoginRequest;
import fpt.swp.workspace.auth.RegisterRequest;

import fpt.swp.workspace.response.ResponseHandler;
import fpt.swp.workspace.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private IAuthService service;

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request){
        AuthenticationResponse response = service.register(request);
        if (response.getStatusCode() ==  400){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request){
        AuthenticationResponse response = new AuthenticationResponse() ;
        try{
            response = service.login(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NullPointerException e) {
            response.setStatusCode(404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/auth/refresh-token")
    public AuthenticationResponse refresh(HttpServletRequest request){
        AuthenticationResponse authenticationResponse = service.refresh(request );
        return authenticationResponse;
    }

    @PostMapping("/auth/log-out")
    public ResponseEntity<AuthenticationResponse> logout(){
        return ResponseEntity.status(HttpStatus.OK).body(service.logout());
    }

    @PostMapping("/owner/create-account")
    public ResponseEntity<Object> createAccount(@RequestParam("userName") String userName,
                                                                @RequestParam("password") String password,
                                                                @RequestParam("role") String role,
                                                                @RequestParam("buildingId") String buildingId){
        try {
            AuthenticationResponse response = service.createAccount(userName, password, role, buildingId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
















}
