package com.d1m.wechat.common;

import com.d1m.wechat.dto.WechatClickMessageDto;
import com.d1m.wechat.exception.CommonException;
import com.d1m.wechat.util.MD5;
import com.d1m.wechat.util.XmlUtil;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.TreeMap;

public class ExceptionCode {

    public static final  String PARAMA_IS_INVALID = "100001";
    public static final  String PAYMENT_SIGN_ERROR = "100002";
    public static final  String PARSE_XML_ERROR = "100003";
    public static final  String SEND_MESSAGE_FAIL = "100004";

    public static final  String MOBILE_BOUND_WECHAT = "100005";

    public static final  String BOUTIQUE_CODE_INVALID = "100006";

    public static final  String COMMON_ERROR_CODE = "999999";

    public static final  String MEMBER_INFO_INVALID = "100007";
}
