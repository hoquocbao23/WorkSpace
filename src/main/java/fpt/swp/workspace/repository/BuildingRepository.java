package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, String> {
}
