package zerobase.reservation.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberDetailsService {

    /*
    순환 참조를 피하려고 UserDetails 가져오는 메소드를 따로 작성하였습니다
     */

    UserDetails getManagerDetailsByEmail(String email) throws UsernameNotFoundException;

    UserDetails getCustomerDetailsByEmail(String email) throws UsernameNotFoundException;

}
