package ecommerce.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class DashboardStatisticsResponse {
    private int totalOrders;
    private int totalPurchases;
    private int totalProducts;
    private int totalRevenue;
    public WeeklyStatistics[] weeklyStatistics;

    @Data
    public static class  WeeklyStatistics{
        private String date;
        private BigDecimal totalValue;
    }
}
