package fpt.swp.workspace.repository;

import fpt.swp.workspace.models.UserNumberShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<UserNumberShip, String> {
}
