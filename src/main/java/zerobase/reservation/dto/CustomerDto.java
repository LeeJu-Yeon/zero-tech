package zerobase.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CustomerDto {

    @Getter
    @NotNull
    @AllArgsConstructor
    public static class SignUp {
        private String email;
        private String password;
        private String name;
        private String phone;
        private String nickname;
    }

    @Getter
    @NotNull
    @AllArgsConstructor
    public static class Login {
        private String email;
        private String password;
    }

}
