package ecommerce.api.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ecommerce.api.entity.base.EntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "categories")
public class Category extends EntityBase {

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;


    @Size(max = 40)
    @Column(name = "name", length = 40 , unique = true)
    private String name;


    @Column(name = "parent")
    private UUID parent;

    @OneToMany(mappedBy = "category" , cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();

}
