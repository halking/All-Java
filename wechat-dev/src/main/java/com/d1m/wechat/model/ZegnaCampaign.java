package com.d1m.wechat.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "zegna_campaign")
public class ZegnaCampaign {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 名称
     */

    @Column(name = "name")
    private String name;

    @Column(name = "remark")
    private String remark;

    @Column(name = "status")
    private String status;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "member_profile_id")
    private Integer memberProfileId;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getMemberProfileId() {
        return memberProfileId;
    }

    public void setMemberProfileId(Integer memberProfileId) {
        this.memberProfileId = memberProfileId;
    }
}
