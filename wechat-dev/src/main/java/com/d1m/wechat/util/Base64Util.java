package com.d1m.wechat.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

/**
 * Created by d1m on 2016/9/21.
 */
public class Base64Util {
    // 加密
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
            s = s.replaceAll("[\\s*\t\n\r]", "");
        }
        return s;
    }

    // 解密
    public static String getFromBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] arg){
        System.out.println(Base64Util.getBase64("http://hola.wechat.d1mgroup.com/site/?from=singlemessage&isappinstalled=0#!/isBindCard"));
        System.out.println(Base64Util.getFromBase64("aHR0cDovL2hvbGEud2VjaGF0LmQxbWdyb3VwLmNvbS9zaXRlLz9mcm9tPXNpbmdsZW1lc3NhZ2UmaXNhcHBpbnN0YWxsZWQ9MCMhL2lzQmluZENhcmQ="));
    }
}
