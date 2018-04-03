package com.d1m.wechat.wxsdk.tag;

import java.util.List;

public class WxTagRequest {

    private List<String> openid_list;

    private Integer tagid;

    public List<String> getOpenid_list() {
        return openid_list;
    }

    public void setOpenid_list(List<String> openid_list) {
        this.openid_list = openid_list;
    }

    public Integer getTagid() {
        return tagid;
    }

    public void setTagid(Integer tagid) {
        this.tagid = tagid;
    }
}
