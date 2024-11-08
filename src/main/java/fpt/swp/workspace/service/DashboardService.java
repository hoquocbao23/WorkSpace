package fpt.swp.workspace.service;

import fpt.swp.workspace.DTO.DashboardDTO;
import fpt.swp.workspace.models.BookingStatus;
import fpt.swp.workspace.models.OrderBooking;
import fpt.swp.workspace.models.User;
import fpt.swp.workspace.repository.OrderBookingRepository;
import fpt.swp.workspace.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService implements IDashboardService {
    @Autowired
    private OrderBookingRepository orderBookingRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private RoomRepository roomRepository;


    @Override
    public DashboardDTO getTotalBookingInDate(String token) {
        int count = 0;
        User user = authService.getUser(token);
        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
            count = orderBookingRepository.countBookingsByDate(LocalDate.now().toString(), buildingId, BookingStatus.CANCELLED);

        } else {
            count = orderBookingRepository.countBookingsByDate(LocalDate.now().toString(), BookingStatus.CANCELLED);
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTotalBookingSlot(count);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO getTotalBookingInWeek(String token) {
        User user = authService.getUser(token);
        int count = 0;
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        String starWeek = startOfWeek.toString();
        String endWeek = endOfWeek.toString();


        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
            count = orderBookingRepository.countBookingManyDays(starWeek, endWeek, buildingId, BookingStatus.CANCELLED);
        } else {
            count = orderBookingRepository.countBookingManyDays(starWeek, endWeek, BookingStatus.CANCELLED);
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTotalBookingSlot(count);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO getTotalBookingInMonth(String token) {
        User user = authService.getUser(token);
        int count = 0;

        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Lấy năm và tháng hiện tại
        int year = today.getYear();
        Month month = today.getMonth();

        // Ngày đầu của tháng
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        // Ngày cuối của tháng
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        String starMonth = startOfMonth.toString();
        String endMonth = endOfMonth.toString();


        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
            count = orderBookingRepository.countBookingManyDays(starMonth, endMonth, buildingId, BookingStatus.CANCELLED);
        } else {
            count = orderBookingRepository.countBookingsByDate(starMonth, endMonth, BookingStatus.CANCELLED);
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTotalBookingSlot(count);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO getTotalSpace(String token) {
        int count = 0;
        User user = authService.getUser(token);
        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
            count = roomRepository.getTotalSpace(buildingId);

        } else {
            count = roomRepository.getTotalSpace();
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTotalSpace(count);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO roomTypeAnalystByDate(String token) {
        User user = authService.getUser(token);
        List<OrderBooking> listOrder;
        Map<String, Integer> roomType = new HashMap<>();
        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
            listOrder = orderBookingRepository.findBookingsByDate(LocalDate.now().toString(), buildingId);
        } else {
            listOrder = orderBookingRepository.findBookingsByDate(LocalDate.now().toString());
        }
        for (OrderBooking order : listOrder) {
            String roomTypeId = order.getRoom().getRoomType().getRoomTypeName();
            if (roomType.containsKey(roomTypeId)) {
                roomType.put(roomTypeId, roomType.get(roomTypeId) + 1);
            } else {
                roomType.put(roomTypeId, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setRoomTypeAnalyst(roomType);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO roomTypeAnalystByWeek(String token) {
        User user = authService.getUser(token);
        List<OrderBooking> listOrder;
        Map<String, Integer> roomType = new HashMap<>();
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        String starWeek = startOfWeek.toString();
        String endWeek = endOfWeek.toString();
        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
            listOrder = orderBookingRepository.findBookingManyDays(starWeek, endWeek, buildingId);
        } else {
            listOrder = orderBookingRepository.findBookingManyDaysOwner(starWeek, endWeek);
        }
        for (OrderBooking order : listOrder) {
            String roomTypeId = order.getRoom().getRoomType().getRoomTypeName();
            if (roomType.containsKey(roomTypeId)) {
                roomType.put(roomTypeId, roomType.get(roomTypeId) + 1);
            } else {
                roomType.put(roomTypeId, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setRoomTypeAnalyst(roomType);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO roomTypeAnalystByMonth(String token) {
        User user = authService.getUser(token);
        List<OrderBooking> listOrder;
        Map<String, Integer> roomType = new HashMap<>();

        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Lấy năm và tháng hiện tại
        int year = today.getYear();
        Month month = today.getMonth();

        // Ngày đầu của tháng
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        // Ngày cuối của tháng
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        String starMonth = startOfMonth.toString();
        String endMonth = endOfMonth.toString();

        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
            listOrder = orderBookingRepository.findBookingManyDays(starMonth, endMonth, buildingId);
        } else {
            listOrder = orderBookingRepository.findBookingManyDaysOwner(starMonth, endMonth);
        }
        for (OrderBooking order : listOrder) {
            String roomTypeId = order.getRoom().getRoomType().getRoomTypeName();
            if (roomType.containsKey(roomTypeId)) {
                roomType.put(roomTypeId, roomType.get(roomTypeId) + 1);
            } else {
                roomType.put(roomTypeId, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setRoomTypeAnalyst(roomType);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO bookingAnalystByDate(String token) {
        User user = authService.getUser(token);
        List<OrderBooking> listOrder;
        Map<String, Integer> booking = new HashMap<>();
        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
            listOrder = orderBookingRepository.findBookingsByDate(LocalDate.now().toString(), buildingId);
        } else {
            listOrder = orderBookingRepository.findBookingsByDate(LocalDate.now().toString());
        }

        for (OrderBooking order : listOrder) {
            String status = order.getStatus().toString();
            if (booking.containsKey(status)) {
                booking.put(status, booking.get(status) + 1);
            } else {
                booking.put(status, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setBookingAnalyst(booking);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO bookingAnalystByWeek(String token) {
        User user = authService.getUser(token);
        List<OrderBooking> listOrder;
        Map<String, Integer> booking = new HashMap<>();
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        String starWeek = startOfWeek.toString();
        String endWeek = endOfWeek.toString();
        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
            listOrder = orderBookingRepository.findBookingManyDays(starWeek, endWeek, buildingId);
        } else {
            listOrder = orderBookingRepository.findBookingManyDaysOwner(starWeek, endWeek);
        }

        for (OrderBooking order : listOrder) {
            String status = order.getStatus().toString();
            if (booking.containsKey(status)) {
                booking.put(status, booking.get(status) + 1);
            } else {
                booking.put(status, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setBookingAnalyst(booking);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO bookingAnalystByMonth(String token) {
        User user = authService.getUser(token);
        List<OrderBooking> listOrder;
        Map<String, Integer> booking = new HashMap<>();
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Lấy năm và tháng hiện tại
        int year = today.getYear();
        Month month = today.getMonth();

        // Ngày đầu của tháng
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        // Ngày cuối của tháng
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        String starMonth = startOfMonth.toString();
        String endMonth = endOfMonth.toString();
        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
            listOrder = orderBookingRepository.findBookingManyDays(starMonth, endMonth, buildingId);
        } else {
            listOrder = orderBookingRepository.findBookingManyDaysOwner(starMonth, endMonth);
        }

        for (OrderBooking order : listOrder) {
            String status = order.getStatus().toString();
            if (booking.containsKey(status)) {
                booking.put(status, booking.get(status) + 1);
            } else {
                booking.put(status, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setBookingAnalyst(booking);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO getRevenue(String token) {
        User user = authService.getUser(token);
        DashboardDTO dashboardDTO = new DashboardDTO();
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Lấy năm và tháng hiện tại
        int year = today.getYear();
        Month month = today.getMonth();

        // Ngày đầu của tháng
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        // Ngày cuối của tháng
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        String starMonth = startOfMonth.toString();
        String endMonth = endOfMonth.toString();

        float revenue = 0.0f;
        if (user.getManager() != null) {
            String buildingId = user.getManager().getBuildingId();
             revenue = orderBookingRepository.getRevenue(starMonth,endMonth,buildingId, BookingStatus.CANCELLED);
            dashboardDTO.setRevenue(revenue);
        }
        return dashboardDTO;
    }

    // -- OWNER --
    @Override
    public DashboardDTO getTotalBookingInDateOwner(String buildingId) {
        int count = 0;
        count = orderBookingRepository.countBookingsByDate(LocalDate.now().toString(), buildingId, BookingStatus.CANCELLED);
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTotalBookingSlot(count);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO getTotalBookingInWeekOwner(String buildingId) {
        int count = 0;
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        String starWeek = startOfWeek.toString();
        String endWeek = endOfWeek.toString();
        count = orderBookingRepository.countBookingManyDays(starWeek, endWeek, buildingId, BookingStatus.CANCELLED);
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTotalBookingSlot(count);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO getTotalBookingInMonthOwner(String buildingId) {

        int count = 0;

        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Lấy năm và tháng hiện tại
        int year = today.getYear();
        Month month = today.getMonth();

        // Ngày đầu của tháng
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        // Ngày cuối của tháng
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        String starMonth = startOfMonth.toString();
        String endMonth = endOfMonth.toString();
        count = orderBookingRepository.countBookingManyDays(starMonth, endMonth, buildingId, BookingStatus.CANCELLED);
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTotalBookingSlot(count);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO getTotalSpaceOwner(String buildingId) {
        int count = 0;
        count = roomRepository.getTotalSpace(buildingId);
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setTotalSpace(count);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO roomTypeAnalystByDateOwner(String buildingId) {

        List<OrderBooking> listOrder;
        Map<String, Integer> roomType = new HashMap<>();
        listOrder = orderBookingRepository.findBookingsByDate(LocalDate.now().toString(), buildingId);
        for (OrderBooking order : listOrder) {
            String roomTypeId = order.getRoom().getRoomType().getRoomTypeName();
            if (roomType.containsKey(roomTypeId)) {
                roomType.put(roomTypeId, roomType.get(roomTypeId) + 1);
            } else {
                roomType.put(roomTypeId, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setRoomTypeAnalyst(roomType);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO roomTypeAnalystByWeekOwner(String buildingId) {

        List<OrderBooking> listOrder;
        Map<String, Integer> roomType = new HashMap<>();
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        String starWeek = startOfWeek.toString();
        String endWeek = endOfWeek.toString();
        listOrder = orderBookingRepository.findBookingManyDays(starWeek, endWeek, buildingId);
        for (OrderBooking order : listOrder) {
            String roomTypeId = order.getRoom().getRoomType().getRoomTypeName();
            if (roomType.containsKey(roomTypeId)) {
                roomType.put(roomTypeId, roomType.get(roomTypeId) + 1);
            } else {
                roomType.put(roomTypeId, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setRoomTypeAnalyst(roomType);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO roomTypeAnalystByMonthOwner(String buildingId) {

        List<OrderBooking> listOrder;
        Map<String, Integer> roomType = new HashMap<>();

        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();

        // Lấy năm và tháng hiện tại
        int year = today.getYear();
        Month month = today.getMonth();

        // Ngày đầu của tháng
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        // Ngày cuối của tháng
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        String starMonth = startOfMonth.toString();
        String endMonth = endOfMonth.toString();


        listOrder = orderBookingRepository.findBookingManyDays(starMonth, endMonth, buildingId);

        for (OrderBooking order : listOrder) {
            String roomTypeId = order.getRoom().getRoomType().getRoomTypeName();
            if (roomType.containsKey(roomTypeId)) {
                roomType.put(roomTypeId, roomType.get(roomTypeId) + 1);
            } else {
                roomType.put(roomTypeId, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setRoomTypeAnalyst(roomType);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO bookingAnalystByDateOwner(String buildingId) {
        List<OrderBooking> listOrder;
        Map<String, Integer> booking = new HashMap<>();
        listOrder = orderBookingRepository.findBookingsByDate(LocalDate.now().toString(), buildingId);
        for (OrderBooking order : listOrder) {
            String status = order.getStatus().toString();
            if (booking.containsKey(status)) {
                booking.put(status, booking.get(status) + 1);
            } else {
                booking.put(status, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setBookingAnalyst(booking);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO bookingAnalystByWeekOwner(String buildingId) {
        List<OrderBooking> listOrder;
        Map<String, Integer> booking = new HashMap<>();
        LocalDate startOfWeek = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        String starWeek = startOfWeek.toString();
        String endWeek = endOfWeek.toString();
        listOrder = orderBookingRepository.findBookingManyDays(starWeek, endWeek, buildingId);
        for (OrderBooking order : listOrder) {
            String status = order.getStatus().toString();
            if (booking.containsKey(status)) {
                booking.put(status, booking.get(status) + 1);
            } else {
                booking.put(status, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setBookingAnalyst(booking);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO bookingAnalystByMonthOwner(String buildingId) {
        List<OrderBooking> listOrder;
        Map<String, Integer> booking = new HashMap<>();
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();
        // Lấy năm và tháng hiện tại
        int year = today.getYear();
        Month month = today.getMonth();
        // Ngày đầu của tháng
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        // Ngày cuối của tháng
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        String starMonth = startOfMonth.toString();
        String endMonth = endOfMonth.toString();
        listOrder = orderBookingRepository.findBookingManyDays(starMonth, endMonth, buildingId);
        for (OrderBooking order : listOrder) {
            String status = order.getStatus().toString();
            if (booking.containsKey(status)) {
                booking.put(status, booking.get(status) + 1);
            } else {
                booking.put(status, 1);
            }
        }
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setBookingAnalyst(booking);
        return dashboardDTO;
    }

    @Override
    public DashboardDTO getRevenueOwner(String buildingId) {
        DashboardDTO dashboardDTO = new DashboardDTO();
        // Lấy ngày hiện tại
        LocalDate today = LocalDate.now();
        // Lấy năm và tháng hiện tại
        int year = today.getYear();
        Month month = today.getMonth();
        // Ngày đầu của tháng
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        // Ngày cuối của tháng
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        String starMonth = startOfMonth.toString();
        String endMonth = endOfMonth.toString();
        float revenue = 0.0f;
        revenue = orderBookingRepository.getRevenue(starMonth,endMonth,buildingId, BookingStatus.CANCELLED);
        dashboardDTO.setRevenue(revenue);
        return dashboardDTO;
    }


}
