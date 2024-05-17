package com.techlambdas.delearmanagementapp.config;

import com.techlambdas.hospitalmanagement.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String userId;
    private String username;
    private String mobileNo;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String userId, String username, String mobileNo,String password,
                            Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username =username;
        this.mobileNo =mobileNo;
        this.password=password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserName(),
                user.getMobileNumber(),
                user.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
