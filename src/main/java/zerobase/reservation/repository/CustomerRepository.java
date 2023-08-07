package zerobase.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.reservation.entity.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByNickname(String nickname);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhone(String phone);

}
