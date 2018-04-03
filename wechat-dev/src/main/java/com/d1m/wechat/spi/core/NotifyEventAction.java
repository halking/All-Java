package com.d1m.wechat.spi.core;

import java.util.Map;

/**
 * NotifyEventAction
 *
 * @author f0rb on 2017-07-18.
 */
public interface NotifyEventAction {

    String getActionName();

    void execute(Integer wechatId, NotifyObject notifyObject, Map<String, String> configParamMap);

}

