package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "customer_info")
public class CustomerInfo {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 微信appid
     */
    private String appid;

    /**
     * D1M内部的appkey
     */
    private String appkey;

    /**
     * 微信id
     */
    @Column(name = "wechat_id")
    private Integer wechatId;

    /**
     * 客户的创建时间
     */
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * 客户状态(0:无效,1:有效)
     */
    private Byte status;

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
     * 获取客户的创建时间
     *
     * @return created_at - 客户的创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置客户的创建时间
     *
     * @param createdAt 客户的创建时间
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取客户状态(0:无效,1:有效)
     *
     * @return status - 客户状态(0:无效,1:有效)
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置客户状态(0:无效,1:有效)
     *
     * @param status 客户状态(0:无效,1:有效)
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}