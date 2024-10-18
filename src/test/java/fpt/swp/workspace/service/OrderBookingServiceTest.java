package fpt.swp.WorkSpace.service;

import fpt.swp.WorkSpace.models.*;
import fpt.swp.WorkSpace.repository.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@SpringBootTest
public class OrderBookingServiceTest {

    @InjectMocks
    private OrderBookingService orderBookingService;

    @Mock
    private JWTService jwtService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BuildingRepository buildingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private OrderBookingRepository orderBookingRepository;

    @Mock
    private ServiceItemsRepository itemsRepository;

    @Mock
    private OrderBookingDetailRepository orderBookingDetailRepository;

    private final String jwttoken = "validToken";
    private final String buildingId = "BD001";
    private final String roomId = "S001";
    private final String checkin = "2024-10-01";
    private final String checkout = "2024-10-03";
    private final List<Integer> slotBooking = Arrays.asList(1, 2);
    private final MultiValueMap<Integer, Integer> items = new LinkedMultiValueMap<>();

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock JWT extraction
        when(jwtService.extractUsername(jwttoken)).thenReturn("bao1");

        // Mock Customer
        Customer customer = new Customer();
        when(customerRepository.findCustomerByUsername("bao1")).thenReturn(customer);

        // Mock Building
        Building building = new Building();
        when(buildingRepository.findById(buildingId)).thenReturn(Optional.of(building));

        // Mock Room
        Room room = new Room();
      //  room.setPrice(100f);  // Giá phòng
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        // Mock TimeSlots
        TimeSlot timeSlot1 = new TimeSlot();
        timeSlot1.setTimeSlotId(1);
        TimeSlot timeSlot2 = new TimeSlot();
        timeSlot1.setTimeSlotId(2);
        when(timeSlotRepository.findById(1)).thenReturn(Optional.of(timeSlot1));
        when(timeSlotRepository.findById(2)).thenReturn(Optional.of(timeSlot2));

        // Mock ServiceItems
        ServiceItems serviceItem = new ServiceItems();
        serviceItem.setServiceId(1);
        //serviceItem.setPrice(50f);  // Giá dịch vụ
        items.add(1, 2); // Service ID 1 với số lượng 2
        when(itemsRepository.findById(1)).thenReturn(Optional.of(serviceItem));
    }

    @Test
    public void testGetTotalPriceReturnWell() {
        // Tạo các mock OrderBooking và OrderBookingDetail
        // giả lập hành vi của save() trong orderBookingRepository.save
        // trả về chính đối tượng đó mà không thực hiện lưu thực tế vào cơ sở dữ liệu.
        when(orderBookingRepository.save(any(OrderBooking.class))).thenAnswer(invocation -> invocation.getArgument(0));


        // Thực hiện phương thức
        OrderBooking result = orderBookingService.createMultiOrderBooking(jwttoken, buildingId, roomId, checkin, checkout, slotBooking, items, "Test Note");


        // Kiểm tra tính toán số ngày giữa checkin và checkout
        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);
        long numberDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;

        // Kiểm tra giá phòng
        float expectedRoomPrice = 100 * slotBooking.size() * numberDays; // Giá phòng
        float expectedServicePrice = 50 * 2 * numberDays; // Giá dịch vụ
        float expectedTotalPrice = expectedRoomPrice + expectedServicePrice;

        // Kiểm tra kết quả
        verify(orderBookingRepository, times(2)).save(any(OrderBooking.class));  // Lưu 2 lần (1 lần cho order và 1 lần cho cập nhật giá)
        assertNotNull(result);
        assertEquals(result.getTotalPrice(), expectedTotalPrice, "Tổng số tiền không đúng.");
    }


    @Test
    public void testGetTotalPriceReturnFailure() {
        // Tạo các mock OrderBooking và OrderBookingDetail
        // giả lập hành vi của save() trong orderBookingRepository.save
        // trả về chính đối tượng đó mà không thực hiện lưu thực tế vào cơ sở dữ liệu.
        when(orderBookingRepository.save(any(OrderBooking.class))).thenAnswer(invocation -> invocation.getArgument(0));


        // Thực hiện phương thức
        OrderBooking result = orderBookingService.createMultiOrderBooking(jwttoken, buildingId, roomId, checkin, checkout, slotBooking, items, "Test Note");


        // Kiểm tra tính toán số ngày giữa checkin và checkout
        LocalDate checkinDate = LocalDate.parse(checkin);
        LocalDate checkoutDate = LocalDate.parse(checkout);
        long numberDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate) + 1;

        // Kiểm tra giá phòng
        // expectedRoomPrice = roomPrice * bookingslot * numberdays
        float expectedRoomPrice = 100 * slotBooking.size() * numberDays; // Giá phòng
        // expectedServicePrice = service price * number * numberdays
        float expectedServicePrice = 50 * 2 * numberDays; // Giá dịch vụ
        float expectedTotalPrice = expectedRoomPrice + expectedServicePrice;

        // Kiểm tra kết quả
        verify(orderBookingRepository, times(2)).save(any(OrderBooking.class));  // Lưu 2 lần (1 lần cho order và 1 lần cho cập nhật giá)
        assertNotNull(result);
        assertEquals(result.getTotalPrice(), expectedTotalPrice, "Tổng số tiền không đúng.");
    }
}
