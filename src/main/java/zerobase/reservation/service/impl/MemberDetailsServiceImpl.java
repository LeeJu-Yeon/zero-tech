package zerobase.reservation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zerobase.reservation.enums.Error;
import zerobase.reservation.exception.MemberException;
import zerobase.reservation.repository.CustomerRepository;
import zerobase.reservation.repository.ManagerRepository;
import zerobase.reservation.service.MemberDetailsService;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements MemberDetailsService {

    private final ManagerRepository managerRepository;
    private final CustomerRepository customerRepository;

    /*
    순환 참조를 피하려고 UserDetails 가져오는 메소드를 따로 작성하였습니다
     */

    @Override
    public UserDetails getManagerDetailsByEmail(String email) throws UsernameNotFoundException {
        return managerRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(Error.USER_NOT_FOUND));
    }

    @Override
    public UserDetails getCustomerDetailsByEmail(String email) throws UsernameNotFoundException {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(Error.USER_NOT_FOUND));
    }

}
