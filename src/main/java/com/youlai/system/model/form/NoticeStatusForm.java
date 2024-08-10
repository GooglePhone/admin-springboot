package com.youlai.system.model.form;

import java.io.Serial;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

/**
 * 用户公告状态表单对象
 *
 * @author youlaitech
 * @since 2024-08-10 16:11
 */
@Getter
@Setter
@Schema(description = "用户公告状态表单对象")
public class NoticeStatusForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "公共通知id")
    private Long noticeId;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "读取状态，0未读，1已读取")
    @NotNull(message = "读取状态，0未读，1已读取不能为空")
    private Long readStatus;

    @Schema(description = "用户阅读时间")
    @NotNull(message = "用户阅读时间不能为空")
    private LocalDateTime readTiem;


}
