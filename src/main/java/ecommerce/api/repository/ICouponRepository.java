package ecommerce.api.repository;

import ecommerce.api.entity.coupon.Coupon;
import ecommerce.api.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ICouponRepository extends JpaRepository<Coupon, UUID>, JpaSpecificationExecutor<Coupon> {
    @Modifying
    @Query("DELETE FROM Coupon c WHERE c.id = :id")
    int deleteCouponById(UUID id);

    @Modifying
    @Query("UPDATE Coupon c SET c.deletedAt = current_timestamp WHERE c.id = :id")
    int updateDeletedAtById(UUID id);
}
