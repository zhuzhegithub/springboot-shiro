package com.space.shiro.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.space.shiro.shiro.AuthRealm;
import com.space.shiro.shiro.CredentialsMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
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

    @Autowired
    private RedisProperties redisProperties;

    /*配置ShiroFilter*/
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
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

    //配置核心安全控制层
    @Bean
    public SecurityManager securityManager(AuthRealm authRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(authRealm);
        // 自定义session管理
        securityManager.setSessionManager(sessionManager);
        // 自定义缓存实现
        //securityManager.setCacheManager(redisCacheManager);
        //注入记住我管理器
        // securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    //配置自定义的权限登录器
    @Bean
    public AuthRealm authRealm() {
        AuthRealm authRealm = new AuthRealm();
        authRealm.setCredentialsMatcher(new CredentialsMatcher());
        return authRealm;
    }

    // 配置session管理器
    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO, RedisCacheManager redisCacheManager) {
        ShiroSessionManager shiroSessionManager = new ShiroSessionManager();
        //设置session过期时间为1小时(单位：毫秒)，默认为30分钟
        shiroSessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        //这里可以不设置。Shiro有默认的session管理。如果缓存为Redis则需改用Redis的管理
        shiroSessionManager.setSessionDAO(redisSessionDAO);
        shiroSessionManager.setCacheManager(redisCacheManager);
        return shiroSessionManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO(RedisManager redisManager) {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        return redisSessionDAO;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost());
        redisManager.setPort(redisProperties.getPort());
        // redisManager.setExpire();
        // redisManager.setTimeout();// 配置缓存过期时间
        // redisManager.setPassword(redisProperties.getPassword());
        return redisManager;
    }

    /**
     * 管理shiro bean生命周期
     */
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
     *
     * @return CookieRememberMeManager
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /*shiro方言  支持shiro标签*/
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
