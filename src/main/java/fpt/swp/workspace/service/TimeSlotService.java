package fpt.swp.WorkSpace.service;

import fpt.swp.WorkSpace.models.TimeSlot;
import fpt.swp.WorkSpace.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class TimeSlotService implements ITimeSlotService{


    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Override
    public TimeSlot addTimeSlot(TimeSlot timeSlot) {
        // handle time overlap
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();
        // get all timeslot
        TimeSlot newTimeSlot = new TimeSlot();

        newTimeSlot.setTimeSlotId(timeSlot.getTimeSlotId());
        newTimeSlot.setTimeStart(timeSlot.getTimeStart());
        newTimeSlot.setTimeEnd(timeSlot.getTimeEnd());
        return timeSlotRepository.save(newTimeSlot);
    }

    @Override
    public List<TimeSlot> getAllTimeSlots() {
        List<TimeSlot> timeSlots = timeSlotRepository.findAll();
        if (timeSlots.isEmpty()) {
            throw new RuntimeException("Chua co slot nao");
        }
        return timeSlots ;

    }

    @Override
    public List<TimeSlot> getAvailableTimeSlots() {

        List<TimeSlot> timeSlots = timeSlotRepository.findAvaiableTimeSlots("AVAILABLE");

        return timeSlots;

    }



    @Override
    public TimeSlot getTimeSlotById(int timeSlotId) {
        TimeSlot findSlot = timeSlotRepository.findById(timeSlotId).orElseThrow();
        if (findSlot == null) {
            throw new NoSuchElementException("TimeSlot " + timeSlotId + " not found");
        }
        return findSlot;
    }

    @Override
    public TimeSlot updateTimeSlot(int timeSlotId,TimeSlot timeSlot) {
        TimeSlot updateTimeSlot = timeSlotRepository.findById(timeSlotId).orElseThrow();
        if (timeSlot.getTimeStart() != null) {
            updateTimeSlot.setTimeStart(timeSlot.getTimeStart());
        }
        if (timeSlot.getTimeEnd() != null) {
            updateTimeSlot.setTimeStart(timeSlot.getTimeEnd());
        }
        if (timeSlot.getStatus() != null) {
            updateTimeSlot.setStatus(timeSlot.getStatus());
        }
        return timeSlotRepository.save(updateTimeSlot);
    }

    @Override
    public void deleteTimeSlot(int timeSlotId) {
        timeSlotRepository.deleteById(timeSlotId);
    }
}
