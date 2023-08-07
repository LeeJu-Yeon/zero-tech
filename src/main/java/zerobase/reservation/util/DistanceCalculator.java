package zerobase.reservation.util;

public class DistanceCalculator {

    // 두 지점 사이의 거리를 계산하는 메소드
    public static double calculateDistance(double storeLat, double storeLon, double userLat, double userLon) {

        // 지구 반지름 (단위: km)
        final double R = 6371.0;

        // 라디안으로 변환
        double lat1Rad = Math.toRadians(storeLat);
        double lon1Rad = Math.toRadians(storeLon);
        double lat2Rad = Math.toRadians(userLat);
        double lon2Rad = Math.toRadians(userLon);

        // Haversine 공식 적용
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 두 지점 사이의 거리 (단위: km)
        double distance = R * c;

        return distance;
    }

}
