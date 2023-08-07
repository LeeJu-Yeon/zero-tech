package zerobase.reservation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.reservation.dto.ReviewDto;
import zerobase.reservation.dto.StoreDto;
import zerobase.reservation.entity.Review;
import zerobase.reservation.entity.Store;
import zerobase.reservation.enums.Error;
import zerobase.reservation.exception.StoreException;
import zerobase.reservation.repository.ReviewRepository;
import zerobase.reservation.repository.StoreRepository;
import zerobase.reservation.service.StoreService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;

    /*
    searchStores, getSortedStores 메소드 호출시
    반환된 매장 리스트에서 간략히 보여줄 항목
    1. 매장명
    2. 간략한 매장 주소 Ex) ㅇㅇ구 ㅇㅇ동
    3. 평점과 리뷰수 Ex) 4.7(150)
    4. 사용자의 현위치로부터 얼마나 떨어져있는지 Ex) 1.5 km
     */

    // 매장명 검색
    @Override
    public List<StoreDto.Brief> searchStores(String name, double userLatitude, double userLongitude) {

        List<Store> storeList = storeRepository.findAllByNameContainingIgnoreCase(name);

        // DTO 리스트로 변환해서 반환 ( 리뷰수 & 평균평점 null 체크, 거리 계산 )
        return StoreDto.Brief.getDtoListFromStoreList(storeList, userLatitude, userLongitude);
    }

    // 매장목록 조회 - 파라미터 : name => 순서 : 매장명순
    @Override
    public List<StoreDto.Brief> getStoresByNameOrder(double userLatitude, double userLongitude) {

        List<Store> storeList = storeRepository.findAllByOrderByNameAsc();

        // DTO 리스트로 변환해서 반환 ( 리뷰수 & 평균평점 null 체크, 거리 계산 )
        return StoreDto.Brief.getDtoListFromStoreList(storeList, userLatitude, userLongitude);
    }

    // 매장목록 조회 - 파라미터 : review => 순서 : 리뷰 많은순
    @Override
    public List<StoreDto.Brief> getStoresByReviewOrder(double userLatitude, double userLongitude) {

        List<Store> storeList = storeRepository.findAllByOrderByReviewCountDesc();

        // DTO 리스트로 변환해서 반환 ( 리뷰수 & 평균평점 null 체크, 거리 계산 )
        return StoreDto.Brief.getDtoListFromStoreList(storeList, userLatitude, userLongitude);
    }

    // 매장목록 조회 - 파라미터 : rating => 순서 : 평균평점 높은순
    @Override
    public List<StoreDto.Brief> getStoresByRatingOrder(double userLatitude, double userLongitude) {

        List<Store> storeList = storeRepository.findAllByOrderByAverageRatingDesc();

        // DTO 리스트로 변환해서 반환 ( 리뷰수 & 평균평점 null 체크, 거리 계산 )
        return StoreDto.Brief.getDtoListFromStoreList(storeList, userLatitude, userLongitude);
    }

    // 매장목록 조회 - 파라미터 : distance => 순서 : 가까운순
    @Override
    public List<StoreDto.Brief> getStoresByDistanceOrder(double userLatitude, double userLongitude) {

        // [Store, distance] 형이 들어있는 리스트로 반환됨
        List<Object[]> list = storeRepository.findAllByOrderByDistanceAsc(userLatitude, userLongitude);

        List<StoreDto.Brief> result = new ArrayList<>();

        for (Object[] item : list) {

            Store store = (Store) item[0];
            double distance = (double) item[1];

            long reviewCount = (store.getReviewCount() != null) ? store.getReviewCount() : 0;
            double averageRating = (store.getAverageRating() != null) ? store.getAverageRating() : 0.0;

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

    // 매장 상세정보 조회
    @Override
    public StoreDto.Detail getStoreDetails(Long storeId) {

        // 매장이 존재하나 확인
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(Error.STORE_NOT_FOUND));

        String phone = (store.getPhone() != null) ? store.getPhone() : "매장 전화 없음";
        String description = (store.getDescription() != null) ? store.getDescription() : "매장 설명 없음";
        long reviewCount = (store.getReviewCount() != null) ? store.getReviewCount() : 0;
        double averageRating = (store.getAverageRating() != null) ? store.getAverageRating() : 0.0;

        return StoreDto.Detail.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .phone(phone)
                .description(description)
                .reviewCount(reviewCount)
                .averageRating(averageRating)
                .build();
    }

    // 매장 리뷰 조회 - 조건 : 매장아이디 / 순서 : 최신리뷰순
    @Override
    public List<ReviewDto.Read> getStoreReviews(Long storeId) {

        // 매장이 존재하나 확인
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(Error.STORE_NOT_FOUND));

        List<Review> reviewList = reviewRepository.findAllByStore(store);

        // DTO 리스트로 변환해서 반환
        return ReviewDto.Read.getDtoListFromReviewList(reviewList);
    }

}
