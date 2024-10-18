package fpt.swp.WorkSpace.controller;

import fpt.swp.WorkSpace.models.TimeSlot;
import fpt.swp.WorkSpace.response.ResponseHandler;
import fpt.swp.WorkSpace.service.ITimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class TimeSlotController {

    @Autowired
    private ITimeSlotService timeSlotService;

    @PostMapping("/manager/create-timeslot")
    public ResponseEntity<Object> addTimeSlot(@RequestBody TimeSlot timeSlot) {
        try{
            TimeSlot newTimeSlot = timeSlotService.addTimeSlot(timeSlot);
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, newTimeSlot);
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/manager/get-all-timeslot")
    public ResponseEntity<Object> getAllTimeSlot() {
        try{
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, timeSlotService.getAllTimeSlots());
        } catch (Exception e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/manager/get-timeslot/{id}")
    public ResponseEntity<Object> getTimeSlot(@PathVariable int id) {
        try{
            TimeSlot timeSlot = timeSlotService.getTimeSlotById(id);
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, timeSlot);
        }catch (NoSuchElementException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/manager/get-avaiable-timeslot")
    public ResponseEntity<Object> getAvailableTimeSlot() {
        try{
            List<TimeSlot> timeSlot = timeSlotService.getAvailableTimeSlots();
            return ResponseHandler.responseBuilder("Success", HttpStatus.OK, timeSlot);
        }catch (NoSuchElementException e) {
            return ResponseHandler.responseBuilder(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }




}
