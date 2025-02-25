package fpt.swp.workspace.repository;

import fpt.swp.workspace.models.Customer;
import fpt.swp.workspace.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> findByOrderBookingId(String orderBookingId);

    Optional<Payment> findByCustomer(Customer customer);

}
