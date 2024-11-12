package fpt.swp.workspace.service;

import fpt.swp.workspace.DTO.ServiceItemsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IServiceItemService {
    List<ServiceItemsDTO> getAllRoomService();
    void addItem(String serviceName, MultipartFile[] files, float price, int quantity, String serviceType);
}
