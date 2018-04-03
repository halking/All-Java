package com.d1m.wechat.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class WechatTextMessageDto {

    /**
     * Follower open id/public account open id
     */
    private String toUserName;

    /**
     * Follower open id/public account open id
     */
    private String fromUserName;

    /**
     * message type
     */
    private String msgType;

    /**
     * text message content
     */
    private String content;

    private Integer createTime;

    private String msgId;

    public String getToUserName() {
        return toUserName;
    }

    @XmlElement(name = "ToUserName")
    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    @XmlElement(name = "FromUserName")
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMsgType() {
        return msgType;
    }

    @XmlElement(name = "MsgType")
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    @XmlElement(name = "Content")
    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    @XmlElement(name = "CreateTime")
    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public String getMsgId() {
        return msgId;
    }

    @XmlElement(name = "MsgId")
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public static WechatTextMessageDto getInstance(WechatClickMessageDto messageDto){
        WechatTextMessageDto textMessageDto = new WechatTextMessageDto();
        textMessageDto.setContent("<客户点击客服菜单>");
        textMessageDto.setFromUserName(messageDto.getFromUserName());
        textMessageDto.setToUserName(messageDto.getToUserName());
        textMessageDto.setCreateTime(messageDto.getCreateTime());
        textMessageDto.setMsgId(null);
        textMessageDto.setMsgType("text");
        return textMessageDto;
    }
}
