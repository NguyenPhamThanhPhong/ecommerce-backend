package ecommerce.api.repository;

import ecommerce.api.dto.projection.RecentRevenuesProjection;
import ecommerce.api.entity.transaction.Order;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    @Modifying
    @Query("DELETE FROM Order o WHERE o.id = :id")
    int deleteOrderById(UUID id);

    @Query("""
            SELECT o FROM Order o
            left join fetch o.orderDetails
            left join fetch o.profile
            left join fetch o.coupon
            left join fetch o.payment
             WHERE o.code = :code
            """)
    Optional<Order> findByCode(Integer code);

    @EntityGraph(attributePaths = {"payment", "profile"})
    Page<Order> findAll(@NotNull Specification<Order> specification, Pageable pageable);

    @Query("""
            select o from Order o left join fetch o.payment
            where o.creatorId = :creatorId
            """)
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
            insert into orders (id, creator_id, coupon_id, total_value)
            values (:#{#order.id}, :#{#order.creatorId},
                    :#{#order.couponId}, :#{#order.totalValue})
            """, nativeQuery = true)
    void insert(Order order);

    @Modifying
    @Query(value = """
            UPDATE orders o set address = :paymentAddress, notes = :orderInfo
            WHERE o.id = :id
            """, nativeQuery = true)
    void updatePaymentAddressAndNotes(UUID id, String paymentAddress, String orderInfo);

    @Query(value = """
            SELECT created_at::date, SUM(COALESCE(total_value, 0))
            FROM orders
            WHERE created_at IS NOT NULL
              AND created_at::date >= (CURRENT_DATE - INTERVAL '7 days')::date
            GROUP BY created_at::date
            """, nativeQuery = true)
    List<RecentRevenuesProjection> getRecentRevenues();

    @Query(value = """
            select count(1)
            from orders o
            where created_at is not null
              and created_at::date >= (CURRENT_DATE - INTERVAL '7 days')::date
            """,nativeQuery = true)
    int getRecentTotalOrders();

    @Query(value = """
            select count(1)
            from orders o left join payments p
            on o.id = p.order_id
            where o.created_at is not null
              and o.created_at::date >= (CURRENT_DATE - INTERVAL '7 days')::date
                and p.payment_method = 'Payment'
            """, nativeQuery = true)
    int getRecentTotalPurchases();



}
