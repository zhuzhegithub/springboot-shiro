package com.space.shiro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhuzhe
 * @date 2018/6/5 13:31
 * @email 1529949535@qq.com
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor());
    }*/

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/index").setViewName("index");

        registry.addViewController("/user").setViewName("user/user");
        registry.addViewController("/shiroTag").setViewName("shiroTag");
    }
}
