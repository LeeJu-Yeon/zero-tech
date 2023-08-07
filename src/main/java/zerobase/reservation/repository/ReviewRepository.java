package zerobase.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import zerobase.reservation.entity.Review;
import zerobase.reservation.entity.Store;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByStore(Store store);

    Long countReviewByStore(Store store);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store = :store")
    Double averageRatingByStore(Store store);

}
