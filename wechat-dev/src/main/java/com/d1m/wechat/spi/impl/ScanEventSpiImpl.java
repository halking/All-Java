package com.d1m.wechat.spi.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.d1m.wechat.model.SpiConfig;
import com.d1m.wechat.spi.action.ScanThenPullArticleAndPushAction;
import com.d1m.wechat.spi.action.ScanThenTagAction;
import com.d1m.wechat.spi.core.NotifyEventSpi;
import com.d1m.wechat.spi.core.NotifyEventSpiManager;
import com.d1m.wechat.spi.core.event.NotifyEventObject;
import com.d1m.wechat.spi.core.event.ScanEventObject;
import com.d1m.wechat.spi.core.event.ScanSubEventObject;

/**
 * 用户扫码后, 如果spi_config表有action为ScanThenPullArticleAndPush的配置时,
 * 会从配置的服务器地址remote拉取一段图文并推送给用户
 *
 * @author f0rb on 2017-07-06.
 */
@Slf4j
@Component
public class ScanEventSpiImpl implements NotifyEventSpi<NotifyEventObject> {

    @Resource
    private ScanThenPullArticleAndPushAction scanThenPullArticleAndPushAction;

    @Resource
    private ScanThenTagAction scanThenTagAction;

    @PostConstruct
    public void init() {
        NotifyEventSpiManager.register(ScanEventObject.class, this);
        NotifyEventSpiManager.register(ScanSubEventObject.class, this);
    }

    @Override
    @Async("callerRunsExecutor")
    public void handle(NotifyEventObject scanEvent, List<SpiConfig> spiConfigList) {
        for (SpiConfig spiConfig : spiConfigList) {
            try {
                if (scanThenPullArticleAndPushAction.getActionName().equals(spiConfig.getAction())) {
                    //String sceneId = scanEvent.getEventKey();
                    //JSONObject params = JSON.parseObject(spiConfig.getParams());
                    //scanThenPullArticleAndPushAction.doExecute(
                    //        spiConfig.getWechatId(), scanEvent.getToUserName(), scanEvent.getFromUserName(),
                    //        sceneId, params.getString("remote"), params.getString("token")
                    //);
                    Map<String, String> params = JSON.parseObject(spiConfig.getParams(), new TypeReference<Map<String, String>>() {});
                    scanThenPullArticleAndPushAction.execute(spiConfig.getWechatId(), scanEvent, params);
                } else if (scanThenTagAction.getActionName().equals(spiConfig.getAction())) {
                    Map<String, String> params = JSON.parseObject(spiConfig.getParams(), new TypeReference<Map<String, String>>() {});
                    scanThenTagAction.execute(spiConfig.getWechatId(), scanEvent, params);
                }
            } catch (Exception e) {
                log.error("ScanThenPullArticleAndPush处理失败", e);
            }
        }

    }

}
