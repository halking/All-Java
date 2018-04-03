package com.d1m.wechat.spi.core.event;

import lombok.Getter;
import lombok.Setter;

/**
 * LocationEventObject
 *
 * @author f0rb on 2017-07-07.
 */
@Getter
@Setter
public class LocationEventObject extends NotifyEventObject {
    private Double latitude;    //地理位置纬度
    private Double longitude;   //地理位置经度
    private Double precision;   //地理位置精度

    @Override
    public String getEvent() {
        return "location";
    }

}
