package com.itmayiedu.api.service.impl;

import com.itmayiedu.api.service.MemberService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018/12/8 15:33
 * @Description:
 */
@RestController
public class MemberServiceImpl implements MemberService{
    @Override
    public List<String> list() throws RuntimeException{
        ArrayList<String> strings = new ArrayList<>();
        strings.add("hello");
        strings.add("world");
        strings.add("!");
        //写入消息队列异步执行
        if (true)
        throw new RuntimeException("测试使用");
        return strings;
    }

    @Override
    public boolean isExist() {
        return true;
    }
}
