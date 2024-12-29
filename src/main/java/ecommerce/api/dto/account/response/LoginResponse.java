package ecommerce.api.dto.account.response;

import jakarta.servlet.http.Cookie;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;

    public Cookie[] makeCookies() {
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        accessTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        refreshTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7);
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7);
        return new Cookie[] {accessTokenCookie, refreshTokenCookie};
    }
}
