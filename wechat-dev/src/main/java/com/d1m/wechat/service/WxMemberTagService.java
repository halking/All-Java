package com.d1m.wechat.service;

/**
 * Created by Lisa on 2018/1/23.
 */
public interface WxMemberTagService {
    void syncMemberTagFromWechat(Integer wechatId, Integer tagId);
}
