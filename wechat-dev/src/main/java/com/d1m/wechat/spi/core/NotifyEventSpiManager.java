package com.d1m.wechat.spi.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.d1m.wechat.model.SpiConfig;

/**
 * NotifyEventSpi
 *
 * @author f0rb on 2017-07-07.
 */
public class NotifyEventSpiManager {

    private final static Map<Class<? extends NotifyObject>, NotifyEventSpi> notifyEventSpiMap = new HashMap<>();

    public static void register(Class<? extends NotifyObject> clazz, NotifyEventSpi notifyEventSpi) {
        notifyEventSpiMap.put(clazz, notifyEventSpi);
    }

    //public static void register(NotifyEventSpi<? extends NotifyObject> notifyEventSpi) {
    //    notifyEventSpiMap.put(notifyEventSpi.getClass().getTypeParameters(), notifyEventSpi);
    //}

    @SuppressWarnings("unchecked")
    public static void doExecute(NotifyObject notifyObject, List<SpiConfig> spiConfigList) {
        if (notifyObject != null && notifyEventSpiMap.containsKey(notifyObject.getClass())) {
            notifyEventSpiMap.get(notifyObject.getClass()).handle(notifyObject, spiConfigList);
        }
    }
}
