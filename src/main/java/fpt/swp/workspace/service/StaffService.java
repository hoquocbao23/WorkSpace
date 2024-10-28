package fpt.swp.workspace.service;

import com.amazonaws.services.kms.model.NotFoundException;
import fpt.swp.workspace.DTO.OrderBookingDetailDTO;
import fpt.swp.workspace.DTO.RoomDTO;
import fpt.swp.workspace.auth.AuthenticationResponse;
import fpt.swp.workspace.models.*;
import fpt.swp.workspace.repository.*;
import fpt.swp.workspace.response.*;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderBookingDetailRepository orderBookingDetailRepository;

    @Autowired
    private OrderBookingRepository orderBookingRepository;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomerRepository customerRepository;



    public AuthenticationResponse createStaff(StaffRequest request) {
        AuthenticationResponse response = new AuthenticationResponse();
        User newUser = new User();
        try {
            User findUser = repository.findByuserName(request.getUserName());
            if (findUser != null){
                throw new RuntimeException("user already exists");
            }
            newUser.setUserId(generateStaffId()); // Generate the User ID
            newUser.setUserName(request.getUserName());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setCreationTime(LocalDateTime.now());
            newUser.setRoleName(request.getRole());

            User savedUser = repository.save(newUser);

            Staff newStaff = new Staff();
            newStaff.setUser(savedUser);
            System.out.println(newStaff.getUser());

            newStaff.setCreateAt(LocalDateTime.now());
            newStaff.setBuildingId(request.getBuildingId());

            // Save the Staff entity
            staffRepository.save(newStaff);
            if (savedUser.getUserId() != null ) {
                response.setStatus("Success");
                response.setStatusCode(200);
                response.setMessage("User and Staff Saved Successfully");
                response.setData(savedUser);
            }
        }
       catch (Exception e ){
           response.setStatus("Error");
           response.setStatusCode(400);
           response.setMessage(e.getMessage());       }
        return response;
    }

