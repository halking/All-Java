package com.d1m.wechat.spi.core.event;

/**
 * ScanEventObject
 *
 * @author f0rb on 2017-07-07.
 */
public class SubscribeEventObject extends NotifyEventObject {
    @Override
    public String getEvent() {
        return "subscribe";
    }
}
