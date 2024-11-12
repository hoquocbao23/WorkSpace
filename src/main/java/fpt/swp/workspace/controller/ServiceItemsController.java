package fpt.swp.workspace.controller;

import fpt.swp.workspace.response.ResponseHandler;
import fpt.swp.workspace.service.IServiceItemService;
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
