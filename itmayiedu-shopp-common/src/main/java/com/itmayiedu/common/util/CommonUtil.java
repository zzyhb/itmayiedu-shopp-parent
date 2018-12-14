package com.itmayiedu.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @Auther: Administrator
 * @Date: 2018/7/20 16:01
 * @Description:
 */
public class CommonUtil {
    public static final String CODEST = "QWERTYUIOPASDFGHJKLMNBVCXZ0123456789";
    public static Random random = new Random();

    public static boolean isNotNull(String text) {
        return text != null && !"".equals(text);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getTicketCode() {
        String uid = UUID.randomUUID().toString().replace("-", "");
        char[] charArray = uid.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(uid.length());
            stringBuilder.append(charArray[index]);
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(getTicketCode());
    }

    public static String getDateFormatStr(String format) {
        return getDateFormatStr(new Date(),format);
    }
    public static String getDateFormatStr(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }


    public static String getSmsCode() {
        String code = "";
        for (int i = 0; i < 6; i++) {
            char ch = CODEST.toLowerCase().charAt(random.nextInt(CODEST.length()));
            code = code + ch;
        }
        return code;
    }

    public static boolean isNotNull(String[] array) {
        return array != null && array.length > 0;
    }

    public static boolean isNotNull(List<?> list) {
        return list != null && list.size() > 0;
    }

    public static String getInviteCode() {
        String code = "";
        for (int i = 0; i < 6; i++) {
            char ch = CODEST.charAt(random.nextInt(CODEST.length()));
            code = code + ch;
        }
        return code;
    }
}
