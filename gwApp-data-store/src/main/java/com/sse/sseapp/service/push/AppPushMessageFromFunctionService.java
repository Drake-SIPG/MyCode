package com.sse.sseapp.service.push;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.push.AppPushMessageFromFunction;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 消息推送系统与功能关系表
 *
 * @author wy
 * @date 2023-07-20
 */
public interface AppPushMessageFromFunctionService extends IService<AppPushMessageFromFunction> {

    /**
     * 列表获取
     *
     * @param appPushMessageFromFunction
     * @return
     */
    List<AppPushMessageFromFunction> list(AppPushMessageFromFunction appPushMessageFromFunction);

    /**
     * 根据id获取详细信息
     */
    AppPushMessageFromFunction getInfo(@PathVariable(value = "id") String id);

    /**
     * 新增
     */
    int add(@Validated @RequestBody AppPushMessageFromFunction appPushMessageFromFunction);

    /**
     * 修改
     */
    int edit(@Validated @RequestBody AppPushMessageFromFunction appPushMessageFromFunction);

    /**
     * 删除
     */
    int remove(@RequestBody List<String> ids);
}
