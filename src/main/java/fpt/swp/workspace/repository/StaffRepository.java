package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.Customer;
import fpt.swp.WorkSpace.models.Staff;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {
    @Query("SELECT c FROM Staff c WHERE c.user.userId = (SELECT u.userId FROM User u WHERE u.userName = ?1)")
    Staff findStaffByUsername(@Param("username") String username);

}