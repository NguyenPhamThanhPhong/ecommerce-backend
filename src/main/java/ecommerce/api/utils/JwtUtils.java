package ecommerce.api.utils;

import ecommerce.api.component.JwtProperties;
import ecommerce.api.entity.user.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtils {
    private final JwtProperties jwtProperties;
    private final Key acccessSecretKey;
    private final Key refreshSecretKey;

    public JwtUtils(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        acccessSecretKey = Keys.hmacShaKeyFor(jwtProperties.getAccessTokenSecret().getBytes());
        refreshSecretKey = Keys.hmacShaKeyFor(jwtProperties.getRefreshTokenSecret().getBytes());
    }

    public String generateAccessToken(Account account) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", account.getRole());
        claims.put("userId", account.getId());

        return Jwts.builder()
                .addClaims(claims)
                .setSubject(account.getEmail()).setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpirationMinutes() * 60 * 1000))
                .signWith(acccessSecretKey)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder().setSubject(username).setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpirationMinutes() * 60 * 1000))
                .signWith(refreshSecretKey)
                .compact();
    }

    public Claims extractClaims(String token, boolean isAccessToken) {
        Key secret = isAccessToken ? acccessSecretKey : refreshSecretKey;
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build().parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token, boolean isAccessToken) {
        return extractClaims(token, isAccessToken).getExpiration().before(new Date());
    }

    public String extractUsername(String token, boolean isAccessToken) {
        return extractClaims(token, isAccessToken).getSubject();
    }
}