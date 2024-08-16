package com.youlai.system.service.impl;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youlai.system.model.entity.CustomSseEmitter;
import com.youlai.system.model.vo.NoticeVO;
import com.youlai.system.service.SseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kylin
 * @date 2024/8/10
 */
@Service
@Slf4j
public class SseServiceImpl implements SseService {

    private final Map<Long, CustomSseEmitter> emitterMap = new HashMap<>();

    @Override
    public boolean addEmitter(CustomSseEmitter emitter) {
        try {
            emitter.onCompletion(() -> removeEmitter(emitter.getUserId()));
            emitter.onTimeout(() -> removeEmitter(emitter.getUserId()));
            emitter.onError((e) -> removeEmitter(emitter.getUserId()));
            emitterMap.put(emitter.getUserId(), emitter);
            log.info("Added SSE emitter for user: {}", emitter.getUserId());
            return true;
        } catch (Exception e) {
            log.error("Failed to add SSE emitter for user: {}", emitter.getUserId(), e);
            return false;
        }
    }

    @Override
    public boolean sendNotification(NoticeVO noticeVO) {
        try {
            String json = JSONUtil.parseObj(noticeVO).toString();
            Set<Long> longs = emitterMap.keySet();
            for (Long userId : longs) {
                try {
                    CustomSseEmitter emitter = emitterMap.get(userId);
                    if(emitter != null) {
                        emitter.send(SseEmitter.event().data(json));
                    }
                } catch (Exception e) {
                    log.error("Failed to send notification to user: {}", userId);
                    removeEmitter(userId);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Failed to serialize notification");
            return false;
        }
    }

    @Override
    public boolean removeEmitter(Long userId) {
        if(emitterMap.containsKey(userId)){
            CustomSseEmitter emitter = emitterMap.remove(userId);
            if (emitter != null) {
                emitter.complete();
                log.info("Removed SSE emitter for user: {}", userId);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean sendNotificationToEmitter(Long userId, NoticeVO noticeVO) {
        CustomSseEmitter emitter = emitterMap.get(userId);
        if (emitter == null) {
            log.warn("No SSE emitter found for user: {}", userId);
            return false;
        }
        try {
            String json = JSONUtil.parseObj(noticeVO).toString();
            emitter.send(SseEmitter.event().data(json));
            log.info("Sent notification to user: {}", userId);
            return true;
        } catch (Exception e) {
            log.error("Failed to send notification to user: {}", userId, e);
            emitter.completeWithError(e);
            removeEmitter(userId);
            return false;
        }
    }

    @Override
    public String getConNum() {
        return emitterMap.keySet().size()+"";
    }
}
