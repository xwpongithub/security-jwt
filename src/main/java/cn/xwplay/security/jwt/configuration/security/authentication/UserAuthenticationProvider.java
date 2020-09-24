package cn.xwplay.security.jwt.configuration.security.authentication;

import cn.xwplay.security.jwt.configuration.security.dto.SecurityUser;
import cn.xwplay.security.jwt.configuration.security.SecurityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * 自定义认证处理
 * @author Xiao Wenpeng
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsServiceImpl userDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();
    SecurityUser userInfo = (SecurityUser) userDetailsService.loadUserByUsername(username);
    if (!BCrypt.checkpw(password,userInfo.getPassword())) {
      throw new BadCredentialsException(SecurityConstant.INCORRECT_PASSWORD);
    }
    return new UsernamePasswordAuthenticationToken(userInfo, password, userInfo.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return true;
  }

}
