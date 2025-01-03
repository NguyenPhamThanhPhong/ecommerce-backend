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

    Optional<Order> findByCode(Integer code);

//    @EntityGraph(attributePaths = {"orderDetails","orderDetails.product"})
    Page<Order> findAll(@NotNull Specification<Order> specification, Pageable pageable);

    @Modifying
    @Query("UPDATE Order o set o.deletedAt = current_timestamp WHERE o.id = :id")
    int updateDeletedAtById(UUID id);

}
