package ecommerce.api.repository;

import ecommerce.api.entity.BlogPost;
import ecommerce.api.entity.user.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBlogPostRepository extends JpaRepository<BlogPost, UUID>, JpaSpecificationExecutor<BlogPost> {
    int deleteBlogPostById(UUID id);
    Optional<BlogPost> findFirstByCodeAndDeletedAtIsNull(long id);
    Optional<BlogPost> findByCode(long id);
    Page<BlogPost> findAllByDeletedAtIsNotNull(Pageable pageable);

    @Modifying
    @Query("UPDATE BlogPost b SET b.deletedAt = CURRENT_TIMESTAMP WHERE b.id = :id")
    int updateBlogPostDeletedAt(UUID id);

    @Override
    @Query("SELECT b FROM BlogPost b")
    Page<BlogPost> findAll(Specification<BlogPost> spec, Pageable pageable);
}
