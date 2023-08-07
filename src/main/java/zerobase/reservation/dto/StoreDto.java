package zerobase.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import zerobase.reservation.entity.Store;
import zerobase.reservation.util.DistanceCalculator;

import java.util.ArrayList;
import java.util.List;

public class StoreDto {

    @Getter
    @AllArgsConstructor
    public static class Registration {
        @NotNull
        private String name;
        @NotNull
        private String address;
        private String phone;
        private String description;

        // 프론트에서 주소 입력시, 위치 조회하여 자동으로 입력된다 가정
        private double latitude;
        private double longitude;
    }

    @Getter
    @Builder
    public static class Brief {
        private Long id;
        private String name;
        private String address;
        private long reviewCount;
        private double averageRating;
        private double distance;

        public static List<StoreDto.Brief> getDtoListFromStoreList(List<Store> storeList,
                                                                   double userLatitude, double userLongitude) {

            List<StoreDto.Brief> result = new ArrayList<>();

            for (Store store : storeList) {

                long reviewCount = (store.getReviewCount() != null) ? store.getReviewCount() : 0;
                double averageRating = (store.getAverageRating() != null) ? store.getAverageRating() : 0.0;
                double distance = DistanceCalculator.calculateDistance(store.getLatitude(), store.getLongitude(),
                        userLatitude, userLongitude);

                result.add(StoreDto.Brief.builder()
                        .id(store.getId())
                        .name(store.getName())
                        .address(store.getAddress())
                        .reviewCount(reviewCount)
                        .averageRating(averageRating)
                        .distance(distance)
                        .build());
            }

            return result;
        }
    }

    @Getter
    @Builder
    public static class Detail {
        private Long id;
        private String name;
        private String address;
        private String phone;
        private String description;
        private long reviewCount;
        private double averageRating;
    }

}
