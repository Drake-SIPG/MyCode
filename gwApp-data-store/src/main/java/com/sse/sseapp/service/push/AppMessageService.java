package com.sse.sseapp.service.push;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.domain.push.AppMessage;
import com.sse.sseapp.dto.appMessage.AppMessageResultDto;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-13
 */
public interface AppMessageService {

    /**
     * 列表获取
     *
     * @param entity
     * @return
     */
    Page<AppMessage> getData(AppMessage entity);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    AppMessageResultDto searchById(String id);

    /**
     * 更新或保存
     *
     * @param tb
     * @return
     */
    int insertOrUpdate(AppMessage tb);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Boolean delete(String id);

    /**
     * 查询所有列表
     *
     * @param entity
     * @return
     */
    List<String> getAllDataMsgId(AppMessage entity);

    /**
     * 批量根据msgId更新状态
     *
     * @param msgIds
     * @param status
     * @return
     */
    Boolean batchUpdateStatusByMsgIds(List<String> msgIds, Integer status);

    /**
     * 根据uuid修改信息
     *
     * @param entity
     * @return
     */
    Boolean updateByUuid(AppMessage entity);
}
