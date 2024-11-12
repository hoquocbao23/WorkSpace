//package fpt.swp.workspace.service;
//
//import fpt.swp.workspace.models.*;
//import fpt.swp.workspace.repository.*;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//
//import static org.mockito.Mockito.*;
//import static org.testng.Assert.*;
//
//@SpringBootTest
//public class OrderBookingServiceTest {
//
//    @InjectMocks
//    private OrderBookingService orderBookingService;
//
//    @Mock
//    private JWTService jwtService;
//
//    @Mock
//    private CustomerRepository customerRepository;
//
//    @Mock
//    private BuildingRepository buildingRepository;
//
//    @Mock
//    private RoomRepository roomRepository;
//
//    @Mock
//    private TimeSlotRepository timeSlotRepository;
//
//    @Mock
//    private OrderBookingRepository orderBookingRepository;
//
//    @Mock
//    private ServiceItemsRepository itemsRepository;
//
//    @Mock
//    private WalletRepository walletRepository;
//
//    @Mock
//    private PaymentRepository paymentRepository;
//
//    @Mock
//    private TransactionRepository transactionRepository;
//
//    @Mock
//    private OrderBookingDetailRepository orderBookingDetailRepository;
//
//    @Mock
//    private SendEmailService sendEmailService;
//
//    private final String jwttoken = "validToken";
//    private final String buildingId = "BD001";
//    private final String roomId = "S001";
//
//    private final List<Integer> slotBooking = Arrays.asList(1, 2);
//    private final MultiValueMap<Integer, Integer> items = new LinkedMultiValueMap<>();
//    Customer customer = new Customer();
//    Building building = new Building();
//    Room room = new Room();
//    TimeSlot timeSlot1 = new TimeSlot();
//    TimeSlot timeSlot2 = new TimeSlot();
//    ServiceItems serviceItem = new ServiceItems();
//    Wallet wallet = new Wallet();
//
//    @BeforeTest
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Mock JWT extraction
//        when(jwtService.extractUsername(jwttoken)).thenReturn("bao1");
//
//        // Mock Customer
//        when(customerRepository.findCustomerByUsername("bao1")).thenReturn(customer);
//
//        // Mock Building
//        when(buildingRepository.findById(buildingId)).thenReturn(Optional.of(building));
//
//        // Mock Room
//        room.setPrice(100000);  // Giá phòng
//        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
//
//        // Mock TimeSlots
//        timeSlot1.setTimeSlotId(1);
//        timeSlot2.setTimeSlotId(2);
//        when(timeSlotRepository.findById(1)).thenReturn(Optional.of(timeSlot1));
//        when(timeSlotRepository.findById(2)).thenReturn(Optional.of(timeSlot2));
//
//        // Mock ServiceItems
//        serviceItem.setServiceId(1);
//        serviceItem.setPrice(50000);  // Giá dịch vụ
//        items.add(1, 2); // Service ID 1 với số lượng 2
//        when(itemsRepository.findById(1)).thenReturn(Optional.of(serviceItem));
//
//        when(walletRepository.findByUserId(customer.getUserId())).thenReturn(Optional.ofNullable(wallet));
//        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(walletRepository.save(any(Wallet.class))).thenAnswer(invocation -> invocation.getArgument(0));
//    }
//
//    // TEST CASE 01
//    // DESCRIPTION: CHECK THE createMultiOrderBooking() METHOD WITH MULTIDATE BOOKING
//    // STEPS/PROCEDURES: SELECT WORKSPACE
//    //                   SELECT START AND END DATE
//    //                   SELECT SLOT TO BOOK
//    //                   SELECT ADDITIONAL SERVICES
//    //                   CLICK BOOK
//    // EXPECTED RESULT: ACTUAL TOTAL PRICE == EXPECTED TOTAL PRICE
//    @Test
//    public void testGetTotalPriceWithOneDateReturnWell() {
//        // Tạo các mock OrderBooking và OrderBookingDetail
//        // giả lập hành vi của save() trong orderBookingRepository.save
//        // trả về chính đối tượng đó mà không thực hiện lưu thực tế vào cơ sở dữ liệu.
//        when(orderBookingRepository.save(any(OrderBooking.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        String checkin = "2024-10-01";
//        String checkout = "2024-10-01";
//        wallet.setAmount(10000000);
//        // Thực hiện phương thức
//        OrderBooking result = orderBookingService.createMultiOrderBooking(jwttoken, buildingId, roomId, checkin, checkout, slotBooking, items, "Test Note");
//
//
//        // Kiểm tra tính toán số ngày giữa checkin và checkout
//        LocalDate checkinDate = LocalDate.parse(checkin);
//        LocalDate checkoutDate = LocalDate.parse(checkout);
//        long numberDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;
//
//
//        // Kiểm tra giá phòng
//        // expectedRoomPrice = roomPrice * bookingslot * numberdays
//        float expectedRoomPrice = room.getPrice() * slotBooking.size() * numberDays; // Giá phòng
//        // expectedServicePrice = service price * number * numberdays
//        float expectedServicePrice = serviceItem.getPrice() * 2 * numberDays; // Giá dịch vụ
//        float expectedTotalPrice = expectedRoomPrice + expectedServicePrice;
//
//        // Kiểm tra kết quả
//        // verify(orderBookingRepository, times(2)).save(any(OrderBooking.class));  // Lưu 2 lần (1 lần cho order và 1 lần cho cập nhật giá)
//        assertNotNull(result);
//        assertEquals(result.getTotalPrice(), expectedTotalPrice, "Tổng số tiền không đúng.");
//    }
//
//
//    // TEST CASE 02
//    // DESCRIPTION: CHECK THE createMultiOrderBooking() METHOD WITH MULTIDATE BOOKING
//    // STEPS/PROCEDURES: SELECT WORKSPACE
//    //                   SELECT START AND END DATE
//    //                   SELECT SLOT TO BOOK
//    //                   SELECT ADDITIONAL SERVICES
//    //                   CLICK BOOK
//    // EXPECTED RESULT: ACTUAL TOTAL PRICE == EXPECTED TOTAL PRICE
//    @Test
//    public void testGetTotalPriceWithMultiDayBookingReturnWell() {
//        // Tạo các mock OrderBooking và OrderBookingDetail
//        // giả lập hành vi của save() trong orderBookingRepository.save
//        // trả về chính đối tượng đó mà không thực hiện lưu thực tế vào cơ sở dữ liệu.
//        when(orderBookingRepository.save(any(OrderBooking.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//          String checkin = "2024-10-01";
//          String checkout = "2024-10-05";
//        wallet.setAmount(10000000);
//        // Thực hiện phương thức
//        OrderBooking result = orderBookingService.createMultiOrderBooking(jwttoken, buildingId, roomId, checkin, checkout, slotBooking, items, "Test Note");
//
//
//        // Kiểm tra tính toán số ngày giữa checkin và checkout
//        LocalDate checkinDate = LocalDate.parse(checkin);
//        LocalDate checkoutDate = LocalDate.parse(checkout);
//        long numberDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;
//
//
//        // Kiểm tra giá phòng
//        // expectedRoomPrice = roomPrice * bookingslot * numberdays
//        float expectedRoomPrice = room.getPrice() * slotBooking.size() * numberDays; // Giá phòng
//        // expectedServicePrice = service price * number * numberdays
//        float expectedServicePrice = serviceItem.getPrice() * 2 * numberDays; // Giá dịch vụ
//        float expectedTotalPrice = expectedRoomPrice + expectedServicePrice;
//
//        // Kiểm tra kết quả
//        // verify(orderBookingRepository, times(2)).save(any(OrderBooking.class));  // Lưu 2 lần (1 lần cho order và 1 lần cho cập nhật giá)
//        assertNotNull(result);
//        assertEquals(result.getTotalPrice(), expectedTotalPrice, "Tổng số tiền không đúng.");
//    }
//
//    // TEST CASE 03
//    // DESCRIPTION: CHECK THE createMultiOrderBooking() METHOD WITH TOTAL PRICE > 5.000.000
//    // STEPS/PROCEDURES: SELECT WORKSPACE
//    //                   SELECT START AND END DATE
//    //                   SELECT SLOT TO BOOK
//    //                   SELECT ADDITIONAL SERVICES
//    //                   CLICK BOOK
//    // EXPECTED RESULT: RETURN ORDER BOOKING STATUS IS PENDING IF TOTAL PRICE > 5.000.000
//    @Test
//    public void testOrderBookingWithStatusIsPending() {
//        // Tạo các mock OrderBooking và OrderBookingDetail
//        // giả lập hành vi của save() trong orderBookingRepository.save
//        // trả về chính đối tượng đó mà không thực hiện lưu thực tế vào cơ sở dữ liệu.
//        when(orderBookingRepository.save(any(OrderBooking.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        String checkin = "2024-10-01";
//        String checkout = "2024-11-01";
//        wallet.setAmount(10000000);
//        // Thực hiện phương thức
//        OrderBooking result = orderBookingService.createMultiOrderBooking(jwttoken, buildingId, roomId, checkin, checkout, slotBooking, items, "Test Note");
//
//        // Kiểm tra tính toán số ngày giữa checkin và checkout
//        LocalDate checkinDate = LocalDate.parse(checkin);
//        LocalDate checkoutDate = LocalDate.parse(checkout);
//        long numberDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;
//
//
//        // Kiểm tra giá phòng
//        // expectedRoomPrice = roomPrice * bookingslot * numberdays
//        float expectedRoomPrice = room.getPrice() * slotBooking.size() * numberDays; // Giá phòng
//        // expectedServicePrice = service price * number * numberdays
//        float expectedServicePrice = serviceItem.getPrice() * 2 * numberDays; // Giá dịch vụ
//        float expectedTotalPrice = expectedRoomPrice + expectedServicePrice;
//
//        // Kiểm tra kết quả
//        System.out.println("Expected: " + expectedTotalPrice);
//        System.out.println("Actual: " + result.getTotalPrice());
//
//        assertNotNull(result);
//        assertEquals(result.getStatus(), BookingStatus.PENDING);
//    }
//
//    // TEST CASE 04
//    // DESCRIPTION: CHECK THE createMultiOrderBooking() METHOD THROWS RUNTIME EXCEPTION
//    // STEPS/PROCEDURES: SELECT WORKSPACE
//    //                   SELECT START AND END DATE
//    //                   SELECT SLOT TO BOOK
//    //                   SELECT ADDITIONAL SERVICES
//    //                   CLICK BOOK
//    // EXPECTED RESULT: THROWS RUNTIME EXCEPTION WITH MESSAGE "Not enough money in the wallet"
//    @Test(expectedExceptions = RuntimeException.class)
//    public void testOrderBookingThrowsRuntimeException() {
//        // Tạo các mock OrderBooking và OrderBookingDetail
//        // giả lập hành vi của save() trong orderBookingRepository.save
//        // trả về chính đối tượng đó mà không thực hiện lưu thực tế vào cơ sở dữ liệu.
//        when(orderBookingRepository.save(any(OrderBooking.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        String checkin = "2024-10-01";
//        String checkout = "2024-11-30";
//        wallet.setAmount(1000000);
//        // Thực hiện phương thức
//        OrderBooking result = orderBookingService.createMultiOrderBooking(jwttoken, buildingId, roomId, checkin, checkout, slotBooking, items, "Test Note");
//
//        // Kiểm tra kết quả
//        assertNotNull(result);
//        assertThrows(RuntimeException.class, () -> { orderBookingService.createMultiOrderBooking(jwttoken, buildingId, roomId, checkin, checkout, slotBooking, items, "Test Note");});
//    }
//
//
//    // TEST CASE 05
//    // DESCRIPTION: CHECK THE cancelOrderBooking(jwt, orderbooking) METHOD REFUND CORRECT IF
//    //              USER CANCELS BOOKING
//    // STEPS/PROCEDURES:
//    //
//    //
//    // EXPECTED RESULT: refund 150.000
//    @Test
//    public void testCancelOrderBookingInDateReturnWell() {
//        // OrderBooking Mock
//        OrderBooking orderBooking = new OrderBooking();
//        orderBooking.setBookingId("order1");
//        orderBooking.setCustomer(customer);
//        orderBooking.setStatus(BookingStatus.UPCOMING);
//        orderBooking.setCreateAt("07-11-2024 10:00:00");
//        orderBooking.setCheckinDate("2024-11-07");
//
//        // Payment Mock
//        Payment payment = new Payment();
//        payment.setOrderBookingId("order1");
//        payment.setAmount(100.0f);
//        payment.setStatus("completed");
//
//        // Wallet Mock
//        wallet = new Wallet();
//        wallet.setCustomer(customer);
//        wallet.setAmount(50.0f);
//
//        // Mock trả về các giá trị cần thiết khi gọi repository
//        when(orderBookingRepository.findById("order1")).thenReturn(Optional.of(orderBooking));
//        when(paymentRepository.findByOrderBookingId("order1")).thenReturn(Optional.of(payment));
//        when(walletRepository.findByUserId(customer.getUserId())).thenReturn(Optional.of(wallet));
//
//        orderBookingService.cancelOrderBooking(jwttoken, "order1");
//
//        // Kiểm tra việc cập nhật trạng thái booking
//        verify(orderBookingRepository, times(1)).save(orderBooking);
//        assertEquals(BookingStatus.CANCELLED, orderBooking.getStatus());
//
//        // Kiểm tra việc cập nhật số dư ví
//        verify(walletRepository, times(1)).save(wallet);
//        assertEquals(150.0f, wallet.getAmount(), 0.01);  // 50 cũ + 100 trả lại
//
//        // Kiểm tra việc lưu giao dịch hoàn tiền
//        verify(transactionRepository, times(1)).save(any(Transaction.class));
//
//        // Kiểm tra việc cập nhật trạng thái thanh toán
//        verify(paymentRepository, times(1)).save(payment);
//        assertEquals("completed", payment.getStatus());
//    }
//
//
//    // TEST CASE 06
//    // DESCRIPTION: CHECK THE cancelOrderBooking(token, bookingId) METHOD FOR REFUND BEFORE 24 HOURS CANCELLATION
//    // STEPS/PROCEDURES:
//    //                   1. PROVIDE VALID JWT TOKEN FOR AUTHENTICATION
//    //                   2. CALL cancelOrderBooking() METHOD WITH A VALID ORDERBOOKING ID THAT IS SCHEDULED TO OCCUR IN MORE THAN 24 HOURS
//    //                   3. CHECK IF ORDERBOOKING STATUS IS UPDATED TO "CANCELLED"
//    //                   4. CHECK IF PAYMENT IS COMPLETED AND REFUND IS ADDED TO THE CUSTOMER'S WALLET
//    //                   5. VERIFY IF THE TRANSACTION RECORD FOR THE REFUND IS SAVED
//    // EXPECTED RESULT:
//    //                   1. ORDERBOOKING STATUS SHOULD BE "CANCELLED"
//    //                   2. WALLET BALANCE SHOULD BE UPDATED TO INCLUDE THE REFUND AMOUNT
//    //                   3. TRANSACTION RECORD FOR THE REFUND SHOULD BE CREATED
//    @Test
//    public void testCancelOrderBooking_Before24Hours() {
//        // OrderBooking Mock
//        OrderBooking orderBooking = new OrderBooking();
//        orderBooking.setBookingId("order1");
//        orderBooking.setCustomer(customer);
//        orderBooking.setStatus(BookingStatus.UPCOMING);
//        orderBooking.setCreateAt("05-11-2024 10:00:00");
//        orderBooking.setCheckinDate("2024-11-11");
//
//        // Payment Mock
//        Payment payment = new Payment();
//        payment.setOrderBookingId("order1");
//        payment.setAmount(100.0f);
//        payment.setStatus("completed");
//
//        // Wallet Mock
//        wallet = new Wallet();
//        wallet.setCustomer(customer);
//        wallet.setAmount(50.0f);
//
//        // Mock trả về các giá trị cần thiết khi gọi repository
//        when(orderBookingRepository.findById("order1")).thenReturn(Optional.of(orderBooking));
//        when(paymentRepository.findByOrderBookingId("order1")).thenReturn(Optional.of(payment));
//        when(walletRepository.findByUserId(customer.getUserId())).thenReturn(Optional.of(wallet));
//
//        orderBookingService.cancelOrderBooking(jwttoken, "order1");
//
//        // Kiểm tra việc cập nhật trạng thái booking
//        verify(orderBookingRepository, times(1)).save(orderBooking);
//        assertEquals(BookingStatus.CANCELLED, orderBooking.getStatus());
//
//        // Kiểm tra việc cập nhật số dư ví
//        verify(walletRepository, times(1)).save(wallet);
//        assertEquals(150.0f, wallet.getAmount(), 0.01);  // 50 cũ + 100 trả lại
//
//        // Kiểm tra việc cập nhật trạng thái thanh toán
//        assertEquals("completed", payment.getStatus());
//    }
//
//
//
//    @Test
//    public void testCancelOrderBooking_After24Hours() {
//        // OrderBooking Mock
//        OrderBooking orderBooking = new OrderBooking();
//        orderBooking.setBookingId("order1");
//        orderBooking.setCustomer(customer);
//        orderBooking.setStatus(BookingStatus.UPCOMING);
//        orderBooking.setCreateAt("05-11-2024 10:00:00");
//        orderBooking.setCheckinDate("2024-11-09");
//
//        // Payment Mock
//        Payment payment = new Payment();
//        payment.setOrderBookingId("order1");
//        payment.setAmount(100.0f);
//        payment.setStatus("completed");
//
//        // Wallet Mock
//        wallet = new Wallet();
//        wallet.setCustomer(customer);
//        wallet.setAmount(50.0f);
//
//        // Mock trả về các giá trị cần thiết khi gọi repository
//        when(orderBookingRepository.findById("order1")).thenReturn(Optional.of(orderBooking));
//        when(paymentRepository.findByOrderBookingId("order1")).thenReturn(Optional.of(payment));
//        when(walletRepository.findByUserId(customer.getUserId())).thenReturn(Optional.of(wallet));
//
//        orderBookingService.cancelOrderBooking(jwttoken, "order1");
//
//        // Kiểm tra việc cập nhật trạng thái booking
//
//        assertEquals(BookingStatus.CANCELLED, orderBooking.getStatus());
//        assertEquals(50.0f, wallet.getAmount(), 0.01);  // Khong hoan tien
//
//
//
//    }
//
//
//
//
//
//
//
//
//}
