package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, String> {

}
