package fpt.swp.workspace.repository;

import fpt.swp.workspace.models.Manager;
import fpt.swp.workspace.models.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {
    List<Manager> findByStatus(UserStatus status);
}
