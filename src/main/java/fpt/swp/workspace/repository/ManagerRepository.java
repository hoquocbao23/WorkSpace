package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {

}
