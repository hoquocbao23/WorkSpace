package fpt.swp.workspace.service;

import fpt.swp.workspace.DTO.ServiceItemsDTO;
import fpt.swp.workspace.models.ServiceItems;
import fpt.swp.workspace.repository.ServiceItemsRepository;
import fpt.swp.workspace.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceItemService implements IServiceItemService {

    @Autowired
    private ServiceItemsRepository serviceItemsRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Override
    public List<ServiceItemsDTO> getAllRoomService() {
        List<ServiceItems> list = serviceItemsRepository.getAllServiceItems();
        List<ServiceItemsDTO> serviceItemsDTOList = new ArrayList<>();
        for (ServiceItems serviceItems : list) {
            ServiceItemsDTO serviceItemsDTO = new ServiceItemsDTO();
            serviceItemsDTO = Helper.mapServiceItemsToDTO(serviceItems);
            serviceItemsDTOList.add(serviceItemsDTO);
        }
        return serviceItemsDTOList;
    }

    @Override
    public void addItem(String serviceName, MultipartFile[] files, float price, int quantity, String serviceType) {
        ServiceItems serviceItems = new ServiceItems();
        serviceItems.setServiceName(serviceName);
        serviceItems.setPrice(price);
        serviceItems.setQuantity(quantity);
        serviceItems.setCreateAt(Helper.convertLocalDateTime());
        serviceItems.setServiceType(serviceType);

        String imgUrl = awsS3Service.saveMultiImgToS3(files);
        serviceItems.setServiceImg(imgUrl);
        serviceItemsRepository.save(serviceItems);
    }


}