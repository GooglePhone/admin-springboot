package com.youlai.boot.system.model.query;

import com.youlai.boot.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 通知公告分页查询对象
 *
 * @author youlaitech
 * @since 2024-08-27 10:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description ="通知公告查询对象")
public class NoticeQuery extends BasePageQuery {

    private static final long serialVersionUID = 1L;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "发布状态(0-未发布 1已发布 2已撤回)")
    private Integer releaseStatus;

    @Schema(description = "发布时间")
    private List<String> releaseTime;

    @Schema(description = "查询人ID")
    private Long userId;
}
