package fpt.swp.workspace.repository;

import fpt.swp.workspace.models.ServiceItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceItemsRepository extends JpaRepository<ServiceItems, Integer> {

    @Query("SELECT s FROM ServiceItems s order by s.createAt desc ")
    List<ServiceItems> getAllServiceItems();

}
