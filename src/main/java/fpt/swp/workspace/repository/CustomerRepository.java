package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.Customer;
import fpt.swp.WorkSpace.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("SELECT c FROM Customer c WHERE c.user.userId = (SELECT u.userId FROM User u WHERE u.userName = ?1)")
    Customer findCustomerByUsername(@Param("username") String username);

    @Query("SELECT c FROM Customer c WHERE c.user.userId = ?1")
    Customer findCustomerByCustomerId(String userId);

    @Query("SELECT c.wallet FROM Customer c WHERE c.user.userId = :userId")
    Wallet findWalletByUserId(@Param("userId") String userId);
}
