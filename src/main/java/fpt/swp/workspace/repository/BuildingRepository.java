package fpt.swp.workspace.repository;

import fpt.swp.workspace.models.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, String> {
}
