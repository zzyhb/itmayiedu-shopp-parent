package com.itmayiedu.itmayiedu_shopp_weixin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018/12/8 16:53
 * @Description:
 */
@Controller
@RequestMapping("${projectName}/shop")
public class ShopController {
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("index")
    public ModelAndView index(){
        return new ModelAndView("shop/index");
    }
    @RequestMapping("friends")
    public ModelAndView friends(){
        ModelAndView mav = new ModelAndView("shop/friends");
        //List list = restTemplate.getForObject("http://MEMBER/member/list", List.class);
        mav.addObject("list","服务器出现故障,请稍后重试!");
        return mav;
    }
    @RequestMapping("discovery")
    public ModelAndView discovery(){
        return new ModelAndView("shop/discovery");
    }
    @RequestMapping("my")
    public ModelAndView my(){
        return new ModelAndView("shop/my");
    }
}
