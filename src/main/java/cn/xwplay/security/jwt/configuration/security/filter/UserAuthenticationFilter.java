package cn.xwplay.security.jwt.configuration.security.filter;

import cn.hutool.core.util.StrUtil;
import cn.xwplay.security.jwt.configuration.ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthenticationFilter extends OncePerRequestFilter {

//  BasicAuthenticationFilter
  private final ConfigProperties configProperties;
  private ConfigProperties.JwtProperties jwtProperties;

  @PostConstruct
  public void init() {
     this.jwtProperties = configProperties.getJwt();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
     // todo
     String tokenFromHeader = request.getHeader(jwtProperties.getTokenKey());
     String tokenFromParameter = request.getParameter(jwtProperties.getTokenKey());
  }

}
