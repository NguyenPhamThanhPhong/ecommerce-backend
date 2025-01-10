package ecommerce.api.dto;

import lombok.Data;

@Data
public class SelfStatisticsResponse {
    private int totalOrders;
    private int totalPurchases;
}
