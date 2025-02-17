package com.venduit.assetmanagement.api_gateway.POJO;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
public class User implements UserDetails {

    private Integer id;
    private String email;

    private String userId;
    private String firstName;
    private String lastName;
    @Setter
    private String userName;
    private String photos;
    private String phoneNumber;
    private String resetOTP;
    private String userTimeZone;
    private LocalDateTime resetOTPRequestTime;
    private LocalDateTime otpRequestTime;
    private boolean emailVerified;
    private String verificationOtp;
    private String password;
    private Role roles;
    private List<Token> tokens;

    public User(int l, String mail, String password123, Role userRole) {
        id = l;
        email = mail;
        password = password123;
        roles = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) {
            return Collections.emptyList(); // Prevent NullPointerException
        }
        return roles.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
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
