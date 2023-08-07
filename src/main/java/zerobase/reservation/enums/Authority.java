package zerobase.reservation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {

    MANAGER("ROLE_MANAGER"),
    CUSTOMER("ROLE_CUSTOMER");

    private final String authority;

}
