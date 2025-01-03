package ecommerce.api.entity.user;

import ecommerce.api.constants.AccountRolesEnum;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account extends EntityBase {
    @Column(name = "enable_date")
    private Date enableDate;

    @Column(name = "disable_date")
    private Date disableDate;

    @Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
    private String password;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Size(max = 6)
    @Column(name = "otp", length = 6)
    private String otp;

    @Column(name = "otp_expiry")
    private Date otpExpiry;

    @ColumnDefault("'ANONYMOUS'")
    @Column(name = "role", length = Integer.MAX_VALUE)
    @Enumerated(EnumType.STRING)
    private AccountRolesEnum role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "favorite_products",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> favoriteProducts;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Profile profile;

    public void setProfile(Profile profile) {
        this.profile = profile;
        if (profile != null) {
            profile.setId(this.id);
        }
    }
}
