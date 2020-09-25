package cn.xwplay.security.jwt.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.xwplay.security.jwt.configuration.ConfigProperties;
import cn.xwplay.security.jwt.entity.AccountTokenEntity;
import cn.xwplay.security.jwt.service.IAccountService;
import cn.xwplay.security.jwt.service.IAccountTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Getter
@Setter
public class JwtUtil {

    private final ConfigProperties configProperties;
    private final IAccountService accountService;
    private final IAccountTokenService accountTokenService;

    private ConfigProperties.JwtProperties jwtProperties;

    @PostConstruct
    public void init() {
        jwtProperties = configProperties.getJwt();
    }

    /**
     * 从token中获取claim
     * @param token token
     * @return claim
     */
    public Claims getClaimsFromToken(String token) throws ExpiredJwtException,UnsupportedJwtException,MalformedJwtException,IllegalArgumentException,SignatureException{
            return Jwts.parserBuilder()
                    .setSigningKeyResolver(new JwtEs512SignKeyResolver())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    /**
     * 为指定用户生成token
     *
     * @param claims 用户信息
     * @return token
     */
    public String generateToken(Long accountId, Map<String, Object> claims, String audience) {
        // You use the private key (keyPair.getPrivate()) to create a JWS and the public key (keyPair.getPublic()) to parse/verify a JWS.
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES512);

        Date createTime = DateUtil.date();
        Date expirationTime = getExpirationTime(createTime);

        AccountTokenEntity accountToken = AccountTokenEntity.builder()
                .accountId(accountId)
                .publicKey(Base64.encode(keyPair.getPublic().getEncoded()))
                .expireTime(expirationTime).build();
        accountTokenService.saveOrUpdate(accountToken);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID,accountId)
                .setClaims(claims)
                .setIssuer(jwtProperties.getIssuer())
                .setSubject(jwtProperties.getSubject())
                .setAudience(audience)
                .setIssuedAt(createTime)
                .setExpiration(expirationTime)
                .setId(audience+"_"+accountId)
                .setNotBefore(createTime)
                // 支持的算法详见：https://github.com/jwtk/jjwt#features
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.ES512)
                .compact();
    }

    /**
     * 判断token是否非法
     *
     * @param token token
     * @return 未过期返回true，否则返回false
     */
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 获取token的过期时间
     *
     * @param token token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 已过期返回true，未过期返回false
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 计算token的过期时间
     *
     * @return 过期时间
     */
    private Date getExpirationTime(Date date) {
        return DateUtil.offset(date, DateField.MINUTE,jwtProperties.getExpirationTimeInMinute());
    }

}
