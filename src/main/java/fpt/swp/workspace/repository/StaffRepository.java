package fpt.swp.workspace.repository;

import fpt.swp.workspace.models.Staff;
import fpt.swp.workspace.models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    @Query("SELECT c FROM Staff c WHERE c.user.userId = (SELECT u.userId FROM User u WHERE u.userName = ?1)")
    Staff findStaffByUsername(@Param("username") String username);

    List<Staff> findByBuildingIdAndStatus(String buildingId, UserStatus status);

}