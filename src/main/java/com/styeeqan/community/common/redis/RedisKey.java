package com.styeeqan.community.common.redis;

import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * Redis key-tool
 *
 * @author yeeq
 * @date 2021/7/26
 */

public enum RedisKey {

    USER(Duration.ofDays(7)),
    TOPIC(Duration.ofDays(7)),
    COMMENT(Duration.ofDays(7)),
    USER_INFO(Duration.ofDays(7)),

    USER_DYNAMIC_TASK_LIST(null)
    ;

    /**
     * key 的 value 的 expire 时间,如果为 null，表示 value 永远不过期
     */
    private final Duration timeout;

    RedisKey(Duration timeout) {
        this.timeout = timeout;
    }

    /**
     * 获取 key 值，参数为空传回枚举值
     */
    public String getKey(String id) {
        if (StringUtils.isEmpty(id)) {
            return this.name();
        }
        return this.name() + "_" + id;
    }

    public Duration getTimeout() {
        return timeout;
    }
}