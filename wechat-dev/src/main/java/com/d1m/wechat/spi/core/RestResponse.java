package com.d1m.wechat.spi.core;

import lombok.Getter;
import lombok.Setter;

/**
 * RestResponse
 *
 * @author f0rb on 2017-07-07.
 */
@Getter
@Setter
public class RestResponse<T> {
    private Integer code;
    private String info;
    private T data;

    public boolean success() {
        return code != null && code == 0;
    }
}
