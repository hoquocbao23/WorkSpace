package fpt.swp.WorkSpace.service;

import fpt.swp.WorkSpace.DTO.BookedSlotDTO;
import fpt.swp.WorkSpace.DTO.CustomerServiceDTO;
import fpt.swp.WorkSpace.DTO.OrderBookingDetailDTO;
import fpt.swp.WorkSpace.models.*;
import fpt.swp.WorkSpace.repository.*;

import fpt.swp.WorkSpace.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Override
    public List<OrderBookingDetailDTO> getBookedSlotByRoomAndDate(String date, String roomId) {
        // get booking list checkin day and room avaiable
        List<OrderBooking> bookings = orderBookingRepository.findTimeSlotBookedByRoomAndDate( date, roomId);


        List<OrderBookingDetailDTO> bookingList = new ArrayList<>();

        if ( bookings.isEmpty()){
            throw new RuntimeException("Ngay " + date + " chua co booking nao.");
        }
        for (OrderBooking orderBooking : bookings){
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(orderBooking.getBookingId());
            dto.setRoomId(orderBooking.getRoom().getRoomId());
            dto.setCheckinDate(orderBooking.getCheckinDate());
            dto.setCheckoutDate(orderBooking.getCheckoutDate());
            dto.setTotalPrice(orderBooking.getTotalPrice());

            // Get all timeslot in Booking
            List<TimeSlot> timeSlotIdBooked = new ArrayList<>();
            int countSlot = orderBooking.getSlot().size();
            for (int i = 0; i < countSlot; i++){
                timeSlotIdBooked.add(orderBooking.getSlot().get(i));
            }
            dto.setSlots(timeSlotIdBooked);
            bookingList.add(dto);
        }
        return bookingList ;
    }

    @Override
    public List<OrderBookingDetailDTO> getBookedSlotByDate(String date) {
        // get booking list checkin day and room avaiable
        List<OrderBooking> bookings = orderBookingRepository.findBookingsByDate(date);


        List<OrderBookingDetailDTO> bookingList = new ArrayList<>();

        if ( bookings.isEmpty()){
            throw new RuntimeException("Ngay " + date + " chua co booking nao.");
        }
        for (OrderBooking orderBooking : bookings){
            OrderBookingDetailDTO dto = new OrderBookingDetailDTO();
            dto.setBookingId(orderBooking.getBookingId());
            dto.setRoomId(orderBooking.getRoom().getRoomId());
            dto.setCheckinDate(orderBooking.getCheckinDate());
            dto.setCheckoutDate(orderBooking.getCheckoutDate());
            dto.setTotalPrice(orderBooking.getTotalPrice());

            // Get all timeslot in Booking
            List<TimeSlot> timeSlotIdBooked = new ArrayList<>();
            int countSlot = orderBooking.getSlot().size();
            for (int i = 0; i < countSlot; i++){
                timeSlotIdBooked.add(orderBooking.getSlot().get(i));
            }
            dto.setSlots(timeSlotIdBooked);
            bookingList.add(dto);
        }
        return bookingList ;
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
        }return bookingList;
    }


        @Override
        public BookedSlotDTO getBookedSlotByEachDay(String checkin, String checkout, String roomId, String buildingId) {
            BookedSlotDTO bookedSlotDTO = new BookedSlotDTO();
            Map<String, ArrayList<Integer>> mapBookedSlots = new LinkedHashMap<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate checkinDate = LocalDate.parse(checkin);
            LocalDate checkoutDate = LocalDate.parse(checkout);
            long numberOfDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1 ;
            IntStream.range(0, (int) numberOfDays).forEach(i -> {
                // increase date
                LocalDate bookingDate = checkinDate.plusDays(i);
                String bookingDateStr = bookingDate.format(formatter);
                ArrayList<Integer> timeSlotIdBooked = new ArrayList<>();
                // get all booked in a day
                List<OrderBooking> bookings = orderBookingRepository.findBookingsByDate(bookingDateStr, buildingId);
                if (!bookings.isEmpty()){
                    // loop each booking in day
                    for (OrderBooking orderBooking : bookings){
                        // get all timeslot booked in each booking
                        List<TimeSlot> timeSlotBooked = orderBooking.getSlot();
                        // create a list to save timeslotId
                        // loop each slot, get slot id
                        for (TimeSlot timeSlot : timeSlotBooked){
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
            return bookedSlotDTO ;
        }


    @Override
    public OrderBooking createOrderBooking(String jwttoken, String roomId, String checkinDate, List<Integer> slotBooking, String note) {

        String username = jwtService.extractUsername(jwttoken);
        Customer customer =  customerRepository.findCustomerByUsername(username);

        Room room = roomRepository.findById(roomId).get();
        int countSlot = slotBooking.size();
        float totalPrice = room.getPrice() * countSlot;

        // get time slot booked by customer
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < countSlot; i++){
            TimeSlot timeSlot = timeSlotRepository.findById(slotBooking.get(i)).get();
            timeSlots.add(timeSlot);
        }

        OrderBooking orderBooking = new OrderBooking();
        orderBooking.setBookingId(Helper.generateRandomString(0,5));
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
        Building building = buildingRepository.findById(buildingId).get();

        Room room = roomRepository.findById(roomId).get();


        // get time slot booked by customer
        int countSlot = slotBooking.size();
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < countSlot; i++){
            TimeSlot timeSlot = timeSlotRepository.findById(slotBooking.get(i)).get();
            timeSlots.add(timeSlot);
        }

        // process total price
        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);
        //days between checkin - checkout
        long numberDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;
        System.out.println(numberDays);
        float roomPrice  = room.getPrice() * countSlot * (int) numberDays;
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
                    orderBookingDetail.setBooking(orderBooking);
                    orderBookingDetail.setService(item);
                    orderBookingDetail.setBookingServiceQuantity(quantity);
                    orderBookingDetail.setBookingServicePrice(servicePrice);
                    servicePriceTotal += servicePrice  ;
                    orderBookingDetailRepository.save(orderBookingDetail);
                }
            }
        }
        float totalPriceWithServices = roomPrice + servicePriceTotal;
        // Áp dụng giảm giá dựa trên loại membership
        totalPriceWithServices -= totalPriceWithServices  * discount;

        orderBooking.setTotalPrice(totalPriceWithServices);
        orderBookingRepository.save(orderBooking);
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
        payment.setTransactionId(transaction.getTransactionId());
        paymentRepository.save(payment);
        transactionRepository.save(transaction);
        // Trừ tiền trong ví
        wallet.setAmount(wallet.getAmount() - totalPriceWithServices);
        walletRepository.save(wallet);
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
        for (int i = 0; i < countSlot; i++){
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
        for (OrderBooking orderBooking : historyBookingList){
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
           for (OrderBookingDetail bookingDetail : bookingDetails){
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
        Customer customer =  customerRepository.findCustomerByUsername(username);
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));

        int countSlot = slotBooking.size();
        float totalPrice = room.getPrice() * countSlot;

        // get time slot booked by customer
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < countSlot; i++){
            TimeSlot timeSlot = timeSlotRepository.findById(slotBooking.get(i)).get();
            timeSlots.add(timeSlot);
        }


        OrderBooking orderBooking = new OrderBooking();
        orderBooking.setBookingId(Helper.generateRandomString(0,5));
        orderBooking.setCustomer(customer);
        orderBooking.setRoom(room);
        orderBooking.setCheckinDate(checkinDate);
        orderBooking.setCheckoutDate(checkinDate);
        orderBooking.setTotalPrice(totalPrice);
        orderBooking.setSlot(timeSlots);
        orderBooking.setCreateAt(Helper.convertLocalDateTime());
        orderBooking.setNote(note);
        orderBookingRepository.save(orderBooking);   // save to table order booking


        if (!items.isEmpty()){
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
    public void updateServiceBooking(String orderBookingId, MultiValueMap<Integer, Integer> items) {
        // get booking
        OrderBooking orderBooking = orderBookingRepository.findById(orderBookingId).orElseThrow();
        int countSlot = orderBooking.getSlot().size();
        float totalPrice = orderBooking.getRoom().getPrice() * countSlot;
        float totalServicePriceChange = 0.0f;

        if (!items.isEmpty()) {
            // Xử lý items (service id và số lượng)
            for (Map.Entry<Integer, List<Integer>> entry : items.entrySet()) {
                Integer serviceId = entry.getKey();
                Integer quantities = entry.getValue().get(0);

                OrderBookingDetail orderBookingDetail = orderBookingDetailRepository.findDetailByBookingIdAndServiceId(orderBooking.getBookingId(), serviceId);

                    // If orderBookingDetail already exist
                if (orderBookingDetail != null) {
                    // update quantity in table
                    int oldQuantity = orderBookingDetail.getBookingServiceQuantity();
                    orderBookingDetail.setBookingServiceQuantity(quantities);
                    float servicePrice = orderBookingDetail.getService().getPrice() * quantities;
                    orderBookingDetail.setBookingServicePrice(servicePrice);
                    totalServicePriceChange += servicePrice - (orderBookingDetail.getService().getPrice() * oldQuantity);
                    orderBookingDetailRepository.save(orderBookingDetail);
                } else {
                    // Add new service
                    OrderBookingDetail neworderBookingDetail = new OrderBookingDetail();
                    ServiceItems item = serviceItemsRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("Service not found"));
                    neworderBookingDetail.setBooking(orderBooking);
                    neworderBookingDetail.setService(item);
                    neworderBookingDetail.setBookingServiceQuantity(quantities);
                    float servicePrice = item.getPrice() * quantities;
                    neworderBookingDetail.setBookingServicePrice(servicePrice);
                    totalServicePriceChange += servicePrice;
                    orderBookingDetailRepository.save(neworderBookingDetail);
                }
            }
        }
        totalPrice += totalServicePriceChange;
        orderBooking.setTotalPrice(totalPrice);
        orderBookingRepository.save(orderBooking);

        Wallet wallet = walletRepository.findByUserId(orderBooking.getCustomer().getUserId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (totalServicePriceChange > 0) {
            if (wallet.getAmount() < totalServicePriceChange) {
                throw new RuntimeException("Not enough money in the wallet");
            }
            wallet.setAmount(wallet.getAmount() - totalServicePriceChange);
        } else if (totalServicePriceChange < 0) {
            wallet.setAmount(wallet.getAmount() + Math.abs(totalServicePriceChange)); //Adding back
        }
        walletRepository.save(wallet);
    }

    @Override
    public CustomerServiceDTO getCustomerService(String orderBookingId) {
        CustomerServiceDTO dto = new CustomerServiceDTO();

        // get detail by booking id
        List<OrderBookingDetail> bookingDetails = orderBookingDetailRepository.findDetailByBookingId(orderBookingId);

        Map<String, Integer> serviceList = new HashMap<>();
        for (OrderBookingDetail bookingDetail : bookingDetails){
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
    public String cancelOrderBooking(String jwttoken,String orderBookingId) {
        String username = jwtService.extractUsername(jwttoken);
        Customer customer = customerRepository.findCustomerByUsername(username);

        OrderBooking orderBooking = orderBookingRepository.findById(orderBookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!orderBooking.getCustomer().getUserId().equals(customer.getUserId())) {
            throw new RuntimeException("Unauthorized access to this booking");
        }
        if (!orderBooking.getStatus().equals(BookingStatus.UPCOMING)) {
            throw new RuntimeException("Booking cannot be canceled");
        }
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

            return "Booking cancelled and payment refunded successfully";
        } else {
            return "Booking cancelled. No payment was made, so no refund is required";
        }
    }
}
