package fpt.swp.workspace.service;

import fpt.swp.workspace.DTO.BookedSlotDTO;
import fpt.swp.workspace.DTO.CustomerServiceDTO;
import fpt.swp.workspace.DTO.OrderBookingDetailDTO;
import fpt.swp.workspace.models.*;
import fpt.swp.workspace.repository.*;

import fpt.swp.workspace.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class OrderBookingService implements IOrderBookingService {
    @Autowired
    private OrderBookingRepository orderBookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ServiceItemsRepository itemsRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private OrderBookingDetailRepository orderBookingDetailRepository;

    @Autowired
    private ServiceItemsRepository serviceItemsRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SendEmailService sendEmailService;

    @Override
    public List<OrderBookingDetailDTO> getBookedSlotByRoomAndDate(String date, String roomId) {
        // get booking list checkin day and room avaiable
        List<OrderBooking> bookings = orderBookingRepository.findTimeSlotBookedByRoomAndDate(date, roomId);


        List<OrderBookingDetailDTO> bookingList = new ArrayList<>();

        if (bookings.isEmpty()) {
            throw new RuntimeException("Ngay " + date + " chua co booking nao.");
        }
        for (OrderBooking orderBooking : bookings) {
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(orderBooking.getBookingId());
            dto.setRoomId(orderBooking.getRoom().getRoomId());
            dto.setCheckinDate(orderBooking.getCheckinDate());
            dto.setCheckoutDate(orderBooking.getCheckoutDate());
            dto.setTotalPrice(orderBooking.getTotalPrice());

            // Get all timeslot in Booking
            List<TimeSlot> timeSlotIdBooked = new ArrayList<>();
            int countSlot = orderBooking.getSlot().size();
            for (int i = 0; i < countSlot; i++) {
                timeSlotIdBooked.add(orderBooking.getSlot().get(i));
            }
            dto.setSlots(timeSlotIdBooked);
            bookingList.add(dto);
        }
        return bookingList;
    }

    @Override
    public List<OrderBookingDetailDTO> getBookedSlotByDate(String date) {
        // get booking list checkin day and room avaiable
        List<OrderBooking> bookings = orderBookingRepository.findBookingsByDate(date);


        List<OrderBookingDetailDTO> bookingList = new ArrayList<>();

        if (bookings.isEmpty()) {
            throw new RuntimeException("Ngay " + date + " chua co booking nao.");
        }
        for (OrderBooking orderBooking : bookings) {
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(orderBooking.getBookingId());
            dto.setRoomId(orderBooking.getRoom().getRoomId());
            dto.setCheckinDate(orderBooking.getCheckinDate());
            dto.setCheckoutDate(orderBooking.getCheckoutDate());
            dto.setTotalPrice(orderBooking.getTotalPrice());

            // Get all timeslot in Booking
            List<TimeSlot> timeSlotIdBooked = new ArrayList<>();
            int countSlot = orderBooking.getSlot().size();
            for (int i = 0; i < countSlot; i++) {
                timeSlotIdBooked.add(orderBooking.getSlot().get(i));
            }
            dto.setSlots(timeSlotIdBooked);
            bookingList.add(dto);
        }
        return bookingList;
    }

    @Override
    public List<OrderBookingDetailDTO> getBookedSlotByCheckinAndCheckout(String checkin, String checkout, String roomId) {
        // get booking list checkin day and room avaiable
        List<OrderBooking> bookings = orderBookingRepository.findBookingsByInOutDate(checkin, checkout);
        List<OrderBookingDetailDTO> bookingList = new ArrayList<>();
        if (bookings.isEmpty()) {
            throw new RuntimeException("Tu ngay " + checkin + " toi ngay " + checkout + " chua co booking nao.");
        }
        for (OrderBooking orderBooking : bookings) {
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(orderBooking.getBookingId());
        }
        return bookingList;
    }


    @Override
    public BookedSlotDTO getBookedSlotByEachDay(String checkin, String checkout, String roomId, String buildingId) {

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new NullPointerException("Không tìm thấy phòng"));
        Building building = buildingRepository.findById(buildingId).orElseThrow(() -> new NullPointerException("Không tìm thấy building"));

        BookedSlotDTO bookedSlotDTO = new BookedSlotDTO();
        Map<String, ArrayList<Integer>> mapBookedSlots = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);
        long numberOfDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;
        IntStream.range(0, (int) numberOfDays).forEach(i -> {
            // increase date
            LocalDate bookingDate = checkinDate.plusDays(i);
            String bookingDateStr = bookingDate.format(formatter);
            ArrayList<Integer> timeSlotIdBooked = new ArrayList<>();
            // get all booked in a day
            List<OrderBooking> bookings = orderBookingRepository.findBookingsByDate(bookingDateStr, buildingId, roomId, BookingStatus.CANCELLED);
            if (!bookings.isEmpty()) {
                // loop each booking in day
                for (OrderBooking orderBooking : bookings) {
                    // get all timeslot booked in each booking
                    List<TimeSlot> timeSlotBooked = orderBooking.getSlot();
                    // create a list to save timeslotId
                    // loop each slot, get slot id
                    for (TimeSlot timeSlot : timeSlotBooked) {
                        int slotId = timeSlot.getTimeSlotId();
                        timeSlotIdBooked.add(slotId);
                        System.out.println(timeSlotIdBooked);
                    }
                }
                // put avaiable date and slot id to map
                mapBookedSlots.put(bookingDateStr, timeSlotIdBooked);
            }
        });
        bookedSlotDTO.setBookedSlots(mapBookedSlots);
        return bookedSlotDTO;
    }


    @Override
    public OrderBooking createOrderBooking(String jwttoken, String roomId, String checkinDate, List<Integer> slotBooking, String note) {

        String username = jwtService.extractUsername(jwttoken);
        Customer customer = customerRepository.findCustomerByUsername(username);

        Room room = roomRepository.findById(roomId).get();
        int countSlot = slotBooking.size();
        float totalPrice = room.getPrice() * countSlot;

        // get time slot booked by customer
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < countSlot; i++) {
            TimeSlot timeSlot = timeSlotRepository.findById(slotBooking.get(i)).get();
            timeSlots.add(timeSlot);
        }

        OrderBooking orderBooking = new OrderBooking();
        orderBooking.setBookingId(Helper.generateRandomString(0, 5));
        orderBooking.setCustomer(customer);
        orderBooking.setRoom(room);
        orderBooking.setCheckinDate(checkinDate);
        orderBooking.setTotalPrice(totalPrice);
        orderBooking.setSlot(timeSlots);
        orderBooking.setCreateAt(Helper.convertLocalDateTime());
        orderBooking.setNote(note);

        OrderBooking result = orderBookingRepository.save(orderBooking);      // saved

