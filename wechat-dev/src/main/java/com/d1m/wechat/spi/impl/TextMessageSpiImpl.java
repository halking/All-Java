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
import com.d1m.wechat.spi.action.PushOutletsArticleAction;
import com.d1m.wechat.spi.core.NotifyEventSpi;
import com.d1m.wechat.spi.core.NotifyEventSpiManager;
import com.d1m.wechat.spi.core.message.TextMessage;

/**
 * 用户扫码后, 如果spi_config表有action为ScanThenPullArticleAndPush的配置时,
 * 会从配置的服务器地址remote拉取一段图文并推送给用户
 *
 * @author f0rb on 2017-07-06.
 */
@Slf4j
@Component
public class TextMessageSpiImpl implements NotifyEventSpi<TextMessage> {

    @Resource
    private PushOutletsArticleAction pushOutletsArticleAction;

    @PostConstruct
    public void init() {
        NotifyEventSpiManager.register(TextMessage.class, this);
    }

    @Override
    @Async("callerRunsExecutor")
    public void handle(TextMessage textMessage, List<SpiConfig> spiConfigList) {
        for (SpiConfig spiConfig : spiConfigList) {
            try {
                if ("PushOutletsArticleAction".equals(spiConfig.getAction())) {
                    doPushOutletsArticle(textMessage, spiConfig);
                }
            } catch (Exception e) {
                log.error(spiConfig.getAction() + " failed", e);
            }
        }
    }

    private void doPushOutletsArticle(TextMessage textMessage, SpiConfig spiConfig) {
        Map<String, String> configParamMap = JSON.parseObject(spiConfig.getParams(), new TypeReference<Map<String, String>>() {});
        pushOutletsArticleAction.doExecute(
                spiConfig.getWechatId(), textMessage.getFromUserName(),
                textMessage.getContent(), configParamMap);
    }
}
