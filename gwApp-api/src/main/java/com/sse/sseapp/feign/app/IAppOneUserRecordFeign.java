package com.sse.sseapp.feign.app;

import com.sse.sseapp.domain.app.AppOneUserRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app一网通办用户业务绑定-feign
 *
 * @author jiamingliang
 * @data 2023-08-22
 */

@FeignClient(value = "gwApp-data-store", path = "/dataStore/app/appOneUserRecord")
public interface IAppOneUserRecordFeign {

    /**
     * 新增
     *
     * @param appOneUserRecord
     * @return
     */
    @PostMapping("/add")
    int add(@RequestBody AppOneUserRecord appOneUserRecord);

    /**
     * 根据用户id获取详细信息
     *
     * @param userId 用户id
     * @return
     */
    @GetMapping(value = "/query/{userId}")
    List<AppOneUserRecord> selectByUserId(@PathVariable(value = "userId") String userId);

    /**
     * 修改
     *
     * @param appOneUserRecord
     * @return
     */
    @PutMapping("/delete")
    int delete(@RequestBody AppOneUserRecord appOneUserRecord);

    /**
     * 根据业务类型获取详细信息
     *
     * @param businessType 用户id
     * @return
     */
    @GetMapping(value = "/queryByBusinessType")
    List<AppOneUserRecord> selectByBusinessType(@RequestParam("businessType") String businessType);
}