//        OrderBookingResponse orderBookingResponse = new OrderBookingResponse();
//        orderBookingResponse.setBookingId(orderBooking.getBookingId());
//        orderBookingResponse.setCustomerId(customer.getUserId());
//        orderBookingResponse.setRoomId(room.getRoomId());
//        orderBookingResponse.setSlotId(slotBooking);
//        orderBookingResponse.setCheckinDate(orderBooking.getCheckinDate());
//        orderBookingResponse.setTotalPrice(orderBooking.getTotalPrice());
//        orderBookingResponse.setNote(orderBooking.getNote())

        return result;
    }

    @Override
    @Transactional
    public OrderBooking createMultiOrderBooking(String jwttoken, String buildingId, String roomId, String checkin, String checkout, List<Integer> slotBooking, MultiValueMap<Integer, Integer> items, String note) {
        String username = jwtService.extractUsername(jwttoken);
        Customer customer = customerRepository.findCustomerByUsername(username);

        UserNumberShip membership = customer.getMembership();
        float discount = 0.0f;

        if (membership != null) {
            if ("Gold".equalsIgnoreCase(membership.getMembershipName())) {
                discount = 0.1f;
            } else if ("Silver".equalsIgnoreCase(membership.getMembershipName())) {
                discount = 0.05f;
            }
        }
        Building building = buildingRepository.findById(buildingId).orElseThrow(() -> new NullPointerException("Building not found"));

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new NullPointerException("Room not found"));


        // get time slot booked by customer
        int countSlot = slotBooking.size();
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < countSlot; i++) {
            TimeSlot timeSlot = timeSlotRepository.findById(slotBooking.get(i)).get();
            timeSlots.add(timeSlot);
        }

        // process total price
        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);
        //days between checkin - checkout
        long numberDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;
        //System.out.println(numberDays);
        float roomPrice = room.getPrice() * countSlot * (int) numberDays;
        float servicePriceTotal = 0.0f;

        OrderBooking orderBooking = new OrderBooking();
        orderBooking.setBookingId(Helper.generateRandomString(0, 5));
        orderBooking.setCustomer(customer);
        orderBooking.setRoom(room);
        orderBooking.setBuilding(building);
        orderBooking.setCheckinDate(checkin);
        orderBooking.setCheckoutDate(checkout);
        orderBooking.setSlot(timeSlots);
        orderBooking.setStatus(BookingStatus.UPCOMING);
        orderBooking.setCreateAt(Helper.convertLocalDateTime());
        orderBooking.setNote(note);
        OrderBooking result = orderBookingRepository.save(orderBooking);

        if (!items.isEmpty()) {
            // Xử lý items (service id và số lượng)
            for (Map.Entry<Integer, List<Integer>> entry : items.entrySet()) {
                Integer serviceId = entry.getKey();
                List<Integer> quantities = entry.getValue();  // Danh sách số lượng cho cùng một service ID

                // Tìm service tương ứng từ database
                ServiceItems item = itemsRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("Service not found"));

                // Lưu thông tin chi tiết đơn hàng cho từng số lượng
                for (Integer quantity : quantities) {
                    float servicePrice = item.getPrice() * quantity * (int) numberDays;
                    OrderBookingDetail orderBookingDetail = new OrderBookingDetail();
                    orderBookingDetail.setBooking(result);
                    orderBookingDetail.setService(item);
                    orderBookingDetail.setBookingServiceQuantity(quantity);
                    orderBookingDetail.setBookingServicePrice(servicePrice);
                    servicePriceTotal += servicePrice;
                    orderBookingDetailRepository.save(orderBookingDetail);
                }
            }
        }
        float totalPriceWithServices = roomPrice + servicePriceTotal;
        // Áp dụng giảm giá dựa trên loại membership
        totalPriceWithServices -= totalPriceWithServices * discount;

        //process if bill > 5000000
        if (totalPriceWithServices >= 5000000) {
            orderBooking.setStatus(BookingStatus.PENDING);
        }

        orderBooking.setTotalPrice(totalPriceWithServices);
        result = orderBookingRepository.save(orderBooking);
        Wallet wallet = walletRepository.findByUserId(customer.getUserId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        if (wallet.getAmount() < totalPriceWithServices) {
            throw new RuntimeException("Not enough money in the wallet");
        }

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setAmount((int) totalPriceWithServices);
        payment.setStatus("completed");
        payment.setPaymentMethod("wallet");
        payment.setOrderBookingId(orderBooking.getBookingId());
        payment.setCustomer(customer);


        Transaction transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAmount(totalPriceWithServices);
        transaction.setStatus("completed");
        transaction.setType("pay for Order");
        transaction.setTransaction_time(LocalDateTime.now());
        transaction.setPayment(payment);
        // payment.setTransactionId(transaction.getTransactionId());
        paymentRepository.save(payment);
        transactionRepository.save(transaction);
        // Trừ tiền trong ví
        wallet.setAmount(wallet.getAmount() - totalPriceWithServices);
        walletRepository.save(wallet);

        sendEmailService.sendHtmlMessage(customer.getEmail(), "XÁC NHẬN ĐẶT PHÒNG", "/templates/email-format.html");

        return result;
    }

    @Override
    public OrderBooking createOrderBookingWithout(String jwttoken, String buildingId, String roomId, String checkin, String checkout, Integer[] slotBooking, String note) {
        String username = jwtService.extractUsername(jwttoken);
        Customer customer = customerRepository.findCustomerByUsername(username);

        Building building = buildingRepository.findById(buildingId).get();

        Room room = roomRepository.findById(roomId).get();


        // get time slot booked by customer
        int countSlot = slotBooking.length;
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < countSlot; i++) {
            TimeSlot timeSlot = timeSlotRepository.findById(slotBooking[i]).get();
            timeSlots.add(timeSlot);
        }

        // process total price
        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);
        //days between checkin - checkout
        long numberDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;
        System.out.println(numberDays);
        float totalPrice = room.getPrice() * countSlot * (int) numberDays;

        OrderBooking orderBooking = new OrderBooking();
        orderBooking.setBookingId(Helper.generateRandomString(0, 5));
        orderBooking.setCustomer(customer);
        orderBooking.setRoom(room);
        orderBooking.setBuilding(building);
        orderBooking.setCheckinDate(checkin);
        orderBooking.setCheckoutDate(checkout);
        orderBooking.setTotalPrice(totalPrice);
        orderBooking.setSlot(timeSlots);
        orderBooking.setCreateAt(Helper.convertLocalDateTime());
        orderBooking.setNote(note);
        OrderBooking result = orderBookingRepository.save(orderBooking);

        return result;
    }

    @Override
    public List<OrderBookingDetailDTO> getCustomerHistoryBooking(String jwttoken) {
        String userName = jwtService.extractUsername(jwttoken);
        List<OrderBooking> historyBookingList = orderBookingRepository.getCustomerHistoryBooking(userName);
        List<OrderBookingDetailDTO> orderDetail = new ArrayList<>();
        for (OrderBooking orderBooking : historyBookingList) {
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(orderBooking.getBookingId());
            dto.setRoomId(orderBooking.getRoom().getRoomId());
            dto.setCheckinDate(orderBooking.getCheckinDate());
            dto.setCheckoutDate(orderBooking.getCheckoutDate());
            dto.setTotalPrice(orderBooking.getTotalPrice());
            dto.setStatus(orderBooking.getStatus());

            // Get all timeslot in Booking
//            List<TimeSlot> timeSlotIdBooked = new ArrayList<>();
//            int countSlot = orderBooking.getSlot().size();
//            for (int i = 0; i < countSlot; i++){
//                timeSlotIdBooked.add(orderBooking.getSlot().get(i));
//            }
            dto.setSlots(orderBooking.getSlot());


            // get service items
            List<OrderBookingDetail> bookingDetails = orderBookingDetailRepository.findDetailByBookingId(orderBooking.getBookingId());
            Map<String, Integer> serviceList = new HashMap<>();
            for (OrderBookingDetail bookingDetail : bookingDetails) {
                String serviceName = bookingDetail.getService().getServiceName();
                int quantity = bookingDetail.getBookingServiceQuantity();
                serviceList.put(serviceName, quantity);
            }
            dto.setServiceItems(serviceList);
            orderDetail.add(dto);
        }
        return orderDetail;
    }


    @Override
    public OrderBooking createOrderBookingService(String jwttoken, String roomId, String checkinDate, List<Integer> slotBooking, MultiValueMap<Integer, Integer> items, String note) {
        String username = jwtService.extractUsername(jwttoken);
        Customer customer = customerRepository.findCustomerByUsername(username);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));

        int countSlot = slotBooking.size();
        float totalPrice = room.getPrice() * countSlot;

        // get time slot booked by customer
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < countSlot; i++) {
            TimeSlot timeSlot = timeSlotRepository.findById(slotBooking.get(i)).get();
            timeSlots.add(timeSlot);
        }


        OrderBooking orderBooking = new OrderBooking();
        orderBooking.setBookingId(Helper.generateRandomString(0, 5));
        orderBooking.setCustomer(customer);
        orderBooking.setRoom(room);
        orderBooking.setCheckinDate(checkinDate);
        orderBooking.setCheckoutDate(checkinDate);
        orderBooking.setTotalPrice(totalPrice);
        orderBooking.setSlot(timeSlots);
        orderBooking.setCreateAt(Helper.convertLocalDateTime());
        orderBooking.setNote(note);
        orderBookingRepository.save(orderBooking);   // save to table order booking


        if (!items.isEmpty()) {
            // Xử lý items (service id và số lượng)
            for (Map.Entry<Integer, List<Integer>> entry : items.entrySet()) {
                Integer serviceId = entry.getKey();
                List<Integer> quantities = entry.getValue();  // Danh sách số lượng cho cùng một service ID

                // Tìm service tương ứng từ database
                ServiceItems item = itemsRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("Service not found"));

                // Lưu thông tin chi tiết đơn hàng cho từng số lượng
                for (Integer quantity : quantities) {
                    float servicePrice = item.getPrice() * quantity;
                    OrderBookingDetail orderBookingDetail = new OrderBookingDetail();
                    orderBookingDetail.setBooking(orderBooking);
                    orderBookingDetail.setService(item);
                    orderBookingDetail.setBookingServiceQuantity(quantity);
                    orderBookingDetail.setBookingServicePrice(servicePrice);
                    totalPrice += servicePrice;
                    orderBookingDetailRepository.save(orderBookingDetail);
                }
            }
            orderBooking.setTotalPrice(totalPrice);
            orderBookingRepository.save(orderBooking);
        }

        return orderBooking;
    }


    @Override
    @Transactional
    public void updateServiceBooking(String orderBookingId, MultiValueMap<Integer, Integer> items) {

        // get booking
        OrderBooking orderBooking = orderBookingRepository.findById(orderBookingId).orElseThrow();
        int countSlot = orderBooking.getSlot().size();
        float totalPrice = orderBooking.getTotalPrice();
        float totalServicePriceChange = 0.0f;
        Payment payment = paymentRepository.findByOrderBookingId(orderBooking.getBookingId()).orElseThrow();

        if (!items.isEmpty()) {
            // nếu customer update trước ngày check in
            LocalDate checkinDate = LocalDate.parse(orderBooking.getCheckinDate());
            LocalDate updateDate = LocalDate.now();
            long numberDays = updateDate.isBefore(checkinDate)
                    ? ChronoUnit.DAYS.between(checkinDate, LocalDate.parse(orderBooking.getCheckoutDate())) + 1
                    : ChronoUnit.DAYS.between(updateDate, LocalDate.parse(orderBooking.getCheckoutDate())) + 1;

            // Xử lý items (service id và số lượng)
            for (Map.Entry<Integer, List<Integer>> entry : items.entrySet()) {
                Integer serviceId = entry.getKey();
                Integer quantities = entry.getValue().get(0);

                OrderBookingDetail orderBookingDetail = orderBookingDetailRepository.findDetailByBookingIdAndServiceId(orderBooking.getBookingId(), serviceId);

                // If orderBookingDetail already exist
                if (orderBookingDetail != null) {

                    int oldQuantity = orderBookingDetail.getBookingServiceQuantity();
                    if (updateDate.isAfter(checkinDate) && oldQuantity > quantities) {
                        throw new RuntimeException("Cannot update already booked service");
                    }
                    orderBookingDetail.setBookingServiceQuantity(quantities);
                    // giá service mới = giá service * so luong * so ngay
                    float newServicePrice = orderBookingDetail.getService().getPrice() * quantities * numberDays;
                    totalServicePriceChange += newServicePrice - (orderBookingDetail.getBookingServicePrice());
                    orderBookingDetail.setBookingServicePrice(newServicePrice);
                    orderBookingDetailRepository.save(orderBookingDetail);
                } else {
                    // Add new service
                    OrderBookingDetail neworderBookingDetail = new OrderBookingDetail();
                    ServiceItems item = serviceItemsRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("Service not found"));
                    neworderBookingDetail.setBooking(orderBooking);
                    neworderBookingDetail.setService(item);
                    neworderBookingDetail.setBookingServiceQuantity(quantities);
                    float servicePrice = item.getPrice() * quantities * numberDays;
                    neworderBookingDetail.setBookingServicePrice(servicePrice);
                    totalServicePriceChange += servicePrice;
                    orderBookingDetailRepository.save(neworderBookingDetail);
                }
            }

            System.out.println("Gia chenh:  " + totalServicePriceChange);
            System.out.println("total ban đầu" + totalPrice);
            // nếu có thay đổi thì mới update cái này
            totalPrice += totalServicePriceChange;
            System.out.println("Tong gia sau update: " + totalPrice);
            orderBooking.setTotalPrice(totalPrice);
            orderBookingRepository.save(orderBooking);

            Wallet wallet = walletRepository.findByUserId(orderBooking.getCustomer().getUserId())
                    .orElseThrow(() -> new RuntimeException("Wallet not found"));

            if (totalServicePriceChange > 0) {
                if (wallet.getAmount() < totalServicePriceChange) {
                    throw new RuntimeException("Not enough money in the wallet");
                }
                payment.setAmount(payment.getAmount() + totalServicePriceChange);
                wallet.setAmount(wallet.getAmount() - totalServicePriceChange);
            } else if (totalServicePriceChange < 0) {
                payment.setAmount(payment.getAmount() + Math.abs(totalServicePriceChange));
                wallet.setAmount(wallet.getAmount() + Math.abs(totalServicePriceChange)); //Adding back
            }

            Transaction transaction = new Transaction();
            transaction.setTransactionId(UUID.randomUUID().toString());
            transaction.setAmount(totalServicePriceChange);
            transaction.setStatus("completed");
            transaction.setType("Update Service");
            transaction.setTransaction_time(LocalDateTime.now());
            transaction.setPayment(payment);
            paymentRepository.save(payment);
            transactionRepository.save(transaction);
            walletRepository.save(wallet);
        }
    }

    @Override
    public CustomerServiceDTO getCustomerService(String orderBookingId) {
        CustomerServiceDTO dto = new CustomerServiceDTO();

        // get detail by booking id
        List<OrderBookingDetail> bookingDetails = orderBookingDetailRepository.findDetailByBookingId(orderBookingId);

        Map<String, Integer> serviceList = new HashMap<>();
        for (OrderBookingDetail bookingDetail : bookingDetails) {
            String serviceName = bookingDetail.getService().getServiceName();
            int quantity = bookingDetail.getBookingServiceQuantity();

            // add key - value to map
            serviceList.put(serviceName, quantity);
            // o cam: 1
            // may chieu: 2
        }
        dto.setServiceItems(serviceList);
        return dto;
    }

    @Override
    @Transactional
    public void cancelOrderBooking(String jwttoken, String orderBookingId) {
        String username = jwtService.extractUsername(jwttoken);
        Customer customer = customerRepository.findCustomerByUsername(username);

        OrderBooking orderBooking = orderBookingRepository.findById(orderBookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
//        if (!orderBooking.getCustomer().getUserId().equals(customer.getUserId())) {
//            throw new RuntimeException("Unauthorized access to this booking");
//        }

        if (!orderBooking.getStatus().equals(BookingStatus.UPCOMING)) {
            throw new RuntimeException("Booking cannot be canceled");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDate createDate = LocalDateTime.parse(orderBooking.getCreateAt(), formatter).toLocalDate();
        // nếu đặt trong ngày -> huỷ
        if (createDate.equals(LocalDate.parse(orderBooking.getCheckinDate()))) {
            orderBooking.setStatus(BookingStatus.CANCELLED);
            orderBookingRepository.save(orderBooking);

            Payment payment = paymentRepository.findByOrderBookingId(orderBookingId)
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
        } else {
//            LocalTime checkinHour = LocalTime.parse(orderBooking.getSlot().get(0).getTimeStart().toString());
//            long hours = ChronoUnit.HOURS.between(LocalTime.now(), checkinHour);
//           System.out.println("Hours: " + hours);
            LocalDate checkinDate = LocalDate.parse(orderBooking.getCheckinDate());

            long days = ChronoUnit.DAYS.between(LocalDate.now(), checkinDate);
            System.out.println("day: " + days);
            System.out.println("now: " + LocalDate.now());
            if (days > 1) {
                // nếu huỷ trước 24 tiếng -> huỷ, hoàn tiền
                orderBooking.setStatus(BookingStatus.CANCELLED);
                orderBookingRepository.save(orderBooking);

                Payment payment = paymentRepository.findByOrderBookingId(orderBookingId)
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
//          } else if (hours < 24 && hours > 6) {
//                // nếu huỷ trước 24 tiếng -> huỷ, hoàn tiền 50%
//                orderBooking.setStatus(BookingStatus.CANCELLED);
//                orderBookingRepository.save(orderBooking);
//
//                Payment payment = paymentRepository.findByOrderBookingId(orderBookingId)
//                        .orElseThrow(() -> new RuntimeException("Payment not found for this booking"));
//
//                Wallet wallet = walletRepository.findByUserId(orderBooking.getCustomer().getUserId())
//                        .orElseThrow(() -> new RuntimeException("Wallet not found"));
//
//                if (payment.getStatus().equals("completed")) {
//                    // Hoàn lại tiền vào ví
//                    wallet.setAmount(wallet.getAmount() + (payment.getAmount() * 0.5f));
//                    walletRepository.save(wallet);
//
//                    Transaction refundTransaction = new Transaction();
//                    refundTransaction.setTransactionId(UUID.randomUUID().toString());
//                    refundTransaction.setAmount(payment.getAmount() * 0.5f);
//                    refundTransaction.setStatus("completed");
//                    refundTransaction.setType("refund");
//                    refundTransaction.setTransaction_time(LocalDateTime.now());
//                    refundTransaction.setPayment(payment);
//                    transactionRepository.save(refundTransaction);
//                    payment.setStatus("completed");
//                    paymentRepository.save(payment);
//                }
            } else {
                orderBooking.setStatus(BookingStatus.CANCELLED);
                orderBookingRepository.save(orderBooking);
            }
        }
    }

    @Override
    public List<OrderBookingDetailDTO> getPendingBooking() {
        List<OrderBooking> pendingList = orderBookingRepository.findByStatus(BookingStatus.PENDING);
        if (pendingList.isEmpty()) {
            return new ArrayList<>();
        }
        List<OrderBookingDetailDTO> orderBookingDetailDTOList = new ArrayList<>();

        for (OrderBooking pending : pendingList) {
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(pending.getBookingId());
            dto.setCustomerId(pending.getCustomer().getUserId());
            dto.setRoomId(pending.getRoom().getRoomId());
            dto.setTotalPrice(pending.getTotalPrice());
            dto.setSlots(pending.getSlot());
            dto.setStatus(pending.getStatus());
            dto.setCheckinDate(pending.getCheckinDate());
            dto.setCheckoutDate(pending.getCheckoutDate());

            List<OrderBookingDetail> bookingDetails = orderBookingDetailRepository.findDetailByBookingId(pending.getBookingId());
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




}

