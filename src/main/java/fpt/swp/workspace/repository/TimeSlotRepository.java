package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {

    @Query("select c from TimeSlot c where c.status = ?1")
    List<TimeSlot> findAvaiableTimeSlots(String status);

}
