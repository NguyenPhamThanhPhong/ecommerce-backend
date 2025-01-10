package ecommerce.api.service.business;

import ecommerce.api.dto.SelfStatisticsResponse;
import ecommerce.api.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final IOrderRepository orderRepository;

    public SelfStatisticsResponse getSelfStatistics(UUID id) {
        SelfStatisticsResponse response = new SelfStatisticsResponse();
        response.setTotalOrders(orderRepository.countByCreatorId(id).orElse(0));
        response.setTotalPurchases(orderRepository.sumOrderDetailQuantity(id).orElse(0));
        return response;
    }

}
