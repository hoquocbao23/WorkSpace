package fpt.swp.workspace.controller;

import fpt.swp.workspace.response.ResponseHandler;
import fpt.swp.workspace.service.IMemberShipService;
import fpt.swp.workspace.service.MemberShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/membership")
public class MemberShipController {
    @Autowired
    private IMemberShipService memberShipService;

    @GetMapping
    public ResponseEntity<Object> getMemberShip() {
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, memberShipService.getAllMemberShip());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMemberShipDetail(@PathVariable("id") String id) {
        return ResponseHandler.responseBuilder("Ok", HttpStatus.OK, memberShipService.getMemberShipById(id));
    }

}
