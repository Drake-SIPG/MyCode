package com.sse.sseapp.feign.app;

import com.sse.sseapp.domain.app.AppOneUserUsed;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * app一网通办用户最近使用-feign
 *
 * @author jiamingliang
 * @data 2023-08-22
 */

@FeignClient(value = "gwApp-data-store", path = "/dataStore/app/appOneUserUsed")
public interface IAppOneUserUsedFeign {

    /**
     * 新增
     *
     * @param appOneUserUsed
     * @return
     */
    @PostMapping("/add")
    int add(@RequestBody AppOneUserUsed appOneUserUsed);

    /**
     * 根据用户id获取详细信息
     *
     * @param userId 用户id
     * @return
     */
    @GetMapping(value = "/query/{userId}")
    List<AppOneUserUsed> selectByUserId(@PathVariable(value = "userId") String userId);

    /**
     * 根据用户id，navId获取详细信息
     *
     * @param appOneUserUsed
     * @return
     */
    @PostMapping(value = "/getNavByUseId")
    int selectByNavId(@RequestBody AppOneUserUsed appOneUserUsed);

    /**
     * 删除
     *
     * @return
     */
    @PutMapping("/delete")
    int delete();

    /**
     * 修改
     *
     * @param appOneUserUsed
     * @return
     */
    @PutMapping("/edit")
    int edit(@RequestBody AppOneUserUsed appOneUserUsed);
}
