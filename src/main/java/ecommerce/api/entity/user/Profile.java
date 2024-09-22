package ecommerce.api.entity.user;

import ecommerce.api.entity.BlogPost;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.transaction.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profiles")
public class Profile extends EntityBase<String> {
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private Account accounts;

    @Size(max = 40)
    @Column(name = "full_name", length = 40)
    private String fullName;

    @Column(name = "age")
    private Short age;

    @Size(max = 2048)
    @Column(name = "avatar_url", length = 2048)
    private String avatarUrl;

    @Size(max = 11)
    @Column(name = "phone", length = 11)
    private String phone;

    @Size(max = 10)
    @Column(name = "date_of_birth", length = 10)
    private String dateOfBirth;

    @OneToMany(targetEntity = BlogPost.class, mappedBy = "author")
    @Transient
    private Set<BlogPost> blogPost;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    @Transient
    private Set<Order> orders = new LinkedHashSet<>();

}
