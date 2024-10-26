package fpt.swp.workspace.service;

import fpt.swp.workspace.models.*;
import fpt.swp.workspace.repository.*;
import fpt.swp.workspace.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;
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

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Override
    public Customer getCustomerProfile(String token) {
        String username = jwtService.extractUsername(token);
        Customer customer =  customerRepository.findCustomerByUsername(username);
        return customer;
    }

    @Override
    @Transactional
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
    @Transactional
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
                if (newCustomer.getDateOfBirth() != null){
                    customer.setDateOfBirth(newCustomer.getDateOfBirth());
                }
            }
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public String buyMembership(String token, String memberShipId) {
        String username = jwtService.extractUsername(token);
        Customer customer = customerRepository.findCustomerByUsername(username);
        if (customer.getMembership() != null) {
            throw new RuntimeException("Customer already has a membership");
        }
        UserNumberShip membership = membershipRepository.findById(memberShipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"));

        int amount = membership.getAmount();

        Wallet wallet = walletRepository.findByUserId(customer.getUserId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        if (wallet.getAmount() < amount) {
            throw new RuntimeException("Insufficient balance in wallet");
        }
        wallet.setAmount(wallet.getAmount() - amount);
        walletRepository.save(wallet);
        customer.setMembership(membership);
        customerRepository.save(customer);

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        // payment.setOrderBookingId(UUID.randomUUID().toString());
        payment.setCustomer(customer);
        payment.setAmount(amount);
        payment.setStatus("completed");
        payment.setPaymentMethod("wallet");
        paymentRepository.save(payment);


        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAmount(amount);
        transaction.setStatus("completed");
        transaction.setType("buy_membership");
        transaction.setTransaction_time(LocalDateTime.now());
        transaction.setPayment(payment);
        transactionRepository.save(transaction);
        return "Membership buy successfully";
    }
}
