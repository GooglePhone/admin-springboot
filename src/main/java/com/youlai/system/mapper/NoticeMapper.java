package com.youlai.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youlai.system.model.entity.Notice;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youlai.system.model.query.NoticeQuery;
import com.youlai.system.model.vo.NoticeVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告Mapper接口
 *
 * @author youlaitech
 * @since 2024-08-09 15:06
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {


}
