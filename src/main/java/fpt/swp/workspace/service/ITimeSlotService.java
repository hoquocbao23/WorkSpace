package fpt.swp.workspace.service;

import fpt.swp.workspace.models.TimeSlot;

import java.util.List;

public interface ITimeSlotService {
    TimeSlot addTimeSlot(TimeSlot timeSlot);

    List<TimeSlot> getAllTimeSlots();

    List<TimeSlot> getAvailableTimeSlots();


    TimeSlot getTimeSlotById(int id);

    TimeSlot updateTimeSlot(int timeSlotId,TimeSlot timeSlot);

    void deleteTimeSlot(int timeSlotId);

}
