package cn.xwplay.security.jwt.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "config", ignoreUnknownFields = false)
public class ConfigProperties {

    private final JwtProperties jwt = new JwtProperties();

    @Getter
    @Setter
    @ToString
    public static class JwtProperties {
        private String issuer;
        private String subject;
        private String tokenHeader;
        private Integer expirationTimeInMinute;
    }

}
