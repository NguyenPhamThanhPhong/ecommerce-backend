package ecommerce.api.filter;

import ecommerce.api.config.property.JwtProperties;
import ecommerce.api.constants.AccountRolesEnum;
import ecommerce.api.dto.general.UserDetailDTO;
import ecommerce.api.entity.user.Account;
import ecommerce.api.service.business.AccountService;
import ecommerce.api.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class JwtAuthFilter extends org.springframework.web.filter.OncePerRequestFilter {

    private final JwtService jwtUtil;
    private final AccountService accountService;
    private final JwtProperties jwtProperties;

    public JwtAuthFilter(JwtService jwtUtil, AccountService accountService, JwtProperties jwtProperties) {
        this.jwtUtil = jwtUtil;
        this.accountService = accountService;
        this.jwtProperties = jwtProperties;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                } else if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }
        UserDetailDTO authAccount = null;

        if (accessToken != null && !jwtUtil.isTokenExpired(accessToken, true)) {
            String userId = jwtUtil.extractId(accessToken, true);
            Claims claims = jwtUtil.extractClaims(accessToken, true);
            authAccount = new UserDetailDTO(UUID.fromString(userId));
            authAccount.fromClaims(claims);

        } else {
            if (refreshToken != null && !jwtUtil.isTokenExpired(refreshToken, false)) {
                String userId = jwtUtil.extractId(refreshToken, false);
                authAccount = (UserDetailDTO) accountService.loadUserByUsername(userId);
            }
        }
        if (authAccount == null){
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authAccount, null, authAccount.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        Account account = new Account();
        account.setId(authAccount.getId());
        account.setRole(AccountRolesEnum.valueOf(authAccount.getRole().name()));
        account.setDeletedAt(authAccount.getDeletedAt());
        account.setDisableDate(authAccount.getDisableDate());
        account.setEnableDate(authAccount.getEnableDate());
        account.setEmail(authAccount.getEmail());
        account.setPassword(authAccount.getPassword());
        account.setIsVerified(authAccount.getIsVerified());

        String newAccessToken = jwtUtil.generateAccessToken(account);
        String newRefreshToken = jwtUtil.generateRefreshToken(account.getId().toString());
        chain.doFilter(request, response);

        Cookie accessTokenCookie = new Cookie("accessToken", newAccessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge((int) jwtProperties.getAccessTokenExpirationMinutes() * 60);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge((int) jwtProperties.getRefreshTokenExpirationMinutes() * 60);
        response.addCookie(refreshTokenCookie);
    }
}