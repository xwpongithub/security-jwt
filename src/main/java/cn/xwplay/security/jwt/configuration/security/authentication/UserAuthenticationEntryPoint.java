package cn.xwplay.security.jwt.configuration.security.authentication;

import cn.hutool.json.JSONUtil;
import cn.xwplay.security.jwt.common.Response;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException  {
    Response r;
    if (e!=null){
      r = Response.error(e.getMessage());
    } else {
      r = Response.error("jwtToken过期!");
    }
    response.getWriter().write(JSONUtil.toJsonStr(r));
  }

}