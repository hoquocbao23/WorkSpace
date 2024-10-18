package fpt.swp.WorkSpace.service;

import fpt.swp.WorkSpace.models.Customer;
import fpt.swp.WorkSpace.models.User;
import fpt.swp.WorkSpace.models.Wallet;
import fpt.swp.WorkSpace.repository.CustomerRepository;
import fpt.swp.WorkSpace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public Customer getCustomerProfile(String token) {
        String username = jwtService.extractUsername(token);
        Customer customer =  customerRepository.findCustomerByUsername(username);
        return customer;
    }

    @Override
    public Customer customerChangePassword(String username, String newpassword) {
        Customer customer =  customerRepository.findCustomerByUsername(username);
        if (customer != null) {
            if (passwordEncoder.matches(newpassword, customer.getUser().getPassword() )){
                throw new RuntimeException("Old password");
            }
            if (newpassword == null){
                throw new RuntimeException("Not empty");
            }
            customer.getUser().setPassword(passwordEncoder.encode(newpassword));
            customerRepository.save(customer);
        }

        return customer;
    }

    @Override
    public Customer customerEditProfile(String username, Customer newCustomer) {
        Customer customer =  customerRepository.findCustomerByUsername(username);

            if (customer != null) {
                if (newCustomer.getEmail() == null || !Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(newCustomer.getEmail()).matches()) {
                    throw new RuntimeException("Email is not in valid format");
                }
                if ( newCustomer.getFullName() != null){
                    customer.setFullName(newCustomer.getFullName());
                }
                if ( newCustomer.getPhoneNumber() != null){
                    customer.setPhoneNumber(newCustomer.getPhoneNumber());
                }
                if (newCustomer.getEmail() != null){
                    customer.setEmail(newCustomer.getEmail());
                }

            }
        return customerRepository.save(customer);
    }

    @Override
    public void updateCustomerImg(String token, MultipartFile file) {
        String username = jwtService.extractUsername(token);
        Customer customer =  customerRepository.findCustomerByUsername(username);
        if (customer == null) {
            throw new RuntimeException("Not found");
        }
        customer.setImgUrl(awsS3Service.saveImgToS3(file));
        customerRepository.save(customer);
    }

    @Override
    public Wallet getWalletByUserId(String userId) {
        return customerRepository.findWalletByUserId(userId);
    }
}
