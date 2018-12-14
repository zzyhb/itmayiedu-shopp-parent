package com.itmayiedu.itmayiedu_shopp_weixin.token;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Auther: Administrator
 * @Date: 2018/12/12 14:56
 * @Description:
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Bean
    public TokenHandlerInterceptor tokenHandlerInterceptor(){
        return new TokenHandlerInterceptor();
    }
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(tokenHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/error");
    }
}
