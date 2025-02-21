package com.sse.sseapp.service.push;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sse.sseapp.domain.push.AppMessagePushRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhengyaosheng
 * @since 2023-03-13
 */
public interface AppMessagePushRecordService {

    /**
     * 列表查询
     *
     * @param entity
     * @return
     */
    Page<AppMessagePushRecord> getData(AppMessagePushRecord entity);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    AppMessagePushRecord searchById(String id);

    /**
     * 添加或修改
     *
     * @param tb
     * @return
     */
    Boolean insertOrUpdate(AppMessagePushRecord tb);


    /**
     * 批量保存
     *
     * @param recordList
     * @return
     */
    Boolean batchInsert(List<AppMessagePushRecord> recordList);

    /**
     * 待转换历史数据
     *
     * @param expirationTime
     * @return
     */
    List<AppMessagePushRecord> historyList(String expirationTime);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    int remove(String id);
}
