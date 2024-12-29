package ecommerce.api.dto.general;

import ecommerce.api.constants.AccountRolesEnum;
import ecommerce.api.constants.AuthRoleConstants;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDetailDTO implements UserDetails {
    private UUID id;
    private Date enableDate;
    private Date disableDate;
    private Date deletedAt;

    private String email;

    private String password;

    private Boolean isVerified;

    private AccountRolesEnum role;

    public UserDetailDTO(UUID userId) {
        this.id = userId;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(AuthRoleConstants.processRoles(role.name()))
                .map(role -> (GrantedAuthority) () -> role)
                .toList();
    }

    public void fromClaims(Claims claims){
        id = UUID.fromString(claims.get("userId", String.class));
        role = AccountRolesEnum.valueOf(claims.get("role", String.class));
        enableDate = claims.get("enableDate", Date.class);
        disableDate = claims.get("disableDate", Date.class);
        deletedAt = claims.get("deletedAt", Date.class);
        isVerified = claims.get("isVerified", Boolean.class);

    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
