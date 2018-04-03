package com.d1m.wechat.spi.core;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.d1m.wechat.spi.core.event.*;
import com.d1m.wechat.spi.core.message.TextMessage;
import com.d1m.wechat.util.ParamUtil;

/**
 * ScanEventObject
 *
 * @author f0rb on 2017-07-07.
 */
@Getter
@Setter
@Slf4j
public abstract class NotifyObjectParser {

    public static NotifyObject parseXml(String xmlMsg) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(IOUtils.toInputStream(xmlMsg, "UTF-8"));
            Element root = document.getRootElement();
            String toUserName = ParamUtil.getElementContent(root, "ToUserName");
            String fromUserName = ParamUtil.getElementContent(root, "FromUserName");
            String event = ParamUtil.getElementContent(root, "Event");
            String createTime = ParamUtil.getElementContent(root, "CreateTime");
            final String msgType = ParamUtil.getElementContent(root, "MsgType");

            NotifyObject notifyObject;
            if ("event".equals(msgType)) {
                if ("SCAN".equals(event)) {
                    ScanEventObject scanEventObject = new ScanEventObject();
                    scanEventObject.setEventKey(ParamUtil.getElementContent(root, "EventKey"));
                    scanEventObject.setTicket(ParamUtil.getElementContent(root, "Ticket"));
                    notifyObject = scanEventObject;
                } else if ("subscribe".equals(event)) {
                    if (root.element("Ticket") != null) {
                        ScanSubEventObject scanSubEventObject = new ScanSubEventObject();
                        scanSubEventObject.setEventKey(ParamUtil.getElementContent(root, "EventKey"));
                        scanSubEventObject.setTicket(ParamUtil.getElementContent(root, "Ticket"));
                        notifyObject = scanSubEventObject;
                    } else {
                        notifyObject = new SubscribeEventObject();
                    }
                } else if ("unsubscribe".equals(event)) {
                    notifyObject = new UnSubscribeEventObject();
                } else if ("CLICK".equals(event)) {
                    MenuClickEventObject menuClickEventObject = new MenuClickEventObject();
                    menuClickEventObject.setEventKey(ParamUtil.getElementContent(root, "EventKey"));
                    notifyObject = menuClickEventObject;
                } else if ("VIEW".equals(event)) {
                    MenuViewEventObject menuViewEventObject = new MenuViewEventObject();
                    menuViewEventObject.setEventKey(ParamUtil.getElementContent(root, "EventKey"));
                    notifyObject = menuViewEventObject;
                } else if ("LOCATION".equals(event)) {
                    LocationEventObject locationEventObject = new LocationEventObject();
                    locationEventObject.setLatitude(ParamUtil.getDouble(ParamUtil.getElementContent(root, "Latitude"), 0d));
                    locationEventObject.setLongitude(ParamUtil.getDouble(ParamUtil.getElementContent(root, "Longitude"), 0d));
                    locationEventObject.setPrecision(ParamUtil.getDouble(ParamUtil.getElementContent(root, "Precision"), 0d));
                    notifyObject = locationEventObject;
                } else {
                    log.warn("UnSupported Notify Event: {}", event);
                    return null;
                }
            } else if ("text".equals(msgType)) {
                TextMessage textMessage = new TextMessage();
                textMessage.setMsgId(ParamUtil.getElementContent(root, "MsgId"));
                textMessage.setContent(ParamUtil.getElementContent(root, "Content"));
                notifyObject = textMessage;
            } else {
                log.warn("UnSupported MsgType: {}", msgType);
                return null;
            }
            notifyObject.setToUserName(toUserName);
            notifyObject.setFromUserName(fromUserName);
            notifyObject.setCreateTime(createTime);

            return notifyObject;
        } catch (Exception e) {
            log.error("Resolve Notify xml failed!", e);
        }
        return null;
    }

}