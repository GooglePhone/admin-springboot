package com.youlai.system.controller;

import com.youlai.system.common.result.Result;
import com.youlai.system.model.entity.CustomSseEmitter;
import com.youlai.system.model.vo.NoticeVO;
import com.youlai.system.security.util.SecurityUtils;
import com.youlai.system.service.SseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        CustomSseEmitter emitter = new CustomSseEmitter(0L,userId); // 设置超时时间为60秒
        try {
            sseService.addEmitter(emitter);
            // 发送一个连接成功的事件，保证前端发送连接请求后能够收到一个响应，不会让前端一直处于等待状态
            emitter.send(SseEmitter.event().name("connected").data("连接成功"));
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    @GetMapping("/sse/logout")
    public void logout(){
        Long userId = SecurityUtils.getUserId();
        sseService.removeEmitter(userId);
    }

    @GetMapping("/sse/test/send")
    public String send(){
        NoticeVO noticeVO = new NoticeVO();
        noticeVO.setTitle("测试通知");
        sseService.sendNotification(noticeVO);
        return "success";
    }

    @GetMapping("/sse/test/getConNum")
    public String getConNum(){
        return "当前连接数：" + sseService.getConNum();
    }
}
