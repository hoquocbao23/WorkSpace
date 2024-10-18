package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByuserName(String username) throws UsernameNotFoundException;








}
