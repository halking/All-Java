package com.d1m.wechat.spi.action;

import java.util.Collections;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.spi.core.AbstractNotifyEventAction;
import com.d1m.wechat.spi.core.NotifyObject;
import com.d1m.wechat.spi.core.event.ScanEventObject;
import com.d1m.wechat.spi.core.event.ScanSubEventObject;
import com.d1m.wechat.wxsdk.user.tag.JwTagApi;

/**
 * ScanThenTagAction
 * <p>
 * 扫码打标签
 *
 * @author f0rb on 2017-07-10.
 */
@Slf4j
@Component
public class ScanThenTagAction extends AbstractNotifyEventAction {

    @Override
    public void execute(Integer wechatId, NotifyObject notifyObject, Map<String, String> configParamMap) {
        String sceneId;
        if (notifyObject instanceof ScanEventObject) {
            sceneId = ((ScanEventObject) notifyObject).getEventKey();
        } else if (notifyObject instanceof ScanSubEventObject) {
            String eventKey = ((ScanSubEventObject) notifyObject).getEventKey();//事件KEY值，qrscene_为前缀，后面为二维码的参数值
            sceneId = eventKey.substring("qrscene_".length());
        } else {
            log.warn("ScanThenTagAction: {}", notifyObject.getClass().getName());
            return;
        }
        String scene = configParamMap.get("scene");
        if (StringUtils.isEmpty(scene)) {
            log.error("扫码打标签: scene未配置");
            return;
        }
        String tag = configParamMap.get("tag");
        if (StringUtils.isEmpty(tag)) {
            log.error("扫码打标签: tag未配置");
            return;
        }
        String[] tags = tag.split(",");
        String[] scenes = scene.split(",");

        String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
        for (String s : scenes) {
            log.info("扫码打标签: [{}] sceneId={}", s, sceneId);
            if (s.equals(sceneId)) {// 如果二维码场景匹配则给用户打标签
                for (String tagId : tags) {
                    String result = JwTagApi.memberBatchTag(accessToken, tagId, Collections.singletonList(notifyObject.getFromUserName()));
                    log.info("扫码打标签: {}", result);
                }
                break;
            }
        }
    }

}
