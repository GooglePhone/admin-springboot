package com.youlai.system.model.entity;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 用户自定义SSE Emitter
 * @author Kylin
 * @date 2024/08/10
 *
 */
public class CustomSseEmitter extends SseEmitter {

    private Long userId; // 或者其他你需要的用户信息

    public CustomSseEmitter(Long timeout, Long userId) {
        super(timeout);
        this.userId = userId;
    }



    public Long getUserId() {
        return userId;
    }
}