//    public Page<StaffResponse> getAllStaffs(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Staff> staffs = staffRepository.findAll(pageable);
//
//        return staffs.map(staff -> {
//            StaffResponse response = new StaffResponse();
//            response.setUserId(staff.getUserId());
//            response.setFullName(staff.getFullName());
//            response.setPhoneNumber(staff.getPhoneNumber());
//            response.setEmail(staff.getEmail());
//
//            if (staff.getDateOfBirth() != null) {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                response.setDateOfBirth(dateFormat.format(staff.getDateOfBirth()));
//            }
//
//            if (staff.getCreateAt() != null) {
//                response.setCreateAt(staff.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//            }
//            response.setWorkShift(staff.getWorkShift());
//            response.setWorkDays(staff.getWorkDays());
//            response.setBuildingId(staff.getBuildingId());
//            response.setStatus(staff.getStatus());
//            return response;
//        });
//    }

    public List<StaffResponse> getAllStaffs(String jwt) {
        List<StaffResponse> responses = new ArrayList<>();
        String userName = jwtService.extractUsername(jwt);
        String buildingId = userRepository.findByuserName(userName).getManager().getBuildingId();
        List<Staff> list = staffRepository.findByBuildingIdAndStatus(buildingId, UserStatus.ACTIVE, UserStatus.VACATION);

        for (Staff staff : list) {
            StaffResponse response = new StaffResponse();
            response.setUserId(staff.getUserId());
            response.setFullName(staff.getFullName());
            response.setPhoneNumber(staff.getPhoneNumber());
            response.setEmail(staff.getEmail());

            if (staff.getDateOfBirth() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                response.setDateOfBirth(dateFormat.format(staff.getDateOfBirth()));
            }

            if (staff.getCreateAt() != null) {
                response.setCreateAt(staff.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            response.setWorkShift(staff.getWorkShift());
            response.setStartTime(staff.getWorkShift().getStartTime().toString());
            response.setEndTime(staff.getWorkShift().getEndTime().toString());
            response.setWorkDays(staff.getWorkDays());

            response.setBuildingId(staff.getBuildingId());
            response.setStatus(staff.getStatus());
            responses.add(response);
        }
        return responses;
    }

    public StaffResponse getWorkShift(String jwt){

        String userName = jwtService.extractUsername(jwt);
        Staff staff = staffRepository.findStaffByUsername(userName);
        StaffResponse response = new StaffResponse();
        response.setUserId(staff.getUserId());
        response.setWorkShift(staff.getWorkShift());
        response.setStartTime(staff.getWorkShift().getStartTime().toString());
        response.setEndTime(staff.getWorkShift().getEndTime().toString());
        response.setWorkDays(staff.getWorkDays());
        return response;

    }

    public StaffResponse getWorkShiftByStaffId(String staffId){
        Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new NullPointerException("Không tìm thấy staff"));
        StaffResponse response = new StaffResponse();
        response.setUserId(staff.getUserId());
        response.setWorkShift(staff.getWorkShift());
        response.setStartTime(staff.getWorkShift().getStartTime().toString());
        response.setEndTime(staff.getWorkShift().getEndTime().toString());
        response.setWorkDays(staff.getWorkDays());
        return response;

    }

    public StaffResponse getStaffById(String staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        return modelMapper.map(staff, StaffResponse.class);
    }

    public Staff updateStaff(String staffId, UpdateStaffRequest request) {
        Staff existedStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        if(request.getFullName() != null){
            existedStaff.setFullName(request.getFullName());
        }
        if(request.getPhoneNumber() != null ){
            existedStaff.setPhoneNumber(request.getPhoneNumber());
        }
        if(request.getEmail() != null){
            existedStaff.setEmail(request.getEmail());
        }
        if(request.getWorkShift() != null){
            existedStaff.setWorkShift(WorkShift.valueOf(request.getWorkShift()));
        }
        if(request.getWorkDays() != null){
            existedStaff.setWorkDays(request.getWorkDays());
        }
        if(request.getBuildingId() != null){
            existedStaff.setBuildingId(request.getBuildingId());
        }
        if(request.getStatus() != null){
            existedStaff.setStatus(request.getStatus());
        }
        return staffRepository.save(existedStaff);
    }

    public void updateStaffStatus(String staffId) {
        Staff staff = staffRepository.findById(staffId).orElseThrow(() -> new RuntimeException("Staff không tồn tại"));
        staff.setStatus(UserStatus.DISABLED);
        staffRepository.save(staff);
    }


    public String generateStaffId() {
        // Query the latest customer and extract their ID to increment
        long latestCustomerId = staffRepository.count();
        if (latestCustomerId != 0) {

            long newId = latestCustomerId + 1;
            return "STAFF" + String.format("%04d", newId); // Format to 4 digits
        } else {
            return "STAFF0001"; // Start from CUS0001 if no customers exist
        }
    }

    public Page<OrderBookingStaffTracking> getOrderBookingsByCustomerId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderBooking> bookings = orderBookingRepository.findByCustomerCustomerId(userId, pageable);

        return bookings.map(booking -> {
            OrderBookingStaffTracking response = new OrderBookingStaffTracking();
            response.setCustomerId(booking.getCustomer().getUserId());
            response.setCustomerName(booking.getCustomer().getFullName());
            response.setBookingId(booking.getBookingId());
            response.setRoomId(booking.getRoom().getRoomId());

            List<Integer> serviceIds = booking.getOrderBookingDetails()
                    .stream()
                    .map(detail -> detail.getService().getServiceId())
                    .collect(Collectors.toList());
            response.setServiceIds(serviceIds);

            List<Integer> slotIds = booking.getSlot()
                    .stream()
                    .map(TimeSlot::getTimeSlotId)
                    .collect(Collectors.toList());

            response.setSlotIds(slotIds);
            response.setStatus(booking.getStatus());

            return response;
        });
    }

    public List<RoomStatusResponse> getAllRoomStatus() {
        List<Room> rooms = roomRepository.findAll();
        if (rooms.isEmpty()) {
            throw new RuntimeException("No rooms found.");
        }
        return rooms.stream()
                .map(room -> new RoomStatusResponse(room.getRoomId(), room.getStatus()))
                .collect(Collectors.toList());
    }

