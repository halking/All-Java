//package com.d1m.wechat.wxsdk.core.mq;
//
//import javax.jms.Message;
//import javax.jms.MessageListener;
//import javax.jms.ObjectMessage;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.d1m.wechat.model.OriginalConversation;
//import com.d1m.wechat.model.enums.OriginalConversationStatus;
//import com.d1m.wechat.service.ConversationService;
//import com.d1m.wechat.service.OriginalConversationService;
//
///**
// * 微信接口消息接收
// * @author d1m
// *
// */
//public class ConsumerMessageListener implements MessageListener {
//	private static Logger log = LoggerFactory.getLogger(ConsumerMessageListener.class);
//	@Autowired
//	private ConversationService conversationService;
//	@Autowired
//	private OriginalConversationService originalConversationService;
//	
//	@Override
//	public void onMessage(Message message) {
//		OriginalConversation originalConversation = null;
//		try {
//			if(message instanceof ObjectMessage){
//				ObjectMessage objectMsg = (ObjectMessage) message;
//				originalConversation = (OriginalConversation) objectMsg.getObject();
//				log.info("original conversation id : {}, status : {}, start handle.",originalConversation.getId(),originalConversation.getStatus());
//				conversationService.memberToWechat(originalConversation);
//				log.info("original conversation id : {} end handle.",originalConversation.getId());
//			}else{
//				log.info("unsupported message!");
//			}
//        } catch (Exception e) {
//        	originalConversation.setStatus(OriginalConversationStatus.FAIL.getValue());
//			originalConversationService.updateAll(originalConversation);
//        	log.error("wechat message process error:"+e.getMessage());
//        }
//	}
//}
