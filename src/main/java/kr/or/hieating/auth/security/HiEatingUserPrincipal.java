package kr.or.hieating.auth.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class HiEatingUserPrincipal implements UserDetails {

  private static final long serialVersionUID = 1L;

  private final Long id;
  private final String email;
  private final String password;
  private final String name;
  private final Collection<? extends GrantedAuthority> authorities;

  public HiEatingUserPrincipal(
      Long id,
      String email,
      String password,
      String name,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.authorities = authorities;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getDisplayName() {
    return name;
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
    return email;
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
