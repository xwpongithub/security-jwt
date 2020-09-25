package cn.xwplay.security.jwt.configuration.security.filter;

import cn.hutool.core.util.StrUtil;
import cn.xwplay.security.jwt.configuration.ConfigProperties;
import cn.xwplay.security.jwt.configuration.security.SecurityConstant;
import cn.xwplay.security.jwt.configuration.security.authentication.UserAuthenticationEntryPoint;
import cn.xwplay.security.jwt.configuration.security.dto.SecurityUser;
import cn.xwplay.security.jwt.entity.AccountEntity;
import cn.xwplay.security.jwt.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthenticationFilter extends OncePerRequestFilter {

  private final ConfigProperties configProperties;
  private ConfigProperties.JwtProperties jwtProperties;
  private final JwtUtil jwtUtil;
  private final UserAuthenticationEntryPoint authenticationEntryPoint;

  @PostConstruct
  public void init() {
     this.jwtProperties = configProperties.getJwt();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String token= request.getHeader(jwtProperties.getTokenKey());
      if (StrUtil.isBlank(token)) {
         token = request.getParameter(jwtProperties.getTokenKey());
      }
      try {
          if (StrUtil.isNotBlank(token)) {
              Claims claims = jwtUtil.getClaimsFromToken(token);
              Object accountIdObj = claims.get("accountId");
              long accountId;
              if (accountIdObj instanceof Integer) {
                  accountId = new BigDecimal((Integer)accountIdObj).longValue();
              } else if (accountIdObj instanceof Long) {
                  accountId = new BigDecimal((Long)accountIdObj).longValue();
              } else {
                  accountId = new BigDecimal((String)accountIdObj).longValue();
              }
              String nickname = (String) claims.get("nickname");
              String username = (String) claims.get("username");

              AccountEntity accountEntity = AccountEntity.builder()
                      .id(accountId)
                      .nickname(nickname)
                      .username(username)
                      .build();
              SecurityUser securityUser = new SecurityUser(accountEntity);
              UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
              SecurityContextHolder.getContext().setAuthentication(authentication);
          }
          filterChain.doFilter(request, response);
      } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException e ) {
          SecurityContextHolder.clearContext();
          if (e instanceof ExpiredJwtException) {
            authenticationEntryPoint.commence(request, response, new AccountExpiredException(SecurityConstant.TOKEN_EXPIRED));
          } else {
            authenticationEntryPoint.commence(request, response, new BadCredentialsException(SecurityConstant.INVALID_TOKEN));
          }
      }
  }

}
