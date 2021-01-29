package cz.cvut.fel.nss.parttimejobportal.security.model;


import cz.cvut.fel.nss.parttimejobportal.model.AbstractUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private AbstractUser user;

    private final Set<GrantedAuthority> authorities;

    public UserDetails(AbstractUser user) {
        Objects.requireNonNull(user);
        this.user = user;
        this.authorities = new HashSet<>();
        addUserRole();
    }

    public UserDetails(AbstractUser user, Collection<GrantedAuthority> authorities) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(authorities);
        this.user = user;
        this.authorities = new HashSet<>();
        addUserRole();
        this.authorities.addAll(authorities);
    }

    private void addUserRole() {
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(authorities);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
//        return user.getUsername();
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
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

    public AbstractUser getUser() {
        return user;
    }

    public void eraseCredentials() {
        user.erasePassword();
    }
}
