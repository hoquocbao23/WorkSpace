package fpt.swp.workspace.repository;

import fpt.swp.workspace.models.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {

}
