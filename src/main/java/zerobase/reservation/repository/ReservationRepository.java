package zerobase.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.reservation.entity.Customer;
import zerobase.reservation.entity.Reservation;
import zerobase.reservation.entity.Store;
import zerobase.reservation.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByStoreAndStatusOrderByCreatedAtAsc(Store store, ReservationStatus status);

    List<Reservation> findAllByStoreAndStatusOrderByReservationDateAsc(Store store, ReservationStatus status);

    List<Reservation> findAllByStoreAndReservationDateOrderByReservationTime(Store store, LocalDate searchDate);

    List<Reservation> findAllByCustomerOrderByCreatedAtDesc(Customer customer);

    List<Reservation> findAllByStoreAndCustomerAndReservationDateAndStatusInOrderByReservationTime(
            Store store, Customer customer, LocalDate date, List<ReservationStatus> status);

    // 위의 메소드 이름이 너무 길어서, 별도의 이름을 지정하였습니다
    default List<Reservation> getReservationsByPhone(Store store, Customer customer, LocalDate date,
                                                     List<ReservationStatus> status) {
        return findAllByStoreAndCustomerAndReservationDateAndStatusInOrderByReservationTime(store, customer, date, status);
    }

}
