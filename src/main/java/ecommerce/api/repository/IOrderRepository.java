package ecommerce.api.repository;

import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.transaction.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IOrderRepository extends JpaRepository<Order, UUID> {

    @Modifying
    @Query("DELETE FROM Order o WHERE o.id = :id")
    int deleteOrderById(UUID id);

}
