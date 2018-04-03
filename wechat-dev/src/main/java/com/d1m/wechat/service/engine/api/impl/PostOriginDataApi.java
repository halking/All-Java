package com.d1m.wechat.service.engine.api.impl;

import com.d1m.wechat.model.Conversation;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.OriginalConversation;
import com.d1m.wechat.service.ConfigService;
import com.d1m.wechat.service.OriginalConversationService;
import com.d1m.wechat.service.engine.api.IApi;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发送扫码事件的原始报文到第三方服务器
 */
@Service
public class PostOriginDataApi implements IApi {
    private static Logger log = LoggerFactory.getLogger(PostOriginDataApi.class);
    @Autowired
    private ConfigService configService;

    @Autowired
    private OriginalConversationService originalConversationService;

    @Override
    public void handle(Integer wechatId, Conversation conversation, List<Member> members) {
        String url = configService.getConfigValue(wechatId,"POST_ORIGIN","URL");
        if(StringUtils.isNotBlank(url)){
            OriginalConversation original =  originalConversationService.get(wechatId,conversation.getOriginalConversationId());
            if(original!=null){
                try {
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpPost httpPost = new HttpPost(url);
                    StringEntity entity = new StringEntity(original.getContent(),"utf-8");
                    httpPost.setEntity(entity);
                    HttpResponse resp = client.execute(httpPost);

                    HttpEntity he = resp.getEntity();
                    String respContent = EntityUtils.toString(he,"UTF-8");
                    log.debug("PostOriginDataApi return:"+respContent);
                }catch (Exception e){
                    log.error("PostOriginDataApi error:",e);
                }
            }
        }
    }
}
