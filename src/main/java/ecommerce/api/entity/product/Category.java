package ecommerce.api.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ecommerce.api.entity.base.EntityBase;
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
@SuperBuilder
@Table(name = "categories")
public class Category extends EntityBase {

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 40)
    @Column(name = "name", length = 40 , unique = true)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

}
