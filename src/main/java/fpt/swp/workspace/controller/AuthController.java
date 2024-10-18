package fpt.swp.WorkSpace.controller;

import fpt.swp.WorkSpace.auth.AuthenticationResponse;
import fpt.swp.WorkSpace.auth.LoginRequest;
import fpt.swp.WorkSpace.auth.RegisterRequest;

import fpt.swp.WorkSpace.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
















}
