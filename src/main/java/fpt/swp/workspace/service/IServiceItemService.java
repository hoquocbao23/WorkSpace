package fpt.swp.WorkSpace.service;

import fpt.swp.WorkSpace.DTO.ServiceItemsDTO;
import fpt.swp.WorkSpace.models.ServiceItems;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IServiceItemService {
    List<ServiceItemsDTO> getAllRoomService();
    void addItem(String serviceName, MultipartFile[] files, float price, int quantity, String serviceType);
}
