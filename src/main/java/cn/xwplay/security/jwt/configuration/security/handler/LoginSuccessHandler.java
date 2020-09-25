package cn.xwplay.security.jwt.configuration.security.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import cn.xwplay.security.jwt.common.Response;
import cn.xwplay.security.jwt.configuration.security.dto.SecurityUser;
import cn.xwplay.security.jwt.configuration.security.SecurityConstant;
import cn.xwplay.security.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtUtil jwtUtil;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    SecurityUser userInfo = (SecurityUser) authentication.getPrincipal();
    Map<String,Object> claims = MapUtil.newHashMap();
    claims.put("accountId", userInfo.getId());
    claims.put("username",authentication.getName());
    claims.put("nickname",userInfo.getNickname());
    String token = jwtUtil.generateToken(userInfo.getId(),claims, SecurityConstant.TOKEN_AUDIENCE);
    Response r = Response.ok("登录成功", MapUtil.builder().put("token",token).build());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("utf-8");
    response.getWriter().write(JSONUtil.toJsonStr(r));
  }

}
