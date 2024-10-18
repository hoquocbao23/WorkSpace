package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    @Query("SELECT t FROM Transaction t WHERE t.payment.customer.user.userId = :userId")
    List<Transaction> findAllByUserId(@Param("userId") String userId);
}
