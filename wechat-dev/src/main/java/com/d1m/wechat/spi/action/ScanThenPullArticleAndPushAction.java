package com.d1m.wechat.spi.action;

import java.io.IOException;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Component;

import com.d1m.wechat.model.enums.MsgType;
import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.spi.core.AbstractNotifyEventAction;
import com.d1m.wechat.spi.core.NotifyObject;
import com.d1m.wechat.spi.core.RestResponse;
import com.d1m.wechat.spi.core.event.ScanEventObject;
import com.d1m.wechat.spi.core.event.ScanSubEventObject;
import com.d1m.wechat.spi.model.ArticleRequest;
import com.d1m.wechat.spi.model.ArticleResponse;
import com.d1m.wechat.util.WeiXinUtils;
import com.d1m.wechat.wxsdk.core.req.model.kfaccount.KfcustomSend;
import com.d1m.wechat.wxsdk.core.req.model.kfaccount.MsgNews;
import com.d1m.wechat.wxsdk.core.sendmsg.JwKfaccountAPI;

/**
 * PullArticleAndPushAction
 *
 * @author f0rb on 2017-07-10.
 */
@Slf4j
@Component
public class ScanThenPullArticleAndPushAction extends AbstractNotifyEventAction {

    public void doExecute(Integer wechatId, String toUserName, String fromUserName, String sceneId, String remote, String token) {
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setToUserName(toUserName);
        articleRequest.setFromUserName(fromUserName);
        articleRequest.setSceneId(sceneId);

        String timestamp = System.currentTimeMillis() + ""; //时间戳
        String nonce = WeiXinUtils.createNonceStr();        //随机数
        String signature;   //微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        signature = WeiXinUtils.getSignature(token, timestamp, nonce);

        StringBuilder url = new StringBuilder();
        url.append(remote)
           .append(remote.contains("?") ? "&" : "?")
           .append("timestamp=").append(timestamp)
           .append("&nonce=").append(nonce)
           .append("&signature=").append(signature);

        try {
            String response = Request.Post(url.toString())
                                     .bodyString(JSON.toJSONString(articleRequest), ContentType.APPLICATION_JSON)
                                     .execute().returnContent().asString();

            RestResponse<ArticleResponse> ret = JSON.parseObject(response, new TypeReference<RestResponse<ArticleResponse>>() {});
            if (ret.success()) {
                ArticleResponse articleResponse = ret.getData();

                if (StringUtils.equals(articleResponse.getFromUserName(), toUserName)
                        && StringUtils.equals(articleResponse.getToUserName(), fromUserName)) {

                    String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
                    KfcustomSend kfcustomSend = new KfcustomSend();
                    kfcustomSend.setAccess_token(accessToken);
                    kfcustomSend.setTouser(fromUserName);

                    MsgNews msgNews = new MsgNews();
                    msgNews.setArticles(articleResponse.getArticles());
                    kfcustomSend.setNews(msgNews);
                    kfcustomSend.setMsgtype(MsgType.NEWS.name().toLowerCase());
                    JwKfaccountAPI.sendKfMessage(kfcustomSend);
                } else {
                    log.warn("To/From user 不匹配, 图文推送取消: \n 事件From {} vs 推送To {}\n事件To {} vs 推送From {}",
                             fromUserName, articleResponse.getToUserName(),
                             toUserName, articleResponse.getFromUserName()
                    );
                }
            } else {
                log.warn("remote failed: {}[{}]", ret.getInfo(), ret.getCode());
            }

        } catch (IOException e) {
            log.error("扫码推图文失败", e);
        }
    }

    @Override
    public void execute(Integer wechatId, NotifyObject notifyObject, Map<String, String> configParamMap) {
        String sceneId;
        if (notifyObject instanceof ScanEventObject) {
            sceneId = ((ScanEventObject) notifyObject).getEventKey();
        } else if (notifyObject instanceof ScanSubEventObject) {
            String eventKey = ((ScanSubEventObject) notifyObject).getEventKey();//事件KEY值，qrscene_为前缀，后面为二维码的参数值
            sceneId = eventKey.substring("qrscene_".length());
        } else {
            log.warn("ScanThenPullArticleAndPush事件触发错误: {}", notifyObject.getClass().getName());
            return;
        }
        doExecute(wechatId, notifyObject.getToUserName(), notifyObject.getFromUserName(),
                  sceneId, configParamMap.get("remote"), configParamMap.get("token"));
    }

}
