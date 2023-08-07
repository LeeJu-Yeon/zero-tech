package zerobase.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.reservation.entity.Manager;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Manager> findByEmail(String email);

}
