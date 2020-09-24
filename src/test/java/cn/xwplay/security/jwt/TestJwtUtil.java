package cn.xwplay.security.jwt;

import cn.hutool.core.map.MapUtil;
import cn.xwplay.security.jwt.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Map;

@SpringBootTest
class TestJwtUtil {

    @Resource
    private JwtUtil jwtUtil;

    @Test
    void generateToken() {
        Map<String,Object> claims = MapUtil.newHashMap();
        claims.put("userId",1);
        claims.put("username","admin");
        claims.put("nickname","电车男");
        String token = jwtUtil.generateToken(1L,claims,"签发用户登录令牌");
        System.out.println(token);
    }

}
