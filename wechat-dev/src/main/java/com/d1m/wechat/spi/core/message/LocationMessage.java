package com.d1m.wechat.spi.core.message;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.d1m.wechat.spi.core.NotifyObject;

/**
 * ScanEventObject
 *
 * @author f0rb on 2017-07-07.
 */
@Getter
@Setter
@Slf4j
public class LocationMessage extends NotifyObject {

    private Double locationX;   // 地理位置维度
    private Double locationY;   // 地理位置经度
    private Integer scale;      // 地图缩放大小
    private String label;       // 地理位置信息
    private String msgId;

    public String getMsgType() {
        return "location";
    }
}
