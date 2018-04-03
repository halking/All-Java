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
public class ScanSubEventObject extends NotifyEventObject {
    private String eventKey;        // 事件KEY值，qrscene_为前缀，后面为二维码的参数值
    private String ticket;          // 二维码的ticket，可用来换取二维码图片

    @Override
    public String getEvent() {
        return "scansubscribe";
    }
}
