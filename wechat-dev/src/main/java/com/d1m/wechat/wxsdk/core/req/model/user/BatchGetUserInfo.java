package com.d1m.wechat.wxsdk.core.req.model.user;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;

/**
 * 批量获取用户基本信息
 *
 * 开发者可通过该接口来批量获取用户基本信息。最多支持一次拉取100条。
 *
 * @author f0rb on 2016-12-08.
 */
@ReqType("batchGetUserInfoList")
@Getter
public class BatchGetUserInfo extends WeixinReqParam {
    private final List<WxUserReq> user_list;

    private BatchGetUserInfo(List<WxUserReq> wxUserReqList) {
        this.user_list = wxUserReqList;
    }

    @Getter
    private static class WxUserReq {
        private String openid;
        private String lang;

        public WxUserReq(String openid) {
            this.openid = openid;
        }

        public WxUserReq(String openid, String lang) {
            this.openid = openid;
            this.lang = lang;
        }
    }

    public static List<BatchGetUserInfo> prepare(List<String> openIdList) {
        List<BatchGetUserInfo> ret = new LinkedList<>();

        int step = 100;
        int fromIndex = 0, toIndex, total = openIdList.size();

        while (fromIndex < total) {
            toIndex = Math.min(fromIndex + step, openIdList.size());

            // 封装请求参数, 100个一组
            List<WxUserReq> wxUserReqList = new LinkedList<>();
            for (String openId : openIdList.subList(fromIndex, toIndex)) {
                wxUserReqList.add(new WxUserReq(openId));
            }

            ret.add(new BatchGetUserInfo(wxUserReqList));

            fromIndex += step;
        }
        return ret;
    }
}
