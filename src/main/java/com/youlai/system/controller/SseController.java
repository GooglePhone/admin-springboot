package com.youlai.system.controller;

import com.youlai.system.model.entity.CustomSseEmitter;
import com.youlai.system.security.util.SecurityUtils;
import com.youlai.system.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author Kylin
 * @date 2024/08/10
 */
@RestController
public class SseController {

    @Autowired
    private SseService sseService;

    @GetMapping("/sse")
    public SseEmitter handleSse(){
        Long userId = SecurityUtils.getUserId();
        CustomSseEmitter emitter = new CustomSseEmitter(60000L,userId); // 设置超时时间为60秒
        try {
            sseService.addEmitter(emitter);
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }
}
