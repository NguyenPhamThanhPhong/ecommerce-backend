package ecommerce.api.repository;

import ecommerce.api.entity.transaction.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

    @Modifying
    @Query(value = """
            insert into order_details (id, quantity, order_id, product_id)
            values (:#{#detail.id}, :#{#detail.quantity}, :#{#detail.orderId}, :#{#detail.productId})
            """, nativeQuery = true)
    void insert(OrderDetail detail);
}
