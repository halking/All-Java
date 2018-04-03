package com.d1m.wechat.security.shiro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.MapCache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 该类基于{@link CachingSessionDAO}实现了基于Redis的session共享功能
 * <p>
 * 基于redis的消息发布订阅机制来同步各个节点的session缓存
 *
 * @author forb on 2016/11/17
 */
@Slf4j
public class RedisCacheSessionDAO extends CachingSessionDAO {
    private static final String REDIS_SHIRO_SESSION = "shiro-session:";
    private static final int SESSION_VAL_TIME_SPAN = 1800;

    /* 缓存清理消息的发布和处理 */
    private final String nodeId = ManagementFactory.getRuntimeMXBean().getName();

    @Setter
    private String uncacheChannel = "redis.topic.shiro.uncache";

    // 保存到Redis中key的前缀 prefix+sessionId
    @Setter
    private String redisShiroSessionPrefix = REDIS_SHIRO_SESSION;

    // 设置会话的过期时间
    @Setter
    private int redisShiroSessionTimeout = SESSION_VAL_TIME_SPAN;

    @Setter
    private RedisTemplate<String, Session> redisTemplate;

    public RedisCacheSessionDAO() {
        setCacheManager(new AbstractCacheManager() {
            @Override
            protected Cache<Serializable, Session> createCache(String name) throws CacheException {
                return new MapCache<Serializable, Session>(name, new ConcurrentHashMap<Serializable, Session>());
            }
        });
    }

    /**
     * 根据会话ID获取会话
     *
     * @param sessionId 会话ID
     * @return ShiroSession
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        log.debug("Session 读取: {} ", sessionId);
        Session session = null;
        try {
            session = redisTemplate.boundValueOps(redisKey(sessionId)).get();
            if (session != null) {
                // 缓存到本地
                cache(session, sessionId);
                // 重置Redis中缓存过期时间
                redisTemplate.expire(redisKey(sessionId), redisShiroSessionTimeout, TimeUnit.SECONDS);
                log.info("Session 读取成功: {}", sessionId);
            }
        } catch (Exception e) {
            log.error("Session 读取失败: " + sessionId, e);
        }
        return session;
    }

    /**
     * 如DefaultSessionManager在创建完session后会调用该方法；
     * 如保存到关系数据库/文件系统/NoSQL数据库；即可以实现会话的持久化；
     * 返回会话ID；主要此处返回的ID.equals(session.getId())；
     */
    @Override
    protected Serializable doCreate(Session session) {
        // 创建一个Id并设置给Session
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        try {
            redisTemplate.opsForValue().set(redisKey(sessionId), session, redisShiroSessionTimeout, TimeUnit.SECONDS);
            log.info("Session 创建成功 {}", sessionId);
        } catch (Exception e) {
            log.error("Session 创建失败", e);
        }
        return sessionId;
    }

    /**
     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
     * <p>
     * fixme: 在一次请求/响应中, 毎次更新session都会'立即'同步到redis, 有可能产生性能问题!
     */
    @Override
    protected void doUpdate(Session session) {
        Serializable sessionId = session.getId();
        //如果会话过期/停止 没必要再更新了
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            log.debug("Session 过期, 不用更新: {}", sessionId);
            return;
        }
        try {
            if (session instanceof ShiroSession) {
                // 如果没有主要字段(除lastAccessTime以外其他字段)发生改变
                ShiroSession ss = (ShiroSession) session;
                if (!ss.isChanged()) {
                    return;
                }
                ss.setChanged(false);
                ss.setLastAccessTime(new Date());

                redisTemplate.boundValueOps(redisKey(sessionId))
                        .set(session, redisShiroSessionTimeout, TimeUnit.SECONDS);
                sendUncacheSessionMessage(sessionId);

                log.debug("Session 更新成功: {}", sessionId);
            } else {
                log.debug("Session 类型错误: {}", session.getClass().getName());
            }
        } catch (Exception e) {
            log.error("Session 更新失败", e);
        }
    }

    /**
     * 删除会话，并发布消息通知其他节点更新
     * <p>
     * 当会话过期/会话停止（如用户退出时）会调用
     */
    @Override
    protected void doDelete(Session session) {
        if (session == null) {
            return;
        }
        Serializable sessionId = session.getId();
        if (sessionId == null) {
            return;
        }
        log.debug("Deleting redis session: {} ", sessionId);
        try {
            redisTemplate.delete(redisKey(sessionId));
            sendUncacheSessionMessage(sessionId);
            log.debug("Deleting succeed!");
        } catch (Exception e) {
            log.error("Deleting failed", e);
        }
    }

    private String redisKey(Serializable sessionId) {
        return redisShiroSessionPrefix + sessionId;
    }

    /**
     * TODO: 获取当前所有活跃用户
     */
    @Override
    public Collection<Session> getActiveSessions() {
        return super.getActiveSessions();
    }

    /**
     * 发送缓存清理通知
     *
     * @param sessionId sessionId
     */
    private void sendUncacheSessionMessage(Serializable sessionId) {
        ShiroMessage message = new ShiroMessage(sessionId, nodeId);
        redisTemplate.convertAndSend(uncacheChannel, message);
        log.debug("发送缓存清理消息: {}", message);
    }

    /**
     * 处理清理本地session cache的通知
     *
     * @param message {@link ShiroMessage}
     */
    public void handleMessage(ShiroMessage message) {
        if (!nodeId.equals(message.getNodeId())) {
            Session session = getCachedSession(message.getSessionId());
            if (session != null) {
                uncache(session);
                log.info("清理已缓存session: {}", message.getSessionId());
            } else {
                log.debug("忽略未缓存session: {}(来自节点: {})", message.getSessionId(), message.getNodeId());
            }
        } else {
            log.debug("忽略本节点消息");
        }
    }

    /**
     * ShiroMessage
     * <p>
     * 包含一个jvm节点的id和session的id
     *
     * @author f0rb create on 2016-11-16
     */
    @AllArgsConstructor
    @Getter
    @ToString
    private static class ShiroMessage implements Serializable {
        private static final long serialVersionUID = 7923149420754878415L;
        private final Serializable sessionId;
        private final String nodeId;
    }
}
