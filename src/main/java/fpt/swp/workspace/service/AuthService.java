package fpt.swp.WorkSpace.service;

import fpt.swp.WorkSpace.auth.AuthenticationResponse;
import fpt.swp.WorkSpace.auth.LoginRequest;
import fpt.swp.WorkSpace.auth.RegisterRequest;

import fpt.swp.WorkSpace.models.Customer;
import fpt.swp.WorkSpace.models.User;
import fpt.swp.WorkSpace.models.Wallet;
import fpt.swp.WorkSpace.repository.CustomerRepository;
import fpt.swp.WorkSpace.repository.UserRepository;
import fpt.swp.WorkSpace.repository.WalletRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

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
            if (findUser != null){
                throw new RuntimeException("user already exists");
            }

            //case CUSTOMER
            if (request.getRole().equals("CUSTOMER")){

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
                if (result.getUserId() != null ){
                    response.setStatus("Success");
                    response.setStatusCode(200);
                    response.setMessage("User Saved Successfully");
                    response.setData(result);
                    response.setRefresh_token(jwtService.generateAccessToken(new HashMap<>(), request.getUserName()));
                    response.setAccess_token(jwtService.generateRefreshToken(request.getUserName()));
                    response.setExpired("1 DAY");
                }
            }

        }catch (Exception e){
            response.setStatus("Error");
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public AuthenticationResponse login(LoginRequest request)  {
        AuthenticationResponse response = new AuthenticationResponse();
//        try{

            User user = repository.findByuserName(request.getUserName());
            if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())){
                throw new NullPointerException("User not found or Password do not match");
            }
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
                String jwt = jwtService.generateAccessToken(new HashMap<>(),user.getUsername());
                String refreshToken = jwtService.generateRefreshToken(user.getUsername());
                response.setStatusCode(200);
                response.setMessage("Successfully Logged In");
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
        if (authHeader != null || authHeader.startsWith("Bearer ")){
            refreshToken = authHeader.substring(7);
            //extract the username from JWT token
            userName = jwtService.extractUsername(refreshToken);
            // Check validtation of token
            if (userName != null){
                UserDetails userDetails = repository.findByuserName(userName);
                if (jwtService.isTokenValid(refreshToken,userDetails)){
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


}