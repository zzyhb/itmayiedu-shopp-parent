package com.itmayiedu.sso.common;

import com.itmayiedu.common.redis.BaseRedisService;
import com.itmayiedu.common.util.JsonMessage;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Auther: Administrator
 * @Date: 2018/12/12 14:22
 * @Description:
 */
@Aspect
@Component
public class TokenHandlerAspect extends BaseRedisService{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Pointcut("@annotation(com.itmayiedu.sso.common.TokenHandler)")
    public void annotationPointCut() {

    }

    @Before("annotationPointCut()")
    public JsonMessage before() {
        String s = get("token");
        Boolean hasKey = stringRedisTemplate.hasKey("token");
        if (s!=null){
            return new JsonMessage(true,"token存在,可以放行","");
        }else{
            return new JsonMessage(false,"无token,返回登录页","");
        }
    }
}