//    public RoomStatusResponse updateRoomStatus(String roomId, RoomStatusRequest request) {
//        Room room = roomRepository.findById(roomId)
//                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
//        room.setStatus(request.getRoomStatus());
//        roomRepository.save(room);
//        return new RoomStatusResponse(room.getRoomId(), room.getStatus());
//    }



    public OrderStatusResponse updateOrderStatus(String bookingId, UpdateOrderBookingStatusRequest request) {
        OrderBooking order = orderBookingRepository.findByOrderId(bookingId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + bookingId));
        order.setStatus(request.getOrderStatus());
        orderBookingRepository.save(order);
        return new OrderStatusResponse(order.getBookingId(), order.getStatus());
    }



    public List<RoomDTO> getRoomsAssigned(String token){
        String userName = jwtService.extractUsername(token);
        Staff staff = staffRepository.findStaffByUsername(userName);
        if (staff == null) {
            throw new NullPointerException("Staff not found");
        }
        List<Room> list = staff.getRooms();
        if (!list.isEmpty()) {
            List<RoomDTO> listRoomDTO = new ArrayList<>();
            for (Room room : list) {
                RoomDTO dto = new RoomDTO();
                dto.setRoomId(room.getRoomId());
                dto.setRoomName(room.getRoomName());
                dto.setRoomStatus(room.getStatus());
                listRoomDTO.add(dto);
            }
            return listRoomDTO;
        }
        return new ArrayList<>();
    }

    public List<OrderBookingDetailDTO> getOrderBookingDetails(){
        List<OrderBooking> orderBookingList = orderBookingRepository.findAll();
        if (orderBookingList.isEmpty()) {
            throw new NotFoundException("No order bookings found.");
        }
        List<OrderBookingDetailDTO> orderBookingDetailDTOList = new ArrayList<>();

        for (OrderBooking orderBooking : orderBookingList) {
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(orderBooking.getBookingId());
            dto.setCustomerId(orderBooking.getCustomer().getUserId());
            dto.setRoomId(orderBooking.getRoom().getRoomId());
            dto.setTotalPrice(orderBooking.getTotalPrice());
            dto.setSlots(orderBooking.getSlot());
            dto.setStatus(orderBooking.getStatus());
            dto.setCheckinDate(orderBooking.getCheckinDate());
            dto.setCheckoutDate(orderBooking.getCheckoutDate());

            List<OrderBookingDetail> bookingDetails = orderBookingDetailRepository.findDetailByBookingId(orderBooking.getBookingId());
            Map<String, Integer> serviceList = new HashMap<>();
            for (OrderBookingDetail bookingDetail : bookingDetails){
                String serviceName = bookingDetail.getService().getServiceName();
                int quantity = bookingDetail.getBookingServiceQuantity();
                serviceList.put(serviceName, quantity);
            }
            dto.setServiceItems(serviceList);
            orderBookingDetailDTOList.add(dto);
        }
        return orderBookingDetailDTOList;
    }

    public List<OrderBookingDetailDTO> getOrderBookingDetails(String token){
        String userName = jwtService.extractUsername(token);
        Staff staff = staffRepository.findStaffByUsername(userName);
        List<OrderBooking> orderBookingList = orderBookingRepository.findBookingsByDate(LocalDate.now().toString(), staff.getBuildingId());
        if (orderBookingList.isEmpty()) {
            throw new NotFoundException("No order bookings found.");
        }
        List<OrderBookingDetailDTO> orderBookingDetailDTOList = new ArrayList<>();

        for (OrderBooking orderBooking : orderBookingList) {
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(orderBooking.getBookingId());
            dto.setCustomerId(orderBooking.getCustomer().getUserId());
            dto.setRoomId(orderBooking.getRoom().getRoomId());
            dto.setTotalPrice(orderBooking.getTotalPrice());
            dto.setSlots(orderBooking.getSlot());
            dto.setStatus(orderBooking.getStatus());
            dto.setCheckinDate(orderBooking.getCheckinDate());
            dto.setCheckoutDate(orderBooking.getCheckoutDate());

            List<OrderBookingDetail> bookingDetails = orderBookingDetailRepository.findDetailByBookingId(orderBooking.getBookingId());
            Map<String, Integer> serviceList = new HashMap<>();
            for (OrderBookingDetail bookingDetail : bookingDetails){
                String serviceName = bookingDetail.getService().getServiceName();
                int quantity = bookingDetail.getBookingServiceQuantity();
                serviceList.put(serviceName, quantity);
            }
            dto.setServiceItems(serviceList);
            orderBookingDetailDTOList.add(dto);
        }
        return orderBookingDetailDTOList;
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void updateBookingStatusSocket(){
        List<OrderBooking> orderBookingList = orderBookingRepository.findBookingsByDate(LocalDate.now().toString());
        for (OrderBooking orderBooking : orderBookingList) {
            if (orderBooking.getBookingId().equals(BookingStatus.UPCOMING.toString())) {
                List<TimeSlot> timeSlots = orderBooking.getSlot();
                for (TimeSlot timeSlot : timeSlots) {
                    if (ChronoUnit.MINUTES.between(LocalTime.parse(timeSlot.getTimeStart().toString()), LocalTime.now()) > 15) {
                        orderBooking.setStatus(BookingStatus.FINISHED);
                        orderBookingRepository.save(orderBooking);
                        // Send the update to WebSocket clients
                        simpMessagingTemplate.convertAndSend("/bookings/status", orderBooking);
                    }
                }
            }
        }
    }



    public List<OrderBookingDetailDTO> checkinByEmail(String date, String email) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer == null) {
            throw new NullPointerException("Không tìm thấy khách hàng");
        }
        List<OrderBooking> orderBookingList = orderBookingRepository.getCustomerOrderByEmailAndDate(date, email);
        if (orderBookingList.isEmpty()) {
            throw new NullPointerException( date + " khách hàng " + email + " chưa có booking nào.");
        }
        List<OrderBookingDetailDTO> orderBookingDetailDTOList = new ArrayList<>();

        for (OrderBooking orderBooking : orderBookingList) {
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(orderBooking.getBookingId());
            dto.setCustomerId(orderBooking.getCustomer().getUserId());
            dto.setRoomId(orderBooking.getRoom().getRoomId());
            dto.setTotalPrice(orderBooking.getTotalPrice());
            dto.setSlots(orderBooking.getSlot());
            dto.setStatus(orderBooking.getStatus());
            dto.setCheckinDate(orderBooking.getCheckinDate());
            dto.setCheckoutDate(orderBooking.getCheckoutDate());

            List<OrderBookingDetail> bookingDetails = orderBookingDetailRepository.findDetailByBookingId(orderBooking.getBookingId());
            Map<String, Integer> serviceList = new HashMap<>();
            for (OrderBookingDetail bookingDetail : bookingDetails) {
                String serviceName = bookingDetail.getService().getServiceName();
                int quantity = bookingDetail.getBookingServiceQuantity();
                serviceList.put(serviceName, quantity);
            }
            dto.setServiceItems(serviceList);
            orderBookingDetailDTOList.add(dto);
        }
        return orderBookingDetailDTOList;
    }

    public List<OrderBookingDetailDTO> checkinByPhonenumber(String date, String phonenumber) {
        Customer customer = customerRepository.findCustomerByPhoneNumber(phonenumber);
        if (customer == null) {
            throw new NullPointerException("Không tìm thấy khách hàng");
        }
        List<OrderBooking> orderBookingList = orderBookingRepository.getCustomerOrderByPhoneAndDate(date, phonenumber);
        if (orderBookingList.isEmpty()) {
            throw new NullPointerException( date + " khách hàng " + phonenumber + " chưa có booking nào.");
        }
        List<OrderBookingDetailDTO> orderBookingDetailDTOList = new ArrayList<>();

        for (OrderBooking orderBooking : orderBookingList) {
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(orderBooking.getBookingId());
            dto.setCustomerId(orderBooking.getCustomer().getUserId());
            dto.setRoomId(orderBooking.getRoom().getRoomId());
            dto.setTotalPrice(orderBooking.getTotalPrice());
            dto.setSlots(orderBooking.getSlot());
            dto.setStatus(orderBooking.getStatus());
            dto.setCheckinDate(orderBooking.getCheckinDate());
            dto.setCheckoutDate(orderBooking.getCheckoutDate());

            List<OrderBookingDetail> bookingDetails = orderBookingDetailRepository.findDetailByBookingId(orderBooking.getBookingId());
            Map<String, Integer> serviceList = new HashMap<>();
            for (OrderBookingDetail bookingDetail : bookingDetails) {
                String serviceName = bookingDetail.getService().getServiceName();
                int quantity = bookingDetail.getBookingServiceQuantity();
                serviceList.put(serviceName, quantity);
            }
            dto.setServiceItems(serviceList);
            orderBookingDetailDTOList.add(dto);
        }
        return orderBookingDetailDTOList;
    }

    public void updateOrderStatus(String bookingId, BookingStatus status) {
        OrderBooking order = orderBookingRepository.findByOrderId(bookingId).orElseThrow(() -> new RuntimeException("Booking không hợp lệ"));
        if (order.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking " + bookingId + " đã bị huỷ.");
        }
        if (order.getStatus() == status){
            throw new RuntimeException("Trạng thái đã được đặt");
        }
        order.setStatus(status);
        orderBookingRepository.save(order);
    }

    public void updateOrderStatus(String bookingId, String status) {
        OrderBooking order = orderBookingRepository.findByOrderId(bookingId).orElseThrow(() -> new RuntimeException("Booking không hợp lệ"));
        if (order.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking " + bookingId + " đã bị huỷ.");
        }
            // convert String -> Enum
            if ( order.getStatus() == BookingStatus.valueOf(status)) {
                throw new RuntimeException("Trạng thái đã được đặt");
            }
            // convert String -> Enum
            order.setStatus(BookingStatus.valueOf(status));
            orderBookingRepository.save(order);
    }



    public void acceptPendingBooking(String bookingId){
        OrderBooking orderBooking = orderBookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking " + bookingId + " không có trong danh sách chờ! "));
        orderBooking.setStatus(BookingStatus.UPCOMING);
        orderBookingRepository.save(orderBooking);
    }

    public void rejectPendingBooking(String bookingId) {
        OrderBooking orderBooking = orderBookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking " + bookingId + " không có trong danh sách chờ! "));
        orderBooking.setStatus(BookingStatus.CANCELLED);
        orderBookingRepository.save(orderBooking);

        Payment payment = paymentRepository.findByOrderBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for this booking"));

        Wallet wallet = walletRepository.findByUserId(orderBooking.getCustomer().getUserId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (payment.getStatus().equals("completed")) {
            // Hoàn lại tiền vào ví
            wallet.setAmount(wallet.getAmount() + payment.getAmount());
            walletRepository.save(wallet);

            Transaction refundTransaction = new Transaction();
            refundTransaction.setTransactionId(UUID.randomUUID().toString());
            refundTransaction.setAmount(payment.getAmount());
            refundTransaction.setStatus("completed");
            refundTransaction.setType("refund");
            refundTransaction.setTransaction_time(LocalDateTime.now());
            refundTransaction.setPayment(payment);
            transactionRepository.save(refundTransaction);
            payment.setStatus("completed");
            paymentRepository.save(payment);
        }
    }
}
