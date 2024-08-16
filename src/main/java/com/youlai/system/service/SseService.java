package com.youlai.system.service;

import com.youlai.system.model.entity.CustomSseEmitter;
import com.youlai.system.model.vo.NoticeVO;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author Kylin
 * @date 2024/8/10
 */
public interface SseService {
    /**
     * 添加订阅者
     * @param emitter   订阅者
     * @return 成功装载返回true，否则返回false
     */
    boolean addEmitter(CustomSseEmitter emitter);

    /**
     * 发送通知
     * @param noticeVO  通知
     * @return 成功发送返回true，否则返回false
     */
    boolean sendNotification(NoticeVO noticeVO);

    /**
     * 移除订阅者
     * @param emitter 订阅者
     * @return 成功移除返回true，否则返回false
     */
    boolean removeEmitter(Long userId);

    /**
     * 向指定订阅者发送通知
     * @param emitter   订阅者
     * @param noticeVO  通知
     * @return 成功发送返回true，否则返回false
     */
    boolean sendNotificationToEmitter(Long userId, NoticeVO noticeVO);
}
