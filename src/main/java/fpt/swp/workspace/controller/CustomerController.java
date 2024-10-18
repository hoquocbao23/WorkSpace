package fpt.swp.WorkSpace.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import fpt.swp.WorkSpace.models.Customer;
import fpt.swp.WorkSpace.models.Room;
import fpt.swp.WorkSpace.models.Transaction;
import fpt.swp.WorkSpace.models.Wallet;
import fpt.swp.WorkSpace.response.APIResponse;
import fpt.swp.WorkSpace.response.ResponseHandler;
import fpt.swp.WorkSpace.response.TransactionResponse;
import fpt.swp.WorkSpace.service.ICustomerService;
import fpt.swp.WorkSpace.service.IRoomService;
import fpt.swp.WorkSpace.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole(CUSTOMER)")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private IRoomService roomService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello User";
    }

    @GetMapping("customer/manage-profile")
    public ResponseEntity<Object> getUserProfile(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        Customer customer = customerService.getCustomerProfile(jwtToken);
        return ResponseHandler.responseBuilder("Success", HttpStatus.OK, customer);
    }

    @PutMapping("customer/manage-profile/change-password/{username}")
    public ResponseEntity<Object> changePassword(@PathVariable String username, HttpServletRequest request){

        String newpassword = request.getParameter("newpassword");
        try {
             customerService.customerChangePassword(username, newpassword);
            // response
            return ResponseHandler.responseBuilder("Change password successfully", HttpStatus.OK);
        }catch (RuntimeException e){
            return ResponseHandler.responseBuilder(e.getMessage(),HttpStatus.BAD_REQUEST);

        }

    }

    @PutMapping("customer/manage-profile/edit-profile/{username}")
    public ResponseEntity<Object> editProfile(@PathVariable String username, @RequestBody Customer customer){
//        String newPhonenumber = request.getParameter("newPhonenumber");
//        String newEmail = request.getParameter("newEmail");
        try {
            customerService.customerEditProfile(username, customer);
            return  ResponseHandler.responseBuilder("successfully", HttpStatus.OK);
        }catch (RuntimeException e){
            return ResponseHandler.responseBuilder(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/customer/manage-profile/update-img")
    public ResponseEntity<Object> updateProfile(@RequestHeader("Authorization") String token,
                                                MultipartFile file){
        String jwtToken = token.substring(7);
        try{
            customerService.updateCustomerImg(jwtToken, file);
            return ResponseHandler.responseBuilder("Update imgage successfully", HttpStatus.OK);
        }catch (RuntimeException e){
            return ResponseHandler.responseBuilder(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<APIResponse<List<TransactionResponse>>> getAllTransactionsByUserId(@PathVariable String userId) {
        try {
            List<TransactionResponse> transactions = transactionService.getAllTransactionsByUserId(userId);
            APIResponse<List<TransactionResponse>> response = new APIResponse<>("Successfully", transactions);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            APIResponse<List<TransactionResponse>> response = new APIResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/user/wallet/{userId}")
    public ResponseEntity<APIResponse<Wallet>> getWalletByUserId(@PathVariable String userId) {
        try {
            Wallet wallet = customerService.getWalletByUserId(userId);
            APIResponse<Wallet> response = new APIResponse<>("Successfully", wallet);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            APIResponse<Wallet> response = new APIResponse<>(e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
