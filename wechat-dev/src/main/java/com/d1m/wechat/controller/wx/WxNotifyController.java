package com.d1m.wechat.controller.wx;

import java.io.StringReader;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.d1m.wechat.component.CommonRepeatChecker;
import com.d1m.wechat.message.WxNotifyMessageDispatcher;
import com.d1m.wechat.model.Wechat;
import com.d1m.wechat.model.enums.WechatStatus;
import com.d1m.wechat.service.WechatService;
import com.d1m.wechat.util.ParamUtil;
import com.d1m.wechat.util.WeiXinUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/notify")
public class WxNotifyController {

    @Resource
    private WxNotifyMessageDispatcher wxNotifyMessageDispatcher;

    @Resource
    private WechatService wechatService;

    @Resource
    private CommonRepeatChecker commonRepeatChecker;

    private boolean checkSignature(String signature, String timestamp, String nonce, String token, String oid) {
        if (oid != null) {
            Wechat wechat = wechatService.getByOpenId(oid);
            if (wechat != null) {
                token = wechat.getToken();
            }
        }
        return WeiXinUtils.checkSignature(signature, timestamp, nonce, token);
    }

    /**
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @param oid       公众号id
     * @param token     明文token, 调试用
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String verify(String signature, String timestamp, String nonce, String echostr, String oid, String token) {

        log.info("oid={},signature={},timestamp={},nonce={},echostr={},token={}",
                 oid, signature, timestamp, nonce, echostr, token);
        boolean checkResult = checkSignature(signature, timestamp, nonce, token, oid);
        log.info("签名验证结果: {}", checkResult);
        return checkResult ? echostr : "";
    }

    /**
     * MsgType 消息类型
     * text:文本,image:图片,voice:语音,video:视频,shortvideo:小视频,location:地理位置
     * ,link:链接,event
	 *
     * Event 事件类型
     * subscribe:关注,unsubscribe:取消关注,SCAN:扫码(前提已关注),LOCATION:上报地理位置,CLICK
     * :点击菜单,VIEW:点击菜单跳转,poi_check_notify:门店审核
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String doNotify(HttpServletRequest request, /*@RequestBody String xml,*/
                           String signature, String timestamp, String nonce, String oid, String token,
                           String encrypt_type, String msg_signature
    ) {
        String query = request.getQueryString();
        log.info("[微信消息]请求参数: {}", query);
        //#8015 2、优化消息重复校验逻辑（可以根据nonce），需要性能更好。
        if (commonRepeatChecker.checkRepeat(query)) {
            log.warn("[微信消息]重复请求: {} ", query);
            return "";
        }
        //#8015 1、针对如上微信后台过来的请求记录，我们需要存储微信请求的参数，同时需要校验数据有效性(是否是微信发过来的，验证signature的正确性)。
        boolean checkResult = checkSignature(signature, timestamp, nonce, token, oid);
        if (!checkResult) {
            log.warn("[微信消息]校验失败: signature={}, timestamp={}, nonce={}, token={}, oid={}",
                     signature, timestamp, nonce, token, oid);
            return "false";
        }

        String msgType = null;
        String toUserName = null;
        String fromUserName = null;
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(request.getInputStream());
            Element root = document.getRootElement();
            toUserName = ParamUtil.getElementContent(root, "ToUserName");
            fromUserName = ParamUtil.getElementContent(root, "FromUserName");
            // 0.获取Wechat
            Wechat wechat = wechatService.getByOpenId(toUserName);

            if (wechat != null) {
                if (wechat.getStatus() == WechatStatus.INUSED.getValue()) {
                    if ("aes".equals(encrypt_type)) {
                        //#8015 4、需要支持『安全模式』，『兼容模式』，『明文模式』。
                        //解密报文, 并更新document和root
                        String encrypt = ParamUtil.getElementContent(root, "Encrypt");
                        String decryptXML = WeiXinUtils.decryptMsg(wechat.getEncodingAesKey(), encrypt, wechat.getAppid());

                        reader = new SAXReader();
                        document = reader.read(new StringReader(decryptXML));
                        root = document.getRootElement();
                    } else if (encrypt_type != null) {
                        log.warn("[微信消息]不支持的加密类型:{}", encrypt_type);
                    }

                    fromUserName = ParamUtil.getElementContent(root, "FromUserName");
                    msgType = ParamUtil.getElementContent(root, "MsgType");
                    //amqpTemplate.convertAndSend(document.asXML());
                    wxNotifyMessageDispatcher.send(document.asXML());

                } else {
                    // #8015 3、当oid对应的公众号后台状态为删除时，我们系统停止服务，同时打印日志做记录。
                    log.warn("[微信消息]已停用的公众号: {}, \n{} ", wechat.getId(), document.asXML());
                }
            } else {
                log.warn("[微信消息]未配置的公众号: {}, \n{} ", toUserName, document.asXML());
            }
        } catch (Exception e) {
            log.error("微信消息处理失败!", e);
        } finally {
            if (!StringUtils.equalsIgnoreCase(msgType, "event")) {
                // 5.同步回复通知，设置客服转发
                String transferCustomerService = buildRetXml(fromUserName, toUserName);
                log.info("Transfer to CustomerService: \n{}", transferCustomerService);
                return transferCustomerService;
            }

            log.info("notify success.");
            return "success";
        }
    }

    /**
     * 设置客服消息转发
     *
     * @param toUserName   接收方帐号（收到的OpenID）
     * @param fromUserName 开发者微信号
     * @return xml string
     */
    private String buildRetXml(String toUserName, String fromUserName) {
        return String.format(
                "<xml>\n" +
                "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                "<CreateTime>%d</CreateTime>\n" +
                "<MsgType><![CDATA[transfer_customer_service]]></MsgType>\n" +
                "</xml>",
                toUserName, fromUserName, System.currentTimeMillis() / 1000
        );
    }

}
