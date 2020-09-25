package cn.xwplay.security.jwt.configuration.security;

import cn.xwplay.security.jwt.configuration.ConfigProperties;
import cn.xwplay.security.jwt.configuration.security.authentication.UserAuthenticationEntryPoint;
import cn.xwplay.security.jwt.configuration.security.authorization.UrlAccessDecisionManager;
import cn.xwplay.security.jwt.configuration.security.authorization.UrlFilterInvocationSecurityMetadataSource;
import cn.xwplay.security.jwt.configuration.security.filter.AuthenticationProcessingFilter;
import cn.xwplay.security.jwt.configuration.security.filter.UserAuthenticationFilter;
import cn.xwplay.security.jwt.configuration.security.handler.UrlAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ConfigProperties configProperties;

    private final UserAuthenticationFilter authenticationFilter;
    private final UserAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationProcessingFilter authenticationProcessingFilter;

    /**
     * 获取访问url所需要的角色信息
     */
    private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;
    /**
     * 认证权限处理 - 将上面所获得角色权限与当前登录用户的角色做对比，如果包含其中一个角色即可正常访问
     */
    private final UrlAccessDecisionManager urlAccessDecisionManager;
    /**
     * 自定义访问无权限接口时403响应内容
     */
    private final UrlAccessDeniedHandler urlAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.antMatcher("/**").authorizeRequests();
        // 禁用CSRF 开启跨域
        http.csrf().disable().cors();
        // 未登录认证异常
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        // 登录过后访问无权限的接口时自定义403响应内容
        http.exceptionHandling().accessDeniedHandler(urlAccessDeniedHandler);
        // 不创建会话 - 即通过前端传token到后台过滤器中验证是否存在访问权限
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin().usernameParameter("username").passwordParameter("password")
                .and().logout().logoutUrl("/logout");
        registry.antMatchers(HttpMethod.OPTIONS, "/**").denyAll();
// 只需要判断用户是否登录的话打开以下两句代码，注释withObjectPostProcessor
//        registry.antMatchers("/").permitAll();
//        registry.anyRequest().authenticated();
        // url权限认证处理
        registry.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                o.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource);
                o.setAccessDecisionManager(urlAccessDecisionManager);
                return o;
            }
        });
        registry.and().rememberMe().disable();
        registry.and().headers().frameOptions().disable();
        // 自定义过滤器在登录时认证用户名、密码
        http.addFilterAt(authenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationFilter, BasicAuthenticationFilter.class);
    }

}
