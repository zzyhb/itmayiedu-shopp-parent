package com.itmayiedu.itmayiedu_shopp_weixin.service.impl;

import com.itmayiedu.itmayiedu_shopp_weixin.service.WeChatService;
import com.itmayiedu.itmayiedu_shopp_weixin.utils.wx.ArticleItem;
import com.itmayiedu.itmayiedu_shopp_weixin.utils.wx.WeChatContant;
import com.itmayiedu.itmayiedu_shopp_weixin.utils.wx.WeChatUtil;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Administrator
 * @Date: 2018/9/19 09:44
 * @Description:
 */
@Service
public class WeChatServiceImpl implements WeChatService {
    @Autowired
    private WxMpService wxService;
    private static final Logger logger = LoggerFactory.getLogger(WeChatServiceImpl.class);
    public String processRequest(String requestBody) {
        // xml格式的消息数据
        String respXml = null;
        // 默认返回的文本消息内容
        String respContent;
        try {
            // 调用parseXml方法解析请求消息
            Map<String, String> requestMap = WeChatUtil.parseXml(requestBody);
            // 消息类型
            String msgType = (String) requestMap.get(WeChatContant.MsgType);
            String mes = null;
            // 文本消息
            if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_TEXT)) {
                mes = requestMap.get(WeChatContant.Content).toString();
                if (mes != null && mes.length() < 2) {
                    List<ArticleItem> items = new ArrayList<>();
                    ArticleItem item = new ArticleItem();
                    item.setTitle("照片墙");
                    item.setDescription("阿狸照片墙");
                    item.setPicUrl("http://img4.imgtn.bdimg.com/it/u=2032439888,2365641606&fm=27&gp=0.jpg");
                    item.setUrl("http://www.baidu.com");
                    items.add(item);

                    item = new ArticleItem();
                    item.setTitle("照片墙");
                    item.setDescription("疾风亚索");
                    item.setPicUrl("http://img4.imgtn.bdimg.com/it/u=1466996379,1814685629&fm=214&gp=0.jpg");
                    item.setUrl("http://www.baidu.com");
                    items.add(item);

                    item = new ArticleItem();
                    item.setTitle("照片墙");
                    item.setDescription("无极剑圣");
                    item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1537361220201&di=2c5229c04581790d4caf2bf57fbb6cfb&imgtype=0&src=http%3A%2F%2Fi1.17173cdn.com%2F2fhnvk%2FYWxqaGBf%2Fcms3%2FmLtWwvbkhCdFgil.jpg");
                    item.setUrl("http://www.baidu.com");
                    items.add(item);

                    item = new ArticleItem();
                    item.setTitle("百度");
                    item.setDescription("百度一下");
                    item.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505100912368&di=69c2ba796aa2afd9a4608e213bf695fb&imgtype=0&src=http%3A%2F%2Ftx.haiqq.com%2Fuploads%2Fallimg%2F170510%2F0634355517-9.jpg");
                    item.setUrl("http://www.baidu.com");
                    items.add(item);
                    respXml = WeChatUtil.sendArticleMsg(requestMap, items);
                }
            }
            // 图片消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_IMAGE)) {
                respContent = "您发送的是图片消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 语音消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_VOICE)) {
                respContent = "您发送的是语音消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 视频消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_VIDEO)) {
                respContent = "您发送的是视频消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 地理位置消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_LOCATION)) {
                respContent = "您发送的是地理位置消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 链接消息
            else if (msgType.equals(WeChatContant.REQ_MESSAGE_TYPE_LINK)) {
                respContent = "您发送的是链接消息！";
                respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
            }
            // 事件推送
            else if (msgType.equalsIgnoreCase(WeChatContant.REQ_MESSAGE_TYPE_EVENT.toString())) {
                // 事件类型
                String eventType = (String) requestMap.get(WeChatContant.Event);
                // 关注
                if (eventType.equalsIgnoreCase(WeChatContant.EVENT_TYPE_SUBSCRIBE.toString())) {
                    respContent = "谢谢您的关注！";
                    respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
                }
                // 取消关注
                else if (eventType.equalsIgnoreCase(WeChatContant.EVENT_TYPE_UNSUBSCRIBE.toString())) {
                    // TODO 取消订阅后用户不会再收到公众账号发送的消息，因此不需要回复
                    String fromUserName = requestMap.get("FromUserName");
                    logger.info(fromUserName+"取消关注了,打死他");
                }
                // 扫描带参数二维码
                else if (eventType.equalsIgnoreCase(WeChatContant.EVENT_TYPE_SCAN.toString())) {
                    // TODO 处理扫描带参数二维码事件
                    if (requestMap.get("EventKey").equals("123")){
                        respContent = "你扫描的产品为华为mate9,价格$1000";
                        respXml = WeChatUtil.sendTextMsg(requestMap, respContent);
                    }
                }
                // 上报地理位置
                else if (eventType.equalsIgnoreCase(WeChatContant.EVENT_TYPE_LOCATION.toString())) {
                    // TODO 处理上报地理位置事件
                }
                // 自定义菜单
                else if (eventType.equalsIgnoreCase(WeChatContant.EVENT_TYPE_CLICK.toString())) {
                    String eventKey = requestMap.get("EventKey");
                    if (eventKey.equals("11")){
                        List<ArticleItem> items = new ArrayList<>();
                        ArticleItem item = new ArticleItem();
                        item.setTitle("今日精选歌曲");
                        item.setDescription("忘情水");
                        item.setPicUrl("http://47.99.84.178/images/20181120140930.png");
                        item.setUrl("http://47.99.84.178/images/file/Hailey Rowe-My Boyfriend Is Gay.mp3");
                        items.add(item);
                        respXml = WeChatUtil.sendArticleMsg(requestMap, items);
                    }
                }
            }
            mes = mes == null ? "不知道你在干嘛" : mes;
            if (respXml == null)
                respXml = WeChatUtil.sendTextMsg(requestMap, mes);
            //System.out.println(respXml);打印返回的内容
            return respXml;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /*@Override
    public List<WxConfig> getWxConfig() {
        return wxConfigMapper.selectByExample(new WxConfigExample());
    }

    @Override
    public void update(WxConfig config) {
        wxConfigMapper.updateByPrimaryKey(config);
    }

    @Override
    public void insert(WxConfig wxConfig) {
        wxConfigMapper.insert(wxConfig);
    }*/

    @Override
    public String getMyAccessToken() {
        try {
            return wxService.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+ WeChatContant.appID+"&secret="+WeChatContant.appsecret,null);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return null;
    }
}
