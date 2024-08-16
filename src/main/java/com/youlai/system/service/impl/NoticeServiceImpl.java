package com.youlai.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.youlai.system.security.util.SecurityUtils;
import com.youlai.system.service.SseService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youlai.system.mapper.NoticeMapper;
import com.youlai.system.service.NoticeService;
import com.youlai.system.model.entity.Notice;
import com.youlai.system.model.form.NoticeForm;
import com.youlai.system.model.query.NoticeQuery;
import com.youlai.system.model.vo.NoticeVO;
import com.youlai.system.converter.NoticeConverter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

/**
 * 通知公告服务实现类
 *
 * @author youlaitech
 * @since 2024-08-08 11:46
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    private final NoticeConverter noticeConverter;


    private final SseService sseService;

    /**
     *   发送通知 getSendStatus() > 0 B端用户  0 未发布  1 已发送  2 已撤回
     * @param notice
     */
    private void sendNotice(Notice notice) {
        // 推送通知到前端
        if(notice.getSendStatus() > 0){
            sseService.sendNotification(noticeConverter.toVO(notice));
        }
    }

    /**
    * 获取通知公告分页列表
    *
    * @param queryParams 查询参数
    * @return {@link IPage<NoticeVO>} 通知公告分页列表
    */
    @Override
    public IPage<NoticeVO> getNoticePage(NoticeQuery queryParams) {
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<Notice>()
                .orderByDesc(Notice::getId);
        Page<Notice> page = this.page(new Page<>(queryParams.getPageNum(), queryParams.getPageSize()), queryWrapper);
        return noticeConverter.toPageVO(page);
    }
    
    /**
     * 获取通知公告表单数据
     *
     * @param id 通知公告ID
     * @return
     */
    @Override
    public NoticeForm getNoticeFormData(Long id) {
        Notice entity = this.getById(id);
        return noticeConverter.toForm(entity);
    }
    
    /**
     * 新增通知公告
     *
     * @param formData 通知公告表单对象
     * @return
     */
    @Override
    public boolean saveNotice(NoticeForm formData) {
        Notice entity = noticeConverter.toEntity(formData);
        entity.setCreateTime(LocalDateTime.now());
        entity.setCreateBy(SecurityUtils.getUserId());
        entity.setIsDelete(0);
        boolean result = this.save(entity);
        if(result){
            sendNotice(entity);
        }
        return result;
    }
    
    /**
     * 更新通知公告
     *
     * @param id   通知公告ID
     * @param formData 通知公告表单对象
     * @return
     */
    @Override
    public boolean updateNotice(Long id,NoticeForm formData) {
        Notice entity = noticeConverter.toEntity(formData);
        boolean result = this.updateById(entity);
        if(result){
            sendNotice(entity);
        }
        return result;
    }
    
    /**
     * 删除通知公告
     *
     * @param ids 通知公告ID，多个以英文逗号(,)分割
     * @return
     */
    @Override
    public boolean deleteNotices(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的通知公告数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }

}
