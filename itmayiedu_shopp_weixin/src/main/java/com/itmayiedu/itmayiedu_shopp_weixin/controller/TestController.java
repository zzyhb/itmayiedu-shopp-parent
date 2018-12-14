package com.itmayiedu.itmayiedu_shopp_weixin.controller;

import com.itmayiedu.common.util.FtpUtil;
import com.itmayiedu.common.util.JsonMessage;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2018/12/8 15:01
 * @Description:
 */
@RestController
@PropertySource("classpath:/ip.properties")
@RequestMapping("${projectName}/test")
public class TestController {
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private RestTemplate restTemplate;

    private static String ip;
    @Value("${ip}")
    public void setIp(String ip) {
        TestController.ip = ip;
    }
    /**
     * 1.使用code换取openID
     * 2.使用openid获取用户信息
     * @return
     */
    @RequestMapping("test1")
    public ModelAndView test1(String code){
        ModelAndView modelAndView = new ModelAndView();
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        try {
            //判断用户未授权则跳转至授权页面
            if (null == code){
                modelAndView = new ModelAndView(new RedirectView("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6fe65e0d9c65ef3b&redirect_uri=http%3a%2f%2fyhbp.natapp1.cc%2fweixin%2ftest%2ftest1&response_type=code&scope=snsapi_userinfo#wechat_redirect"));
                return modelAndView;
            }
            //根据回传的code换取authAccessToken
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            //验证access_token
            boolean b = wxMpService.oauth2validateAccessToken(wxMpOAuth2AccessToken);
            if (!b){//如果access_token过期  则从新获取
                wxMpOAuth2AccessToken = wxMpService.oauth2refreshAccessToken(wxMpOAuth2AccessToken.getRefreshToken());
            }
            //从获取的authAccessToken的里的openid获取用户信息
            WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            if (wxMpUser!=null){
                modelAndView.setViewName("index");
                modelAndView.addObject("openid",wxMpUser.getOpenId());
                modelAndView.addObject("wxMpUser",wxMpUser);
            }
            /*//js-sdk签名
            String jsapiTicket = wxMpService.getJsapiTicket();
            String noncestr = UUID.randomUUID().toString();
            String timestamp = Long.toString(System.currentTimeMillis()/1000);
            String url = "http://yhbp.natapp1.cc/weixin/test/test1?code="+code+"&state=";
            String string = "jsapi_ticket="+jsapiTicket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
            String sign = SHA1.encode(string);

            //查询签名时的noncestr，timestamp
            modelAndView.addObject("noncestr",noncestr);
            modelAndView.addObject("timestamp",timestamp);
            modelAndView.addObject("signature",sign);*/
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    /**
     * 1.使用code换取openID
     * 2.使用openid获取用户信息
     * 跳转至商城首页
     * @return
     */
    @RequestMapping("test2")
    public ModelAndView test2(){
        ModelAndView mav = new ModelAndView("shop");
        return mav;
    }

    /**
     * 上传文件
     */
    @ResponseBody
    @RequestMapping("/uploadFile")
    public JsonMessage uploadFile(@RequestParam("lifeServeInfofile") MultipartFile[] uploadFile, HttpServletRequest
            request, HttpServletResponse response) throws IOException {
        JsonMessage jsonMessage = new JsonMessage();
        String s = "";
        int result = 0;
        String name = "";
        for (int i = 0; i < uploadFile.length; i++) {
            name = uploadFile[i].getOriginalFilename();
            /*String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);//获取上传的文件类型
            name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "." + suffix;*/
            try {
                FileInputStream in = (FileInputStream) uploadFile[i].getInputStream();
                boolean flag = FtpUtil.uploadFile(ip, 21, "yhb", "Yhbjia521!", "/home/yhb/images/file", "",
                        name, in);
                if (flag == true) {
                    jsonMessage.setSuccess(true);
                    jsonMessage.setMessage("上传成功");
                    jsonMessage.setObject("http://"+ip+"/images/file/"+name);
                } else {
                    jsonMessage.setSuccess(true);
                    jsonMessage.setMessage("上传失败");
                }
            } catch (IOException e) {
                jsonMessage.setSuccess(true);
                jsonMessage.setMessage("上传异常");
                e.printStackTrace();
            }
        }
        return jsonMessage;
    }
}
