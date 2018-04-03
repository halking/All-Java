package com.d1m.wechat.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "member_conversation_session")
public class MemberConversationSession {
    @Id
    private Integer id;

    /**
     * 会员openId
     */
    @Column(name = "member_open_id")
    private String memberOpenId;

    /**
     * communication account id
     */
    @Column(name = "account_id")
    private String accountId;

    /**
     * session 类型
     */
    private String type;

    /**
     * session 开始时间
     */
    @Column(name = "start_at")
    private Date startAt;

    /**
     * session 结束时间
     */
    @Column(name = "end_at")
    private Date endAt;

    @Column(name = "is_closed")
    private Boolean isClosed;

    @Column(name = "wechat_id")
    private Integer wechatId;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取会员openId
     *
     * @return member_open_id - 会员openId
     */
    public String getMemberOpenId() {
        return memberOpenId;
    }

    /**
     * 设置会员openId
     *
     * @param memberOpenId 会员openId
     */
    public void setMemberOpenId(String memberOpenId) {
        this.memberOpenId = memberOpenId == null ? null : memberOpenId.trim();
    }

    /**
     * 获取communication account id
     *
     * @return account_id - communication account id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置communication account id
     *
     * @param accountId communication account id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    /**
     * 获取session 类型
     *
     * @return type - session 类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置session 类型
     *
     * @param type session 类型
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * 获取session 开始时间
     *
     * @return start_at - session 开始时间
     */
    public Date getStartAt() {
        return startAt;
    }

    /**
     * 设置session 开始时间
     *
     * @param startAt session 开始时间
     */
    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    /**
     * @return is_closed
     */
    public Boolean getIsClosed() {
        return isClosed;
    }

    /**
     * @param isClosed
     */
    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    /**
     * @return wechat_id
     */
    public Integer getWechatId() {
        return wechatId;
    }

    /**
     * @param wechatId
     */
    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }
}