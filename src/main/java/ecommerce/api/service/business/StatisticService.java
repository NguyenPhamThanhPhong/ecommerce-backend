package ecommerce.api.service.business;

import ecommerce.api.dto.DashboardStatisticsResponse;
import ecommerce.api.dto.SelfStatisticsResponse;
import ecommerce.api.dto.projection.RecentRevenuesProjection;
import ecommerce.api.repository.IOrderRepository;
import ecommerce.api.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;

    public SelfStatisticsResponse getSelfStatistics(UUID id) {
        SelfStatisticsResponse response = new SelfStatisticsResponse();
        response.setTotalOrders(orderRepository.countByCreatorId(id).orElse(0));
        response.setTotalPurchases(orderRepository.sumOrderDetailQuantity(id).orElse(0));
        return response;
    }

    public DashboardStatisticsResponse getDashboardStatistics() {
        int totalOrders = orderRepository.getRecentTotalOrders();
        int totalPurchases = orderRepository.getRecentTotalPurchases();
        long totalProducts = productRepository.count();
        List<RecentRevenuesProjection> recentRevenues = orderRepository.getRecentRevenues();
        BigDecimal totalRecentRevenue = recentRevenues.stream()
                .map(RecentRevenuesProjection::getSum)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        DashboardStatisticsResponse response = new DashboardStatisticsResponse();
        response.setTotalOrders(totalOrders);
        response.setTotalPurchases(totalPurchases);
        response.setTotalProducts((int) totalProducts);
        response.setTotalRevenue(totalRecentRevenue.intValue());
        response.setWeeklyStatistics(recentRevenues.stream()
                .map(recentRevenue -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");
                    DashboardStatisticsResponse.WeeklyStatistics weeklyStatistics = new DashboardStatisticsResponse.WeeklyStatistics();
                    weeklyStatistics.setDate(recentRevenue.getCreatedAt().format(formatter));
                    weeklyStatistics.setTotalValue(recentRevenue.getSum());
                    return weeklyStatistics;
                }).toArray(DashboardStatisticsResponse.WeeklyStatistics[]::new));
        return response;
    }

}
