package fpt.swp.WorkSpace.service;

import fpt.swp.WorkSpace.configuration.VNPAYConfig;
import fpt.swp.WorkSpace.models.Customer;
import fpt.swp.WorkSpace.models.Payment;
import fpt.swp.WorkSpace.models.Transaction;
import fpt.swp.WorkSpace.models.Wallet;
import fpt.swp.WorkSpace.repository.CustomerRepository;
import fpt.swp.WorkSpace.repository.PaymentRepository;
import fpt.swp.WorkSpace.repository.TransactionRepository;
import fpt.swp.WorkSpace.repository.WalletRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VNPAYService {
    @Autowired
    private WalletService walletService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CustomerRepository customerRepository;



    public String createRecharge(HttpServletRequest request, int amount, String userId){
        Customer customer = customerRepository.findCustomerByCustomerId(userId);
        if (customer == null) {
            throw new RuntimeException("User not found: " + userId);
        }
        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setAmount(amount);
        payment.setStatus("pending");
        payment.setPaymentMethod("VNPay");
        payment.setCustomer(customer);
        paymentRepository.save(payment);

        //Các bạn có thể tham khảo tài liệu hướng dẫn và điều chỉnh các tham số
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPAYConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPAYConfig.getIpAddress(request);
        String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;
        String orderType = "topup";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((amount * 100))); // Amount in VND (multiplied by 100 for VNPay)
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Top-up Wallet for User: " + userId);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);
        vnp_Params.put("vnp_ReturnUrl", VNPAYConfig.vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String salt = VNPAYConfig.vnp_HashSecret;
        String vnp_SecureHash = VNPAYConfig.hmacSHA512(salt, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPAYConfig.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }



    public ResponseEntity<Map<String, Object>> returnRecharge(HttpServletRequest request, HttpServletResponse restResponse) throws IOException {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = null;
            String fieldValue = null;
            try {
                fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = VNPAYConfig.hashAllFields(fields);

        Map<String, Object> response = new HashMap<>();

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String userId = extractUserIdFromOrderInfo(orderInfo);
        float currentWalletBalance = walletService.getWalletBalance(userId);

        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                String amountStr = request.getParameter("vnp_Amount");
                float amount = Float.parseFloat(amountStr) / 100; // VNPay sends amount in *100
                String transactionId = UUID.randomUUID().toString();

                walletService.updateWalletBalance(userId, amount);
                currentWalletBalance = walletService.getWalletBalance(userId);

                Customer customer = customerRepository.findCustomerByCustomerId(userId);
                if (customer == null) {
                    throw new RuntimeException("User not found: " + userId);
                }
                Payment payment = new Payment();
                payment.setPaymentId(UUID.randomUUID().toString());
                payment.setAmount((int) amount);
                payment.setStatus("completed");
                payment.setPaymentMethod("VNPay");
                payment.setTransactionId(transactionId);
                payment.setCustomer(customer);
                paymentRepository.save(payment);

                Transaction transaction = new Transaction();
                transaction.setTransactionId(transactionId);
                transaction.setAmount(amount);
                transaction.setStatus("completed");
                transaction.setType("TopUp");
                transaction.setTransaction_time(LocalDateTime.now());
                transaction.setPayment(payment);

                transactionRepository.save(transaction);
                response.put("message", "Top-up successful");
                response.put("amountTopUp", amount);
                response.put("currentWalletBalance", currentWalletBalance);
                restResponse.sendRedirect("http://localhost:3000/");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Transaction failed");
                response.put("amountTopUp", 0);
                response.put("currentWalletBalance", currentWalletBalance);
                restResponse.sendRedirect("http://localhost:3000/");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } else {
            response.put("message", "Invalid signature");
            response.put("amountTopUp", 0);
            response.put("currentWalletBalance", currentWalletBalance);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);        }
    }

    private String extractUserIdFromOrderInfo(String orderInfo) {
        String[] parts = orderInfo.split(":");
        if (parts.length > 1) {
            return parts[1].trim(); // Lấy userId và loại bỏ khoảng trắng
        }
        return null;
    }

}