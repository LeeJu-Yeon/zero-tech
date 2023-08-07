package zerobase.reservation.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // token -> null 가능, 유효성 미검증
        String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token)) {
            // 유효성 검사 & 인증객체 반환
            Authentication authentication = tokenProvider.getAuthentication(token);

            // 시큐리티에 인증객체 셋팅
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // 토큰의 형태만 맞으면 토큰 추출 / 유효성 검증 아직 x
    private String getTokenFromRequest(HttpServletRequest request) {

        String rawToken = request.getHeader(TOKEN_HEADER);

        if (StringUtils.hasText(rawToken) && rawToken.startsWith(TOKEN_PREFIX)) {
            return rawToken.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

}
