package com.itmayiedu.sso.exception;

import com.itmayiedu.common.util.JsonMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: Administrator
 * @Date: 2018/12/12 15:20
 * @Description:
 */
public class MyException extends RuntimeException{
    @ExceptionHandler
    @ResponseBody
    public JsonMessage exp(HttpServletRequest request,
                           HttpServletResponse response, Exception ex)
            throws ServletException, IOException {
        if (ex.getMessage().indexOf("登录超时") != -1) {//无token
            return new JsonMessage(false,ex.getMessage(),null);
        }
        //其他错误
        return new JsonMessage(false, ex.getMessage(), null);
    }
}
