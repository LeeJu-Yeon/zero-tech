package zerobase.reservation.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import zerobase.reservation.entity.Review;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReviewDto {

    @Getter
    @Builder
    public static class Read {
        private LocalDate visitDate;
        private String nickname;
        private double rating;
        private String content;

        public static List<ReviewDto.Read> getDtoListFromReviewList(List<Review> reviewList) {

            List<ReviewDto.Read> result = new ArrayList<>();

            for (Review review : reviewList) {

                result.add(ReviewDto.Read.builder()
                        .visitDate(review.getReservation().getReservationDate())
                        .nickname(review.getCustomer().getNickname())
                        .rating(review.getRating())
                        .content(review.getContent())
                        .build());
            }

            return result;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Write {
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "5.0")
        private double rating;
        private String content;
    }

}
