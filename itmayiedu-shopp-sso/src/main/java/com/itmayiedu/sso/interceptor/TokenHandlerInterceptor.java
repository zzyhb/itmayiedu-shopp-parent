package com.itmayiedu.sso.interceptor;

import com.itmayiedu.common.redis.BaseRedisService;
import com.itmayiedu.common.util.JsonMessage;
import com.itmayiedu.sso.common.TokenHandler;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Auther: Administrator
 * @Date: 2018/12/12 14:53
 * @Description:
 */
public class TokenHandlerInterceptor extends BaseRedisService implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 将handler强转为HandlerMethod, 前面已经证实这个handler就是HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) o;
        // 从方法处理器中获取出要调用的方法
        Method method = handlerMethod.getMethod();
        // 获取出方法上的TokenHandler注解
        TokenHandler annotation = method.getAnnotation(TokenHandler.class);
        if (annotation!=null){
            String s = get("token");
            if (s!=null){
                return true;
            }else{
                throw new RuntimeException("登录超时");
            }
        }else {
            throw new RuntimeException("登录超时");
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
