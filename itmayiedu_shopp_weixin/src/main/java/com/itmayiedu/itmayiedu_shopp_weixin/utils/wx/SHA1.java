package com.itmayiedu.itmayiedu_shopp_weixin.utils.wx;

import java.security.MessageDigest;

/**
 * @Auther: Administrator
 * @Date: 2018/9/18 17:17
 * @Description:
 */
public class SHA1 {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String encode = SHA1.encode("jsapi_ticket=HoagFKDcsGMVCIY2vOjf9hhcnuSlfYgG9pxko-Un_OBqj-BJoMCnEHZxKxG21lQP4tpHDYqJyVmUkZBoWjajyg&noncestr=1176c92b-1a8b-4cdd-a18a-be0c7e589fde&timestamp=1543976261&url=http://yhbp.natapp1.cc/weixin/test/test1");
        System.out.println(encode);
    }
}
