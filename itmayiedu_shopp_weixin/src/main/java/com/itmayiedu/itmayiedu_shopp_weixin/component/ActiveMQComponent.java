package com.itmayiedu.itmayiedu_shopp_weixin.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018/12/10 09:31
 * @Description:
 */
@Component
public class ActiveMQComponent {
    @JmsListener(destination = "test")
    public void testActiveMQ(List list){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(list.get(i).toString()+",");
        }
        stringBuilder.substring(0,stringBuilder.length()-1);
        System.out.println("收到队列消息内容为:"+stringBuilder);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("队列消息测试成功!!!");
    }
}
