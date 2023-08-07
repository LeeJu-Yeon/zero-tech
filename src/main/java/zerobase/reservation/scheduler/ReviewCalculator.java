package zerobase.reservation.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zerobase.reservation.entity.Store;
import zerobase.reservation.repository.ReviewRepository;
import zerobase.reservation.repository.StoreRepository;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ReviewCalculator {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;

    @Scheduled(cron = "0 0 5 * * *")
    public void calculateReviews() {

        log.info("Start calculating reviews.");

        List<Store> allStores = storeRepository.findAll();

        for (Store store : allStores) {

            store.setReviewCount(reviewRepository.countReviewByStore(store));

            Double averageRating = reviewRepository.averageRatingByStore(store);
            averageRating = (averageRating != null) ? averageRating : 0.0;

            store.setAverageRating(Math.round(averageRating * 100.0) / 100.0);

            storeRepository.save(store);
        }

    }

}
