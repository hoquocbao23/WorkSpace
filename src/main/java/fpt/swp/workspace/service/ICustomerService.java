package fpt.swp.workspace.service;

import fpt.swp.workspace.models.Customer;
import fpt.swp.workspace.models.Wallet;
import org.springframework.web.multipart.MultipartFile;

public interface ICustomerService {

    Customer getCustomerProfile(String token);

    Customer customerChangePassword(String username, String newPassword);

    Customer customerEditProfile(String username, Customer newCustomer);

    void updateCustomerImg(String token, MultipartFile file);

    Wallet getWalletByUserId(String userId);

    String buyMembership(String token, String memberShipId);
}
