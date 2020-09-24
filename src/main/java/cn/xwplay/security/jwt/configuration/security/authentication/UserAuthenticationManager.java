package cn.xwplay.security.jwt.configuration.security.authentication;

import cn.xwplay.security.jwt.configuration.security.SecurityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 自定义认证管理器
 * @author Xiao Wenpeng
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthenticationManager implements AuthenticationManager {

  private final UserAuthenticationProvider authenticationProvider;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Authentication result = authenticationProvider.authenticate(authentication);
    if (Objects.nonNull(result)) {
      return result;
    }
    throw new ProviderNotFoundException(SecurityConstant.PROVIDER_NOT_FOUND);
  }

}
