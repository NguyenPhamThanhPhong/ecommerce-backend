package ecommerce.api.repository;

import ecommerce.api.entity.product.Brand;
import ecommerce.api.entity.product.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ICategoryRepository extends JpaRepository<Category, UUID>, JpaSpecificationExecutor<Category> {
    Page<Category> findAllByDeletedAtIsNotNull(Pageable pageable);

    int deleteCategoryById(UUID id);

    Optional<Category> findCategoryByCode(long code);

    @Modifying
    @Query("""
            update Category c set c.deletedAt = current_timestamp where c.id = :id
            """)
    int updateCategoryDeletedAt(UUID id);

//    @Override
//    Page<Category> findAll(Specification<Category> specification, Pageable pageable);

}
