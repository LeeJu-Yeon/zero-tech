package zerobase.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.reservation.dto.ReviewDto;
import zerobase.reservation.dto.StoreDto;
import zerobase.reservation.service.StoreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;

    /*
    searchStores, getSortedStores 메소드 호출시
    반환된 매장 리스트에서 간략히 보여줄 항목
    1. 매장명
    2. 간략한 매장 주소 Ex) ㅇㅇ구 ㅇㅇ동
    3. 평점과 리뷰수 Ex) 4.7(150)
    4. 사용자의 현위치로부터 얼마나 떨어져있는지 Ex) 1.5 km
     */

    // 매장명 검색
    @GetMapping("/search")
    public ResponseEntity<List<StoreDto.Brief>> searchStores(@RequestParam String name,
                                                             @RequestParam double userLatitude,
                                                             @RequestParam double userLongitude) {
        return ResponseEntity.ok(storeService.searchStores(name, userLatitude, userLongitude));
    }

    // 매장목록 조회
    // 파라미터 : name => 순서 : 매장명순
    // 파라미터 : review => 순서 : 리뷰 많은순
    // 파라미터 : rating => 순서 : 평균평점 높은순
    // 파라미터 : distance => 순서 : 가까운순
    @GetMapping
    public ResponseEntity<List<StoreDto.Brief>> getSortedStores(@RequestParam String sort,
                                                                @RequestParam double userLatitude,
                                                                @RequestParam double userLongitude) {

        List<StoreDto.Brief> storeList;

        switch (sort) {
            default:
            case "name":
                storeList = storeService.getStoresByNameOrder(userLatitude, userLongitude);
                break;
            case "review":
                storeList = storeService.getStoresByReviewOrder(userLatitude, userLongitude);
                break;
            case "rating":
                storeList = storeService.getStoresByRatingOrder(userLatitude, userLongitude);
                break;
            case "distance":
                storeList = storeService.getStoresByDistanceOrder(userLatitude, userLongitude);
                break;
        }

        return ResponseEntity.ok(storeList);
    }

    // 매장 상세정보 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDto.Detail> getStoreDetails(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStoreDetails(storeId));
    }

    // 매장 리뷰 조회 - 조건 : 매장아이디 / 순서 : 최신리뷰순
    @GetMapping("/{storeId}/review")
    public ResponseEntity<List<ReviewDto.Read>> getStoreReviews(@PathVariable Long storeId) {
        return ResponseEntity.ok(storeService.getStoreReviews(storeId));
    }

}
