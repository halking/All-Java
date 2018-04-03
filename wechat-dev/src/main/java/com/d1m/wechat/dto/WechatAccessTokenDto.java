package com.d1m.wechat.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class WechatAccessTokenDto {
    private String accessToken;
    private String expireTime;
}
