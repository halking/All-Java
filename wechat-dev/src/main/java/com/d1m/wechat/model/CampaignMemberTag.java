package com.d1m.wechat.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "campaign_member_tag")
public class CampaignMemberTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "wx_tag_id")
    private Integer wxTagId;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "member_id")
    private Integer memberId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWxTagId() {
        return wxTagId;
    }

    public void setWxTagId(Integer wxTagId) {
        this.wxTagId = wxTagId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
