package fpt.swp.workspace.repository;

import fpt.swp.workspace.models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, String> {

}
