package fpt.swp.workspace.controller;

import fpt.swp.workspace.response.ResponseHandler;
import fpt.swp.workspace.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    @GetMapping("/all-user")
    public ResponseEntity<Object> getAllUser(){
        return ResponseHandler.responseBuilder("Thành công", HttpStatus.OK, ownerService.getAllUsers());
    }
}
