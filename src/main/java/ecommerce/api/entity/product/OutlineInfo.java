package ecommerce.api.entity.product;

import ecommerce.api.entity.base.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OutlineInfo extends EntityBase<String> {
    @Column(name = "name", length = 40)
    private String name;
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;
    @Column(name = "image_url", length = 2048)
    private String imageUrl;

    // triggers for these fields
    @Column(name = "sold")
    private int sold;
    @Column(name = "stock")
    private int stock;
}
