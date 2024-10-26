package ecommerce.api.repository;

import ecommerce.api.entity.product.Brand;
import ecommerce.api.entity.product.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ICategoryRepository extends JpaRepository<Category, UUID> {
    Page<Category> findAllByDeletedAtIsNotNull(Pageable pageable);

    int deleteCategoryById(UUID id);


    @Query("""
    select distinct c from Category c where c.name = :categoryName
""")
    Optional<Category> findCategoryByName(String categoryName);
}
