package fpt.swp.workspace.service;

import fpt.swp.workspace.auth.AuthenticationResponse;
import fpt.swp.workspace.auth.LoginRequest;
import fpt.swp.workspace.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

    AuthenticationResponse refresh(HttpServletRequest request);

    AuthenticationResponse logout();

    AuthenticationResponse createAccount(String username, String password, String role, String buldingId);

    String generateCustomerId();


    String generateManagerId();

    String generateStaffId();
}
