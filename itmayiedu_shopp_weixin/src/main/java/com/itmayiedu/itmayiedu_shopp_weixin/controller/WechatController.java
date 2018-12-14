package com.itmayiedu.itmayiedu_shopp_weixin.controller;

import com.itmayiedu.itmayiedu_shopp_weixin.service.WeChatService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@RestController
@RequestMapping("${projectName}/wechat/portal")
public class WechatController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private WeChatService weChatService;
	@Autowired
	private WxMpService wxService;

	@Autowired
	private WxMpMessageRouter router;

	@GetMapping(value = "test.do",produces = "text/plain;charset=utf-8")
	public String authGet(@RequestParam(name = "signature", required = false) String signature,
			@RequestParam(name = "timestamp", required = false) String timestamp,
			@RequestParam(name = "nonce", required = false) String nonce,
			@RequestParam(name = "echostr", required = false) String echostr) {

		this.logger.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);

		if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
			throw new IllegalArgumentException("请求参数非法，请核实!");
		}

		/*if (this.wxService.checkSignature(timestamp, nonce, signature)) {
			List<WxConfig> config = weChatService.getWxConfig();
			if (config.size()>0){
				config.get(0).setNonce(nonce);
				config.get(0).setTimestamp(timestamp);
				config.get(0).setSignature(signature);
				weChatService.update(config.get(0));
			}else{
				WxConfig wxConfig = new WxConfig();
				wxConfig.setId(CommonUtil.getUUID());
				wxConfig.setNonce(nonce);
				wxConfig.setTimestamp(timestamp);
				wxConfig.setSignature(signature);
				weChatService.insert(wxConfig);
			}
			return echostr;
		}*/
		return echostr;
		//return "非法请求";
	}

	@PostMapping(value = "test.do",produces = "application/xml; charset=UTF-8")
	public String post(@RequestBody String requestBody, @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
		this.logger.debug(
				"\n接收微信请求：[signature=[{}], encType=[{}], msgSignature=[{}],"
						+ " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
				signature, encType, msgSignature, timestamp, nonce, requestBody);

		if (!this.wxService.checkSignature(timestamp, nonce, signature)) {
			throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
		}

		String out = null;
		if (encType == null) {
			out = weChatService.processRequest(requestBody);
			/*// 明文传输的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
			WxMpXmlOutMessage outMessage = this.route(inMessage);
			if (outMessage == null) {
				return "";
			}*/
		} else if ("aes".equals(encType)) {
			// aes加密的消息
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody,
					this.wxService.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
			this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
			WxMpXmlOutMessage outMessage = this.route(inMessage);
			if (outMessage == null) {
				return "";
			}

			out = outMessage.toEncryptedXml(this.wxService.getWxMpConfigStorage());
		}
		this.logger.debug("\n组装回复信息：{}", out);

		return out;
	}

	private WxMpXmlOutMessage route(WxMpXmlMessage message) {
		try {
			return this.router.route(message);
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
		}

		return null;
	}

	@GetMapping("/createMenu")
	public String menuCreateSample() throws WxErrorException {
		WxMenu menu = new WxMenu();
		WxMenuButton button2 = new WxMenuButton();
		button2.setType(WxConsts.BUTTON_VIEW);
		button2.setName("商城首页");
		//微信认证的回调函数
		button2.setUrl("http://yhbp.natapp1.cc/weixin/test/test2");

		WxMenuButton button1 = new WxMenuButton();
		button1.setType(WxConsts.BUTTON_VIEW);
		button1.setName("首页");
		//微信认证的回调函数
		button1.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6fe65e0d9c65ef3b&redirect_uri=http%3a%2f%2fyhbp.natapp1.cc%2fweixin%2ftest%2ftest1&response_type=code&scope=snsapi_userinfo#wechat_redirect");
		//button1.setUrl("http://yhbp.natapp1.cc/weixin/test/test1");

		WxMenuButton button3 = new WxMenuButton();
		button3.setName("菜单");

		menu.getButtons().add(button1);
        menu.getButtons().add(button2);
		menu.getButtons().add(button3);

		WxMenuButton button31 = new WxMenuButton();
		button31.setType(WxConsts.BUTTON_VIEW);
		button31.setName("搜索");
		button31.setUrl("http://www.soso.com/");

		WxMenuButton button32 = new WxMenuButton();
		button32.setType(WxConsts.BUTTON_VIEW);
		button32.setName("视频");
		button32.setUrl("http://v.qq.com/");

		WxMenuButton button33 = new WxMenuButton();
		button33.setType(WxConsts.BUTTON_CLICK);
		button33.setName("赞一下我们");
		button33.setKey("V1001_GOOD");

		button3.getSubButtons().add(button31);
		button3.getSubButtons().add(button32);
		button3.getSubButtons().add(button33);

		return wxService.post("https://api.weixin.qq.com/cgi-bin/menu/create",menu.toJson());
	}
	@RequestMapping("accessToken")
	public String getAccessToken() throws WxErrorException {
		//获取accessToken
		String response = weChatService.getMyAccessToken();
		System.out.println(response);
		return response;
	}
}
