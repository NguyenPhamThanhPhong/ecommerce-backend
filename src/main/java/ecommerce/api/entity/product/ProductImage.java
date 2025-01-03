package ecommerce.api.entity.product;

import ecommerce.api.entity.compositekey.ProductImageId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products_images")
@Data
@IdClass(ProductImageId.class)
public class ProductImage {
    @Id
    @Column(name = "seq_no")
    private int seqNo;
    @Id
    @Column(name = "product_id")
    private UUID productId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "colour")
    private String colour;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Size(max = 2048)
    @NotNull
    @Column(name = "image_url", nullable = false, length = 2048)
    private String imageUrl;
}
