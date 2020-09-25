package cn.xwplay.security.jwt.configuration.security.handler;

import cn.hutool.json.JSONUtil;
import cn.xwplay.security.jwt.common.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UrlAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        Response r = Response.error(HttpStatus.FORBIDDEN.value(), e.getMessage());
        response.getWriter().write(JSONUtil.toJsonStr(r));
    }

}
