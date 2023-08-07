package zerobase.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import zerobase.reservation.entity.Reservation;
import zerobase.reservation.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDto {

    @Getter
    @NotNull
    @AllArgsConstructor
    public static class Make {
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate reservationDate;
        @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime reservationTime;
        private int guestCount;
    }

    @Getter
    @Builder
    public static class Read {
        private Long id;
        private String storeName;
        private String customerName;
        private String customerPhone;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate reservationDate;
        @JsonFormat(pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime reservationTime;
        private int guestCount;
        private ReservationStatus status;

        public static ReservationDto.Read getDtoFromReservation(Reservation reservation) {
            return ReservationDto.Read.builder()
                    .id(reservation.getId())
                    .storeName(reservation.getStore().getName())
                    .customerName(reservation.getCustomer().getName())
                    .customerPhone(reservation.getCustomer().getPhone())
                    .reservationDate(reservation.getReservationDate())
                    .reservationTime(reservation.getReservationTime())
                    .guestCount(reservation.getGuestCount())
                    .status(reservation.getStatus())
                    .build();
        }

        public static List<ReservationDto.Read> getDtoListFromReservationList(List<Reservation> reservationList) {

            List<ReservationDto.Read> result = new ArrayList<>();

            for (Reservation reservation : reservationList) {

                result.add(ReservationDto.Read.builder()
                        .id(reservation.getId())
                        .storeName(reservation.getStore().getName())
                        .customerName(reservation.getCustomer().getName())
                        .customerPhone(reservation.getCustomer().getPhone())
                        .reservationDate(reservation.getReservationDate())
                        .reservationTime(reservation.getReservationTime())
                        .guestCount(reservation.getGuestCount())
                        .status(reservation.getStatus())
                        .build());
            }

            return result;
        }
    }

}
