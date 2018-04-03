package com.d1m.wechat.zegna;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.util.DateUtil;
import com.d1m.wechat.util.MD5;
import com.d1m.wechat.util.Rand;
import com.d1m.wechat.wxsdk.core.util.HttpRequestProxy;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by D1M on 2017/4/21.
 */
public class ZegnaSMSUtil {
    private static Logger log = LoggerFactory.getLogger(ZegnaSMSUtil.class);

    private static String url = "http://www.wemediacn.net/webservice/smsservice.asmx/SendSMS";
    private static String formatId = "8";
    private static String smstoken = "7103006130947241";
    private static String tmpl = "您正在进行手机验证，验证码：%s，请勿将验证码泄漏于他人。";

    /**
     * @param mobile
     *            手机号
     * @param verfiyCode
     *            验证码，6位字符
     * @return
     */
    public static boolean sendMsg(String mobile, String verfiyCode) {
        boolean ret = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobile", mobile);
        params.put("FormatID", formatId);
        params.put("Content", new String(String.format(tmpl, verfiyCode).getBytes(), Charset.forName("UTF-8")));
        params.put("ScheduleDate", "2010-1-1");
        params.put("TokenID", smstoken);

        String result = HttpRequestProxy.doPost(url, params, "UTF-8");
        return ret;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        ZegnaSMSUtil.sendMsg("15000849908", "123437");
    }

}
