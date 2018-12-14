package com.itmayiedu.itmayiedu_shopp_weixin.controller;

import com.itmayiedu.common.util.JsonMessage;
import com.itmayiedu.itmayiedu_shopp_weixin.token.TokenHandler;
import com.itmayiedu.itmayiedu_shopp_weixin.utils.RedisUtil;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018/12/10 09:20
 * @Description:
 */
@RestController
@RequestMapping("${projectName}/myTest")
public class MyTestController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("test1")
    public List test1(){
        List list = null;
        try {
            list = restTemplate.getForObject("http://MEMBER/member/list", List.class);
        }catch (Exception e){
            list = new ArrayList();
            list.add("接口调用出现异常,请稍后重试~!");
            list.add("滚   吧");
        }

        jmsTemplate.convertAndSend(new ActiveMQQueue("test"),list);
        return list;
    }

    @RequestMapping("test2")
    @ResponseBody
    @TokenHandler
    public JsonMessage test2(){
        boolean b = redisUtil.set("test", "test");
        return new JsonMessage(true,Boolean.toString(b),"");
    }

    /**
     * 模拟登录
     * @return
     */
    @RequestMapping("test3")
    @ResponseBody
    public JsonMessage test3(){
        boolean b = redisUtil.set("token", "111");
        return new JsonMessage(true,b==true?"成功":"失败","");
    }
}
