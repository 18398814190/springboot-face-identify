package com.wjp.config;

import com.wjp.intercepter.LoginIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 */
//@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginIntercepter loginIntercepter;

    /**
     * 注册并配置拦截器
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginIntercepter)
                .excludePathPatterns("/user/login","/user/loginVerification")  //配置不需要拦截的路径 登录路径
                .addPathPatterns("/**"); //配置拦击路径 非登录
    }
}
