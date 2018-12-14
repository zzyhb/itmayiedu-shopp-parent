package com.itmayiedu.itmayiedu_shopp_weixin.token;

import com.itmayiedu.common.util.JsonMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: Administrator
 * @Date: 2018/12/12 17:18
 * @Description:
 */
@ControllerAdvice
@ResponseBody
public class MyExceptionHandler {
    @ExceptionHandler(MyException.class)
    public JsonMessage myExceptionHandler(MyException ex){
        return new JsonMessage(false,ex.getMessage(),"");
    }
}
