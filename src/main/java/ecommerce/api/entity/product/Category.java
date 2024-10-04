package ecommerce.api.entity.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "categories")
public class Category extends OutlineInfo {

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 2048)
    @Column(name = "image_url", length = 2048)
    private String imageUrl;

    @Size(max = 40)
    @Column(name = "name", length = 40)
    private String name;
}
