package ecommerce.api.entity.product;

import ecommerce.api.entity.base.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutlineInfo extends EntityBase<String> {
    private String name;
    private String description;
    private String imageUrl;
    // triggers for these fields
    private int sold;
    private int stock;
}
