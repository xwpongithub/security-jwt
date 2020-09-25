package cn.xwplay.security.jwt.configuration.security.authorization;

import cn.hutool.core.collection.CollUtil;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    /***
     * 返回该url所需要的用户权限信息
     *
     * @param object: 储存请求url信息
     * @return: null：标识不需要任何权限都可以访问
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        System.out.println("==--");
        // 获取当前请求url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        if ("/".equals(requestUrl)){
            return null;
        }
        if ("/test1".equals(requestUrl)) {
            return SecurityConfig.createList("admin");
        }
        // 如果数据中没有找到相应url资源则为非法访问，要求用户登录再进行操作
        return SecurityConfig.createList("login");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }

}
