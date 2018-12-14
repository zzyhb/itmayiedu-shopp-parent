package com.itmayiedu.itmayiedu_shopp_weixin.utils.wx;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class SignUtil {
	public static void main(String[] args) {
		String timestamp = "1506078200";
		String nonce_str =  "5b59914c5e39228da0902a41e3eb7aee";
		String jsapi_ticket = "HoagFKDcsGMVCIY2vOjf9m-4ne3Bb9TVxmD9IAMrlhZpk2fydB6FT7PiuxbiHS7fTDrd8JsZPbs0RH3PO59VHg";
		System.out.println(sign(nonce_str, timestamp, jsapi_ticket, "http://e7451e3c.ngrok.io/mim/index.do"));
	};

	public static String sign(String nonce_str,String timestamp,String jsapi_ticket, String url) {
		String string1;
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
				+ "&timestamp=" + timestamp + "&url=" + url;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return signature;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

}
