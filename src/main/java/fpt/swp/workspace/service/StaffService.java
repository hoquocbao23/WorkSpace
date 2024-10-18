package fpt.swp.WorkSpace.service;

import com.amazonaws.services.kms.model.NotFoundException;
import fpt.swp.WorkSpace.DTO.OrderBookingDetailDTO;
import fpt.swp.WorkSpace.DTO.RoomDTO;
import fpt.swp.WorkSpace.auth.AuthenticationResponse;
import fpt.swp.WorkSpace.models.*;
import fpt.swp.WorkSpace.repository.*;
import fpt.swp.WorkSpace.response.*;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

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

    public Page<StaffResponse> getAllStaffs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Staff> staffs = staffRepository.findAll(pageable);

        return staffs.map(staff -> {
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
            response.setWorkDays(staff.getWorkDays());
            response.setBuildingId(staff.getBuildingId());
            response.setStatus(staff.getStatus());
            return response;
        });
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
            existedStaff.setWorkShift(request.getWorkShift());
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

    public void deleteStaff(String userId) {
        Optional<Staff> staffOptional = staffRepository.findById(userId);
        if (staffOptional.isPresent()) {
            staffRepository.deleteById(userId);
        } else {
            throw new EntityNotFoundException("Staff with ID " + userId + " not found.");
        }    }


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

    public RoomStatusResponse updateRoomStatus(String roomId, RoomStatusRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomId));
        room.setStatus(request.getRoomStatus());
        roomRepository.save(room);
        return new RoomStatusResponse(room.getRoomId(), room.getStatus());
    }

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


}
