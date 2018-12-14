package com.itmayiedu.sso.configuration;

import com.itmayiedu.sso.interceptor.TokenHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Auther: Administrator
 * @Date: 2018/12/12 14:56
 * @Description:
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(new TokenHandlerInterceptor());
    }
}
