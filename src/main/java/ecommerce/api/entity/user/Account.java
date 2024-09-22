package ecommerce.api.entity.user;

import ecommerce.api.constants.AccountStatus;
import ecommerce.api.entity.base.EntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class Account extends EntityBase<UUID> {

    @Column(name = "enable_date")
    private Date enableDate;

    @Column(name = "disable_date")
    private Date disableDate;

    @NotNull
    @Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
    private String password;

    @Column(name = "login_id", length = Integer.MAX_VALUE)
    private String loginId;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Size(max = 6)
    @Column(name = "otp", length = 6)
    private String otp;

    @Column(name = "otp_expiry")
    private Date otpExpiry;

    @ColumnDefault("'ANONYMOUS'")
    @Column(name = "role", length = Integer.MAX_VALUE)
    private String role;

    @OneToOne(mappedBy = "account",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Profile profile;

}
//    private String email;
//    private String password;
//    private String loginId;
//    private AccountStatus status;
//
//    private List<String> roles;
//    private String OTP;
//
//
//    private Profile profile;
//    private List<AuthToken> tokens;
