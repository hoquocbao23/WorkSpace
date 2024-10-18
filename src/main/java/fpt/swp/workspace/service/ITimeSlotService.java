package fpt.swp.WorkSpace.service;

import fpt.swp.WorkSpace.models.TimeSlot;

import java.util.List;

public interface ITimeSlotService {
    TimeSlot addTimeSlot(TimeSlot timeSlot);

    List<TimeSlot> getAllTimeSlots();

    List<TimeSlot> getAvailableTimeSlots();


    TimeSlot getTimeSlotById(int id);

    TimeSlot updateTimeSlot(int timeSlotId,TimeSlot timeSlot);

    void deleteTimeSlot(int timeSlotId);

}
