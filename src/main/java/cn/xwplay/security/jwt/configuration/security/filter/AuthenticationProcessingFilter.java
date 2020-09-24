package cn.xwplay.security.jwt.configuration.security.filter;

import cn.hutool.json.JSONUtil;
import cn.xwplay.security.jwt.common.MultiReadHttpServletRequest;
import cn.xwplay.security.jwt.configuration.security.dto.LoginDTO;
import cn.xwplay.security.jwt.configuration.security.handler.LoginFailureHandler;
import cn.xwplay.security.jwt.configuration.security.handler.LoginSuccessHandler;
import cn.xwplay.security.jwt.configuration.security.authentication.UserAuthenticationManager;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义用户密码校验过滤器
 */
@Component
public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  public AuthenticationProcessingFilter(UserAuthenticationManager authenticationManager,
                                        LoginSuccessHandler loginSuccessHandler,
                                        LoginFailureHandler loginFailureHandler) {
    super(new AntPathRequestMatcher("/login", "POST"));
    setAuthenticationManager(authenticationManager);
    setAuthenticationSuccessHandler(loginSuccessHandler);
    setAuthenticationFailureHandler(loginFailureHandler);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    if (request.getContentType() == null || !request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
      throw new AuthenticationServiceException("请求头类型不支持: " + request.getContentType());
    }
    UsernamePasswordAuthenticationToken authRequest;
    try {
      MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(request);
      LoginDTO form = JSONUtil.toBean(wrappedRequest.getBodyJsonStrByJson(wrappedRequest), LoginDTO.class);
      authRequest = new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword(), null);
      authRequest.setDetails(authenticationDetailsSource.buildDetails(wrappedRequest));
    } catch (Exception e) {
         throw new AuthenticationServiceException(e.getMessage());
    }
    return getAuthenticationManager().authenticate(authRequest);
  }

}
