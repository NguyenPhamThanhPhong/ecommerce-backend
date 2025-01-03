package ecommerce.api.repository;

import ecommerce.api.entity.compositekey.ProductImageId;
import ecommerce.api.entity.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IProductImageRepository extends JpaRepository<ProductImage, ProductImageId> {
    int deleteAllByProductIdAndSeqNoIn(UUID productId, List<Integer> seqNos);



}
