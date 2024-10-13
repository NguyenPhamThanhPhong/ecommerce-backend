package ecommerce.api.entity.user;

import ecommerce.api.entity.base.EntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tokens")
public class AuthToken extends EntityBase<String> {
    @NotNull
    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @NotNull
    @Column(name = "access_token", nullable = false, length = Integer.MAX_VALUE)
    private String accessToken;

    @NotNull
    @Column(name = "refresh_token", nullable = false, length = Integer.MAX_VALUE)
    private String refreshToken;

    @NotNull
    @Column(name = "access_expiry", nullable = false)
    private Date accessExpiry;

    @NotNull
    @Column(name = "refresh_expiry", nullable = false)
    private Date refreshExpiry;

}
//    private String accountId;
//    private String accessToken;
//    private String refreshToken;
//    private Date accessExpiry;
//    private Date refreshExpiry;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @Transient
//    private Account account;
