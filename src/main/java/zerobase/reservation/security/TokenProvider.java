package zerobase.reservation.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import zerobase.reservation.enums.Error;
import zerobase.reservation.enums.MemberType;
import zerobase.reservation.exception.MemberException;
import zerobase.reservation.service.MemberDetailsService;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MEMBER_TYPE = "memberType";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;   // 24시간 = 1000ms(1초) * 60초 * 60분 * 24시간

    private final MemberDetailsService memberDetailsService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    // 토큰 생성
    public String generateToken(Long id, String email, MemberType memberType) {

        Claims claims = Jwts.claims();
        claims.put(KEY_ID, id);
        claims.put(KEY_EMAIL, email);
        claims.put(KEY_MEMBER_TYPE, memberType.toString());

        Date issueDate = new Date();
        Date expirationDate = new Date(issueDate.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issueDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // 토큰 유효성 검증 & 파싱해서 Claim 정보 가져오기
    private Claims validateTokenAndGetClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new MemberException(Error.INVALID_TOKEN);
        }
    }

    public String getMemberEmail(String token) {
        return validateTokenAndGetClaims(token).get(KEY_EMAIL).toString();
    }

    public MemberType getMemberType(String token) {
        return MemberType.valueOf(validateTokenAndGetClaims(token).get(KEY_MEMBER_TYPE).toString());
    }

    public Authentication getAuthentication(String token) {

        UserDetails userDetails;

        if (getMemberType(token) == MemberType.MANAGER) {
            userDetails = memberDetailsService.getManagerDetailsByEmail(getMemberEmail(token));
        } else {
            userDetails = memberDetailsService.getCustomerDetailsByEmail(getMemberEmail(token));
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
