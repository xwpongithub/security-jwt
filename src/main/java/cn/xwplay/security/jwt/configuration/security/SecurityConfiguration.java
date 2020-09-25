package cn.xwplay.security.jwt.configuration.security;

import cn.xwplay.security.jwt.configuration.ConfigProperties;
import cn.xwplay.security.jwt.configuration.security.authentication.UserAuthenticationEntryPoint;
import cn.xwplay.security.jwt.configuration.security.filter.AuthenticationProcessingFilter;
import cn.xwplay.security.jwt.configuration.security.filter.UserAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.PostConstruct;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ConfigProperties configProperties;
    private ConfigProperties.JwtProperties jwtProperties;

    private final UserAuthenticationFilter authenticationFilter;
    private final UserAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationProcessingFilter authenticationProcessingFilter;

    @PostConstruct
    public void init() {
         this.jwtProperties = configProperties.getJwt();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.antMatcher("/**").authorizeRequests();
        // 禁用CSRF 开启跨域
        http.csrf().disable().cors();
        // 未登录认证异常
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        // 不创建会话 - 即通过前端传token到后台过滤器中验证是否存在访问权限
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 允许匿名的url - 可理解为放行接口 - 除配置文件忽略url以外，其它所有请求都需经过认证和授权
        registry.antMatchers("/").permitAll();
        http.formLogin().usernameParameter("username").passwordParameter("password")
                .and().logout().logoutUrl("/logout");
        registry.antMatchers(HttpMethod.OPTIONS, "/**").denyAll();
        registry.anyRequest().authenticated();
        registry.and().rememberMe().disable();
        registry.and().headers().frameOptions().disable();
        // 自定义过滤器在登录时认证用户名、密码
        http.addFilterAt(authenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationFilter, BasicAuthenticationFilter.class);
    }

}
