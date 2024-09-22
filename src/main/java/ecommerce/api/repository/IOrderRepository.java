package ecommerce.api.repository;

import ecommerce.api.entity.transaction.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Order,Integer> {
}
