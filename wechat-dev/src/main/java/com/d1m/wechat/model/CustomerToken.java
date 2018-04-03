package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "customer_token")
public class CustomerToken {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 客户资料ID
     */
    @Column(name = "customer_info_id")
    private Integer customerInfoId;

    /**
     * 微信appid
     */
    private String appid;

    /**
     * D1M内部的appkey
     */
    private String appkey;

    /**
     * D1M内部的访问token
     */
    private String token;

    /**
     * 微信id
     */
    @Column(name = "wechat_id")
    private Integer wechatId;

    /**
     * token的创建时间
     */
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * app_token的过期时间
     */
    @Column(name = "expired_at")
    private Date expiredAt;

    /**
     * 令牌状态(0:无效,1:有效,2过期)
     */
    private Byte status;

    @Transient
    private int expiresIn;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取客户资料ID
     *
     * @return customer_info_id - 客户资料ID
     */
    public Integer getCustomerInfoId() {
        return customerInfoId;
    }

    /**
     * 设置客户资料ID
     *
     * @param customerInfoId 客户资料ID
     */
    public void setCustomerInfoId(Integer customerInfoId) {
        this.customerInfoId = customerInfoId;
    }

    /**
     * 获取微信appid
     *
     * @return appid - 微信appid
     */
    public String getAppid() {
        return appid;
    }

    /**
     * 设置微信appid
     *
     * @param appid 微信appid
     */
    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    /**
     * 获取D1M内部的appkey
     *
     * @return appkey - D1M内部的appkey
     */
    public String getAppkey() {
        return appkey;
    }

    /**
     * 设置D1M内部的appkey
     *
     * @param appkey D1M内部的appkey
     */
    public void setAppkey(String appkey) {
        this.appkey = appkey == null ? null : appkey.trim();
    }

    /**
     * 获取D1M内部的访问token
     *
     * @return token - D1M内部的访问token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置D1M内部的访问token
     *
     * @param token D1M内部的访问token
     */
    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    /**
     * 获取微信id
     *
     * @return wechat_id - 微信id
     */
    public Integer getWechatId() {
        return wechatId;
    }

    /**
     * 设置微信id
     *
     * @param wechatId 微信id
     */
    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }

    /**
     * 获取token的创建时间
     *
     * @return created_at - token的创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置token的创建时间
     *
     * @param createdAt token的创建时间
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取app_token的过期时间
     *
     * @return expired_at - app_token的过期时间
     */
    public Date getExpiredAt() {
        return expiredAt;
    }

    /**
     * 设置app_token的过期时间
     *
     * @param expiredAt app_token的过期时间
     */
    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }

    /**
     * 获取令牌状态(0:无效,1:有效,2过期)
     *
     * @return status - 令牌状态(0:无效,1:有效,2过期)
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置令牌状态(0:无效,1:有效,2过期)
     *
     * @param status 令牌状态(0:无效,1:有效,2过期)
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}