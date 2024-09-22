package ecommerce.api.repository;

import ecommerce.api.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBlogPostRepository extends JpaRepository<BlogPost,String> {
}
