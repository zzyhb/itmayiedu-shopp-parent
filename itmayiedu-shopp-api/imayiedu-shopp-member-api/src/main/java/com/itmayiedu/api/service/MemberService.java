package com.itmayiedu.api.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018/12/8 15:29
 * @Description:
 */
@RequestMapping("/member")
public interface MemberService {
    @RequestMapping("list")
    public List<String> list();

    @RequestMapping("isExist")
    public boolean isExist();
}
