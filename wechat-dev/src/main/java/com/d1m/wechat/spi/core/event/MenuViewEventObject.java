package com.d1m.wechat.spi.core.event;

import lombok.Getter;
import lombok.Setter;

/**
 * ScanEventObject
 *
 * @author f0rb on 2017-07-07.
 */
@Getter
@Setter
public class MenuViewEventObject extends NotifyEventObject {
    private String eventKey;        // 事件KEY值，与自定义菜单接口中KEY值对应

    @Override
    public String getEvent() {
        return "VIEW";
    }
}
