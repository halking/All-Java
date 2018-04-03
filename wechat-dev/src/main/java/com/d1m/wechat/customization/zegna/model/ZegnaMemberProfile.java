package com.d1m.wechat.customization.zegna.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "zegna_member_profile")
public class ZegnaMemberProfile {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 会员名
     */
    private String firstname;

    /**
     * 会员姓
     */
    private String lastname;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 会员ID
     */
    @Column(name = "member_id")
    private Integer memberId;

    /**
     * 所属公众号ID
     */
    @Column(name = "wechat_id")
    private Integer wechatId;

    /**
     * 绑定状态(0:已解绑,1:已绑定)
     */
    private Byte status;

    /**
     * 绑定时间
     */
    @Column(name = "bind_at")
    private Date bindAt;

    /**
     * 解绑时间
     */
    @Column(name = "unbind_at")
    private Date unbindAt;

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
     * 获取会员名
     *
     * @return firstname - 会员名
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * 设置会员名
     *
     * @param firstname 会员名
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname == null ? null : firstname.trim();
    }

    /**
     * 获取会员姓
     *
     * @return lastname - 会员姓
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * 设置会员姓
     *
     * @param lastname 会员姓
     */
    public void setLastname(String lastname) {
        this.lastname = lastname == null ? null : lastname.trim();
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取会员ID
     *
     * @return member_id - 会员ID
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * 设置会员ID
     *
     * @param memberId 会员ID
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取所属公众号ID
     *
     * @return wechat_id - 所属公众号ID
     */
    public Integer getWechatId() {
        return wechatId;
    }

    /**
     * 设置所属公众号ID
     *
     * @param wechatId 所属公众号ID
     */
    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }

    /**
     * 获取绑定状态(0:已解绑,1:已绑定)
     *
     * @return status - 绑定状态(0:已解绑,1:已绑定)
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置绑定状态(0:已解绑,1:已绑定)
     *
     * @param status 绑定状态(0:已解绑,1:已绑定)
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取绑定时间
     *
     * @return bind_at - 绑定时间
     */
    public Date getBindAt() {
        return bindAt;
    }

    /**
     * 设置绑定时间
     *
     * @param bindAt 绑定时间
     */
    public void setBindAt(Date bindAt) {
        this.bindAt = bindAt;
    }

    /**
     * 获取解绑时间
     *
     * @return unbind_at - 解绑时间
     */
    public Date getUnbindAt() {
        return unbindAt;
    }

    /**
     * 设置解绑时间
     *
     * @param unbindAt 解绑时间
     */
    public void setUnbindAt(Date unbindAt) {
        this.unbindAt = unbindAt;
    }
}