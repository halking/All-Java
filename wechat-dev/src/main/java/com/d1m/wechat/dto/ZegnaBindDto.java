package com.d1m.wechat.dto;

import org.apache.commons.lang.StringUtils;

public class ZegnaBindDto {

    private String mobile;

    private String name;

    private String level;

    private String birthday;

    private String country;

    private Byte sex;

    private String email;

    private Integer id;

    private String[] interests;

    private String[] styles;

    private String occupation;

    private Boolean isvip;

    private String alias;

    private String rise;

    /**fasle : 有货
     * true ： 缺货
     */
    private Boolean isStockout;

    private Boolean campaignStatus;

    private Object context;

    private String status;

    private String remark;

    private Byte bindStatus;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String[] getInterests() {
        return interests;
    }

    public void setInterests(String interestsStr) {
        this.interests = StringUtils.isNotBlank(interestsStr)?interestsStr.split(","):null;
    }

    public String[] getStyles() {
        return styles;
    }

    public void setStyles(String stylesStr) {
        this.styles = StringUtils.isNotBlank(stylesStr)? stylesStr.split(","):null;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Boolean getIsvip() {
        return isvip;
    }

    public void setIsvip(Boolean isvip) {
        this.isvip = isvip;
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

    public Boolean getStockout() {
        return isStockout;
    }

    public void setStockout(Boolean stockout) {
        isStockout = stockout;
    }

    public Boolean getCampaignStatus() {
        return campaignStatus;
    }

    public void setCampaignStatus(Boolean campaignStatus) {
        this.campaignStatus = campaignStatus;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Byte getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(Byte bindStatus) {
        this.bindStatus = bindStatus;
    }

    public ZegnaBindDto() {
        this.name = "";
        this.sex = 0;
        this.email = "";
        this.birthday = "";
        this.interests = new String[0];
        this.styles = new String[0];
    }
}
