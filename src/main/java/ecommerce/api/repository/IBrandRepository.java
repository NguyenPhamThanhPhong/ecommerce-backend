package ecommerce.api.repository;

import ecommerce.api.entity.product.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IBrandRepository  extends JpaRepository<Brand, UUID>, JpaSpecificationExecutor<Brand> {
    Page<Brand> findAllByDeletedAtIsNotNull(Pageable pageable);
//    @Override
//    @Query(value = "select a from Brand a",
//            countQuery = "select count(1) from Brand a")
//    Page<Brand> findAll(Specification specification, Pageable pageable);

    Optional<Brand> findBrandByCode(long code);

    @Query("update Brand b set b.deletedAt = current_timestamp where b.id = :id")
    int updateBrandDeletedAt(UUID id);

}
