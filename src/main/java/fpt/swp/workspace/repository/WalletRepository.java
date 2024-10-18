package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    @Query("SELECT w FROM Wallet w WHERE w.customer.userId = :userId")
    Optional<Wallet> findByUserId(@Param("userId") String userId);}
