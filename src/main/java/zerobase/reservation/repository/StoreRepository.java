package zerobase.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zerobase.reservation.entity.Manager;
import zerobase.reservation.entity.Store;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findAllByNameContainingIgnoreCase(String name);

    List<Store> findAllByOrderByNameAsc();

    List<Store> findAllByOrderByReviewCountDesc();

    List<Store> findAllByOrderByAverageRatingDesc();

    @Query("SELECT s, " +
            " ( 6371 * acos(   cos(radians(:userLatitude)) * cos(radians(s.latitude)) * cos(radians(s.longitude) - radians(:userLongitude)) " +
            " + sin(radians(:userLatitude)) * sin(radians(s.latitude))   ) ) AS distance " +
            "FROM Store s ORDER BY distance ASC")
    List<Object[]> findAllByOrderByDistanceAsc(double userLatitude, double userLongitude);

    Optional<Store> findByManager(Manager manager);

}
