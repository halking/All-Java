package com.d1m.wechat.spi.core;

import java.util.List;

import com.d1m.wechat.model.SpiConfig;

/**
 * NotifyEventSpi
 *
 * @author f0rb on 2017-07-07.
 */
public interface NotifyEventSpi<T extends NotifyObject> {

    void handle(T notifyObject, List<SpiConfig> spiConfigList);

}
