package fpt.swp.WorkSpace.util;


import fpt.swp.WorkSpace.DTO.RoomDTO;
import fpt.swp.WorkSpace.DTO.ServiceItemsDTO;
import fpt.swp.WorkSpace.models.Room;
import fpt.swp.WorkSpace.models.ServiceItems;



import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;



public class Helper {

    public static String convertLocalDateTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String creationTime = now.format(formatter);
        return creationTime;
    }

    public static String generateRandomString(int begin, int end){
        String randomString =  UUID.randomUUID().toString().replace("-", "").substring(begin, end);
        return randomString ;
    }

    public static String generateRoomId(){
        String randomString =  UUID.randomUUID().toString().replace("-", "").substring(0, 3);
        return randomString ;

    }

    public static RoomDTO mapRoomToDTO(Room room){
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoomId(room.getRoomId());
        roomDTO.setRoomName(room.getRoomName());
        roomDTO.setPrice(room.getPrice());
        roomDTO.setDescription(room.getDescription());
        if (room.getRoomImg() != null && !room.getRoomImg().isEmpty()) {
            roomDTO.setRoomImg(room.getRoomImg().split(", "));
        } else {
            roomDTO.setRoomImg(new String[]{""});
        }
        roomDTO.setBuilding(room.getBuilding().getBuildingName());
        roomDTO.setRoomType(room.getRoomType().getRoomTypeName());
        return roomDTO;
    }

    public static ServiceItemsDTO mapServiceItemsToDTO(ServiceItems serviceItems){
        ServiceItemsDTO serviceItemsDTO = new ServiceItemsDTO();
        serviceItemsDTO.setServiceId(serviceItems.getServiceId());
        serviceItemsDTO.setServiceName(serviceItems.getServiceName());
        serviceItemsDTO.setPrice(serviceItems.getPrice());
        if (serviceItems.getServiceImg() != null && !serviceItems.getServiceImg().isEmpty()) {
            serviceItemsDTO.setServiceImg(serviceItems.getServiceImg().split(", "));
        } else {
            serviceItemsDTO.setServiceImg(new String[]{""});
        }
        serviceItemsDTO.setQuantity(serviceItems.getQuantity());
        serviceItemsDTO.setServiceType(serviceItems.getServiceType());
        return serviceItemsDTO;
    }


}
