package com.itmayiedu.itmayiedu_shopp_weixin.service;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018/9/19 09:44
 * @Description:
 */
public interface WeChatService {
    String processRequest(String requestBody);

    /*List<WxConfig> getWxConfig();

    void update(WxConfig config);

    void insert(WxConfig wxConfig);*/

    String getMyAccessToken();
}
