package fpt.swp.WorkSpace.controller;

import fpt.swp.WorkSpace.repository.ServiceItemsRepository;
import fpt.swp.WorkSpace.response.ResponseHandler;
import fpt.swp.WorkSpace.service.IServiceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ServiceItemsController {
    @Autowired
    private IServiceItemService serviceItemService;

    @GetMapping("/get-service-items")
    public ResponseEntity<Object> getServiceItems() {
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, serviceItemService.getAllRoomService() );
    }

    @PostMapping("/manager/add-new-service")
    public ResponseEntity<Object> getServiceItems(@RequestParam("serviceName") String serviceName,
                                                  @RequestParam("image") MultipartFile[] image,
                                                  @RequestParam("price") float  price,
                                                  @RequestParam("quantity") int quantity,
                                                  @RequestParam("serviceType") String serviceType) {
        serviceItemService.addItem(serviceName, image, price, quantity, serviceType);
        return ResponseHandler.responseBuilder("Add service succesfully", HttpStatus.OK );
    }
    }
