package com.d1m.wechat.spi.core.message;

import lombok.Getter;
import lombok.Setter;

import com.d1m.wechat.spi.core.NotifyObject;

/**
 * TextMessage
 *
 * @author f0rb on 2017-07-18.
 */
@Getter
@Setter
public class TextMessage extends NotifyObject {
    private String content; //文本消息内容
    private String msgId;   //消息id，64位整型
    @Override
    public String getMsgType() {
        return "text";
    }
}
