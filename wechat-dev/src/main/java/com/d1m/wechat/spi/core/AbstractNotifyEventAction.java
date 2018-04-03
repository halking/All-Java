package com.d1m.wechat.spi.core;

/**
 * AbstractNotifyEventAction
 *
 * @author f0rb on 2017-07-18.
 */
public abstract class AbstractNotifyEventAction implements NotifyEventAction {
    @Override
    public final String getActionName() {
        return this.getClass().getSimpleName();
    }

}
