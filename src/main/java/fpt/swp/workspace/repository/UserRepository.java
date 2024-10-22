package fpt.swp.workspace.repository;

import fpt.swp.workspace.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByuserName(String username) throws UsernameNotFoundException;








}
