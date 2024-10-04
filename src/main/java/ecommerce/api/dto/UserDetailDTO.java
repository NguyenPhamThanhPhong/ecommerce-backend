package ecommerce.api.dto;

import ecommerce.api.constants.RoleConstants;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Data
public class UserDetailDTO implements UserDetails {
    private Date enableDate;

    private Date disableDate;

    private String email;

    private String password;

    private String loginId;

    private Boolean isVerified;

    private String otp;

    private Date otpExpiry;

    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(RoleConstants.processRoles(role))
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
