package com.d1m.wechat.schedule.job;

import com.d1m.wechat.schedule.BaseJob;
import com.d1m.wechat.schedule.ExecResult;
import com.d1m.wechat.service.WxMemberTagService;
import com.d1m.wechat.util.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Lisa on 2018/1/23.
 */
@Slf4j
@Component
public class SyncMemberTagJob  extends BaseJob {

    @Autowired
    WxMemberTagService wxMemberTagService;

    @Override
    public ExecResult run(Map<String, String> params) {
        log.info("=======>>SyncMemberTagJob start<<=======");
        Integer wechatId = ParamUtil.getInt(params.get("wechatId"), null);
        Integer tagId = ParamUtil.getInt(params.get("tagId"), null);
        log.info("wechatId:" + wechatId);
        if(wechatId == null){
            return null;
        }
        wxMemberTagService.syncMemberTagFromWechat(wechatId, tagId);
        log.info("=======>>SyncMemberTagJob end<<=======");
        return null;
    }
}
