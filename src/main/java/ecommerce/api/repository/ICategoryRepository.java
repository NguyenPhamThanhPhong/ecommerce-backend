package ecommerce.api.repository;

import ecommerce.api.entity.product.Brand;
import ecommerce.api.entity.product.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ICategoryRepository extends JpaRepository<Category, UUID> {
    Page<Category> findAllByDeletedAtIsNotNull(Pageable pageable);

    @Transactional
    int deleteCategoryById(UUID id);


    @Query(nativeQuery = true , value =  """
    select distinct * from categories where name = ?
""")
    Category findCategoryByName(String categoryName);


    @Query(nativeQuery = true , value = "select * from categories where parent is null")
    List<Category> findAllParentCategories();

    @Query(nativeQuery = true , value = "select * from categories where parent = ?")
    List<Category> findAllChildren(UUID parentId);
}
