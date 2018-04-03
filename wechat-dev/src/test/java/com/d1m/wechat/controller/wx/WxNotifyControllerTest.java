package com.d1m.wechat.controller.wx;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;

import com.d1m.wechat.mapper.OriginalConversationMapper;
import com.d1m.wechat.mapper.WechatMapper;
import com.d1m.wechat.model.OriginalConversation;
import com.d1m.wechat.model.Wechat;
import com.d1m.wechat.test.SpringTest;
import com.d1m.wechat.util.WeiXinUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.springframework.core.task.TaskExecutor;

/**
 * WxNotifyControllerTest
 *
 * @author f0rb on 2017-04-27.
 */
@Slf4j
public class WxNotifyControllerTest extends SpringTest {

    @Resource
    private OriginalConversationMapper originalConversationMapper;

    @Resource
    private WechatMapper wechatMapper;

    @Resource
    private TaskExecutor callerRunsExecutor;

    @Test
    public void doNotify() {
        List<OriginalConversation> originalConversationList = originalConversationMapper.selectAll();

        Map<Integer, Wechat> wechatCache = new ConcurrentHashMap<>();
        //final String url = "http://cloud.wechat.d.d1m.cn/api/v2/wechat-notify/notify";
        final String url = "http://wechat.d.d1m.cn/backend/notify";
        for (final OriginalConversation originalConversation : originalConversationList) {
            if (originalConversation.getWechatId() == null) {
                continue;
            }
            Wechat wechat = wechatCache.get(originalConversation.getWechatId());
            if (wechat == null) {
                wechat = wechatMapper.selectByPrimaryKey(originalConversation.getWechatId());
                if (wechat == null) {
                    continue;
                }
                wechatCache.put(wechat.getId(), wechat);
            }

            final String oid = wechat.getOpenId();
            final String token = wechat.getToken();

            callerRunsExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String nonce = WeiXinUtils.createNonceStr();
                    String timestamp = System.currentTimeMillis() / 1000 + "";
                    String signature = WeiXinUtils.getSignature(timestamp, nonce, token);

                    String uri = url +
                            "?oid=" + oid +
                            "&signature=" + signature +
                            "&timestamp=" + timestamp +
                            "&nonce=" + nonce +
                            "&token=" + token;

                    Request request = Request.Post(uri);
                    request.bodyString(originalConversation.getContent(), ContentType.APPLICATION_XML);
                    try {
                        log.info("http request: {}", uri);
                        String rsp = "";
                        request.execute().discardContent();
                        //rsp = request.execute().returnContent().asString();
                        log.info("ret: {}", rsp);
                    } catch (IOException e) {
                        log.info("IOException: {}", e.getMessage());
                    }
                }
            });
        }


    }

}