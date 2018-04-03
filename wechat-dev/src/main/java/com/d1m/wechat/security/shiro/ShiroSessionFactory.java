package com.d1m.wechat.security.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

/**
 * 创建{@link ShiroSession}的工厂
 * <p>
 * 配置到sessionManager的sessionFactory属性
 *
 * @author forb on 2016/11/16
 */
public class ShiroSessionFactory implements SessionFactory {

    @Override
    public Session createSession(SessionContext initData) {

        if (initData != null) {
            String host = initData.getHost();
            if (host != null) {
                return new ShiroSession(host);
            }
        }

        return new ShiroSession();
    }
}
