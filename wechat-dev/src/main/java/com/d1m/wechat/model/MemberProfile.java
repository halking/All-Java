package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "member_profile")
public class MemberProfile {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 会员卡号
     */
    @Column(name = "card_id")
    private String cardId;

    /**
     * 会员卡类型
     */
    @Column(name = "card_type")
    private Byte cardType;

    /**
     * 到期时间
     */
    @Column(name = "issue_date")
    private Date issueDate;

    /**
     * 会员卡状态
     */
    @Column(name = "card_status")
    private Byte cardStatus;

    /**
     * 门店ID
     */
    @Column(name = "store_id")
    private String storeId;

    /**
     * 积分
     */
    private Integer credits;

    /**
     * 等级
     */
    private String level;

    /**
     * 会员姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 会员地址
     */
    private String address;

    /**
     * 会员生日
     */
    @Column(name = "birth_date")
    private Date birthDate;

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
     * 接受促销信息
     */
    @Column(name = "accept_promotion")
    private Boolean acceptPromotion;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 绑定状态(0:已解绑,1:已绑定, 2:手机号填，兴趣爱好没填)
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
    @Column(name = "unbund_at")
    private Date unbundAt;

    /**
     * 姓
     */
    private String xing;

    /**
     * 名
     */
    private String ming;

    /**
     * 用户来源ID
     */
    @Column(name = "sourceId")
    private Integer sourceid;

    private String interests;

    private  String styles;

    private Boolean isVip;

    /**
     * 0:先生  | 1：女士
     */

    private Byte sex;

    private String occupation;

    private String alias;

    private String rise;

    private String country;

    private String source;

    private String keyword;
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
     * 获取会员卡号
     *
     * @return card_id - 会员卡号
     */
    public String getCardId() {
        return cardId;
    }

    /**
     * 设置会员卡号
     *
     * @param cardId 会员卡号
     */
    public void setCardId(String cardId) {
        this.cardId = cardId == null ? null : cardId.trim();
    }

    /**
     * 获取会员卡类型
     *
     * @return card_type - 会员卡类型
     */
    public Byte getCardType() {
        return cardType;
    }

    /**
     * 设置会员卡类型
     *
     * @param cardType 会员卡类型
     */
    public void setCardType(Byte cardType) {
        this.cardType = cardType;
    }

    /**
     * 获取到期时间
     *
     * @return issue_date - 到期时间
     */
    public Date getIssueDate() {
        return issueDate;
    }

    /**
     * 设置到期时间
     *
     * @param issueDate 到期时间
     */
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * 获取会员卡状态
     *
     * @return card_status - 会员卡状态
     */
    public Byte getCardStatus() {
        return cardStatus;
    }

    /**
     * 设置会员卡状态
     *
     * @param cardStatus 会员卡状态
     */
    public void setCardStatus(Byte cardStatus) {
        this.cardStatus = cardStatus;
    }

    /**
     * 获取门店ID
     *
     * @return store_id - 门店ID
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * 设置门店ID
     *
     * @param storeId 门店ID
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId == null ? null : storeId.trim();
    }

    /**
     * 获取积分
     *
     * @return credits - 积分
     */
    public Integer getCredits() {
        return credits;
    }

    /**
     * 设置积分
     *
     * @param credits 积分
     */
    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    /**
     * 获取等级
     *
     * @return level - 等级
     */
    public String getLevel() {
        return level;
    }

    /**
     * 设置等级
     *
     * @param level 等级
     */
    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    /**
     * 获取会员姓名
     *
     * @return name - 会员姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置会员姓名
     *
     * @param name 会员姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取会员地址
     *
     * @return address - 会员地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置会员地址
     *
     * @param address 会员地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取会员生日
     *
     * @return birth_date - 会员生日
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * 设置会员生日
     *
     * @param birthDate 会员生日
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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
     * 获取接受促销信息
     *
     * @return accept_promotion - 接受促销信息
     */
    public Boolean getAcceptPromotion() {
        return acceptPromotion;
    }

    /**
     * 设置接受促销信息
     *
     * @param acceptPromotion 接受促销信息
     */
    public void setAcceptPromotion(Boolean acceptPromotion) {
        this.acceptPromotion = acceptPromotion;
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
     * @return unbund_at - 解绑时间
     */
    public Date getUnbundAt() {
        return unbundAt;
    }

    /**
     * 设置解绑时间
     *
     * @param unbundAt 解绑时间
     */
    public void setUnbundAt(Date unbundAt) {
        this.unbundAt = unbundAt;
    }

    /**
     * 获取姓
     *
     * @return xing - 姓
     */
    public String getXing() {
        return xing;
    }

    /**
     * 设置姓
     *
     * @param xing 姓
     */
    public void setXing(String xing) {
        this.xing = xing == null ? null : xing.trim();
    }

    /**
     * 获取名
     *
     * @return ming - 名
     */
    public String getMing() {
        return ming;
    }

    /**
     * 设置名
     *
     * @param ming 名
     */
    public void setMing(String ming) {
        this.ming = ming == null ? null : ming.trim();
    }

    /**
     * 获取用户来源ID
     *
     * @return sourceId - 用户来源ID
     */
    public Integer getSourceid() {
        return sourceid;
    }

    /**
     * 设置用户来源ID
     *
     * @param sourceid 用户来源ID
     */
    public void setSourceid(Integer sourceid) {
        this.sourceid = sourceid;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getStyles() {
        return styles;
    }

    public void setStyles(String styles) {
        this.styles = styles;
    }

    public Boolean getVip() {
        return isVip;
    }

    public void setVip(Boolean vip) {
        isVip = vip;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}