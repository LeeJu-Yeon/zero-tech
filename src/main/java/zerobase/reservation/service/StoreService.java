package zerobase.reservation.service;

import zerobase.reservation.dto.ReviewDto;
import zerobase.reservation.dto.StoreDto;

import java.util.List;

public interface StoreService {

    /*
    searchStores, getSortedStores 메소드 호출시
    반환된 매장 리스트에서 간략히 보여줄 항목
    1. 매장명
    2. 간략한 매장 주소 Ex) ㅇㅇ구 ㅇㅇ동
    3. 평점과 리뷰수 Ex) 4.7(150)
    4. 사용자의 현위치로부터 얼마나 떨어져있는지 Ex) 1.5 km
     */

    // 매장명 검색
    List<StoreDto.Brief> searchStores(String name, double userLatitude, double userLongitude);

    // 매장목록 조회 - 파라미터 : name => 순서 : 매장명순
    List<StoreDto.Brief> getStoresByNameOrder(double userLatitude, double userLongitude);

    // 매장목록 조회 - 파라미터 : review => 순서 : 리뷰 많은순
    List<StoreDto.Brief> getStoresByReviewOrder(double userLatitude, double userLongitude);

    // 매장목록 조회 - 파라미터 : rating => 순서 : 평균평점 높은순
    List<StoreDto.Brief> getStoresByRatingOrder(double userLatitude, double userLongitude);

    // 매장목록 조회 - 파라미터 : distance => 순서 : 가까운순
    List<StoreDto.Brief> getStoresByDistanceOrder(double userLatitude, double userLongitude);

    // 매장 상세정보 조회
    StoreDto.Detail getStoreDetails(Long storeId);

    // 매장 리뷰 조회 - 조건 : 매장아이디 / 순서 : 최신리뷰순
    List<ReviewDto.Read> getStoreReviews(Long storeId);

}
