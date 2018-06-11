package com.space.shiro.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.space.shiro.shiro.AuthRealm;
import com.space.shiro.shiro.CredentialsMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * shiro的配置类
 *
 * @author zhuzhe
 * @date 2018/6/4 15:55
 * @email 1529949535@qq.com
 */
@Slf4j
@Configuration
public class ShiroConfig {

    /**
     * 配置ShiroFilter
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager manager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(manager);
        //配置登录的url和登录成功的url
        bean.setLoginUrl("/login");
//        bean.setSuccessUrl("/index");

        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        // anon 表示可以匿名访问
        filterChainDefinitionMap.put("/login*", "anon");
        filterChainDefinitionMap.put("/loginUser", "anon");
        filterChainDefinitionMap.put("/logout*", "anon");

        //authc  表示需要认证才可以访问
        filterChainDefinitionMap.put("/*", "authc");
        filterChainDefinitionMap.put("/**", "authc");
        filterChainDefinitionMap.put("/*.*", "authc");

        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    /**
     * 开启shiro aop注解支持,开启 权限注解
     * Controller才能使用 @RequiresPermissions
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }

    //配置核心安全事务管理器
    @Bean(name = "securityManager")
    public SecurityManager securityManager(AuthRealm authRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(authRealm);
        //注入记住我管理器
//        manager.setRememberMeManager(rememberMeManager());
        return manager;
    }

    //配置自定义的权限登录器
    @Bean
    public AuthRealm authRealm() {
        AuthRealm authRealm = new AuthRealm();
        //配置自定义的密码比较器
        authRealm.setCredentialsMatcher(new CredentialsMatcher());
        return authRealm;
    }

    /*管理shiro bean生命周期*/
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    /*remember me*/
    @Bean
    public SimpleCookie rememberMeCookie() {
        /*这个参数是cookie的名称，对应前端的checkbox的name = rememberMe  */
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        /* 记住我cookie生效时间1天 ,单位秒;  */
        simpleCookie.setMaxAge(60 * 60 * 24);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * shiro方言  支持shiro标签
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
