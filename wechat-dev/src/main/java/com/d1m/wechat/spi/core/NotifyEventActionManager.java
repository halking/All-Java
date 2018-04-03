//package com.d1m.wechat.spi.core;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//
//import com.d1m.wechat.model.SpiConfig;
//
///**
// * NotifyEventAction
// *
// * @author f0rb on 2017-07-18.
// */
//public class NotifyEventActionManager {
//
//    private final static Map<String, NotifyEventAction> notifyEventActionMap = new HashMap<>();
//
//    public static void register(String action, NotifyEventAction notifyEventAction) {
//        notifyEventActionMap.put(action, notifyEventAction);
//    }
//
//    @SuppressWarnings("unchecked")
//    public static void doExecute(NotifyObject notifyObject, SpiConfig spiConfig) {
//        if (notifyEventActionMap.containsKey(spiConfig.getAction())) {
//            NotifyEventAction notifyEventAction = notifyEventActionMap.get(spiConfig.getAction());
//            Map<String, String> configParamMap = JSON.parseObject(spiConfig.getParams(), new TypeReference<Map<String, String>>() {});
//            notifyEventAction.execute(
//                    spiConfig.getWechatId(), notifyObject.getToUserName(), notifyObject.getFromUserName(),
//                    eventParamMap, configParamMap);
//        }
//    }
//}
//
