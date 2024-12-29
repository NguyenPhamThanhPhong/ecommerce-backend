package ecommerce.api.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@PropertySource("classpath:cloudinary.yml")
@Component
public class CloudinaryProperties {
    @Value("${cloud-name}")
    private String cloudName;
    @Value("${api-key}")
    private String apiKey;
    @Value("${api-secret}")
    private String apiSecret;
    @Value("${account-folder:ecommerce/account}")
    private String accountDir;
    @Value("${product-folder:ecommerce/product}")
    private String productDir;
    @Value("${category-folder:ecommerce/category}")
    private String categoryDir;
    @Value("${brand-folder:ecommerce/brand}")
    private String brandDir;
    @Value("${blog-folder:ecommerce/blog}")
    private String blogDir;
    @Value("${blog-folder:ecommerce/coupon")
    private String couponDir;
}
