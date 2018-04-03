package com.d1m.wechat.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Lisa on 2017/11/7.
 */
@Getter
@Setter
@Data
public class UserProfileDto {

    private Integer subscribe;

    private String unionId;

    private String openId;

    private String nickname;

    private Byte sex;

    private String language;

    private String city;

    private String province;

    private String country;

    private String headImgUrl;

    private Integer subscribeTime;

    private String remark;
}
