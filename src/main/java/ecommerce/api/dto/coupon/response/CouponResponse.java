package ecommerce.api.dto.coupon.response;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class CouponResponse {

    private UUID id;

    private String code;

    private Integer usageLimit;

    private String description;

    private Integer currentUsage;

    private String couponType;

    private BigDecimal value;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endDate;
}
