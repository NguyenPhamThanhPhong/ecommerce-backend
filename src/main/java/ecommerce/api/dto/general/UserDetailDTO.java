package ecommerce.api.dto.general;

import ecommerce.api.constants.AccountRolesEnum;
import ecommerce.api.constants.AuthRoleConstants;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Data
public class UserDetailDTO implements UserDetails {
    private UUID id;
    private Date enableDate;

    private Date disableDate;

    private String email;

    private String password;

    private String loginId;

    private Boolean isVerified;

    private String otp;

    private Date otpExpiry;

    private AccountRolesEnum role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(AuthRoleConstants.processRoles(role.name()))
                .map(role -> (GrantedAuthority) () -> role)
                .toList();
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
