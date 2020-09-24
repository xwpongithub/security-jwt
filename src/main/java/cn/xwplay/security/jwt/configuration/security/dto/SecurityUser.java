package cn.xwplay.security.jwt.configuration.security.dto;

import cn.hutool.core.collection.CollUtil;
import cn.xwplay.security.jwt.entity.AccountEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SecurityUser implements UserDetails {

  private Long id;
  private String username;
  private String password;
  private String nickname;

  public SecurityUser(AccountEntity account) {
    this.id = account.getId();
    this.username = account.getUsername();
    this.password = account.getPassword();
    this.nickname = account.getNickname();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities = CollUtil.newArrayList();
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("admin");
    authorities.add(authority);
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
