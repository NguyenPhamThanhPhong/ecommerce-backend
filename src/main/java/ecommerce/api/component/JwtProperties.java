package ecommerce.api.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "authentication.configs")
public class JwtProperties {
    private String accessTokenSecret;
    private String refreshTokenSecret;
    private long accessTokenExpirationMinutes;
    private long refreshTokenExpirationMinutes;
    private String issuer;
    private String audience;
}
