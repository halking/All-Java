package com.d1m.wechat.security.shiro;

import org.apache.shiro.session.mgt.SimpleSession;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 配合{@link RedisCacheSessionDAO}
 * <p>
 * 由于SimpleSession的lastAccessTime属性更改后也会调用SessionDao的update方法，
 * 增加标识位changed，如果只是更新lastAccessTime, 则SessionDao的update方法直接返回
 *
 * @author forb on 2016/11/16
 */
public class ShiroSession extends SimpleSession implements Serializable {
    private static final long serialVersionUID = 6280155304342244740L;
    // 除lastAccessTime以外其他字段发生改变时为true
    private boolean changed;

    public ShiroSession() {
        super();
        this.setChanged(true);
    }

    public ShiroSession(String host) {
        super(host);
        this.setChanged(true);
    }

    @Override
    public void setId(Serializable id) {
        super.setId(id);
        this.setChanged(true);
    }

    @Override
    public void setStopTimestamp(Date stopTimestamp) {
        super.setStopTimestamp(stopTimestamp);
        this.setChanged(true);
    }

    @Override
    public void setExpired(boolean expired) {
        super.setExpired(expired);
        this.setChanged(true);
    }

    @Override
    public void setTimeout(long timeout) {
        super.setTimeout(timeout);
        this.setChanged(true);
    }

    @Override
    public void setHost(String host) {
        super.setHost(host);
        this.setChanged(true);
    }

    @Override
    public void setAttributes(Map<Object, Object> attributes) {
        super.setAttributes(attributes);
        this.setChanged(true);
    }

    @Override
    public void setAttribute(Object key, Object value) {
        super.setAttribute(key, value);
        this.setChanged(true);
    }

    @Override
    public Object removeAttribute(Object key) {
        this.setChanged(true);
        return super.removeAttribute(key);
    }

    /**
     * 停止
     */
    @Override
    public void stop() {
        super.stop();
        this.setChanged(true);
    }

    /**
     * 设置过期
     */
    @Override
    protected void expire() {
        this.stop();
        this.setExpired(true);
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean isChanged) {
        this.changed = isChanged;
    }
}
