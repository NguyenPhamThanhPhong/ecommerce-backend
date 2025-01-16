package ecommerce.api.entity.user;

import ecommerce.api.entity.BlogPost;
import ecommerce.api.entity.transaction.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;

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

    @Column(name = "primary_address")
    private String primaryAddress;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    private Account account;

    @Column(name = "addresses")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> addresses;

    @OneToMany(targetEntity = BlogPost.class, mappedBy = "author")
    @Transient
    private Set<BlogPost> blogPost;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    @Column(insertable = false, updatable = false)
    private List<Order> orders = new ArrayList<>();
}
