package com.d1m.wechat.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
public class WechatClickMessageDto {

    /** Follower open id/public account open id */
    private String toUserName;

    /** Follower open id/public account open id */
    private String fromUserName;

    /** message type */
    private String msgType;

    /** text message content */
    private String content;

    private Integer createTime;

    private String event;

    private String eventKey;

    public String getToUserName() {
        return toUserName;
    }
    @XmlElement(name="ToUserName")
    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    @XmlElement(name="FromUserName")
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMsgType() {
        return msgType;
    }

    @XmlElement(name="MsgType")
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    @XmlElement(name="Content")
    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    @XmlElement(name="CreateTime")
    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getEvent() {
        return event;
    }

    @XmlElement(name="Event")
    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    @XmlElement(name="EventKey")
    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
