package com.sse.sseapp.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.TreeMultimap;
import com.sse.sseapp.core.utils.uuid.SnowUtils;
import com.sse.sseapp.core.web.controller.BaseController;
import com.sse.sseapp.core.web.domain.AjaxResult;
import com.sse.sseapp.domain.system.SysVideoConfig;
import com.sse.sseapp.domain.system.vo.VideoTreeVo;
import com.sse.sseapp.redis.service.RedisService;
import com.sse.sseapp.service.system.SysVideoConfigService;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 参数配置 信息操作处理
 *
 * @author sse
 */
@RestController
@RequestMapping("/dataStore/system/video/config")
public class SysVideoConfigController extends BaseController {
    @Autowired
    private SysVideoConfigService sysVideoConfigService;

    @Autowired
    private RedisService redisService;

    private static final String APP_VIDEO_CONFIG = "app_video_config";
    /**
     * 获取视频配置树
     */
    @PostMapping("/tree/{rootId}")
    public AjaxResult tree(@PathVariable(value = "rootId") Long rootId) {
        List<SysVideoConfig> list = sysVideoConfigService.list();
        //仅在查询所有根目录时从redis获取
        if (ObjectUtil.equals(rootId,0L)){
            if (ObjectUtil.isNotEmpty(redisService.getCacheObject(APP_VIDEO_CONFIG))){
                return AjaxResult.success((JSONArray) redisService.getCacheObject(APP_VIDEO_CONFIG));
            }
        }
        ImmutableListMultimap<Long, VideoTreeVo> temp = list.stream()
                .map(v -> BeanUtil.copyProperties(v, VideoTreeVo.class))
                .collect(ImmutableListMultimap.toImmutableListMultimap(VideoTreeVo::getPid, Function.identity()));
        TreeMultimap<Long, VideoTreeVo> index = TreeMultimap.create(Comparator.naturalOrder(), Comparator.comparing(VideoTreeVo::getSort));
        index.putAll(temp);
        Collection<VideoTreeVo> root = index.get(rootId);
        recursion(root, index);
        if (ObjectUtil.equals(rootId,0L)){
            redisService.setCacheObject(APP_VIDEO_CONFIG, JSONUtil.parse(root),24L, TimeUnit.HOURS);
        }
        return AjaxResult.success(root);
    }

    /**
     * 获取视频配置列表
     */
    @PostMapping("/list")
    public AjaxResult list() {
        List<SysVideoConfig> list = sysVideoConfigService.lambdaQuery().eq(SysVideoConfig::getType, "video").orderByAsc(SysVideoConfig::getSort).list();
        return AjaxResult.success(list);
    }

    /**
     * 新增视频配置
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody SysVideoConfig config) {
        if (config.getSort() == null) {
            config.setSort(System.currentTimeMillis());
        }
        AjaxResult ajax = toAjax(sysVideoConfigService.save(config));
        //刷新redis
        redisService.deleteObject(APP_VIDEO_CONFIG);
        redisService.setCacheObject(APP_VIDEO_CONFIG,JSONUtil.parse(tree(0L).get("data")),24L,TimeUnit.HOURS);
        return ajax;
    }

    /**
     * 修改视频配置
     */
    @PostMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody SysVideoConfig config) {
        AjaxResult ajax = toAjax(sysVideoConfigService.updateById(config));
        redisService.deleteObject(APP_VIDEO_CONFIG);
        redisService.setCacheObject(APP_VIDEO_CONFIG,JSONUtil.parse(tree(0L).get("data")),24L,TimeUnit.HOURS);
        return ajax;
    }

    /**
     * 删除参数配置
     */
    @PostMapping("/remove/{configIds}")
    public AjaxResult remove(@PathVariable(value = "configIds") Long[] configIds) {
        boolean result = sysVideoConfigService.removeByIds(Arrays.asList(configIds));
        redisService.deleteObject(APP_VIDEO_CONFIG);
        redisService.setCacheObject(APP_VIDEO_CONFIG,JSONUtil.parse(tree(0L).get("data")),24L,TimeUnit.HOURS);
        return toAjax(result);
    }

    private void recursion(Collection<VideoTreeVo> list, TreeMultimap<Long, VideoTreeVo> index) {
        for (VideoTreeVo item : list) {
            val id = item.getId();
            val sub = index.get(id);
            if (sub.isEmpty()) {
                continue;
            }
            item.setChild(sub);

            VideoTreeVo first = IterUtil.getFirst(sub);
            item.setCoverAddress(first.getCoverAddress());
            item.setVideoAddress(first.getVideoAddress());

            recursion(sub, index);
        }
    }

    /**
     * 查询详情
     *
     * @param configId
     * @return
     */
    @PostMapping("/query/{configId}")
    public AjaxResult query(@PathVariable(value = "configId") Long configId) {
        return AjaxResult.success(sysVideoConfigService.getById(configId));
    }

    @PostConstruct
    public void init() {
        redisService.deleteObject(APP_VIDEO_CONFIG);
        redisService.setCacheObject(APP_VIDEO_CONFIG,JSONUtil.parse(tree(0L).get("data")),24L,TimeUnit.HOURS);
    }

}
