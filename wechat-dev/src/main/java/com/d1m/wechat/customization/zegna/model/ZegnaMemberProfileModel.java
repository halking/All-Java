package com.d1m.wechat.customization.zegna.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZegnaMemberProfileModel {

    /**
     * 会员名
     */
    private String firstname;

    /**
     * 会员姓
     */
    private String lastname;

    /**
     * 邮箱
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 所属公众号ID
     */
    private Integer wechatId;

    /**
     * 绑定状态(0:已解绑,1:已绑定)
     */
    private Byte status;

    /**
     * 绑定时间
     */
    private Date bindAt;

    /**
     * 解绑时间
     */
    private Date unbindAt;

    private String openId;


}