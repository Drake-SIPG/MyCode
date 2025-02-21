package com.sse.sseapp.domain.system.vo;

import com.sse.sseapp.domain.system.SysVideoConfig;
import java.util.Collection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述具体功能。<br>
 *
 * @author hanjian
 * @date 2023/4/25 14:25 hanjian 创建
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class VideoTreeVo extends SysVideoConfig {
    private Collection<VideoTreeVo> child;
}
