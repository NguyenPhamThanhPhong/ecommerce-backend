package ecommerce.api.dto.projection;


import java.math.BigDecimal;
import java.time.LocalDate;

public interface RecentRevenuesProjection {
    LocalDate getCreatedAt();
    BigDecimal getSum();
}
