package com.itmayiedu.itmayiedu_shopp_weixin.token;

import java.lang.annotation.*;

/**
 * @Auther: Administrator
 * @Date: 2018/12/12 14:20
 * @Description:  自定义注解 (有此注解者将执行token的验证)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TokenHandler {
    boolean value() default true;
}
