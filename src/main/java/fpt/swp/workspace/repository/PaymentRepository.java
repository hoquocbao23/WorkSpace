package fpt.swp.WorkSpace.repository;

import fpt.swp.WorkSpace.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByOrderBookingId(String orderBookingId);

}
