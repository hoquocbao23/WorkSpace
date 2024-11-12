package fpt.swp.workspace.repository;

import fpt.swp.workspace.DTO.UserDto;
import fpt.swp.workspace.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.userName = ?1 and u.status not like 'DISABLE'  ")
    User findByuserName(String username) throws UsernameNotFoundException;

    @Query("Select u from User u where u.roleName not like 'OWNER' and u.status not like 'DISABLE' order by u.creationTime desc ")
    List<User> getAllUser();








}
