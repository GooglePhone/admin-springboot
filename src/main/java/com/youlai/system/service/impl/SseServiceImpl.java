package com.youlai.system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youlai.system.model.entity.CustomSseEmitter;
import com.youlai.system.model.vo.NoticeVO;
import com.youlai.system.service.SseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Kylin
 * @date 2024/8/10
 */
@Service
@Slf4j
public class SseServiceImpl implements SseService {

    private final CopyOnWriteArrayList<CustomSseEmitter > emitterList = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean addEmitter(CustomSseEmitter emitter) {
        try {
            emitter.onCompletion(() -> emitterList.remove(emitter));
            emitter.onTimeout(() -> emitterList.remove(emitter));
            emitterList.add(emitter);
            return true;
        } catch (Exception e) {
            log.error("添加SSE客户端失败", e);
            return false;
        }
    }

    @Override
    public boolean sendNotification(NoticeVO noticeVO) {
        try {
            String json = objectMapper.writeValueAsString(noticeVO);
            emitterList.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().data(json));
                } catch (IOException e) {
                    log.error("发送通知失败", e);
                    emitter.completeWithError(e);
                }
            });
            return true;
        } catch (Exception e) {
            log.error("序列化通知失败", e);
            return false;
        }
    }

    @Override
    public boolean removeEmitter(CustomSseEmitter  emitter) {
        return emitterList.remove(emitter);
    }

    @Override
    public boolean sendNotificationToEmitter(CustomSseEmitter emitter, NoticeVO noticeVO) {
        try {
            String json = objectMapper.writeValueAsString(noticeVO);
            emitter.send(SseEmitter.event().data(json));
            return true;
        } catch (Exception e) {
            log.error("向指定的SSE客户端发送通知失败", e);
            return false;
        }
    }
}
