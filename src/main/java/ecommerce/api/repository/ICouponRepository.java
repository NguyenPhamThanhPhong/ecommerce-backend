package ecommerce.api.repository;

import ecommerce.api.entity.coupon.Coupon;
import ecommerce.api.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ICouponRepository extends JpaRepository<Coupon, UUID>, JpaSpecificationExecutor<Coupon> {
    @Modifying
    @Query("DELETE FROM Coupon c WHERE c.id = :id")
    int deleteCouponById(UUID id);

    Optional<Coupon> findByCode(String code);

    @Query(value = """
            update coupons c set current_usage = current_usage + 1
                             where c.code = :code and c.current_usage < c.usage_limit
            and c.deleted_at is null
            and c.start_date <= now() and c.end_date >= now()
            returning *
            """,nativeQuery = true)
    Optional<Coupon> updateCouponUsage(String code);

    @Modifying
    @Query("UPDATE Coupon c SET c.deletedAt = current_timestamp WHERE c.id = :id")
    int updateDeletedAtById(UUID id);
}
