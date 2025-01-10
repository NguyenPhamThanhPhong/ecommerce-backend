package ecommerce.api.repository;

import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.transaction.Order;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;
import java.util.UUID;

public interface IOrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    @Modifying
    @Query("DELETE FROM Order o WHERE o.id = :id")
    int deleteOrderById(UUID id);

    @Query("""
            SELECT o FROM Order o
            left join fetch o.orderDetails od
            left join fetch od.product pd
            left join fetch o.profile
            left join fetch o.coupon
            left join fetch o.payment
            left join fetch pd.category
            left join fetch pd.brand
             WHERE o.code = :code
            """)
    @EntityGraph(attributePaths = {"orderDetails.product"})
    Optional<Order> findByCode(Integer code);

    Page<Order> findAll(@NotNull Specification<Order> specification, Pageable pageable);

    @EntityGraph(attributePaths = {"orderDetails", "orderDetails.product"})
    Page<Order> findAllByCreatorId(UUID creatorId, Pageable pageable);

    @Modifying
    @Query("UPDATE Order o set o.deletedAt = current_timestamp WHERE o.id = :id")
    int updateDeletedAtById(UUID id);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.creatorId = :creatorId")
    Optional<Integer> countByCreatorId(UUID creatorId);

    @Query(value = """
            select sum(od.quantity) from order_details od
                join orders o on od.order_id = o.id
            where o.creator_id = :creatorId
            """, nativeQuery = true)
    Optional<Integer> sumOrderDetailQuantity(UUID creatorId);

    @Modifying
    @Query(value = """
            insert into orders (id, address, notes, creator_id, coupon_id, total_value)
            values (:#{#order.id}, :#{#order.address}, :#{#order.notes},
                    :#{#order.creatorId}, :#{#order.couponId}, :#{#order.totalValue})
            """, nativeQuery = true)
    void insert(Order order);


}
