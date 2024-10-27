package ecommerce.api.entity.user;

import ecommerce.api.entity.BlogPost;
import ecommerce.api.entity.transaction.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profiles")
@DynamicInsert
@DynamicUpdate
public class Profile {
    @Id
    private UUID id;

    @Size(max = 40)
    @Column(name = "full_name", length = 40)
    private String fullName;

    @Size(max = 2048)
    @Column(name = "avatar_url", length = 2048)
    private String avatarUrl;

    @Size(max = 11)
    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @OneToMany(targetEntity = BlogPost.class, mappedBy = "author")
    @Transient
    private Set<BlogPost> blogPost;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    @Transient
    private Set<Order> orders = new LinkedHashSet<>();
}
