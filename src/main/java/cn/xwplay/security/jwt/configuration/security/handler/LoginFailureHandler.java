package cn.xwplay.security.jwt.configuration.security.handler;

import cn.hutool.json.JSONUtil;
import cn.xwplay.security.jwt.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException {
    Response result;
    if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
      result = Response.error(e.getMessage());
    } else if (e instanceof LockedException) {
      result = Response.error("账户被锁定，请联系管理员!");
    } else if (e instanceof CredentialsExpiredException) {
      result = Response.error("证书过期，请联系管理员!");
    } else if (e instanceof AccountExpiredException) {
      result = Response.error("账户过期，请联系管理员!");
    } else if (e instanceof DisabledException) {
      result = Response.error("账户被禁用，请联系管理员!");
    } else {
      log.error("登录失败：", e);
      result = Response.error("登录失败!");
    }
    response.getWriter().write(JSONUtil.toJsonStr(result));
  }


}
