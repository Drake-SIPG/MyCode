package com.sse.sseapp.service.push;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sse.sseapp.domain.push.AppPushMessageFrom;

import java.util.List;

/**
 * 消息推送系统来源
 *
 * @author wy
 * @date 2023-07-20
 */
public interface AppPushMessageFromService extends IService<AppPushMessageFrom> {

    /**
     * 列表获取
     *
     * @param appPushMessageFrom
     * @return
     */
    List<AppPushMessageFrom> list(AppPushMessageFrom appPushMessageFrom);

    /**
     * 根据id获取详细信息
     */
    AppPushMessageFrom getInfo(String id);

    /**
     * 根据id获取详细信息
     */
    AppPushMessageFrom getInfoByFromId(String formId);

    /**
     * 新增
     */
    int add(AppPushMessageFrom appPushMessageFrom);

    /**
     * 修改
     */
    int edit(AppPushMessageFrom appPushMessageFrom);

    /**
     * 删除
     */
    int remove(List<String> ids);
}
