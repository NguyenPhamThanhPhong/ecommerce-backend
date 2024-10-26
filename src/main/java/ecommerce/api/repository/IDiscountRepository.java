package ecommerce.api.repository;

import ecommerce.api.entity.discount.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IDiscountRepository extends JpaRepository<Discount, UUID> {
}
