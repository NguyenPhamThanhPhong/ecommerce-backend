package ecommerce.api.entity.user;

import ecommerce.api.entity.BlogPost;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.transaction.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "profiles")
public class Profile{
    @Id
    private UUID id;

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

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @OneToMany(targetEntity = BlogPost.class, mappedBy = "author")
    @Transient
    private Set<BlogPost> blogPost;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    @Transient
    @Builder.Default
    private Set<Order> orders = new LinkedHashSet<>();


}
