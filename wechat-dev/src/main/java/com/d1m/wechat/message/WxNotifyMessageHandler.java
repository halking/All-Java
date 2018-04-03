package com.d1m.wechat.message;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.d1m.wechat.model.*;
import com.d1m.wechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import com.d1m.wechat.component.CommonRepeatChecker;
import com.d1m.wechat.mapper.SpiConfigMapper;
import com.d1m.wechat.model.enums.Event;
import com.d1m.wechat.spi.core.NotifyEventSpiManager;
import com.d1m.wechat.spi.core.NotifyObject;
import com.d1m.wechat.spi.core.NotifyObjectParser;
import com.d1m.wechat.util.ParamUtil;

/**
 * WxNotifyMessageHandler
 *
 * @author f0rb on 2017-05-02.
 */
@Slf4j
@Component
public class WxNotifyMessageHandler {

    @Resource
    private ConversationService conversationService;

    @Resource
    private WechatService wechatService;

    @Resource
    private MemberService memberService;

    @Resource
    private OriginalConversationService originalConversationService;

    @Resource
    private CommonRepeatChecker commonRepeatChecker;

    @Resource
    SpiConfigMapper spiConfigMapper;

    public void handle(String xmlMsg) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(IOUtils.toInputStream(xmlMsg, "UTF-8"));
            Element root = document.getRootElement();
            String toUserName = ParamUtil.getElementContent(root, "ToUserName");
            // 0.获取Wechat
            Wechat wechat = wechatService.getByOpenId(toUserName);
            // 1.存储原始报文
            OriginalConversation originalConversation =
                    conversationService.preHandleMemberToWechat(wechat, document);

            String fromUserName = ParamUtil.getElementContent(root, "FromUserName");
            String event = ParamUtil.getElementContent(root, "Event");
            String createTime = ParamUtil.getElementContent(root, "CreateTime");
            Date current = new Date(ParamUtil.getLong(createTime, null) * 1000);

            // 2.判断用户是否存在，没有则创建，系统事件除外
            Member member = null;
            Event et = Event.getValueByName(event);
            if (et == null || !et.isSystem()) {
                member = memberService.createMember(wechat.getId(), fromUserName, current);

                // 3.更新会员最后会话时间
                // 设置最后交互时间
                if (et == null || et.isInteractive()) {
                    Integer memberId = member.getId();
                    Integer updatePerSeconds = 60;
                    if (!commonRepeatChecker.checkIfExistsInSeconds("wechat:member:" + memberId, updatePerSeconds)) {
                        Member update = new Member();
                        update.setId(memberId);
                        update.setLastConversationAt(current);
                        memberService.updateNotNull(update);
                        log.info("更新会员交互时间: memberId={} ", memberId);
                    } else {
                        log.info("会员交互时间在[{}]秒内已更新: memberId={} ", updatePerSeconds, memberId);
                    }
                }
            }

            // 4.处理消息
            originalConversationService.execute(member, document, originalConversation);

            // 处理spi 按wechatId和event查询
            SpiConfig query = new SpiConfig();
            query.setWechatId(wechat.getId());
            query.setStatus((byte) 1);// 有效
            List<SpiConfig> spiConfigList = spiConfigMapper.select(query);

            NotifyObject notifyObject = NotifyObjectParser.parseXml(xmlMsg);

            NotifyEventSpiManager.doExecute(notifyObject, spiConfigList);

        } catch (Exception e) {
            log.error("微信消息处理失败!", e);
        }
    }
}
