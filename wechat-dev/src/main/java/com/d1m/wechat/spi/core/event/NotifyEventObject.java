package com.d1m.wechat.spi.core.event;

import com.d1m.wechat.spi.core.NotifyObject;

/**
 * ScanEventObject
 *
 * @author f0rb on 2017-07-07.
 */
public abstract class NotifyEventObject extends NotifyObject {

    public abstract String getEvent();

    public String getMsgType() {
        return "event";
    }
}
