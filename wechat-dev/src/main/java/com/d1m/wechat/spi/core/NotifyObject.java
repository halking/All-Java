package com.d1m.wechat.spi.core;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * ScanEventObject
 *
 * @author f0rb on 2017-07-07.
 */
@Getter
@Setter
@Slf4j
public abstract class NotifyObject {
    private String toUserName;      // 开发者微信号
    private String fromUserName;    // 发送方帐号（一个OpenID）
    private String createTime;      // 消息创建时间 （整型）

    public abstract String getMsgType();

    //public static NotifyObject parseXml(String xmlMsg) {
    //    try {
    //        SAXReader reader = new SAXReader();
    //        Document document = reader.read(IOUtils.toInputStream(xmlMsg, "UTF-8"));
    //        Element root = document.getRootElement();
    //        String toUserName = ParamUtil.getElementContent(root, "ToUserName");
    //        String fromUserName = ParamUtil.getElementContent(root, "FromUserName");
    //        String event = ParamUtil.getElementContent(root, "Event");
    //        String createTime = ParamUtil.getElementContent(root, "CreateTime");
    //        final String msgType = ParamUtil.getElementContent(root, "MsgType");
    //
    //        NotifyObject notifyObject = new NotifyObject() {
    //            @Override
    //            public String getMsgType() {
    //                return msgType;
    //            }
    //        };
    //        //if ("SCAN".equals(event) || ("subscribe".equals(event) && root.element("Ticket") != null)) {
    //        //    ScanEventObject scanEventObject = new ScanEventObject();
    //        //    scanEventObject.setEventKey(ParamUtil.getElementContent(root, "EventKey"));
    //        //    scanEventObject.setTicket(ParamUtil.getElementContent(root, "Ticket"));
    //        //    notifyObject = scanEventObject;
    //        //}
    //
    //        notifyObject.setToUserName(toUserName);
    //        notifyObject.setFromUserName(fromUserName);
    //        notifyObject.setCreateTime(createTime);
    //
    //        return notifyObject;
    //    } catch (Exception e) {
    //        log.error("Resolve Notify xml failed!", e);
    //    }
    //    return null;
    //}

}
