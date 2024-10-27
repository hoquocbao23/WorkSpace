package fpt.swp.workspace.service;

import fpt.swp.workspace.auth.AuthenticationResponse;
import fpt.swp.workspace.auth.LoginRequest;
import fpt.swp.workspace.auth.RegisterRequest;

import fpt.swp.workspace.models.*;
import fpt.swp.workspace.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private WalletRepository customerWalletRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        AuthenticationResponse response = new AuthenticationResponse();
        User newUser = new User();
        try {
            User findUser = repository.findByuserName(request.getUserName());
            if (findUser != null) {
                throw new RuntimeException("Người dùng đã tồn tại");
            }

            //case CUSTOMER
            if (request.getRole().equals("CUSTOMER")) {

                // create a wallet for customer
                Wallet wallet = new Wallet();
                String walletId = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
                wallet.setWalletId(walletId);
                Wallet customerWallet = customerWalletRepository.save(wallet);

                // insert to user table
                newUser.setUserId(generateCustomerId());
                newUser.setUserName(request.getUserName());
                newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                newUser.setCreationTime(LocalDateTime.now());
                newUser.setRoleName(request.getRole());
                User result = repository.save(newUser);

                // insert to customer table
                Customer newCustomer = new Customer();
                newCustomer.setUser(result);
                newCustomer.setFullName(request.getFullName());
                newCustomer.setEmail(request.getEmail());
                newCustomer.setPhoneNumber(request.getPhoneNumber());
                newCustomer.setDateOfBirth(request.getDateOfBirth());
                newCustomer.setWallet(customerWallet);
                customerRepository.save(newCustomer);
                if (result.getUserId() != null) {
                    response.setStatus("Thành công");
                    response.setStatusCode(200);
                    response.setMessage("Đăng ký thành công");
                    response.setData(result);
                    response.setRefresh_token(jwtService.generateAccessToken(new HashMap<>(), request.getUserName()));
                    response.setAccess_token(jwtService.generateRefreshToken(request.getUserName()));
                    response.setExpired("1 DAY");
                }
            }

        } catch (Exception e) {
            response.setStatus("Error");
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        AuthenticationResponse response = new AuthenticationResponse();
//        try{

        User user = repository.findByuserName(request.getUserName());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new NullPointerException("Username hoặc password không đúng");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        String jwt = jwtService.generateAccessToken(new HashMap<>(), user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        response.setStatusCode(200);
        response.setMessage("Đăng nhập thành công");
        response.setData(user);
        response.setAccess_token(jwt);
        response.setRefresh_token(refreshToken);
        return response;

//        }catch (NullPointerException e){
//            response.setStatusCode(404);
//            response.setMessage(e.getMessage());
//            response.setStatus("Error");
//            return response;
//        }


    }

    @Override
    public AuthenticationResponse refresh(HttpServletRequest request) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        // get header
        final String authHeader = request.getHeader("Authorization");
        final String refreshToken;     // our token
        final String userName;

        // check JWT Token
        if (authHeader != null || authHeader.startsWith("Bearer ")) {
            refreshToken = authHeader.substring(7);
            //extract the username from JWT token
            userName = jwtService.extractUsername(refreshToken);
            // Check validtation of token
            if (userName != null) {
                UserDetails userDetails = repository.findByuserName(userName);
                if (jwtService.isTokenValid(refreshToken, userDetails)) {
                    String accessToken = jwtService.generateAccessToken(new HashMap<>(), userDetails.getUsername());
                    authenticationResponse.setStatusCode(200);
                    authenticationResponse.setAccess_token(accessToken);
                    authenticationResponse.setRefresh_token(refreshToken);
                }
            }
        }
        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse logout() {
        AuthenticationResponse response = new AuthenticationResponse();
        response.setStatus("Successfully");
        response.setStatusCode(200);
        response.setMessage("Successfully Logged Out");
        return response;
    }


    @Override
    public String generateCustomerId() {
        // Query the latest customer and extract their ID to increment
        long latestCustomerId = customerRepository.count();
        if (latestCustomerId != 0) {

            long newId = latestCustomerId + 1;
            return "CUS" + String.format("%04d", newId); // Format to 4 digits
        } else {
            return "CUS0001"; // Start from CUS0001 if no customers exist
        }
    }

    @Override
    public String generateManagerId() {
        // Query the latest customer and extract their ID to increment
        long latestManagerId = managerRepository.count();
        if (latestManagerId != 0) {

            long newId = latestManagerId + 1;
            return "MA" + String.format("%04d", newId); // Format to 4 digits
        } else {
            return "MA0001"; // Start from CUS0001 if no customers exist
        }
    }

    @Override
    public String generateStaffId() {
        // Query the latest customer and extract their ID to increment
        long latestStaffId = staffRepository.count();
        if (latestStaffId != 0) {

            long newId = latestStaffId + 1;
            return "ST" + String.format("%04d", newId); // Format to 4 digits
        } else {
            return "ST0001"; // Start from CUS0001 if no customers exist
        }
    }

    public AuthenticationResponse createAccount(String username, String password, String role, String buldingId) {
        AuthenticationResponse response = new AuthenticationResponse();
        User newUser = new User();

        User findUser = repository.findByuserName(username);
        if (findUser != null) {
            throw new RuntimeException("Account already exists");
        }

        if (role.equalsIgnoreCase("MANAGER")) {

            newUser.setUserId(generateManagerId());
            newUser.setUserName(username);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setCreationTime(LocalDateTime.now());
            newUser.setRoleName(role);
            User manager = repository.save(newUser);

            Manager newManager = new Manager();
            newManager.setUser(manager);
            newManager.setRoleName(role);
            newManager.setBuildingId(buldingId);
            newManager.setStatus(UserStatus.AVAILABLE);
            managerRepository.save(newManager);

        }else if (role.equalsIgnoreCase("STAFF")) {
            newUser.setUserId(generateStaffId());
            newUser.setUserName(username);
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setCreationTime(LocalDateTime.now());
            newUser.setRoleName(role);
            User staff = repository.save(newUser);

            Staff newStaff = new Staff();
            newStaff.setUser(staff);
            newStaff.setRoleName(role);
            newStaff.setStatus(UserStatus.ACTIVE);
            newStaff.setBuildingId(buldingId);
            staffRepository.save(newStaff);
        }

        if (newUser.getUserId() != null) {
                response.setStatus("Success");
                response.setStatusCode(200);
                response.setMessage("User Saved Successfully");
                response.setData(newUser);
        }

        return response;

    }


    public User getUser(String token) {
        String username = jwtService.extractUsername(token);
        User user = repository.findByuserName(username);
        return user;
    }
}